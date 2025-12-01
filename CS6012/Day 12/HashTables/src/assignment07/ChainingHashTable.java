package assignment07;

import java.util.Collection;
import java.util.LinkedList;

public class ChainingHashTable implements Set<String> {
    private LinkedList<String>[] storage;
    private HashFunctor functor;
    private int intialCapacity;
    private int size;

    public int hashcode(String item) {
        int index = functor.hash(item) %  storage.length;
        if(index < 0) {
            return index + storage.length;
        } else {
            return index;
        }
    }

        @SuppressWarnings("unchecked")
    public ChainingHashTable(int capacity, HashFunctor functor) {
        this.intialCapacity = capacity;
        this.size = 0;
        this.functor = functor;
        storage = (LinkedList<String>[]) new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            storage[i] = new LinkedList<>();
        }
    }

    @Override
    public boolean add(String item) {
        if(contains(item) || item == null) {
            return false;
        }
        int hashValue = hashcode(item);
        storage[hashValue].add(item);
        size++;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends String> items) {
        boolean changed = false;
        for(String item : items) {
            if(add(item)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public void clear() {
        storage = new LinkedList[intialCapacity];
        size = 0;
    }

    @Override
    public boolean contains(String item) {
        if(isEmpty() || item == null) {
            return false;
        }
        int hashValue = hashcode(item);
        if(storage[hashValue] != null) {
            return storage[hashValue].contains(item);
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<? extends String> items) {
        boolean contained = false;
        if(items.isEmpty()) {
            return true;
        }
        if(isEmpty()) {
            return contained;
        }
        for(String item : items) {
            if(contains(item)) {
                contained = true;
            }
        }
        return contained;
    }

    @Override
    public boolean isEmpty() {
        if(size == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(String item) {
        int hashValue = hashcode(item);
        if(storage[hashValue].remove(item)) {
            size--;
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<? extends String> items) {
        boolean changed = false;
        for(String item : items) {
            if(remove(item)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public int size() {
        return size;
    }

    public int bucketCheck() {
        int filledBuckets = 0;
        for(LinkedList<String> bucket : storage) {
            if(!bucket.isEmpty()) {
                filledBuckets++;
            }
        }
        return filledBuckets;
    }

    public int collisions() {
        int collisions = 0;
        for(LinkedList<String> bucket : storage) {
            if(bucket != null && bucket.size() > 1) {
                collisions += bucket.size() - 1;
            }
        }
        return collisions;
    }
}
