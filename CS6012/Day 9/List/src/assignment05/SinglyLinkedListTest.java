// By Sophia Perez and Dylan Weiner

package assignment05;

import static org.junit.jupiter.api.Assertions.*;
import assignment05.SinglyLinkedList;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;


class SinglyLinkedListTest {
    SinglyLinkedList<Integer> list;
    SinglyLinkedList<Integer> list2;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        list = new SinglyLinkedList<>();
        list2 = new SinglyLinkedList<>();

        list2.insert(0,5);
        list2.insert(1,3);
        list2.insert(2,7);
        list2.insert(3,1);
    }

    @org.junit.jupiter.api.Test
    void insertFirst() {
        list.insertFirst(7);
        assertTrue(list.getFirst() == 7);
        list.insertFirst(5);
        assertTrue(list.getFirst() == 5);
        list.insertFirst(3);
        assertTrue(list.getFirst() == 3);
    }

    @org.junit.jupiter.api.Test
    void insert() {
        list.insert(0,5);
        assertEquals(5,list.get(0));
        list.insert(1,3);
        assertEquals(3,list.get(1));
        list.insert(2,7);
        assertEquals(7,list.get(2));
        list.insert(3,1);
        assertEquals(1,list.get(3));
        list.insert(0,9);
        assertEquals(9,list.get(0));
        assertEquals(5,list.get(1));
    }

    @org.junit.jupiter.api.Test
    void getFirst() {
        list.insertFirst(7);
        assertTrue(list.getFirst() == 7);
        list.insertFirst(5);
        assertTrue(list.getFirst() == 5);
        list.insertFirst(3);
        assertTrue(list.getFirst() == 3);
    }

    @org.junit.jupiter.api.Test
    void get() {
        list.insertFirst(7);
        list.insertFirst(5);
        list.insertFirst(3);
        assertTrue(list.get(0) == 3);
        assertTrue(list.get(2) == 7);
        assertTrue(list.get(1) == 5);
    }

    @org.junit.jupiter.api.Test
    void deleteFirst() {
        assertEquals(5, (int) list2.getFirst());
        list2.deleteFirst();
        assertEquals(3, (int) list2.getFirst());
        list2.deleteFirst();
        assertEquals(7, (int) list2.getFirst());
        list2.deleteFirst();
        assertEquals(1, (int) list2.getFirst());
        list2.deleteFirst();
        assertTrue(list2.isEmpty());
        assertThrows(NoSuchElementException.class, () -> list2.deleteFirst());
    }

    @org.junit.jupiter.api.Test
    void delete() {
        assertEquals(3, list2.get(1));
        assertEquals(3, list2.delete(1));
        assertEquals(7, list2.get(1));
        list2.delete(1);
        assertEquals(1, list2.get(1));
        list2.delete(0);
        assertEquals(1, list2.get(0));
        list2.delete(0);
        assertTrue(list2.isEmpty());
        assertThrows(IndexOutOfBoundsException.class, () -> list2.delete(0));
    }

    @org.junit.jupiter.api.Test
    void indexOf() {

    }

    @org.junit.jupiter.api.Test
    void size() {
        assertEquals(4, list2.size());
    }

    @org.junit.jupiter.api.Test
    void isEmpty() {
        assertFalse(list2.isEmpty());
        assertTrue(list.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void clear() {
        assertFalse(list2.isEmpty());
        list2.clear();
        assertTrue(list2.isEmpty());
        list2.clear(); // Doesn't error out from clearing an empty list.
    }

    @org.junit.jupiter.api.Test
    void toArray() {
        Object arr[] = list2.toArray();
        for(int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }

    @org.junit.jupiter.api.Test
    void iterator() {
        var  iterator = list2.iterator();
        while(iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
    @org.junit.jupiter.api.Test
    void iterator2() {
        var  iterator = list2.iterator();
        while(iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
        assertTrue(list2.isEmpty());
    }
}