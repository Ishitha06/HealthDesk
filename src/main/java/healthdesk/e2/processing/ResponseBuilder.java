package healthdesk.e2.processing;

/**
 * Builds responses to send back to E4.
 */
public class ResponseBuilder {

    /**
     * Creates a success response.
     */
    public String success(String message) {
        return "SUCCESS|" + message;
    }

    /**
     * Creates an error response.
     */
    public String error(String message) {
        return "ERROR|" + message;
    }
}