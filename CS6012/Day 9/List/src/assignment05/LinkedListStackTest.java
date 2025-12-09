package assignment05;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkedListStackTest {
    LinkedListStack<Integer> set = new LinkedListStack<>();

    @Test
    void clear() {
    }

    @Test
    void isEmpty() {
    }

    @Test
    void peek() {
    }

    @Test
    void pop() {
        set.push(1);
        assertEquals(1, set.pop());
    }

    @Test
    void push() {
        set.push(1);
    }

    @Test
    void size() {
    }
}