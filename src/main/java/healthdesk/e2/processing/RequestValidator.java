package healthdesk.e2.processing;

import java.util.Set;

/**
 * Validates parsed requests before processing.
 */
public class RequestValidator {

    private static final Set<String> SUPPORTED_COMMANDS =
            Set.of("REGISTER", "NEXT_TOKEN", "STATUS", "UPDATE");

    public void validate(Request request) {

        // Step 1: Check whether the request exists
        if (request == null) {
            throw new IllegalArgumentException(
                    "Request cannot be null");
        }

        String command = request.getCommand();
        var parameters = request.getParameters();

        // Step 2: Check whether the command is supported
        if (!SUPPORTED_COMMANDS.contains(command)) {
            throw new IllegalArgumentException(
                    "Unsupported command: " + command);
        }

        // Step 3: Check the required number of parameters
        switch (command) {

            case "REGISTER":
                if (parameters.size() != 3) {
                    throw new IllegalArgumentException(
                            "REGISTER requires patient ID, name and department");
                }
                break;

            case "NEXT_TOKEN":
                if (parameters.size() != 1) {
                    throw new IllegalArgumentException(
                            "NEXT_TOKEN requires department");
                }
                break;

            case "STATUS":
                if (parameters.size() != 1) {
                    throw new IllegalArgumentException(
                            "STATUS requires department");
                }
                break;

            case "UPDATE":
                if (parameters.size() != 3) {
                    throw new IllegalArgumentException(
                            "UPDATE requires patient ID, field and new value");
                }
                break;

            default:
                throw new IllegalArgumentException(
                        "Invalid command");
        }

        // Step 4: Check that no parameter is empty
        for (String parameter : parameters) {
            if (parameter == null || parameter.isBlank()) {
                throw new IllegalArgumentException(
                        "Request parameters cannot be empty");
            }
        }
    }
}