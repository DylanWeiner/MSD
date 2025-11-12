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
        set.add(2);
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

    @org.junit.jupiter.api.Test
    void first() {
        assertEquals(1, set.first());
    } //Tests first value is correctly returned

    @org.junit.jupiter.api.Test
    void last() {
        assertEquals(3, set.last());
    } //Tests last value is correctly returned

    @org.junit.jupiter.api.Test
    void add() {
        BinarySearchSet<Integer> set = new BinarySearchSet<>();
        assertTrue(set.add(3));
    } //Tests add function works correctly

    @org.junit.jupiter.api.Test
    void addAll() {
        set.addAll(list);
        assertEquals(8, set.last());
    } //Tests addAll function works correctly

    @org.junit.jupiter.api.Test
    void clear() {
        set.clear();
        assertTrue(set.isEmpty());
    } //Tests clear function actually empties array.

    @org.junit.jupiter.api.Test
    void contains() {
        assertTrue(set.contains(1));
        assertFalse(set.contains(10));
    } //Tests contains function works correctly

    @org.junit.jupiter.api.Test
    void containsAll() {
        assertTrue(set.containsAll(list2));
        assertFalse(set.containsAll(list));
    } //Tests containsAll function works correctly

    @org.junit.jupiter.api.Test
    void isEmpty() {
        BinarySearchSet<Integer> set2 = new BinarySearchSet<>();
        assertTrue(set2.isEmpty());
        assertFalse(set.isEmpty());
    } //Tests isEmpty function correctly checks array.

    @org.junit.jupiter.api.Test
    void remove() {
        set.remove(1); // Tests remove function works correctly
        assertFalse(set.contains(1)); // Tests value is no longer contained
        assertTrue(set.remove(2)); // Tests remove returns correct boolean response.
        assertFalse(set.remove(4));
    }

    @org.junit.jupiter.api.Test
    void removeAll() {
        set.removeAll(list2); // tests removeAll function
        assertFalse(set.containsAll(list2)); //tests containAll and that the values were properly removed.
    }

    @org.junit.jupiter.api.Test
    void size() {
        assertEquals(3, set.size());
    } // checks size of the array.

    @org.junit.jupiter.api.Test
    void toArray() {
        int iter = 1;
        for(Object e : set.toArray()) {
            System.out.println("Value " + iter + ":" + e);
            iter++;
        } // Checks that the array is iterable when set to toArray().
    }
}