package assignment06;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BinarySearchTreeTest {
    private List<Integer> list = new ArrayList<>();
    private BinarySearchTree<Integer> BST = new BinarySearchTree<>();

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        assertTrue(BST.add(7));
    }

    @org.junit.jupiter.api.Test
    void addRecursive() {
    }

    @org.junit.jupiter.api.Test
    void add() {
        assertTrue(BST.add(8));
    }

    @org.junit.jupiter.api.Test
    void addAll() {
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        assertTrue(BST.addAll(list));
    }

    @org.junit.jupiter.api.Test
    void clear() {
    }

    @org.junit.jupiter.api.Test
    void contains() {
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        assertTrue(BST.addAll(list));
//        BST.normalizeTree();
        assertTrue(BST.contains(5));
    }

    @org.junit.jupiter.api.Test
    void containsAll() {
    }

    @org.junit.jupiter.api.Test
    void first() {

    }

    @org.junit.jupiter.api.Test
    void isEmpty() {
    }

    @org.junit.jupiter.api.Test
    void last() {
    }

    @org.junit.jupiter.api.Test
    void remove() {
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        assertTrue(BST.addAll(list));
        assertTrue(BST.remove(4));
        assertFalse(BST.remove(12));
        assertFalse(BST.contains(4));
        ArrayList<Integer> list2 = BST.toArrayList();
        for (int i = 0; i < list2.size(); i++) {
//            assertEquals(list2.get(i), list.get(i));
            System.out.println(list2.get(i));
        }
    }

    @org.junit.jupiter.api.Test
    void removeAll() {
    }

    @org.junit.jupiter.api.Test
    void size() {
    }

    @org.junit.jupiter.api.Test
    void toArrayList() {
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        assertTrue(BST.addAll(list));
        list.add(7);
        ArrayList<Integer> list2 = BST.toArrayList();
        for (int i = 0; i < list2.size(); i++) {
//            assertEquals(list2.get(i), list.get(i));
            System.out.println(list2.get(i));
        }
    }
}