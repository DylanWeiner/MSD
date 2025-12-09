package assignment06;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

public class BinarySearchTree<E extends Comparable<? super E>> implements SortedSet<E> {
    private static class Node<E> {
        E data = null;
        Node<E> left = null;
        Node<E> right = null;

        public Node(E data) {
            this.data = data;
            this.left = null;
            this.right = null;
        };

        private Node<E> getRightNode() {
            return right;
        }

        private Node<E> getLeftNode() {
            return left;
        }

        private E getData() {
            return data;
        }
    }
    private int size = 0;
    private Node<E> root;
    int decCount = 0;

    private Node<E> addRecursive(E item, Node<E> root) {
        if (root == null) {
            root = new Node<>(item);
            size++;
            return root;
        }
        else if(item.compareTo(root.getData()) < 0) {
            root.left = addRecursive(item, root.left);
        }
        else if(item.compareTo(root.getData()) > 0) {
            root.right = addRecursive(item, root.right);
        }
        return root;
    }

    @Override
    public boolean add(E item) {
        if(item == null) {
            throw new NullPointerException("item is null");
        }
        int prevSize = size;
        root = addRecursive(item, root);
        return size == prevSize + 1;
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
        root = null;
        size = 0;
    }

    private boolean containsRecursive(E item, Node<E> root) {
        if (root == null) {
            return false;
        }
        else if(item.compareTo(root.getData()) < 0) {
            return containsRecursive(item, root.left);
        }
        else if(item.compareTo(root.getData()) > 0) {
            return containsRecursive(item, root.right);
        }
        else { // if(item.compareTo(root.getData()) == 0)
            return true;
        }
    }

    @Override
    public boolean contains(E item) {
        if(item == null) {
            throw new NullPointerException("item is null");
        }
        return containsRecursive(item, root);
    }

    @Override
    public boolean containsAll(Collection<? extends E> items) {
        boolean result = false;
        for(E item : items) {
            if(contains(item)) {
                result = true;
            }
            else {
                return false;
            }
        }
        return result;
    }

    @Override
    public E first() throws NoSuchElementException {
        Node<E> temp = root;
        if(root == null) {
            throw new NoSuchElementException();
        }
        while(temp.left != null) {
            temp = temp.left;
        }
        return temp.data;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public E last() throws NoSuchElementException {
        Node<E> temp = root;
        if(root == null) {
            throw new NoSuchElementException();
        }
        while(temp.right != null) {
            temp = temp.right;
        }
        return temp.data;
    }

    private Node<E> getSuccessor(Node<E> node) {
        node = node.right;
        while (node != null && node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node<E> removeRecursive(E item, Node<E> root) {
        if (root == null) {
            return null;
        }
        if(item.compareTo(root.getData()) < 0) {
            root.left = removeRecursive(item, root.left);
        }
        else if(item.compareTo(root.getData()) > 0) {
            root.right = removeRecursive(item, root.right);
        } else { // if(item.compareTo(root.getData()) == 0)
            if (root.left == null) {
                size--;
                return root.right;
            }
            else if (root.right == null) {
                size--;
                return root.left;
            }
            else{
                Node<E> successor = getSuccessor(root);
                root.data = successor.data;
                root.right = removeRecursive(successor.data, root.right);

            }
        }

        return root;
    }

    private Node<E> removeMin(Node<E> root) {
        if(root.left == null) {
            return root.right;
        }
        root.left = removeMin(root.left);
        return root;
    }

    @Override
    public boolean remove(E item) {
        if(item == null) {
            throw new NullPointerException("item is null");
        }
        int prevSize = size;
        root = removeRecursive(item, root);
        return size < prevSize;
    }

    @Override
    public boolean removeAll(Collection<? extends E> items) {
        boolean result = false;
        for(E item : items) {
            if(remove(item)) {
                result = true;
            }
        }
//        System.out.println(decCount);
        System.out.println(size);
        return result;
    }

    @Override
    public int size() {
        return size;
    }

    private void RecursiveSearchLowest(Node<E> root, ArrayList<E> list) {
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
        RecursiveSearchLowest(root, list);
        return list;
    }
}
