
package healthdesk.e3.persistence;

import java.util.Map;

/**
 * Holds the result of a persistence recovery operation.
 *
 * Contains the reconstructed key-value state and the highest LSN
 * incorporated into that recovered state.
 */
public class RecoveryResult {

    private final Map<String, String> recoveredState;
    private final long recoveredLSN;

    /**
     * Creates a recovery result containing the recovered state and LSN.
     *
     * @param recoveredState reconstructed key-value state
     * @param recoveredLSN highest LSN incorporated during recovery
     * @throws IllegalArgumentException if state is null or LSN is negative
     */
    public RecoveryResult(Map<String, String> recoveredState, long recoveredLSN) {
        if (recoveredState == null) {
            throw new IllegalArgumentException("Recovered state cannot be null");
        }

        if (recoveredLSN < 0) {
            throw new IllegalArgumentException("Recovered LSN cannot be negative");
        }

        this.recoveredState = recoveredState;
        this.recoveredLSN = recoveredLSN;
    }

    /**
     * Returns the reconstructed in-memory state.
     *
     * @return recovered key-value entries
     */
    public Map<String, String> getRecoveredState() {
        return recoveredState;
    }

    /**
     * Returns the last LSN incorporated into the recovered state.
     *
     * @return recovered LSN
     */
    public long getRecoveredLSN() {
        return recoveredLSN;
    }
}
