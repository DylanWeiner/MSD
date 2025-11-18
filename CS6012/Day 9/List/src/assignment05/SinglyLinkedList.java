package assignment05;

import java.util.*;

public class SinglyLinkedList<E> implements List<E> {
    private class Node {
        E data;
        Node next;

        public Node(E data, Node next) {
            this.data = data;
            this.next = next;
        };
    }

    private List<E> myList;
    private int size;
    private Node current;
//    private Node head;

    public SinglyLinkedList() {
        this.current = null;
        this.size = 0;
    }

    @Override
    public void insertFirst(E element) {
        current = new Node(element, current);
        size++;
    }

    @Override
    public void insert(int index, E element) throws IndexOutOfBoundsException {
        ListIterator<E> listIterator = (ListIterator<E>) myList.iterator();
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Out of Bounds!");
        }

        if(index == (listIterator.nextIndex()-1)) {
            listIterator.add(element);
        }
    }

    @Override
    public E getFirst() throws NoSuchElementException {
        if(isEmpty()) {
            throw new IndexOutOfBoundsException();
        }

        return current.data;
    }

    @Override
    public E get(int index) throws IndexOutOfBoundsException {
        Node n = current;
        for (int i = 0; i < myList.size(); i++) {
            n = n.next;
            if(index == i) {
                return n.data;
            }
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public E deleteFirst() throws NoSuchElementException {
        return null;
    }

    @Override
    public E delete(int index) throws IndexOutOfBoundsException {
        return null;
    }

    @Override
    public int indexOf(E element) {
        return 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public Iterator<E> iterator() {
        return new ListIterator<>() {
            private int index = 0;
            private Node current = SinglyLinkedList.this.current;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public E next() {
                if(!hasNext()) {
                    return null;
                }
                current = current.next;
                nextIndex();
                return current.data;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public E previous() {
                return null;
            }

            @Override
            public int nextIndex() {
                return index++;
            }

            @Override
            public int previousIndex() {
                return 0;
            }

            @Override
            public void remove() {

            } //Store a reference to the previous node and modify it without starting from the beginning for O(1) implementation.

            @Override
            public void set(E e) {

            }

            @Override
            public void add(E e) {
                current = new Node(e, current);
                size++;
            }
        };
    }
}