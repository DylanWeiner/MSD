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
    private Node head;
    private Node tail;

    public SinglyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    @Override
    public void insertFirst(E element) {
        head = new Node(element, head);
        size++;
    }

    @Override
    public void insert(int index, Object element) throws IndexOutOfBoundsException {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException;
        }
        if(isEmpty()) {
            insertFirst(element);
        }
        for(int i = 0; i < index; i++) {
            current = current.next;
            if(i == index) {
                current = element;
            }
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
        Node n = head;
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
        if(isEmpty()) {
            throw new NoSuchElementException;
        }
        head = head.next;
        return head.data;
    }

    @Override
    public E delete(int index) throws IndexOutOfBoundsException {
        if(isEmpty()) {
            throw new IndexOutOfBoundsException;
        }
        Node n = head;
        while(hasNext()) {
            n = n.next;
            if(i == index) {
                Node val = n;
                n = n.next;
            }
        }
        n.next = null;
        return val.data;
    }

    @Override
    public int indexOf(Object element) {
        return null;
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
        return new LinkedListIterator<E>();
    }
    private class LinkedListIterator<E> implements Stack<E> {
        private Node current;
        private Node previous;
        public Node beforePrev;
        private boolean canRemove;

        void LinkedListIterator(){
            current = head;
            previous = null;
            beforePrev = null;
            canRemove = false;
        }

        @Override
        public void clear() {

        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public E peek() throws NoSuchElementException {
            return null;
        }

        @Override
        public E pop() throws NoSuchElementException {
            return null;
        }

        @Override
        public void push(E element) {

        }

        @Override
        public int size() {
            return 0;
        }
    }

}