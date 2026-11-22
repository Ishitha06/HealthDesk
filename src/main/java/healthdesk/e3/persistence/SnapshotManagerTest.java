
package healthdesk.e3.persistence;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Validates snapshot creation, file existence, snapshot loading,
 * state reconstruction and snapshot LSN preservation.
 */

public class SnapshotManagerTest {

    public static void main(String[] args) throws Exception {

        Path testDirectory = Path.of(
                "data", "test-snapshots");

        // Clean previous test snapshots.
        if (Files.exists(testDirectory)) {
            try (var paths = Files.walk(testDirectory)) {
                paths.sorted((a, b) -> b.compareTo(a))
                        .forEach(path -> {
                            try {
                                Files.deleteIfExists(path);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });
            }
        }

        // Create sample in-memory state.
        Map<String, String> patientData = new HashMap<>();

        patientData.put("PATIENT-P101", "Abhinav");
        patientData.put("PATIENT-P102", "Priya");
        patientData.put("QUEUE-OPD", "LastToken=15");

        SnapshotManager snapshotManager =
                new SnapshotManager(testDirectory);

        // Test 1: Save snapshot.
        Path snapshotPath =
                snapshotManager.saveSnapshot(3, patientData);

        System.out.println("TEST 1: Snapshot saved");
        System.out.println("File: " + snapshotPath);

        // Test 2: Verify file exists.
        if (Files.exists(snapshotPath)) {
            System.out.println("\nTEST 2 PASSED: Snapshot file exists.");
        }

        // Test 3: Load snapshot.
        SnapshotData snapshotData =
                snapshotManager.loadSnapshot(snapshotPath);

        Map<String, String> recoveredData =
                snapshotData.getState();

        long recoveredLSN =
                snapshotData.getSnapshotLSN();

        System.out.println("\nTEST 3: Recovered snapshot data");
        System.out.println("Recovered Snapshot LSN: " + recoveredLSN);
        for (Map.Entry<String, String> entry
                : recoveredData.entrySet()) {
            System.out.println(
                    entry.getKey() + " -> " + entry.getValue());
        }

        // Test 4: Verify recovered state.
        if (patientData.equals(recoveredData)) {
            System.out.println(
                    "\nTEST 4 PASSED: Snapshot data matches original state.");
        } else {
            System.out.println(
                    "\nTEST 4 FAILED: Snapshot data does not match.");
        }

        if (recoveredLSN == 3) {
            System.out.println("TEST 5 PASSED: Snapshot LSN recovered correctly.");
        } else {
            System.out.println("TEST 5 FAILED: Incorrect snapshot LSN.");
        }

        System.out.println("\nSnapshotManager tests completed.");
    }
}
