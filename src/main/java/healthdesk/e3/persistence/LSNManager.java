
package healthdesk.e3.persistence;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Manages Log Sequence Numbers (LSNs) used to uniquely order
 * persistence operations in the HealthDesk system.
 *
 * LSNs are used by the Write-Ahead Log and recovery mechanism
 * to identify the order in which state changes occurred.
 */
public class LSNManager {

    private final AtomicLong currentLSN;

    /**
     * Initializes the LSN manager with a sequence starting at zero.
     */
    public LSNManager() {
        this(0);
    }

    /**
     * Initializes the LSN manager with a specified starting value.
     *
     * @param initialLSN initial sequence number
     * @throws IllegalArgumentException if the initial LSN is negative
     */
    public LSNManager(long initialLSN) {
        if (initialLSN < 0) {
            throw new IllegalArgumentException("LSN cannot be negative");
        }

        this.currentLSN = new AtomicLong(initialLSN);
    }

    /**
     * Atomically generates and returns the next LSN.
     *
     * @return the next unique sequence number
     */
    public long nextLSN() {
        return currentLSN.incrementAndGet();
    }

    /**
     * Returns the currently assigned LSN without modifying it.
     *
     * @return the current LSN value
     */
    public long getCurrentLSN() {
        return currentLSN.get();
    }

    /**
     * Advances the LSN counter to at least the recovered LSN.
     * The counter is never moved backwards.
     *
     * @param recoveredLSN the highest LSN recovered from persistent storage
     * @throws IllegalArgumentException if the recovered LSN is negative
     */
    public void advanceTo(long recoveredLSN) {
        if (recoveredLSN < 0) {
            throw new IllegalArgumentException("LSN cannot be negative");
        }

        currentLSN.updateAndGet(
                current -> Math.max(current, recoveredLSN)
        );
    }
}
