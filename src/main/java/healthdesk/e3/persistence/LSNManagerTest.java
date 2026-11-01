
package healthdesk.e3.persistence;

import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class LSNManagerTest {

    public static void main(String[] args) throws InterruptedException {

        // One shared LSNManager for all three counters
        LSNManager manager = new LSNManager();

        CountDownLatch ready = new CountDownLatch(3);
        CountDownLatch start = new CountDownLatch(1);

        long[] assignedLSNs = new long[3];
        AtomicReference<Throwable> error = new AtomicReference<>();

        Thread[] counters = new Thread[3];

        for (int i = 0; i < 3; i++) {
            final int counter = i;

            counters[i] = new Thread(() -> {
                ready.countDown();

                try {
                    start.await();
                    assignedLSNs[counter] = manager.nextLSN();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    error.compareAndSet(null, e);
                }
            }, "Counter-" + (i + 1));

            counters[i].start();
        }

        // Wait until all three threads are ready
        ready.await();

        // Release all three threads together
        start.countDown();

        for (Thread thread : counters) {
            thread.join();
        }

        if (error.get() != null) {
            throw new RuntimeException("Thread test failed", error.get());
        }

        Set<Long> uniqueLSNs = new HashSet<>();

        System.out.println("Three Counter Concurrent LSN Test");

        for (int i = 0; i < 3; i++) {
            System.out.println(
                    "Counter " + (i + 1) +
                            " received LSN: " + assignedLSNs[i]
            );

            uniqueLSNs.add(assignedLSNs[i]);
        }

        System.out.println("\nUnique LSN count: " + uniqueLSNs.size());
        System.out.println("Current LSN: " + manager.getCurrentLSN());

        if (uniqueLSNs.size() == 3 &&
                manager.getCurrentLSN() == 3) {
            System.out.println("\nTEST PASSED");
        } else {
            System.out.println("\nTEST FAILED");
        }
    }
}
