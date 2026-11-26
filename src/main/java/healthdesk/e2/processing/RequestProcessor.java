package healthdesk.e2.processing;

/**
 * Coordinates request parsing, validation
 * and response generation.
 */
public class RequestProcessor {

    private final RequestParser parser;
    private final RequestValidator validator;
    private final ConcurrencyManager concurrencyManager;
    private final ResponseBuilder responseBuilder;

    // Default constructor for standalone testing
    public RequestProcessor() {
        this(new ConcurrencyManager());
    }

    // Constructor accepting a shared ConcurrencyManager
    public RequestProcessor(ConcurrencyManager concurrencyManager) {

        if (concurrencyManager == null) {
            throw new IllegalArgumentException(
                    "ConcurrencyManager cannot be null");
        }

        this.parser = new RequestParser();
        this.validator = new RequestValidator();
        this.concurrencyManager = concurrencyManager;
        this.responseBuilder = new ResponseBuilder();
    }

    public String process(String rawRequest) {

        try {
            Request request = parser.parse(rawRequest);

            validator.validate(request);

            return responseBuilder.success(
                    "Request validated: " + request.getCommand()
            );

        } catch (IllegalArgumentException e) {

            return responseBuilder.error(e.getMessage());
        }
    }

    public ConcurrencyManager getConcurrencyManager() {
        return concurrencyManager;
    }
}