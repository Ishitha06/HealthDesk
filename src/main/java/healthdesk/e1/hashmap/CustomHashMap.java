package healthdesk.e1.hashmap;

public class CustomHashMap<K,V> {
    private EntryNode<K,V>[] buckets;
    private int capacity;
    private int size;
    @SuppressWarnings("unchecked")
    public CustomHashMap(int capacity ){
        if(capacity<=0)
        {throw new IllegalArgumentException("Capacity should be positive!");}
        this.capacity=capacity;
        this.buckets=(EntryNode<K,V>[]) new EntryNode[capacity];
        this.size=0;
    }
    public void put(K key,V value){
     if(key==null){
         throw new IllegalArgumentException("Key cant be empty!");
     }

     int index=HashFunction.hash(key.toString(),capacity);
     EntryNode<K,V> current =buckets[index];
     EntryNode<K,V> prev=null;
     EntryNode<K,V> newNode=new EntryNode<>(key,value);
     while(current!=null){
         prev=current;

         if(current.getkey().equals(key)){
             current.setValue(value);
             return;
         }
         current=current.getNext();
     }
     if(prev==null){
buckets[index]=newNode;
     }
     else{
       prev.setNext(newNode);
     }
     size++;

    }
    public V get(K key){
        if(key==null){
            throw new IllegalArgumentException("Key cant be empty!");
        }

        int index=HashFunction.hash(key.toString(),capacity);
        EntryNode<K,V> current =buckets[index];

        while(current!=null){

            if(current.getkey().equals(key)){
                return current.getValue();
            }
            current=current.getNext();
        }

        return null;

    }
    public boolean containsKey(K key){
        if(key==null){
            throw new IllegalArgumentException("Key cannot be null!");
        }
        int index=HashFunction.hash(key.toString(),capacity);
        EntryNode<K,V> current=buckets[index];
        while(current!=null){
            if(current.getkey().equals(key)){
                return true;
            }
            current=current.getNext();
        }
        return false;
    }
    public boolean remove(K key){
        if(key==null){
            throw new IllegalArgumentException("Key cannot be null!");
        }
        int index=HashFunction.hash(key.toString(),capacity);
        EntryNode<K,V> current=buckets[index];
        EntryNode<K,V> prev=null;
        while(current!=null) {
        if(current.getkey().equals(key)){
            if(prev==null){
                buckets[index]=current.getNext();
            }
            else{
                prev.setNext(current.getNext());
            }
            size--;
            return true;
        }
        prev=current;
        current=current.getNext();
        }
        return false;
        }
        public int size(){
        return size;
        }
        public boolean isEmpty(){
        if(size==0){return true;}
        else{return false;}
        }

    }

