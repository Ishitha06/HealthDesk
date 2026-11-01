
package healthdesk.e3.persistence;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class WALManagerConcurrencyTest {

    public static void main(String[] args) throws Exception {

        Path testPath = Path.of("data", "concurrent-test-wal.log");
        Files.deleteIfExists(testPath);

        // One shared LSNManager and WALManager for all counters.
        LSNManager lsnManager = new LSNManager();
        WALManager walManager = new WALManager(lsnManager, testPath);

        CountDownLatch ready = new CountDownLatch(3);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch finished = new CountDownLatch(3);

        AtomicReference<Exception> error = new AtomicReference<>();

        // Simulate three counter threads.
        for (int i = 1; i <= 3; i++) {

            final int counterId = i;

            Thread counter = new Thread(() -> {
                try {
                    ready.countDown();

                    // Wait until all three counters are ready.
                    start.await();

                    long lsn = walManager.append(
                            WALRecord.Operation.PUT,
                            "PATIENT-C" + counterId,
                            "Registered by Counter " + counterId
                    );

                    System.out.println(
                            "Counter " + counterId
                                    + " wrote record with LSN: " + lsn
                    );

                } catch (Exception e) {
                    error.compareAndSet(null, e);
                } finally {
                    finished.countDown();
                }
            });

            counter.start();
        }

        // Release all three counters together.
        ready.await();
        start.countDown();

        // Wait for all writes to finish.
        finished.await();

        if (error.get() != null) {
            throw new RuntimeException("Concurrent WAL test failed", error.get());
        }

        // Check the WAL file.
        List<String> lines = Files.readAllLines(testPath);

        System.out.println("\nWAL file contents:");

        long previousLSN = 0;
        boolean correctOrder = true;

        for (String line : lines) {
            System.out.println(line);

            long currentLSN = Long.parseLong(line.split("\\|")[0]);

            if (currentLSN != previousLSN + 1) {
                correctOrder = false;
            }

            previousLSN = currentLSN;
        }

        if (lines.size() == 3 && correctOrder) {
            System.out.println(
                    "\nTEST PASSED: All three records were written "
                            + "in correct LSN order."
            );
        } else {
            System.out.println("\nTEST FAILED: WAL order is incorrect.");
        }
    }
}
