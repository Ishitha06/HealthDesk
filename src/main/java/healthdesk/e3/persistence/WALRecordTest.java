
package healthdesk.e3.persistence;

public class WALRecordTest {

    public static void main(String[] args) {

        // Test 1: Create a PUT record
        WALRecord record1 = new WALRecord(
                1,
                WALRecord.Operation.PUT,
                "PATIENT-P101",
                "Abhishek"
        );

        System.out.println("Test 1: PUT Record");
        System.out.println("LSN: " + record1.getLsn());
        System.out.println("Operation: " + record1.getOperation());
        System.out.println("Key: " + record1.getKey());
        System.out.println("Value: " + record1.getValue());

        // Test 2: Create a DELETE record
        WALRecord record2 = new WALRecord(
                2,
                WALRecord.Operation.DELETE,
                "PATIENT-P101",
                null
        );

        System.out.println("\nTest 2: DELETE Record");
        System.out.println("LSN: " + record2.getLsn());
        System.out.println("Operation: " + record2.getOperation());
        System.out.println("Key: " + record2.getKey());

        // Test 3: Check invalid LSN
        try {
            new WALRecord(
                    0,
                    WALRecord.Operation.PUT,
                    "PATIENT-P102",
                    "Test"
            );
            System.out.println("Test 3 FAILED");
        } catch (IllegalArgumentException e) {
            System.out.println("\nTest 3 PASSED: " + e.getMessage());
        }

        System.out.println("\nAll basic WALRecord tests completed.");
    }
}
