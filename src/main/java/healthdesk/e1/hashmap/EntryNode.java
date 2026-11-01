package healthdesk.e1.hashmap;

public class EntryNode<K,V> {
    private K key;
    private V value;
    private EntryNode<K,V> next;
    EntryNode(K key,V value){
        this.key=key;
        this.value=value;
        this.next=null;
    }

    public EntryNode<K, V> getNext() {
        return next;
    }
    public K getkey(){
        return key;
    }
    public void setValue(V value){
        this.value=value;
    }

    public void setNext(EntryNode<K,V> next) {
    this.next=next;
    }
    public V getValue(){
        return value;
    }
}
