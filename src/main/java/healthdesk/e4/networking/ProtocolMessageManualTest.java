package healthdesk.e4.networking;

/**
 * Manual test runner for HealthDesk protocol components.
 * Tests request parsing, malformed request rejection,
 * and response formatting.
 */
public class ProtocolMessageManualTest {

    public static void main(String[] args) {

        testValidRequest();
        testMalformedRequest();
        testSuccessResponse();
        testErrorResponse();

        System.out.println("\nAll protocol tests passed!");
    }

    private static void testValidRequest() {

        String rawMessage =
                "REQUEST|REQ001|REGISTER|name=Abhishek;department=General";

        ProtocolMessage message = ProtocolMessage.parse(rawMessage);

        if (!"REQ001".equals(message.getRequestId())
                || !"REGISTER".equals(message.getOperation())
                || !"name=Abhishek;department=General".equals(message.getPayload())) {
            throw new AssertionError("Valid request parsing failed");
        }

        System.out.println("PASS: Valid request parsing");
    }

    private static void testMalformedRequest() {

        try {
            ProtocolMessage.parse("HELLO");
            throw new AssertionError("Malformed request was not rejected");

        } catch (IllegalArgumentException exception) {
            System.out.println("PASS: Malformed request rejected");
        }
    }

    private static void testSuccessResponse() {

        ProtocolResponse response =
                ProtocolResponse.success("REQ001", "PATIENT_REGISTERED");

        String expected =
                "RESPONSE|REQ001|SUCCESS|PATIENT_REGISTERED";

        if (!expected.equals(response.toMessage())) {
            throw new AssertionError("Success response formatting failed");
        }

        System.out.println("PASS: Success response formatting");
    }

    private static void testErrorResponse() {

        ProtocolResponse response =
                ProtocolResponse.error("REQ002", "UNKNOWN_OPERATION");

        String expected =
                "RESPONSE|REQ002|ERROR|UNKNOWN_OPERATION";

        if (!expected.equals(response.toMessage())) {
            throw new AssertionError("Error response formatting failed");
        }

        System.out.println("PASS: Error response formatting");
    }
}