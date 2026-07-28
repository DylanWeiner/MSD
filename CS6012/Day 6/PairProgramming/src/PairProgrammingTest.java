// By Alexis Blood & Dylan Weiner

import static org.junit.jupiter.api.Assertions.assertEquals;

class PairProgrammingTest {

    @org.junit.jupiter.api.Test
    void testfindMedian() {
        Integer[] arr1 = new Integer[]{5, 9, 3, 1, 2};
        assertEquals(3, PairProgramming.findMedian(arr1));
    }
    @org.junit.jupiter.api.Test
    void testfindMedianTwo() {
        String[] arr1 = new String[]{"People", "Person", "people", "pan", "person", "Pan", "dance"};
        assertEquals("dance", PairProgramming.findMedian(arr1));
    }
    @org.junit.jupiter.api.Test
    void testGenericFindMedian() {
        Integer[] arr1 = new Integer[]{5, 9, 3, 1, 2, 4, 8};
        assertEquals(4, PairProgramming.findMedian(Integer::compare, arr1));
    }
    @org.junit.jupiter.api.Test
    void testGenericFindMedianTwo() {
        Integer[] arr1 = new Integer[]{5, 9, 3, 1, 9, 9, 2};
        assertEquals(5, PairProgramming.findMedian(Integer::compare, arr1));
    }
    @org.junit.jupiter.api.Test
    void testGenericFindMedianThree() {
        String[] arr1 = new String[]{"A", "C", "B", "a", "c", "b"};
        assertEquals("C", PairProgramming.findMedian(String::compareTo, arr1));
    }
    @org.junit.jupiter.api.Test
    void testGenericFindMedianFour() {
        String[] arr1 = new String[]{"People", "Person", "people", "pan", "person", "Pan", "dance"};
        assertEquals("dance", PairProgramming.findMedian(String::compareTo, arr1));
    }
}