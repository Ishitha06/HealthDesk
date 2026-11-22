
package healthdesk.e3.persistence;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.HashMap;
import java.nio.file.Files;

/**
 * Reconstructs the HealthDesk in-memory state after a server restart.
 *
 * Recovery begins with the latest available snapshot and replays
 * WAL records whose LSNs are newer than the snapshot.
 */
public class RecoveryManager {

    private final SnapshotManager snapshotManager;
    private final WALManager walManager;
    private final Path snapshotPath;
    private final LSNManager lsnManager;

    /**
     * Initializes the recovery manager with persistence dependencies.
     *
     * @param snapshotManager manager used to load snapshots
     * @param walManager manager used to read WAL records
     * @param lsnManager sequence manager updated after recovery
     * @param snapshotPath path of the snapshot to load
     * @throws IllegalArgumentException if a dependency is null
     */
    public RecoveryManager(
            SnapshotManager snapshotManager,
            WALManager walManager,
            LSNManager lsnManager,
            Path snapshotPath) {

        if (snapshotManager == null
                || walManager == null
                || lsnManager == null
                || snapshotPath == null) {
            throw new IllegalArgumentException(
                    "Managers and snapshot path cannot be null");
        }

        this.snapshotManager = snapshotManager;
        this.walManager = walManager;
        this.lsnManager = lsnManager;
        this.snapshotPath = snapshotPath;
    }

    /**
     * Loads the configured snapshot if it exists.
     *
     * @return snapshot data, or null if no snapshot exists
     * @throws IOException if the existing snapshot cannot be read
     */
    public SnapshotData loadSnapshotIfExists() throws IOException {

        // Avoid attempting to load a snapshot that does not exist.
        if (java.nio.file.Files.exists(snapshotPath)) {
            return snapshotManager.loadSnapshot(snapshotPath);
        }

        return null;
    }

    /**
     * Reconstructs the key-value state from a snapshot and the WAL.
     *
     * Starts with an empty state when no snapshot is available.
     * Applies only WAL records newer than the recovered snapshot LSN.
     * Advances the LSN manager after replay is completed.
     *
     * @return reconstructed state and highest recovered LSN
     * @throws IOException if snapshot or WAL reading fails
     */
    public RecoveryResult recover() throws IOException {

        Map<String, String> recoveredState = new HashMap<>();
        long recoveredLSN = 0;

        // Step 1: Load the snapshot when one is available.
        SnapshotData snapshotData = loadSnapshotIfExists();

        if (snapshotData != null) {
            recoveredState.putAll(snapshotData.getState());
            recoveredLSN = snapshotData.getSnapshotLSN();
        }

        // Step 2: Read the persisted WAL records.
        var walRecords = walManager.readAllRecords();

        // Step 3: Replay only operations newer than the snapshot.
        for (WALRecord record : walRecords) {

            // Skip operations already included in the snapshot.
            if (record.getLsn() <= recoveredLSN) {
                continue;
            }

            // Reapply the original state modification.
            if (record.getOperation() == WALRecord.Operation.PUT) {
                recoveredState.put(record.getKey(), record.getValue());
            }
            else if (record.getOperation() == WALRecord.Operation.DELETE) {
                recoveredState.remove(record.getKey());
            }

            // Track the most recent replayed LSN.
            recoveredLSN = record.getLsn();
        }

        // Step 4: Synchronize the LSN counter with recovered history.
        lsnManager.advanceTo(recoveredLSN);

        // Step 5: Return the reconstructed state to the caller.
        return new RecoveryResult(recoveredState, recoveredLSN);
    }
}
