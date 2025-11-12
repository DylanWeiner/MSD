package assignment03;

import java.util.*;

public class BinarySearchSet<E> implements SortedSet<E> {
    private E[] set;
    private Comparator<? super E> comparator;
    private int size = 0;

    public BinarySearchSet() {
        this.set = (E[]) new Object[10];
        this.comparator = null;
    } // This uses natural ordering

    public BinarySearchSet(Comparator<? super E> comparator) {
        this.set = (E[]) new Object[10];
        this.comparator = comparator;
    } // This uses the comparator specified

        @Override
        public Comparator<? super E> comparator() {
            return comparator;
        } // sets non-natural ordering comparator.

        @Override
        public E first() throws NoSuchElementException {
            if(size == 0) { // returns null for first value of empty arrays
                return null;
            }
            return set[0];
        } // Returns the first value

        @Override
        public E last() throws NoSuchElementException {
            if(size == 0) { // returns null for last value of empty arrays
                return null;
            }
            return set[size - 1];
        } // Returns last value

        @Override
        public boolean add(E element) {
            if(element == null) {
                return false;
            } // returns false if element submitted is null
            if(!contains(element)) {
                if(size+1>=set.length) {
                    doubleArray(); // doubles the array if the size that is about to be achieved is greater than the number of available slots in the array.
                }
                int index = Math.abs(BinarySearch(element)); // Takes the index the value should be placed at.
                for(int i = size-1; i > index; i--) {
                    set[i] = set[i-1]; // Moves all values by one index to insert the new value in the right position.
                }
                set[index] = element; // Places element in the correct position.
                size++; // increases size.
                return true;
            }
            return false; // returns false if the array already contains the element.
        }

        @Override
        public boolean addAll(Collection<? extends E> elements) {
            boolean result = false;
            for(E e : elements) { // iterates through every element in the Collection of elements passed in.
                if(add(e)) { // Attempts to add each element to the array
                    result = true; // marks true for any value being added to the array
                }
            }
            return result; // Returns value of whether or not any values are added.
        }

        @Override
        public void clear() {
            E[] newSet = (E[]) new Object[10];
            size = 0;
            set = newSet;
        } // Creates new empty array to replace the current one.

        @Override
        public boolean contains(E element) {
        if (BinarySearch(element) > 0) { // Checks if element exists within the array by looking for its index
            return true;
        } else if(BinarySearch(element) == 0 && set[0] == element) { // Checks if element is at index 0
            return true;
        }
        return false; // returns false if element is not found.
        }

        @Override
        public boolean containsAll(Collection<? extends E> elements) {
            boolean isContained = false;
            for (E e : elements) { // Iterates through every value in elements
                if (contains(e)) {
                    isContained = true; // returns a value from the containsAll function as true if any are found.
                }
            }
            return isContained;
        }

        @Override
        public boolean isEmpty() {
            boolean noValue = true;
                if (iterator().hasNext()) { // Checks to see if there is a value in the array by searching for the next value.
                    noValue = false;
                }
            return noValue;
        }

        @Override
        public Iterator<E> iterator() {
            return new ListIterator<>() {
                private int index = 0;

                @Override
                public boolean hasNext() {
                    return index < size;
                } // checks if there is a value in the next index.

                @Override
                public E next() {
                    if(!hasNext()) {
                        throw new NoSuchElementException();
                    } // Throws an error if there is no next element.
                    return set[index++]; // returns value at the next index.
                }

                @Override
                public boolean hasPrevious() {
                    return index > 0;
                }

                @Override
                public E previous() {
                    if(!hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    return set[index--];
                }

                @Override
                public int nextIndex() {
                    return index++;
                }

                @Override
                public int previousIndex() {
                    return index--;
                }

                @Override
                public void remove() {

                }

                @Override
                public void set(E e) {

                }

                @Override
                public void add(E e) {

                }
            };
        }

        @Override
        public boolean remove(E element) throws NoSuchElementException {
            int index;
            if(contains(element)) {
                index = Math.abs(BinarySearch(element)); // Grabs desired index of value you wish to remove
                set[index] = null; // removes value
                for (int i = index; i < size-1; i++) {
                    set[i] = set[i + 1]; // moves other values over to fill the gap
                }
                set[size-1] = null; //
                size--;
                return true;
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
            Object[] arraySet = (Object[]) Arrays.copyOf(set, size);
            return arraySet;
        }

        private int BinarySearch(E element) {
            int low = 0;
            int high = size - 1;
            while(low <= high) {
                int mid = (low + high)/2;
                int cmp = 0;
                if(comparator != null) {
                    cmp = comparator.compare(element, set[mid]);
                } else {
                    cmp = ((Comparable<E>) element).compareTo(set[mid]);
                }
                if(cmp == 0) {
                    return mid;
                } else if(cmp > 0) {
                    low = mid + 1;
                } else  {
                    high = mid - 1;
                }
            }
            return -(low);
        }

        public void doubleArray() {
            E[] newSet = Arrays.copyOf(set, set.length * 2);
            set = newSet;
        }
}
