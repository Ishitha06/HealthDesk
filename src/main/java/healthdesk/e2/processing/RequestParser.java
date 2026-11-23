package healthdesk.e2.processing;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Converts raw request text into a Request object.
 */
public class RequestParser {

    public Request parse(String rawRequest) {

        // Step 1: Check whether the request is empty
        if (rawRequest == null || rawRequest.isBlank()) {
            throw new IllegalArgumentException(
                    "Request cannot be empty"
            );
        }

        // Step 2: Split the request into command and parameters
        String[] parts = rawRequest.trim().split("\\|", -1);

        // Step 3: Extract and standardize the command
        String command = parts[0].trim().toUpperCase(Locale.ROOT);

        if (command.isEmpty()) {
            throw new IllegalArgumentException(
                    "Command cannot be empty"
            );
        }

        // Step 4: Extract parameters
        List<String> parameters = new ArrayList<>();

        for (int i = 1; i < parts.length; i++) {
            parameters.add(parts[i].trim());
        }

        // Step 5: Create and return the Request object
        return new Request(command, parameters);
    }
}