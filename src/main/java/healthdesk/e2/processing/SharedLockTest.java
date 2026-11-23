package healthdesk.e2.processing;

public class SharedLockTest {

    public static void main(String[] args) {

        ConcurrencyManager sharedManager =
                new ConcurrencyManager();

        RequestProcessor processor1 =
                new RequestProcessor(sharedManager);

        RequestProcessor processor2 =
                new RequestProcessor(sharedManager);

        System.out.println(
                "Shared manager: "
                        + (processor1.getConcurrencyManager()
                        == processor2.getConcurrencyManager())
        );
    }
}