package healthdesk.e1.hashmap;

public class HashFunctionTest { public static  void main(String[] args){
    String key="PATIENT-P101";
    int capacity=10;
    int index=HashFunction.hash(key,capacity);
    System.out.println("Key:"+key);
    System.out.println("Bucket Index:"+index);
}
}
