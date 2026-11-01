package healthdesk.e1.hashmap;

public class HashFunction {
    public static int hash(String key,int capacity){
        if(key==null){
            throw new IllegalArgumentException("Key cannot be null");
        }
        if(capacity<=0){
            throw new IllegalArgumentException("Capacity must be positive");
        }
        int hash=5381;
        for(int i=0;i<key.length();i++){
            hash=(hash*33)+key.charAt(i);
        }
        return Math.floorMod(hash,capacity);
    }
}


