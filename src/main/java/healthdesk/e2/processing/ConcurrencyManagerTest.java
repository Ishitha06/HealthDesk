package healthdesk.e2.processing;

public class ConcurrencyManagerTest {

    public static void main(String[] args) {

        ConcurrencyManager manager =
                new ConcurrencyManager();

        // Test 1: Read lock
        manager.acquireReadLock();

        try {
            System.out.println("Read lock acquired successfully");
        } finally {
            manager.releaseReadLock();
        }

        // Test 2: Write lock
        manager.acquireWriteLock();

        try {
            System.out.println("Write lock acquired successfully");
        } finally {
            manager.releaseWriteLock();
        }

        System.out.println("All lock tests completed");
    }
}