package assignment06;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

import static java.util.Collections.swap;

public class BinarySearchTree<E extends Comparable<? super E>> implements SortedSet<E> {
    private class Node {
        E data = null;
        Node left = null;
        Node right = null;

        public Node(E data) {
            this.data = data;
            this.left = null;
            this.right = null;
        };

        private Node getRightNode() {
            return right;
        }

        private Node getLeftNode() {
            return left;
        }

        private E getData() {
            return data;
        }
    }
    private int size;
    private Node head;
    private int found = 0;

    public Node addRecursive(E item, Node root) {
        if (root == null) {
            root = new Node(item);
            size++;
        }
        else if(item.compareTo(root.getData()) < 0) {
            root.left = addRecursive(item, root.left);
        }
        else { // if(item.compareTo(head.getData()) > 0)
            root.right = addRecursive(item, root.right);
        }
        return root;
    }

    @Override
    public boolean add(E item) {
        if(head == null) {
            head = new Node(item);
            return true;
        }
        if(item == null) {
            throw new NullPointerException("item is null");
        }
        int prevSize = size;
        addRecursive(item, head);
        if(prevSize != size) {
            return true;
        }
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> items) {
        boolean result = false;
        if(items.isEmpty()) {
            return result;
        }
        for(E item : items) {
            if(add(item)) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
    }

    public Node containsRecursive(E item, Node root) {
        if (root == null) {
            return null;
        }
        else if(item.compareTo(root.getData()) < 0) {
            root.left = containsRecursive(item, root.left);
        }
        else if(item.compareTo(root.getData()) > 0) {
            root.right = containsRecursive(item, root.right);
        }
        else { // if(item.compareTo(root.getData()) == 0)
            found++;
            return root;
        }
        return root;
    }

    @Override
    public boolean contains(E item) {
        if(head == null) {
            return false;
        }
        containsRecursive(item, head);
        if(found == 1) {
            found--;
            return true;
        };
        return false;
    }

    @Override
    public boolean containsAll(Collection<? extends E> items) {
        boolean result = false;
        for(E item : items) {
            if(contains(item)) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public E first() throws NoSuchElementException {
        Node temp = head;
        if(head == null) {
            throw new NoSuchElementException();
        }
        while(temp.left != null) {
            temp = temp.right;
        }
        if(temp.right != null) {
            temp = temp.right;
        }
        return temp.data;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public E last() throws NoSuchElementException {
        Node temp = head;
        if(head == null) {
            throw new NoSuchElementException();
        }
        while(temp.right != null) {
            temp = temp.right;
        }
        if(temp.left != null) {
            temp = temp.left;
        }
        return temp.data;
    }

    public Node getSuccessor(Node node) {
        node = node.right;
        while (node != null && node.left != null) {
            node = node.left;
        }
        return node;
    }

    public Node removeRecursive(E item, Node root) {
        if (root == null) {
            return root;
        }
        else if(item.compareTo(root.getData()) < 0) {
            root.left = removeRecursive(item, root.left);
        }
        else if(item.compareTo(root.getData()) > 0) {
            root.right = removeRecursive(item, root.right);
        } else { // if(item.compareTo(root.getData()) == 0)
            size--; // Found it!
            if (root.left == null) { return root.right; }
            if (root.right == null) { return root.left; }

            Node successor = getSuccessor(root);
            root.data = successor.data;
            root.right = removeRecursive(successor.data, root.right);
        }
        return root;
    }

    @Override
    public boolean remove(E item) {
        if(head == null || item == null) {
            throw new NullPointerException("item or head is null");
        }
        int prevSize = size;
        removeRecursive(item, head);
        if(size != prevSize) {
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<? extends E> items) {
        boolean result = false;
        for(E item : items) {
            if(remove(item)) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public int size() {
        return size;
    }

    public void RecursiveSearchLowest(Node root, ArrayList<E> list) {
        if (root == null) {
            return;
        }
        RecursiveSearchLowest(root.left, list);
        list.add(root.getData());
        RecursiveSearchLowest(root.right, list);
    }

    @Override
    public ArrayList<E> toArrayList() {
        ArrayList<E> list = new ArrayList<>();
        RecursiveSearchLowest(head, list);
        return list;
    }
}
