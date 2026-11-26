package healthdesk.e3.persistence;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests the recovery manager under different persistence scenarios.
 *
 * Covers empty recovery, WAL-only recovery and recovery using
 * a snapshot followed by newer WAL records.
 */

public class RecoveryManagerTest {

    public static void main(String[] args) throws Exception {

        Path testRoot = Path.of("data", "recovery-tests");

        cleanDirectory(testRoot);

        testEmptyRecovery(testRoot);
        testWALOnlyRecovery(testRoot);
        testSnapshotAndWALRecovery(testRoot);

        System.out.println("\nALL RECOVERY TESTS PASSED!");
    }

    // TEST 1: No snapshot and no WAL
    private static void testEmptyRecovery(Path root) throws Exception {

        Path directory = root.resolve("empty");
        Path walPath = directory.resolve("wal.log");
        Path snapshotPath = directory.resolve("missing.snapshot");

        LSNManager lsnManager = new LSNManager();
        WALManager walManager = new WALManager(lsnManager, walPath);
        SnapshotManager snapshotManager =
                new SnapshotManager(directory.resolve("snapshots"));

        RecoveryManager recoveryManager = new RecoveryManager(
                snapshotManager, walManager, lsnManager, snapshotPath);

        RecoveryResult result = recoveryManager.recover();

        if (!result.getRecoveredState().isEmpty()
                || result.getRecoveredLSN() != 0) {
            throw new AssertionError("Empty recovery failed");
        }

        System.out.println("TEST 1 PASSED: Empty recovery");
    }

    // TEST 2: Replay WAL without snapshot
    private static void testWALOnlyRecovery(Path root) throws Exception {

        Path directory = root.resolve("wal-only");
        Path walPath = directory.resolve("wal.log");
        Path snapshotPath = directory.resolve("missing.snapshot");

        // Simulate writes before server failure.
        LSNManager writerLSN = new LSNManager();
        WALManager writer = new WALManager(writerLSN, walPath);

        writer.append(WALRecord.Operation.PUT,
                "PATIENT-P101", "Abhinav");

        writer.append(WALRecord.Operation.PUT,
                "PATIENT-P102", "Priya");

        writer.append(WALRecord.Operation.DELETE,
                "PATIENT-P102", null);

        // Simulate restart using a fresh LSNManager.
        LSNManager recoveryLSN = new LSNManager();
        WALManager reader = new WALManager(recoveryLSN, walPath);

        SnapshotManager snapshotManager =
                new SnapshotManager(directory.resolve("snapshots"));

        RecoveryManager recoveryManager = new RecoveryManager(
                snapshotManager, reader, recoveryLSN, snapshotPath);

        RecoveryResult result = recoveryManager.recover();

        Map<String, String> expected = new HashMap<>();
        expected.put("PATIENT-P101", "Abhinav");

        if (!result.getRecoveredState().equals(expected)) {
            throw new AssertionError("WAL-only state is incorrect");
        }

        if (result.getRecoveredLSN() != 3) {
            throw new AssertionError("Incorrect recovered LSN");
        }

        if (recoveryLSN.nextLSN() != 4) {
            throw new AssertionError("LSN did not advance correctly");
        }

        System.out.println("TEST 2 PASSED: WAL-only recovery");
    }

    // TEST 3: Restore snapshot and replay newer WAL
    private static void testSnapshotAndWALRecovery(Path root)
            throws Exception {

        Path directory = root.resolve("snapshot-wal");
        Path walPath = directory.resolve("wal.log");
        Path snapshotDirectory = directory.resolve("snapshots");

        // Simulate operations before the snapshot.
        LSNManager writerLSN = new LSNManager();
        WALManager writer = new WALManager(writerLSN, walPath);

        writer.append(WALRecord.Operation.PUT,
                "PATIENT-P101", "Abhinav");

        writer.append(WALRecord.Operation.PUT,
                "PATIENT-P102", "Priya");

        // Snapshot represents state at LSN 2.
        Map<String, String> snapshotState = new HashMap<>();
        snapshotState.put("PATIENT-P101", "Abhinav");
        snapshotState.put("PATIENT-P102", "Priya");

        SnapshotManager snapshotManager =
                new SnapshotManager(snapshotDirectory);

        Path snapshotPath =
                snapshotManager.saveSnapshot(2, snapshotState);

        // Changes after snapshot.
        writer.append(WALRecord.Operation.PUT,
                "PATIENT-P103", "Rohan");

        writer.append(WALRecord.Operation.DELETE,
                "PATIENT-P101", null);

        // Simulate server restart with fresh LSN.
        LSNManager recoveryLSN = new LSNManager();
        WALManager reader = new WALManager(recoveryLSN, walPath);

        RecoveryManager recoveryManager = new RecoveryManager(
                snapshotManager, reader, recoveryLSN, snapshotPath);

        RecoveryResult result = recoveryManager.recover();

        // Expected: snapshot + WAL records newer than LSN 2.
        Map<String, String> expected = new HashMap<>();
        expected.put("PATIENT-P102", "Priya");
        expected.put("PATIENT-P103", "Rohan");

        if (!result.getRecoveredState().equals(expected)) {
            throw new AssertionError(
                    "Snapshot + WAL recovery produced incorrect state");
        }

        if (result.getRecoveredLSN() != 4) {
            throw new AssertionError(
                    "Incorrect LSN after snapshot + WAL recovery");
        }

        if (recoveryLSN.nextLSN() != 5) {
            throw new AssertionError(
                    "LSN did not advance after snapshot recovery");
        }

        System.out.println(
                "TEST 3 PASSED: Snapshot + WAL recovery");
    }

    // Delete previous test files safely.
    private static void cleanDirectory(Path directory) throws Exception {

        if (!Files.exists(directory)) {
            return;
        }

        try (var paths = Files.walk(directory)) {
            paths.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }
}