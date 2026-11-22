
package healthdesk.e3.persistence;

import java.util.Map;

/**
 * Represents the contents of a successfully loaded snapshot.
 *
 * Stores the snapshot's LSN and the key-value state captured
 * at that point in the persistence history.
 */
public class SnapshotData {

    private final long snapshotLSN;
    private final Map<String, String> state;

    /**
     * Creates a snapshot data object.
     *
     * @param snapshotLSN LSN associated with the snapshot
     * @param state key-value state stored in the snapshot
     */
    public SnapshotData(long snapshotLSN, Map<String, String> state) {
        this.snapshotLSN = snapshotLSN;
        this.state = state;
    }

    /**
     * Returns the LSN at which this snapshot was captured.
     *
     * @return snapshot LSN
     */
    public long getSnapshotLSN() {
        return snapshotLSN;
    }

    /**
     * Returns the state loaded from the snapshot.
     *
     * @return snapshot key-value data
     */
    public Map<String, String> getState() {
        return state;
    }
}
