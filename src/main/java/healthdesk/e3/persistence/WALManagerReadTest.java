package healthdesk.e3.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Tests WAL record serialization and deserialization.
 *
 * Verifies that records written to the WAL can be read back with
 * their original operations, keys, values and sequence numbers.
 */

public class WALManagerReadTest {

    public static void main(String[] args) throws IOException {

        Path testWALPath = Paths.get("data", "test-wal-read.log");

        // Remove the previous test file so this test starts fresh.
        Files.deleteIfExists(testWALPath);

        LSNManager lsnManager = new LSNManager();

        WALManager walManager = new WALManager(
                lsnManager,
                testWALPath
        );

        // TEST 1: Write records to the WAL.
        walManager.append(
                WALRecord.Operation.PUT,
                "PATIENT-P101",
                "Abhishek"
        );

        walManager.append(
                WALRecord.Operation.PUT,
                "PATIENT-P102",
                "Priya"
        );

        walManager.append(
                WALRecord.Operation.DELETE,
                "PATIENT-P101",
                null
        );

        System.out.println("TEST 1: WAL records written.");

        // TEST 2: Read records from the WAL file.
        List<WALRecord> records = walManager.readAllRecords();

        System.out.println("\nTEST 2: WAL records read");

        for (WALRecord record : records) {
            System.out.println(
                    "LSN: " + record.getLsn()
                            + " | Operation: " + record.getOperation()
                            + " | Key: " + record.getKey()
                            + " | Value: " + record.getValue()
            );
        }

        // TEST 3: Verify record count.
        if (records.size() == 3) {
            System.out.println("\nTEST 3 PASSED: 3 records recovered.");
        } else {
            System.out.println("\nTEST 3 FAILED: Incorrect record count.");
        }

        // TEST 4: Verify decoded data.
        WALRecord first = records.get(0);
        WALRecord second = records.get(1);
        WALRecord third = records.get(2);

        if (first.getKey().equals("PATIENT-P101")
                && first.getValue().equals("Abhishek")
                && second.getKey().equals("PATIENT-P102")
                && second.getValue().equals("Priya")
                && third.getKey().equals("PATIENT-P101")
                && third.getOperation() == WALRecord.Operation.DELETE
                && third.getValue() == null) {

            System.out.println(
                    "TEST 4 PASSED: WAL records decoded correctly."
            );

        } else {
            System.out.println(
                    "TEST 4 FAILED: WAL record data mismatch."
            );
        }

        // TEST 5: Verify LSN order.
        if (first.getLsn() < second.getLsn()
                && second.getLsn() < third.getLsn()) {

            System.out.println(
                    "TEST 5 PASSED: LSN order is correct."
            );

        } else {
            System.out.println(
                    "TEST 5 FAILED: LSN order is incorrect."
            );
        }

        System.out.println("\nWALManagerReadTest completed.");
    }
}