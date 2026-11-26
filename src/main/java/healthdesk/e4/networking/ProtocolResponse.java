package healthdesk.e4.networking;

/**
 * Represents a standardized response sent from HealthDesk
 * to a connected counter client.
 *
 * Format:
 * RESPONSE|requestId|status|payload
 */
public class ProtocolResponse {

    private final String requestId;
    private final String status;
    private final String payload;

    /**
     * Creates a protocol response.
     *
     * @param requestId ID of the request being answered
     * @param status response status (SUCCESS or ERROR)
     * @param payload response data or error code
     */
    public ProtocolResponse(String requestId, String status, String payload) {
        this.requestId = requestId;
        this.status = status;
        this.payload = payload;
    }

    /**
     * Converts the response into the protocol's text format.
     *
     * @return formatted response message
     */
    public String toMessage() {
        return "RESPONSE|" + requestId + "|" + status + "|" + payload;
    }

    /**
     * Creates a successful response.
     *
     * @param requestId request identifier
     * @param payload result data
     * @return success response
     */
    public static ProtocolResponse success(String requestId, String payload) {
        return new ProtocolResponse(requestId, "SUCCESS", payload);
    }

    /**
     * Creates an error response.
     *
     * @param requestId request identifier
     * @param errorCode error code
     * @return error response
     */
    public static ProtocolResponse error(String requestId, String errorCode) {
        return new ProtocolResponse(requestId, "ERROR", errorCode);
    }
}