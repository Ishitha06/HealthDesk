package healthdesk.e2.processing;

public class RequestProcessorTest {

    public static void main(String[] args) {

        RequestProcessor processor = new RequestProcessor();

        // Test 1: Valid registration
        System.out.println("Test 1:");
        System.out.println(
                processor.process(
                        "REGISTER|P101|Abhishek|CARDIOLOGY"
                )
        );

        // Test 2: Valid status request
        System.out.println("\nTest 2:");
        System.out.println(
                processor.process(
                        "STATUS|CARDIOLOGY"
                )
        );

        // Test 3: Invalid command
        System.out.println("\nTest 3:");
        System.out.println(
                processor.process(
                        "INVALID|test"
                )
        );

        // Test 4: Missing parameters
        System.out.println("\nTest 4:");
        System.out.println(
                processor.process(
                        "REGISTER|P101"
                )
        );
    }
}