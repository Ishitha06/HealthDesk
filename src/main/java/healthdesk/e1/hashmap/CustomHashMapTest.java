package healthdesk.e1.hashmap;

public class CustomHashMapTest {
    public static void main(String[] args) {

        CustomHashMap<String, String> map = new CustomHashMap<>(10);

        // TEST 1: Insert
        System.out.println("TEST 1: INSERT");
        map.put("P101", "Ananya");
        map.put("P102", "Ishitha");
        map.put("P103", "Sonika");

        System.out.println(map.get("P101"));
        System.out.println(map.get("P102"));
        System.out.println(map.get("P103"));

        // TEST 2: Search
        System.out.println("\nTEST 2: SEARCH");
        System.out.println(map.containsKey("P101"));
        System.out.println(map.containsKey("P999"));

        // TEST 3: Update
        System.out.println("\nTEST 3: UPDATE");
        map.put("P101", "Updated Ananya");
        System.out.println(map.get("P101"));

        // TEST 4: Delete first node
        System.out.println("\nTEST 4: DELETE FIRST NODE");
        System.out.println(map.remove("P101"));
        System.out.println(map.get("P101"));

        // TEST 5: Delete middle node
        System.out.println("\nTEST 5: DELETE MIDDLE NODE");
        map.put("P104", "Namrata");
        System.out.println(map.remove("P102"));
        System.out.println(map.get("P103"));

        // TEST 6: Delete last node
        System.out.println("\nTEST 6: DELETE LAST NODE");
        System.out.println(map.remove("P104"));

        // TEST 7: Delete missing key
        System.out.println("\nTEST 7: DELETE MISSING KEY");
        System.out.println(map.remove("P999"));

        // TEST 8: Search missing key
        System.out.println("\nTEST 8: SEARCH MISSING KEY");
        System.out.println(map.get("P999"));
        System.out.println("Current size: " + map.size());
        System.out.println("Is map empty? " + map.isEmpty());
        CustomHashMap<String, String> emptyMap = new CustomHashMap<>(10);

        System.out.println(emptyMap.size());    // 0
        System.out.println(emptyMap.isEmpty()); // true

    }
}