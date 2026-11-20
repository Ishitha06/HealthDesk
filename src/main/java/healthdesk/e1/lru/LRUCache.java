package healthdesk.e1.lru;
import healthdesk.e1.hashmap.CustomHashMap;
public class LRUCache<K,V> {
    private CustomHashMap<K, DoublyNode<CacheEntry<K, V>>> map;
    private DoublyLinkedList<CacheEntry<K, V>> list;
private int capacity;
public LRUCache(int capacity){
    if(capacity<=0){
        throw new IllegalArgumentException("Capacity must be positive");
    }
    this.capacity=capacity;
    this.map=new CustomHashMap<>(capacity);
    this.list=new DoublyLinkedList<>();
}
public void put(K key,V value){
    if(key==null){
        throw new IllegalArgumentException("Key cannot be null");
    }
    if(value==null){
        throw  new IllegalArgumentException("Value cannot be null");
    }
    if(map.containsKey(key)){
       DoublyNode<CacheEntry<K,V>> node=map.get(key);
       node.getData().setValue(value);
       list.moveToFirst(node);
    }
    else{
        CacheEntry<K,V> entry=new CacheEntry<>(key,value);

        list.addFirst(entry);
        map.put(key,list.getHead());
        if (map.size() > capacity) {
            evictLRU();
        }
    }
}
private void evictLRU(){
    DoublyNode<CacheEntry<K,V>> lruNode=list.getTail();
    if(lruNode==null){
        return ;
    }
    K lruKey=lruNode.getData().getKey();
    map.remove(lruKey);
    list.removeLast();
}
public V get(K key) {
    if (key == null) {
        throw new IllegalArgumentException("Key cannot be null");
    }

    if (!map.containsKey(key)) {
        return null;
    }

    DoublyNode<CacheEntry<K, V>> node = map.get(key);
    list.moveToFirst(node);
    return node.getData().getValue();



}
    public boolean remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        if (!map.containsKey(key)) {
            return false;
        }
        DoublyNode<CacheEntry<K, V>> node = map.get(key);
        map.remove(key);
        list.removeNode(node);
        return true;
    }
    public int size(){
    return map.size();
    }
    public boolean isEmpty(){
    return map.isEmpty();
    }
}
