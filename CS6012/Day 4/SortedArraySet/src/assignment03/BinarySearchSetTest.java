package assignment03;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BinarySearchSetTest {
    BinarySearchSet<Integer> set = new BinarySearchSet<>();
    List<Integer> list = new ArrayList<>();
    List<Integer> list2 = new ArrayList<>();

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        set.add(1);
        set.add(10);
        set.add(3);

        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        list.add(8);

        list2.add(1);
        list2.add(2);
        list2.add(3);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void comparator() {
    }

    @org.junit.jupiter.api.Test
    void first() {
        assertEquals(1, set.first());
    }

    @org.junit.jupiter.api.Test
    void last() {
        assertEquals(3, set.last());
    }

    @org.junit.jupiter.api.Test
    void add() {
        BinarySearchSet<Integer> set = new BinarySearchSet<>();
        assertTrue(set.add(3));
    }

    @org.junit.jupiter.api.Test
    void addAll() {
        set.addAll(list);
        assertEquals(8, set.last());
    }

    @org.junit.jupiter.api.Test
    void clear() {
        set.clear();
        assertTrue(set.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void contains() {
        assertTrue(set.contains(1));
        assertFalse(set.contains(10));
    }

    @org.junit.jupiter.api.Test
    void containsAll() {
        assertTrue(set.containsAll(list2));
        assertFalse(set.containsAll(list));
    }

    @org.junit.jupiter.api.Test
    void isEmpty() {
        BinarySearchSet<Integer> set = new BinarySearchSet<>();
        assertTrue(set.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void iterator() {
    }

    @org.junit.jupiter.api.Test
    void remove() {
        set.remove(1);
        assertFalse(set.contains(1));
        assertTrue(set.remove(2));
        assertFalse(set.remove(4));
    }

    @org.junit.jupiter.api.Test
    void removeAll() {
        set.removeAll(list2);
        assertFalse(set.containsAll(list2));
    }

    @org.junit.jupiter.api.Test
    void size() {
        assertEquals(3, set.size());
    }

    @org.junit.jupiter.api.Test
    void toArray() {
        int iter = 1;
        for(Object e : set.toArray()) {
            System.out.println("Value " + iter + ":" + e);
            iter++;
        }
    }
}