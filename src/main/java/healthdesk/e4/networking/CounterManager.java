package healthdesk.e4.networking;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the three counter connection slots in HealthDesk.
 *
 * Assigns counter IDs to connected clients, prevents connections
 * beyond the configured capacity, and releases slots on disconnection.
 */
public class CounterManager {

    private static final int MAX_COUNTERS = 3;

    private final Map<Integer, Boolean> counterSlots;

    /**
     * Initializes all counter slots as available.
     */
    public CounterManager() {
        counterSlots = new HashMap<>();

        for (int counterId = 1; counterId <= MAX_COUNTERS; counterId++) {
            counterSlots.put(counterId, false);
        }
    }

    /**
     * Assigns the first available counter slot.
     *
     * @return counter ID if assigned, or -1 if all slots are occupied
     */
    public synchronized int assignCounter() {

        for (int counterId = 1; counterId <= MAX_COUNTERS; counterId++) {

            if (!counterSlots.get(counterId)) {
                counterSlots.put(counterId, true);

                System.out.println("Counter " + counterId + " assigned.");

                return counterId;
            }
        }

        System.out.println("All counter slots are occupied.");
        return -1;
    }

    /**
     * Releases an assigned counter slot.
     *
     * @param counterId counter ID to release
     */
    public synchronized void releaseCounter(int counterId) {

        if (counterSlots.containsKey(counterId)
                && counterSlots.get(counterId)) {

            counterSlots.put(counterId, false);

            System.out.println("Counter " + counterId + " released.");
        }
    }

    /**
     * Returns the number of currently connected counters.
     *
     * @return active counter count
     */
    public synchronized int getActiveCounterCount() {

        int activeCount = 0;

        for (boolean occupied : counterSlots.values()) {
            if (occupied) {
                activeCount++;
            }
        }

        return activeCount;
    }
}