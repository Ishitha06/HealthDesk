package healthdesk.e2.processing;

import java.util.List;

public class RequestTest {

    public static void main(String[] args) {

        Request request = new Request(
                "REQ101",
                "REGISTER",
                List.of("P101", "Abhishek", "CARDIOLOGY")
        );

        System.out.println("Request ID: " + request.getRequestId());
        System.out.println("Command: " + request.getCommand());
        System.out.println("Parameters: " + request.getParameters());
    }
}