
package healthdesk.e3.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WALManagerTest {

    public static void main(String[] args) throws IOException {

        Path testPath = Path.of("data", "test-wal.log");

        // Start with a clean test file.
        Files.deleteIfExists(testPath);

        LSNManager lsnManager = new LSNManager();
        WALManager walManager = new WALManager(lsnManager, testPath);

        // Test 1: Write a PUT record.
        long lsn1 = walManager.append(
                WALRecord.Operation.PUT,
                "PATIENT-P101",
                "Abhishek"
        );

        System.out.println("Test 1 - PUT record written");
        System.out.println("Assigned LSN: " + lsn1);

        // Test 2: Write another PUT record.
        long lsn2 = walManager.append(
                WALRecord.Operation.PUT,
                "QUEUE-OPD",
                "Token=15"
        );

        System.out.println("\nTest 2 - Second PUT record written");
        System.out.println("Assigned LSN: " + lsn2);

        // Test 3: Write a DELETE record.
        long lsn3 = walManager.append(
                WALRecord.Operation.DELETE,
                "PATIENT-P101",
                null
        );

        System.out.println("\nTest 3 - DELETE record written");
        System.out.println("Assigned LSN: " + lsn3);

        // Test 4: Verify file contents.
        System.out.println("\nTest 4 - WAL file contents:");

        for (String line : Files.readAllLines(testPath)) {
            System.out.println(line);
        }

        // Test 5: Verify LSN sequence.
        if (lsn1 == 1 && lsn2 == 2 && lsn3 == 3) {
            System.out.println("\nTest 5 PASSED: LSN sequence is correct.");
        } else {
            System.out.println("\nTest 5 FAILED: Incorrect LSN sequence.");
        }

        System.out.println("\nWALManager basic tests completed.");
    }
}
