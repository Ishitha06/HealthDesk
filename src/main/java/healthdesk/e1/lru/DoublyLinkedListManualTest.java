
package healthdesk.e1.lru;

public class DoublyLinkedListManualTest {

    public static void main(String[] args) {

        DoublyLinkedList<String> list = new DoublyLinkedList<>();

        // Test 1: Initially list should be empty
        System.out.println("Test 1: Empty list - "
                + (list.isEmpty() ? "PASS" : "FAIL"));

        // Test 2: Add first node
        list.addFirst("A");
        System.out.println("Test 2: Add first node - "
                + (list.getHead().getData().equals("A")
                && list.getTail().getData().equals("A") ? "PASS" : "FAIL"));

        // Test 3: Add multiple nodes
        list.addFirst("B");
        list.addFirst("C");
        System.out.println("Test 3: Add multiple nodes - "
                + (list.getHead().getData().equals("C")
                && list.getTail().getData().equals("A") ? "PASS" : "FAIL"));

        // Test 4: Check previous and next links
        DoublyNode<String> head = list.getHead();
        System.out.println("Test 4: Check links - "
                + (head.getNext().getData().equals("B")
                && head.getNext().getPrevious() == head ? "PASS" : "FAIL"));

        // Test 5: Remove first node
        list.removeFirst();
        System.out.println("Test 5: Remove first - "
                + (list.getHead().getData().equals("B") ? "PASS" : "FAIL"));

        // Test 6: Remove last node
        list.removeLast();
        System.out.println("Test 6: Remove last - "
                + (list.getTail().getData().equals("B")
                && list.getHead() == list.getTail() ? "PASS" : "FAIL"));

        // Test 7: Remove only remaining node
        list.removeLast();
        System.out.println("Test 7: Remove only node - "
                + (list.isEmpty()
                && list.getHead() == null
                && list.getTail() == null ? "PASS" : "FAIL"));

        // Test 8: Remove from empty list
        list.removeFirst();
        System.out.println("Test 8: Remove first from empty list - "
                + (list.isEmpty() ? "PASS" : "FAIL"));

        list.removeLast();
        System.out.println("Test 9: Remove last from empty list - "
                + (list.isEmpty() ? "PASS" : "FAIL"));

        // Test 10: Move middle node to first
        list.addFirst("C");
        list.addFirst("B");
        list.addFirst("A");

        DoublyNode<String> middleNode = list.getHead().getNext();
        list.moveToFirst(middleNode);

        System.out.println("Test 10: Move middle node to first - "
                + (list.getHead().getData().equals("B")
                && list.getHead().getNext().getData().equals("A")
                && list.getTail().getData().equals("C") ? "PASS" : "FAIL"));

        // Test 11: Move tail node to first
        DoublyNode<String> tailNode = list.getTail();
        list.moveToFirst(tailNode);

        System.out.println("Test 11: Move tail to first - "
                + (list.getHead().getData().equals("C")
                && list.getTail().getData().equals("A")
                ? "PASS" : "FAIL"));


        DoublyNode<String> currentHead = list.getHead();
        list.moveToFirst(currentHead);

        System.out.println("Test 12: Move existing head - "
                + (list.getHead() == currentHead ? "PASS" : "FAIL"));

        System.out.println("\nManual testing completed.");
    }

}
