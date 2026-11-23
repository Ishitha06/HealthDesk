package healthdesk.e2.processing;

import java.util.List;

public class RequestValidatorTest {

    public static void main(String[] args) {

        RequestValidator validator = new RequestValidator();

        test(validator, new Request(
                "REGISTER",
                List.of("P101", "Abhishek", "CARDIOLOGY")
        ));

        test(validator, new Request(
                "NEXT_TOKEN",
                List.of("CARDIOLOGY")
        ));

        test(validator, new Request(
                "STATUS",
                List.of("CARDIOLOGY")
        ));

        test(validator, new Request(
                "INVALID",
                List.of("test")
        ));

        test(validator, new Request(
                "REGISTER",
                List.of("P101", "Abhishek")
        ));

        test(validator, new Request(
                "REGISTER",
                List.of("P101", "", "CARDIOLOGY")
        ));
    }

    private static void test(
            RequestValidator validator, Request request) {

        try {
            validator.validate(request);
            System.out.println(
                    request.getCommand() + " : VALID");

        } catch (IllegalArgumentException e) {
            System.out.println(
                    request.getCommand() + " : ERROR - "
                            + e.getMessage());
        }
    }
}