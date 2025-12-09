package assignment05;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayStackTest {
    ArrayStack<Integer> set = new ArrayStack<>();

    @BeforeEach
    void setUp() {
    }

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
        set.pop();
        assertTrue(set.isEmpty());
    }

    @Test
    void push() {
        set.push(1);
    }

    @Test
    void size() {
    }
}