
package healthdesk.e1.model;

public class QueueRecordTest {

    public static void main(String[] args) {

        System.out.println("===== QueueRecord Manual Testing =====");

        // TEST 1: Valid QueueRecord creation
        try {
            QueueRecord queue = new QueueRecord(
                    "QUEUE-OPD", "General", 5, 10, 5, 2
            );

            System.out.println("TEST 1 PASSED: Valid QueueRecord created");

            System.out.println("Queue ID: " + queue.getQueueId());
            System.out.println("Department: " + queue.getDepartment());
            System.out.println("Serving Token: " + queue.getServingToken());
            System.out.println("Last Issued Token: " + queue.getLastIssuedToken());
            System.out.println("Waiting Count: " + queue.getWaitingCount());
            System.out.println("Active Counters: " + queue.getActiveCounters());

        } catch (Exception e) {
            System.out.println("TEST 1 FAILED: " + e.getMessage());
        }

        // TEST 2: Negative serving token
        try {
            new QueueRecord("Q1", "General", -1, 10, 5, 2);
            System.out.println("TEST 2 FAILED: Negative serving token accepted");
        } catch (IllegalArgumentException e) {
            System.out.println("TEST 2 PASSED: Negative serving token rejected");
        }

        // TEST 3: Negative last issued token
        try {
            new QueueRecord("Q1", "General", 0, -1, 5, 2);
            System.out.println("TEST 3 FAILED: Negative last issued token accepted");
        } catch (IllegalArgumentException e) {
            System.out.println("TEST 3 PASSED: Negative last issued token rejected");
        }

        // TEST 4: Last issued token smaller than serving token
        try {
            new QueueRecord("Q1", "General", 10, 5, 5, 2);
            System.out.println("TEST 4 FAILED: Invalid token relationship accepted");
        } catch (IllegalArgumentException e) {
            System.out.println("TEST 4 PASSED: Invalid token relationship rejected");
        }

        // TEST 5: Negative waiting count
        try {
            new QueueRecord("Q1", "General", 0, 10, -1, 2);
            System.out.println("TEST 5 FAILED: Negative waiting count accepted");
        } catch (IllegalArgumentException e) {
            System.out.println("TEST 5 PASSED: Negative waiting count rejected");
        }

        // TEST 6: Negative active counters
        try {
            new QueueRecord("Q1", "General", 0, 10, 5, -1);
            System.out.println("TEST 6 FAILED: Negative active counters accepted");
        } catch (IllegalArgumentException e) {
            System.out.println("TEST 6 PASSED: Negative active counters rejected");
        }

        // TEST 7: Serving token cannot exceed last issued token
        try {
            QueueRecord queue = new QueueRecord(
                    "Q1", "General", 5, 10, 5, 2
            );

            queue.setServingToken(11);
            System.out.println("TEST 7 FAILED: Invalid serving token accepted");

        } catch (IllegalArgumentException e) {
            System.out.println("TEST 7 PASSED: Invalid serving token rejected");
        }

        // TEST 8: Last issued token cannot be less than serving token
        try {
            QueueRecord queue = new QueueRecord(
                    "Q1", "General", 5, 10, 5, 2
            );

            queue.setLastIssuedToken(4);
            System.out.println("TEST 8 FAILED: Invalid last issued token accepted");

        } catch (IllegalArgumentException e) {
            System.out.println("TEST 8 PASSED: Invalid last issued token rejected");
        }

        // TEST 9: Update waiting count
        try {
            QueueRecord queue = new QueueRecord(
                    "Q1", "General", 0, 10, 5, 2
            );

            queue.setWaitingCount(8);

            if (queue.getWaitingCount() == 8) {
                System.out.println("TEST 9 PASSED: Waiting count updated");
            } else {
                System.out.println("TEST 9 FAILED: Waiting count not updated");
            }

        } catch (Exception e) {
            System.out.println("TEST 9 FAILED: " + e.getMessage());
        }

        // TEST 10: Update active counters
        try {
            QueueRecord queue = new QueueRecord(
                    "Q1", "General", 0, 10, 5, 2
            );

            queue.setActiveCounters(3);

            if (queue.getActiveCounters() == 3) {
                System.out.println("TEST 10 PASSED: Active counters updated");
            } else {
                System.out.println("TEST 10 FAILED: Active counters not updated");
            }

        } catch (Exception e) {
            System.out.println("TEST 10 FAILED: " + e.getMessage());
        }

        System.out.println("===== Testing Completed =====");
    }
}
