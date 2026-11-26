
package healthdesk.e2.processing;

import java.util.Scanner;

public class E2Test {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        RequestProcessor processor = new RequestProcessor();

        System.out.println("===== HealthDesk E2 Request Processing =====");

        System.out.print("Enter command: ");
        String command = scanner.nextLine().trim();

        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine().trim();

        System.out.print("Enter Patient Name: ");
        String patientName = scanner.nextLine().trim();

        System.out.print("Enter Department: ");
        String department = scanner.nextLine().trim();

        String request = command + "|" + patientId + "|"
                + patientName + "|" + department;

        System.out.println("\nProcessing request...");

        String response = processor.process(request);

        System.out.println("Server Response: " + response);

        scanner.close();
    }
}
