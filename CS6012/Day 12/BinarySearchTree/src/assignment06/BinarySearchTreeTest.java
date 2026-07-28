package assignment06;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BinarySearchTreeTest {
    private List<Integer> list = new ArrayList<>();
    private List<String> listString = new ArrayList<>();
    private BinarySearchTree<Integer> BST = new BinarySearchTree<>();
    private BinarySearchTree<String> StringBST = new BinarySearchTree<>();

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
        BST.remove(7);
        for(int i = 1; i <= 10; i++) {
            BST.add(i);
        }
        BST.clear();
        assertTrue(BST.isEmpty());
        for(Integer i : BST.toArrayList()) {
            System.out.println(i);
        }
        BST.remove(5);
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
        BST.remove(7);
        assertTrue(BST.isEmpty());
        list.add(1);
        list.add(2);
        BST.addAll(list);
        assertFalse(BST.isEmpty());
        BST.removeAll(list);
        assertTrue(BST.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void last() {
        assertEquals(1, BST.size());
        BST.clear();
        BST.add(8);
        assertEquals(8, BST.last());
        assertEquals(1, BST.size());
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
        assertTrue(BST.remove(7));
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
        for(int i = 1; i <= 10000; i++) {
            list.add(i);
        }
        BST.addAll(list);
        assertTrue(BST.containsAll(list));
        assertEquals(10000, BST.size());
        BST.removeAll(list);
        for(Integer i : BST.toArrayList()) {
            System.out.println("List: " + i);
        }
        assertEquals(0, BST.size());
        assertFalse(BST.containsAll(list));

        listString.add("Hello");
        listString.add("World");
        listString.add("My");
        listString.add("Goat");
        StringBST.addAll(listString);
        assertEquals(4, StringBST.size());
        StringBST.removeAll(listString);
        assertEquals(0, BST.size());
    }

    @org.junit.jupiter.api.Test
    void size() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("texts.txt"));
            String[] dimensions = br.readLine().split(", ");
            int testSize = 0;
            for(String dimension : dimensions) {
                listString.add(dimension);
                testSize++;
            }
        System.out.println(testSize);
        StringBST.addAll(listString);
        assertEquals(36, StringBST.size());
        StringBST.remove("assignment");
        assertFalse(StringBST.contains("assignment"));
        StringBST.removeAll(listString);
        assertEquals(0, StringBST.size());

    }

    @org.junit.jupiter.api.Test
    void sizeTwo() {
        BST.clear();
        BST.add(5);
        BST.add(2);
        BST.add(4);
        BST.add(3);
        BST.add(1);

        BST.remove(2);

        assertFalse(BST.contains(2));
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