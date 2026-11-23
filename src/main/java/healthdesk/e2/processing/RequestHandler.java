package healthdesk.e2.processing;

import java.util.concurrent.Callable;

/**
 * Handles an individual request in a separate worker thread.
 */
public class RequestHandler implements Callable<String> {

    private final RequestProcessor processor;
    private final String rawRequest;

    public RequestHandler(
            RequestProcessor processor,
            String rawRequest) {

        if (processor == null) {
            throw new IllegalArgumentException(
                    "RequestProcessor cannot be null");
        }

        if (rawRequest == null) {
            throw new IllegalArgumentException(
                    "Request cannot be null");
        }

        this.processor = processor;
        this.rawRequest = rawRequest;
    }

    /**
     * Executes the request when submitted to an ExecutorService.
     */
    @Override
    public String call() {
        return processor.process(rawRequest);
    }
}