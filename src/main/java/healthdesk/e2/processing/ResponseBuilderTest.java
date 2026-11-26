package healthdesk.e2.processing;

public class ResponseBuilderTest {

    public static void main(String[] args) {

        ResponseBuilder builder = new ResponseBuilder();

        String successResponse =
                builder.success("Patient registered successfully");

        String errorResponse =
                builder.error("Invalid patient ID");

        System.out.println("Success Response:");
        System.out.println(successResponse);

        System.out.println("\nError Response:");
        System.out.println(errorResponse);
    }
}