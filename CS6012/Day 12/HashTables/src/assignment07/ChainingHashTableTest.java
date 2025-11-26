package assignment07;

import com.sun.net.httpserver.Filter;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class ChainingHashTableTest {
    ChainingHashTable goodHT;
    ChainingHashTable mediocreHT;
    ChainingHashTable badHT;
    HashFunctor badHF;
    HashFunctor mediocreHF;
    HashFunctor goodHF;
    int capacity;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        capacity = 100;
        badHF = new ChainingHashTable.BadHashFunctor();
        mediocreHF = new ChainingHashTable.MediocreHashFunctor();
        goodHF = new ChainingHashTable.GoodHashFunctor();
        goodHT = new ChainingHashTable(capacity, goodHF);
        mediocreHT = new ChainingHashTable(capacity, mediocreHF);
        badHT = new ChainingHashTable(capacity, badHF);
    }

    @org.junit.jupiter.api.Test
    void add() {
        assertTrue(goodHT.add("Cat"));
        assertTrue(goodHT.add("Dog"));
        assertTrue(goodHT.add("Act"));
        assertFalse(goodHT.add("Cat"));
    }

    @org.junit.jupiter.api.Test
    void addAll() {
        LinkedList<String> l = new LinkedList<>();
        l.add("Cat");
        l.add("Dog");
        l.add("Act");
        l.add("Animal");
        assertTrue(goodHT.addAll(l));
        for(String s : l) {
            assertTrue(goodHT.contains(s));
        }
    }

    @org.junit.jupiter.api.Test
    void clear() {
        LinkedList<String> l = new LinkedList<>();
        l.add("Cat");
        l.add("Dog");
        l.add("Act");
        l.add("Animal");
        assertTrue(goodHT.addAll(l));
        assertTrue(goodHT.containsAll(l));
        goodHT.clear();
        assertFalse(goodHT.containsAll(l));
    }

    @org.junit.jupiter.api.Test
    void contains() {
        goodHT.add("Cat");
        goodHT.add("Dog");
        goodHT.add("Act");
        assertTrue(goodHT.contains("Cat"));
        assertFalse(goodHT.contains("Pet"));
    }

    @org.junit.jupiter.api.Test
    void containsAll() {
        LinkedList<String> l = new LinkedList<>();
        l.add("Cat");
        l.add("Dog");
        l.add("Act");
        l.add("Animal");
        assertTrue(goodHT.addAll(l));
        assertTrue(goodHT.containsAll(l));
    }

    @org.junit.jupiter.api.Test
    void isEmpty() {
        LinkedList<String> l = new LinkedList<>();
        l.add("Cat");
        l.add("Dog");
        l.add("Act");
        l.add("Animal");
        assertTrue(goodHT.addAll(l));
        assertTrue(goodHT.containsAll(l));
        goodHT.clear();
        assertTrue(goodHT.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void remove() {
        goodHT.add("Cat");
        goodHT.add("Dog");
        goodHT.add("Act");
        assertTrue(goodHT.contains("Cat"));
        assertTrue(goodHT.remove("Cat"));
        assertFalse(goodHT.contains("Cat"));
    }

    @org.junit.jupiter.api.Test
    void removeAll() {
        LinkedList<String> l = new LinkedList<>();
        l.add("Cat");
        l.add("Dog");
        l.add("Act");
        l.add("Animal");
        assertTrue(goodHT.addAll(l));
        assertTrue(goodHT.add("Friends"));
        assertTrue(goodHT.containsAll(l));
        assertTrue(goodHT.removeAll(l));
        assertFalse(goodHT.containsAll(l));
        assertTrue(goodHT.contains("Friends"));
    }

    @org.junit.jupiter.api.Test
    void size() {
        LinkedList<String> l = new LinkedList<>();
        l.add("Cat");
        l.add("Dog");
        l.add("Act");
        l.add("Animal");
        assertEquals(0, goodHT.size());
        goodHT.add("Cat");
        goodHT.add("Dog");
        goodHT.add("Act");
        goodHT.add("Animal");
        goodHT.add("Cat");
        assertEquals(4, goodHT.size());
        assertTrue(goodHT.remove("Cat"));
        assertEquals(3, goodHT.size());
        assertTrue(goodHT.removeAll(l));
        assertEquals(0, goodHT.size());
    }

    @org.junit.jupiter.api.Test
    void bucketCheck() {
        goodHT.add("Cat");
        goodHT.add("Dog");
        goodHT.add("Act");
        goodHT.add("Animal");
        goodHT.add("Cat");
        assertEquals(4, goodHT.bucketCheck());
        mediocreHT.add("Cat");
        mediocreHT.add("Dog");
        mediocreHT.add("Act");
        mediocreHT.add("Animal");
        mediocreHT.add("Cat");
        assertEquals(3, mediocreHT.bucketCheck());
        badHT.add("Cat");
        badHT.add("Dog");
        badHT.add("Act");
        badHT.add("Animal");
        badHT.add("Cat");
        assertEquals(2, badHT.bucketCheck());
    }
}

// for analysis use enum for testFunc to separate good, mediocre, and bad
// Use another hashmap to store n values and strings (HashMap<Integer, ArrayList<String>>
// We can use hashmaps to track collisions.