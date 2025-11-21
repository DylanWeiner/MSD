// By Sophia Perez and Dylan Weiner

package assignment05;

import java.util.*;
import java.util.Iterator;

public class SinglyLinkedList<E> implements List<E> {
    private class Node {
        E data = null;
        Node next = null;

        public Node(E data) {
            this.data = data;
            this.next = null;
        };

        public Node(E data, Node next) {
            this.data = data;
            this.next = next;
        };

        private Node getNextNode() {
            return next;
        }

        private E getNextData() {
            return data;
        }
    }
    private int size;
    private Node head;
    private Node tail;

    public SinglyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    @Override
    public void insertFirst(E element) {
        if (head != null) {
            Node temp = new Node(element, head);
            head = temp;
        }
        else if(tail != null) {
            head = new Node(element, tail);
        }
        else {
            head = new Node(element);
            head.next = null;
        }
        size++;
    }

    @Override
    public void insert(int index, E element) throws IndexOutOfBoundsException {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        if(isEmpty() || index == 0) {
            insertFirst(element);
        }
        else {
            Node prev = head;
            Node current = head;
            for (int i = 0; i < index; i++) {
                prev = current;
                current = current.next;
            }
            prev.next = new Node(element, current);
            size++;
        }
    }

    @Override
    public E getFirst() throws NoSuchElementException {
        if(isEmpty()) {
            throw new IndexOutOfBoundsException();
        }
        return head.data;
    }

    @Override
    public E get(int index) throws IndexOutOfBoundsException {
        if(index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        Node n = head;
        for (int i = 0; i < index; i++) {
            n = n.next;
        }
        return n.data;
    }

    @Override
    public E deleteFirst() throws NoSuchElementException {
        if(isEmpty()) {
            throw new NoSuchElementException();
        }
        if(head.next == null) {
            head = null;
            size--;
            return null;
        }
        head = head.next;
        size--;
        return head.data;
    }

    @Override
    public E delete(int index) throws IndexOutOfBoundsException {
        if(isEmpty()) {
            throw new IndexOutOfBoundsException();
        }
        if(index == 0) {
            return deleteFirst();
        }
        else {
            Node current = head;
            int count = 0;
            while (count < index - 1) {
                current = current.next;
                count++;
            }
            E data = current.next.data;
            current.next = current.next.next;
            size--;
            return data;
        }
    }

    @Override
    public int indexOf(E element) {
        int index = 0;
        for(int i = 0; i < size; i++) {
            if(head.data.equals(element)) {
                index = i;
            }
        }
        return index;
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
        head = new Node(null, null);
        size = 0;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        for(int i = 0; i < size; i++) {
            arr[i] = head.data;
            head = head.next;
        }
        return arr;
    }

    @Override
    public Iterator<E> iterator() {
        return new SinglyLinkedListIterator();
    }

    private class SinglyLinkedListIterator implements Iterator<E> {

            private Node next = head;
            private Node current = null;
            private Node previous = null;
            private boolean canRemove = false;

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public E next() {
                if(!hasNext()) {
                    throw new NoSuchElementException();
                }
                canRemove = true;

                E value = next.data;
                canRemove = true;
                previous = current;
                current = next;
                next = next.getNextNode();

                return value;
            }

            @Override
            public void remove() {
                if(!canRemove) {
                    throw new IllegalStateException();
                }
                if(current == head) {
                    deleteFirst();
                } else if(next != null) {
                    previous = new Node(next.data, current);
                }
            } //Store a reference to the previous node and modify it without starting from the beginning for O(1) implementation.
    }

}