package assignment03;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BinarySearchSet<E> implements SortedSet<E> {
    private E[] set = (E[]) new Object[1];
    private Comparator<? super E> comparator;
    private int size = 0;
    private Iterator<E> iterator;

    public BinarySearchSet() {
        this.set = set;
        this.iterator = iterator();
    }

    public BinarySearchSet(Comparator<? super E> comparator) {
        this.set = set;
        this.comparator = comparator;
        this.iterator = iterator();
    }

        @Override
        public Comparator<? super E> comparator() {
            return comparator;
        }

        @Override
        public E first() throws NoSuchElementException {
            return set[0];
        }

        @Override
        public E last() throws NoSuchElementException {
            return set[set.length - 1];
        }

        @Override
        public boolean add(E element) {
            boolean isContained = false;
            if(size()+1>set.length) {
                doubleArray();
            }
            for(E e : set) {
                if(!e.equals(element)) {
                    isContained = true;
                }
            }
            if(!isContained && element!=null) {
                for(int i = 0; i < set.length - 1; i++) {
                    if(set[i] == null) {
                        set[i] = element;
                        size++;
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends E> elements) {
            boolean result = false;
            boolean isContained = false;
            if(size()+elements.size()>set.length) {
                doubleArray();
            }
            for(E e : elements) {
                for (int i = 0; i < elements.size(); i++) {
                    if (!set[i].equals(e)) {
                        isContained = true;
                    }
                }
                if (!isContained) {
                    for (int i = 0; i < set.length - 1; i++) {
                        if (set[i] == null) {
                            set[i] = e;
                            size++;
                            result = true;
                        }
                    }
                }
            }
            return result;
        }

        @Override
        public void clear() {
            for(int i = set.length-1; i > 0; i--) {
                while (set[i] != null) {
                    set[i] = null;
                }
            }
        }

        @Override
        public boolean contains(E element) {
            for (E e : set) {
                if (element.equals(e)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean containsAll(Collection<? extends E> elements) {
            boolean isContained = false;
            for (E e : elements) {
                if (contains(e)) {
                    isContained = true;
                } else {
                    return false;
                }
            }
            return isContained;
        }

        @Override
        public boolean isEmpty() {
            boolean noValue = true;
            for(int i = 0; i < set.length-1; i++) {
                if (set[i] != null) {
                    noValue = false;
                }
            }
            return noValue;
        }

        @Override
        public Iterator<E> iterator() {
            return iterator;
        }

        @Override
        public boolean remove(E element) {
            int index;
            if(!contains(element)) {
                for (int i = 0; i < set.length - 1; i++) {
                    if (set[i].equals(element)) {
                        set[i] = null;
                        index = i;
                        for (int j = index; j < set.length - 1; j++) {
                            set[j] = set[j + 1];
                        }
                        size--;
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public boolean removeAll(Collection<? extends E> elements) {
            boolean isRemoved = false;
            int numRemoved = 0;
            int index;
            for(E e : elements) {
                if(!contains(e)) {
                    for (int i = 0; i < set.length - 1; i++) {
                        if (set[i].equals(e)) {
                            set[i] = null;
                            index = i;
                            for (int j = index; j < set.length - 1; j++) {
                                set[j] = set[j + 1];
                            }
                            isRemoved = true;
                            numRemoved++;
                        }
                    }
                }
            }
            size-=numRemoved;
            return isRemoved;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public Object[] toArray() {
            E temp;
            E[] sortedSet = (E[]) new Object[size];
            for(int i = 0; i < sortedSet.length; i++) {
                sortedSet[i] = set[i];
            }
            for(int i = 1; i < sortedSet.length; i++) {
                for(int j = i; j < sortedSet.length; j++) {
                    if (comparator.compare(sortedSet[i], sortedSet[i - 1]) < 0) {
                        temp = sortedSet[i];
                        sortedSet[i] = sortedSet[i - 1];
                        sortedSet[i - 1] = temp;
                    }
                }
            }
            return ((Object[]) sortedSet);
        }

        public void doubleArray() {
            E[] newSet = (E[]) new Object[set.length*2];
            for(int i = 0; i < set.length-1; i++) {
                newSet[i] = set[i];
            }
            set = newSet;
        }
}
