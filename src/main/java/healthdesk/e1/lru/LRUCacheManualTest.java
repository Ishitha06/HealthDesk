package healthdesk.e1.lru;

public class LRUCacheManualTest {
    public static void main(String[] args){


            LRUCache<String, String> cache = new LRUCache<>(3);

            // Test 1: Insert entries
            cache.put("P101", "Ananya");
            cache.put("P102", "Ishitha");
            cache.put("P103", "Sonika");

            // Test 2: Retrieve entries
            System.out.println("P101: " + cache.get("P101"));
            System.out.println("P102: " + cache.get("P102"));
            System.out.println("P103: " + cache.get("P103"));

            // Test 3: Retrieve a missing key
            System.out.println("P104: " + cache.get("P104"));

            // Test 4: Check size
            System.out.println("Cache size: " + cache.size());
            // Test 5: Update an existing key
            cache.put("P102", "Ishitha Updated");

            System.out.println("Updated P102: " + cache.get("P102"));
            System.out.println("Cache size after update: " + cache.size());
            // Test 6: LRU eviction
            LRUCache<String, String> evictionCache = new LRUCache<>(3);

            evictionCache.put("A", "Patient A");
            evictionCache.put("B", "Patient B");
            evictionCache.put("C", "Patient C");

// Access A to make it most recently used
            evictionCache.get("A");

// Insert D — B should be evicted
            evictionCache.put("D", "Patient D");

            System.out.println("A: " + evictionCache.get("A"));
            System.out.println("B: " + evictionCache.get("B"));
            System.out.println("C: " + evictionCache.get("C"));
            System.out.println("D: " + evictionCache.get("D"));
            System.out.println("Cache size: " + evictionCache.size());

// Test 7: Remove an existing entry
            LRUCache<String, String> removeCache = new LRUCache<>(3);

            removeCache.put("P101", "Ananya");
            removeCache.put("P102", "Ishitha");
            removeCache.put("P103", "Sonika");

            System.out.println("Removed P102: " + removeCache.remove("P102"));
            System.out.println("P102 after removal: " + removeCache.get("P102"));
            System.out.println("Cache size: " + removeCache.size());

// Test 8: Remove a non-existing entry
            System.out.println("Remove P104: " + removeCache.remove("P104"));
            // Test 9: Empty cache
            LRUCache<String, String> emptyCache = new LRUCache<>(3);

            System.out.println("Is empty: " + emptyCache.isEmpty());
            System.out.println("Size: " + emptyCache.size());

// Test 10: Remove from empty cache
            System.out.println("Remove from empty cache: " + emptyCache.remove("P101"));

// Test 11: Get from empty cache
            System.out.println("Get from empty cache: " + emptyCache.get("P101"));
            // Test 12: Capacity 100
            LRUCache<Integer, String> capacityCache = new LRUCache<>(100);

// Insert 100 records
            for (int i = 1; i <= 100; i++) {
                    capacityCache.put(i, "Patient" + i);
            }

            System.out.println("Size after 100 insertions: " + capacityCache.size());

// Insert record 101
            capacityCache.put(101, "Patient101");

            System.out.println("Size after 101 insertions: " + capacityCache.size());
            System.out.println("Record 1: " + capacityCache.get(1));
            System.out.println("Record 101: " + capacityCache.get(101));
            // Test 13: Updating an entry changes its recency
            LRUCache<String, String> updateCache = new LRUCache<>(3);

            updateCache.put("A", "Patient A");
            updateCache.put("B", "Patient B");
            updateCache.put("C", "Patient C");

// Update A, making it MRU
            updateCache.put("A", "Updated Patient A");

// Insert D, so B should be evicted
            updateCache.put("D", "Patient D");

            System.out.println("A: " + updateCache.get("A"));
            System.out.println("B: " + updateCache.get("B"));
            System.out.println("C: " + updateCache.get("C"));
            System.out.println("D: " + updateCache.get("D"));
            System.out.println("Size: " + updateCache.size());
    }
}
