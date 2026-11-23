package healthdesk.e2.processing;

import java.util.List;

/**
 * Represents a request received by the E2 engine.
 */
public class Request {

    private final String requestId;
    private final String command;
    private final List<String> parameters;

    public Request(String command, List<String> parameters) {
        this(null, command, parameters);
    }

    public Request(String requestId, String command,
                   List<String> parameters) {

        if (command == null || command.isBlank()) {
            throw new IllegalArgumentException(
                    "Command cannot be empty"
            );
        }

        if (parameters == null) {
            throw new IllegalArgumentException(
                    "Parameters cannot be null"
            );
        }

        this.requestId = requestId;
        this.command = command;
        this.parameters = List.copyOf(parameters);
    }

    public String getRequestId() {
        return requestId;
    }

    public String getCommand() {
        return command;
    }

    public List<String> getParameters() {
        return parameters;
    }
}