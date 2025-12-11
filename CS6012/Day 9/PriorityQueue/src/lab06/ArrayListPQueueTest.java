package lab06;

import static org.junit.jupiter.api.Assertions.*;

class ArrayListPQueueTest {
    ArrayListPQueue<Integer> queue = new ArrayListPQueue<>();

    @org.junit.jupiter.api.Test
    void heapify() {
    }

    @org.junit.jupiter.api.Test
    void add() {
        queue.add(9);
        queue.add(8);
        queue.add(7);
        queue.add(6);
        queue.add(1);
        queue.add(2);
        queue.add(3);
        queue.add(10);
        queue.add(4);
        queue.add(5);
    }

    @org.junit.jupiter.api.Test
    void removeMin() {

        queue.add(16);
        queue.add(31);
        queue.add(41);
        queue.add(13);
        queue.add(51);
        queue.add(100);
        queue.add(115);
        assertEquals(13,queue.removeMin());
        assertEquals(16,queue.removeMin());
        assertEquals(31,queue.removeMin());
        assertEquals(41,queue.removeMin());
        assertEquals(51,queue.removeMin());
        assertEquals(100,queue.removeMin());
        assertEquals(115,queue.removeMin());
    }

    @org.junit.jupiter.api.Test
    void isEmpty() {
    }
}