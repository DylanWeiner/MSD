package assignment03;

import java.util.*;

public class BinarySearchSet<E> implements SortedSet<E> {
    private E[] set;
    private Comparator<? super E> comparator;
    private int size = 0;

    public BinarySearchSet() {
        this.set = (E[]) new Object[10];
    } // This uses natural ordering

    public BinarySearchSet(Comparator<? super E> comparator) {
        this.set = (E[]) new Object[10];
        this.comparator = comparator;
    } // This uses the comparator specified

        @Override
        public Comparator<? super E> comparator() {
            return comparator;
        }

        @Override
        public E first() throws NoSuchElementException {
            if(size == 0) {
                return null;
            }
            return set[0];
        } // Returns the first value

        @Override
        public E last() throws NoSuchElementException {
            if(size == 0) {
                return null;
            }
            return set[size - 1];
        } // Returns last value

        @Override
        public boolean add(E element) {
            if(element == null) {
                return false;
            }
            if(!contains(element)) {
                if(size+1>=set.length) {
                    doubleArray();
                }
                int index = -(BinarySearch(element));
                for(int i = size; i > index; i--) {
                    set[i] = set[i-1];
                }
                set[index] = element;
                size++;
                return true;
            }
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends E> elements) {
            boolean result = false;
            for(E e : elements) {
                if(add(e)) {
                    result = true;
                }
            }
            return result;
        }

        @Override
        public void clear() {
            E[] newSet = (E[]) new Object[10];
            size = 0;
            set = newSet;
        }

        @Override
        public boolean contains(E element) {
        if (BinarySearch(element) > 0) {
            return true;
        }
        return false;
        }

        @Override
        public boolean containsAll(Collection<? extends E> elements) {
            boolean isContained = false;
            for (E e : elements) {
                if (contains(e)) {
                    isContained = true;
                }
            }
            return isContained;
        }

        @Override
        public boolean isEmpty() {
            boolean noValue = true;
            for(int i = 0; i < set.length; i++) {
                if (set[i] != null) {
                    noValue = false;
                }
            }
            return noValue;
        }

        @Override
        public Iterator<E> iterator() {
            return null;
        }

//        @Override
//        public boolean hasNext() {
//            if(size()<set.length) {
//                return true;
//            }
//            return false;
//        }

        @Override
        public boolean remove(E element) {
            int index;
            if(contains(element)) {
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
            for(E e : elements) {
                if(remove(e)) {
                    isRemoved = true;
                }
            }
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

        private int BinarySearch(E element) {
            int low = 0;
            int high = size - 1;
            while(low <= high) {
                int mid = (low + high)/2;
                if((comparator != null && element.equals(set[mid]))
                        || (comparator == null && (((Comparable<E>) element).equals((E) set[mid])))) {
                    return mid;
                }
                else if((comparator != null && comparator.compare(element, set[mid]) < 0)
                        || ((Comparable<E>) element).compareTo(set[mid]) < 0) {
                    low = mid + 1;
                }
                else if((comparator != null && comparator.compare(element, set[mid]) > 0)
                        || (comparator == null && (((Comparable<E>) element).compareTo((E) set[mid]) > 0))) {
                    high = mid - 1;
                }
            }
            return -(low + 1);
        }

        public void doubleArray() {
            E[] newSet = Arrays.copyOf(set, set.length * 2);
            set = newSet;
        }
}
