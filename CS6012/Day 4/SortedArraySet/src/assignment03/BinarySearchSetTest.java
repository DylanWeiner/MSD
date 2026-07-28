package assignment03;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BinarySearchSetTest {
    private BinarySearchSet<Integer> set = new BinarySearchSet<>();
    private BinarySearchSet<Integer> emptySet = new BinarySearchSet<>();
    private List<Integer> list = new ArrayList<>();
    private List<Integer> list2 = new ArrayList<>();
    private BinarySearchSet<String> stringSet;
    private BinarySearchSet<Integer> customComparatorSet;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        set = new BinarySearchSet<>();
        stringSet = new BinarySearchSet<>();
        customComparatorSet = new BinarySearchSet<>(Comparator.reverseOrder());

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
    void clear() {
        set.clear();
        assertTrue(set.isEmpty());
    } //Tests clear function actually empties array.

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
        set.clear();
        assertEquals(0, set.size());
        set.addAll(list);
        assertEquals(5, set.size());
        set.removeAll(list);
        assertEquals(0, set.size());
    } // checks size of the array.

    @org.junit.jupiter.api.Test
    void
    toArray() {
        int iter = 1;
        for(Object e : set.toArray()) {
            System.out.println("Value " + iter + ":" + e);
            iter++;
        } // Checks that the array is iterable when set to toArray().
    }

    @Test
    void testConstructors() {
        assertNotNull(emptySet);
        assertEquals(0, emptySet.size());
        assertNull(emptySet.comparator());
        assertNotNull(customComparatorSet);
        assertEquals(0, customComparatorSet.size());
        assertNotNull(customComparatorSet.comparator());
    }

    @Test
    void testAdd() {
        assertTrue(emptySet.add(5));
        assertTrue(emptySet.add(3));
        assertTrue(emptySet.add(7));
        assertEquals(3, emptySet.size());        // Test adding duplicate
        assertFalse(emptySet.add(5));
        assertEquals(3, emptySet.size());        // Test null element
        assertFalse(() -> emptySet.add(null));        // Test ordering
        assertEquals(3, emptySet.first());
        assertEquals(7, emptySet.last());
        BinarySearchSet<Integer> set = new BinarySearchSet<>();
        assertTrue(set.add(3));
    } //Tests add function works correctly

    @Test
    void testAddWithCustomComparator() {
        assertTrue(customComparatorSet.add(5));
        assertTrue(customComparatorSet.add(3));
        assertTrue(customComparatorSet.add(7));        // With reverse order comparator, 7 should be first and 3 should be last
        assertEquals(7, customComparatorSet.first());
        assertEquals(3, customComparatorSet.last());
    }

    @Test
    void testAddAll() {
        ArrayList<Integer> numbers = new ArrayList<>();
        numbers.add(5);
        numbers.add(3);
        numbers.add(7);
        numbers.add(5); // Duplicate
        assertTrue(emptySet.addAll(numbers));
        assertEquals(3, emptySet.size());        // Test adding same elements again
        assertFalse(emptySet.addAll(numbers));
        assertEquals(3, emptySet.size());
        set.addAll(list);
        assertEquals(8, set.last());
    } //Tests addAll function works correctly

    @Test
    void testFirst() {
        assertEquals(1, set.first());
        assertThrows(NoSuchElementException.class, () -> emptySet.first());
        emptySet.add(5);
        emptySet.add(3);
        emptySet.add(7);
        assertEquals(3, emptySet.first());
    }

    @Test
    void testLast() {
        assertEquals(3, set.last());
        assertThrows(NoSuchElementException.class, () -> emptySet.last());
        emptySet.add(5);
        emptySet.add(3);
        emptySet.add(7);
        assertEquals(7, emptySet.last());
        for(int i = 8; i <= 108; i++) {
            emptySet.add(i);
        }
        assertEquals(108, emptySet.last());
    }

    @Test
    void testContains() {
        assertTrue(set.contains(1));
        assertFalse(set.contains(10));
        emptySet.add(5);
        emptySet.add(3);
        emptySet.add(7);
        assertTrue(emptySet.contains(5));
        assertTrue(emptySet.contains(3));
        assertTrue(emptySet.contains(7));
        assertFalse(emptySet.contains(4));
        assertThrows(NullPointerException.class, () -> emptySet.contains(null));
    } //Tests contains function works correctly

    @Test
    void testContainsAll() {
        assertTrue(set.containsAll(list2));
        assertFalse(set.containsAll(list));
        emptySet.add(5);
        emptySet.add(3);
        emptySet.add(7);
        ArrayList<Integer> subset = new ArrayList<>();
        subset.add(3);
        subset.add(7);
        assertTrue(emptySet.containsAll(subset));
        subset.add(9);
        assertTrue(emptySet.containsAll(subset));
    } //Tests containsAll function works correctly

    @Test
    void testRemove() {
        emptySet.add(5);
        emptySet.add(3);
        emptySet.add(7);
        assertTrue(emptySet.remove(5));
        assertEquals(2, emptySet.size());
        assertFalse(emptySet.contains(5));        // Try to remove non-existent element
        assertFalse(emptySet.remove(10));
        assertEquals(2, emptySet.size());        // Try to remove null
        assertThrows(NullPointerException.class, () -> emptySet.remove(null));
    }

    @Test
    void testRemoveAll() {
        emptySet.add(5);
        emptySet.add(3);
        emptySet.add(7);

        ArrayList<Integer> toRemove = new ArrayList<>();
        toRemove.add(3);
        toRemove.add(7);
        toRemove.add(9); // Non-existent element
        assertTrue(emptySet.removeAll(toRemove));
        assertEquals(1, emptySet.size());
        assertFalse(toRemove.contains(5));
    }

    @Test
    void testClear() {
        set.add(5);
        set.add(3);
        set.add(7);
        set.clear();
        assertEquals(0, set.size());
        assertTrue(set.isEmpty());
    }

    @Test
    void testToArray() {
        emptySet.add(5);
        emptySet.add(3);
        emptySet.add(7);

        Object[] array = emptySet.toArray();
        assertEquals(3, array.length);
        assertEquals(3, array[0]);
        assertEquals(5, array[1]);
        assertEquals(7, array[2]);
    }

    @Test
    void testIterator() {
        var iterator = set.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next());
        assertEquals(2, iterator.next());
        assertEquals(3, iterator.next());
        assertFalse(iterator.hasNext());        // Test iterator’s remove method
        iterator = set.iterator();
        iterator.next();
        iterator.remove();  // remove 5 instead of 3
        assertEquals(2, set.size());
        assertFalse(set.contains(1));        // Test removing without calling next
        iterator = set.iterator();
        assertThrows(IllegalStateException.class, iterator::remove);
        iterator.next();
        iterator.remove();
        assertFalse(set.contains(2));
    }

    @Test
    void testStringSet() {
        stringSet.add("Bruce");
        stringSet.add("Peter");
        stringSet.add("Xavier");
        assertEquals("Bruce", stringSet.first());
        assertEquals("Xavier", stringSet.last());
        assertEquals(3, stringSet.size());
    }

    @Test
    void testResizing() {
        // Add more than initial capacity (100) elements
        for (int i = 0; i < 150; i++) {
            set.add(i);
        }
        assertEquals(150, set.size());
        // Verify all elements are still accessible
        for (int i = 0; i < 150; i++) {
            assertTrue(set.contains(i));
        }
    }
}