package healthdesk.e4.networking;

/**
 * Represents a parsed application-level request received through WebSocket.
 *
 * Expected format:
 * REQUEST|requestId|operation|payload
 *
 * This class handles message structure only.
 * Business validation and request processing belong to E2.
 */
public class ProtocolMessage {

    private final String requestId;
    private final String operation;
    private final String payload;

    /**
     * Creates a protocol message with its parsed fields.
     *
     * @param requestId unique identifier of the request
     * @param operation requested operation
     * @param payload request data
     */
    public ProtocolMessage(String requestId, String operation, String payload) {
        this.requestId = requestId;
        this.operation = operation;
        this.payload = payload;
    }

    /**
     * Parses a raw incoming message into a ProtocolMessage.
     *
     * @param rawMessage raw message received from the WebSocket client
     * @return parsed protocol message
     * @throws IllegalArgumentException if the message structure is invalid
     */
    public static ProtocolMessage parse(String rawMessage) {

        if (rawMessage == null || rawMessage.isBlank()) {
            throw new IllegalArgumentException("MALFORMED_REQUEST");
        }

        String[] parts = rawMessage.split("\\|", 4);

        if (parts.length != 4 || !"REQUEST".equals(parts[0])) {
            throw new IllegalArgumentException("MALFORMED_REQUEST");
        }

        String requestId = parts[1].trim();
        String operation = parts[2].trim();
        String payload = parts[3].trim();

        if (requestId.isEmpty() || operation.isEmpty() || payload.isEmpty()) {
            throw new IllegalArgumentException("MALFORMED_REQUEST");
        }

        return new ProtocolMessage(requestId, operation, payload);
    }

    /**
     * Returns the request ID.
     *
     * @return request identifier
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Returns the requested operation.
     *
     * @return operation name
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Returns the request payload.
     *
     * @return request data
     */
    public String getPayload() {
        return payload;
    }
}