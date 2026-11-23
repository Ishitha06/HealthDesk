package healthdesk.e2.processing;

import java.util.List;

public class RequestParserTest {

    public static void main(String[] args) {

        RequestParser parser = new RequestParser();

        // Test 1: Valid request
        test(parser, "REGISTER|P101|Abhishek|CARDIOLOGY");

        // Test 2: Lowercase command
        test(parser, "register|P101|Abhishek|CARDIOLOGY");

        // Test 3: Empty request
        test(parser, "");

        // Test 4: Empty trailing parameter
        test(parser, "REGISTER|P101|Abhishek|");

        // Test 5: Empty command
        test(parser, "|P101|Abhishek|CARDIOLOGY");
    }

    private static void test(RequestParser parser, String input) {

        System.out.println("\nInput: " + input);

        try {
            Request request = parser.parse(input);

            System.out.println("Command: " + request.getCommand());
            System.out.println("Parameters: " + request.getParameters());

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}