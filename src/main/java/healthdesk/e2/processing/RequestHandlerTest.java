package healthdesk.e2.processing;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RequestHandlerTest {

    public static void main(String[] args) throws Exception {

        RequestProcessor processor = new RequestProcessor();

        // Create a pool containing 3 worker threads
        ExecutorService executor =
                Executors.newFixedThreadPool(3);

        try {
            String[] requests = {
                    "REGISTER|P101|Abhishek|CARDIOLOGY",
                    "NEXT_TOKEN|CARDIOLOGY",
                    "STATUS|CARDIOLOGY"
            };

            Future<String>[] results = new Future[3];

            // Submit all requests for concurrent execution
            for (int i = 0; i < requests.length; i++) {

                RequestHandler handler =
                        new RequestHandler(processor, requests[i]);

                results[i] = executor.submit(handler);
            }

            // Retrieve responses
            for (int i = 0; i < results.length; i++) {

                System.out.println(
                        "Counter " + (i + 1) + ": "
                                + results[i].get()
                );
            }

        } finally {
            executor.shutdown();
        }
    }
}