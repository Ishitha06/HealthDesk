package healthdesk.e2.processing;

public class E2IntegrationTest {

    public static void main(String[] args) {

        RequestProcessor processor = new RequestProcessor();

        String[] requests = {
                "REGISTER|P101|Abhishek|CARDIOLOGY",
                "NEXT_TOKEN|CARDIOLOGY",
                "STATUS|CARDIOLOGY",
                "UPDATE|P101|status|COMPLETED",
                "INVALID|test",
                "REGISTER|P102",
                "STATUS|"
        };

        for (int i = 0; i < requests.length; i++) {

            System.out.println("\nRequest " + (i + 1));
            System.out.println("Input: " + requests[i]);

            String response = processor.process(requests[i]);

            System.out.println("Output: " + response);
        }
    }
}