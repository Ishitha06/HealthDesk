package healthdesk.e1.lru;

public class DoublyLinkedList<T> {
    private DoublyNode<T> head;
    private DoublyNode<T> tail;
public void addFirst(T data){
    DoublyNode<T> newNode=new DoublyNode<>(data);
    if(head==null){
        head=newNode;
        tail=newNode;
    }
    else{
        newNode.setNext(head);
        head.setPrevious(newNode);
        head=newNode;
    }
}
public void removeFirst(){
    if(head==null){
        return;
    }
    head=head.getNext();
    if(head==null){
        tail=null;
    }
    else{
        head.setPrevious(null);
    }
}
public void removeLast(){
    if(tail==null){
        return;
    }
    tail=tail.getPrevious();
    if(tail==null){
        head=null;
    }
    else{
        tail.setNext(null);
    }
}
    public void moveToFirst(DoublyNode<T> node) {
        if (head == null || node == head) {
            return;
        }
        DoublyNode<T> previous = node.getPrevious();
        DoublyNode<T> next = node.getNext();
        if (previous != null) {
            previous.setNext(next);
        }
        if (next != null) {
            next.setPrevious(previous);
        }
        if (node == tail) {
            tail = previous;
        }
        node.setPrevious(null);
        node.setNext(head);
        head.setPrevious(node);
        head = node;
    }
    public DoublyNode<T> getTail() {
        return tail;
    }
    public DoublyNode<T> getHead() {
        return head;
    }
    public boolean isEmpty() {
        return head == null;
    }
    public void removeNode(DoublyNode<T> node) {

        if (node == null) {
            return;
        }

        if (node == head) {
            removeFirst();
            return;
        }

        if (node == tail) {
            removeLast();
            return;
        }

        DoublyNode<T> previous = node.getPrevious();
        DoublyNode<T> next = node.getNext();

        previous.setNext(next);
        next.setPrevious(previous);

        node.setPrevious(null);
        node.setNext(null);
    }

}
