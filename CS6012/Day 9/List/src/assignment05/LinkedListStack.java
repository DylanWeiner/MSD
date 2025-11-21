// By Sophia Perez and Dylan Weiner

package assignment05;

import java.util.NoSuchElementException;

public class LinkedListStack <E> implements Stack<E> {
    SinglyLinkedList<E> data = null;

    public LinkedListStack() {
        data = new SinglyLinkedList<>();
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public E peek() throws NoSuchElementException {
        if(data.isEmpty()) {
            throw new NoSuchElementException();
        }
        return data.getFirst();
    }

    @Override
    public E pop() throws NoSuchElementException {
        if(data.isEmpty()) {
            throw new NoSuchElementException();
        }
        return data.deleteFirst();
    }

    @Override
    public void push(E element) {
        data.insertFirst(element);
    }

    @Override
    public int size() {
        return data.size();
    }
}
