package lab06;

import java.util.ArrayList;
import java.util.Comparator;

import static java.util.Collections.min;
import static java.util.Collections.swap;

public class ArrayListPQueue<E extends Comparable<? super E>> implements PriorityQueue<E> {
    private ArrayList<E> arrayList;

    public ArrayListPQueue() {
        arrayList = new ArrayList<E>();
    }

    public ArrayListPQueue(ArrayList<E> arrayList) {
        this.arrayList = arrayList;
        heapify(arrayList);
    }

    public void heapify(ArrayList<E> arrayList) {
        for(int i = (arrayList.size()/2)-1; i > 0; i--) { // Everything right of size/2 is a leaf and can't percolate down
            percolateDown(i);
        }
    }

    private int MinFinder(int index) {
        int leftChild = 2*index+1;
        int rightChild = 2*index+2;
        int minIndex = index;

        if(leftChild < arrayList.size()) {
            E left = arrayList.get(leftChild);

            if(left.compareTo(arrayList.get(minIndex)) < 0) {
                minIndex = leftChild;
            }
        } else if(rightChild < arrayList.size()) {
            E right = arrayList.get(rightChild);
            if(right.compareTo(arrayList.get(minIndex)) < 0) {
                minIndex = rightChild;
            }
        }
        return minIndex;
    }

    public void percolateDown(int index) {
//        System.out.println("current size: " + arrayList.size());

        int minIndex = MinFinder(index);
        if(arrayList.get(index).compareTo(arrayList.get(minIndex)) > 0) {
            swap(arrayList, index, minIndex);
            percolateDown(minIndex);
        }
    }

    private int MaxFinder(int index) {
        int parentIndex = 2*index-1;
        int maxIndex = index;
        if(parentIndex < 0) {
            E itemVal = arrayList.get(index);
        } else if(parentIndex < arrayList.size()) {
            E parent = arrayList.get(parentIndex);
            if(parent.compareTo(arrayList.get(maxIndex)) < 0) {
                maxIndex = parentIndex;
            }
        }
        return maxIndex;
    }

    public void percolateUp(int index) {
        int maxIndex = MaxFinder(index);
        if(arrayList.get(index).compareTo(arrayList.get(maxIndex)) > 0) {
            swap(arrayList, index, maxIndex);
            percolateUp(maxIndex);
        }
    }

    @Override
    public void add(E element) {
        arrayList.add(element);
        percolateUp(arrayList.size()-1);
    }

    @Override
    public E removeMin() {
        if(arrayList.isEmpty()) {
            return null;
        }

        if(arrayList.size() == 1) {
            return arrayList.remove(0);
        }
//        else if(arrayList.size() > 1) {
            E removedValue = arrayList.getFirst();
        swap(arrayList, 0, arrayList.size()-1);
//            arrayList.removeFirst();
//            arrayList.set(0, arrayList.getLast());
            arrayList.removeLast();
            percolateDown(0);
            return removedValue;
    }

    @Override
    public boolean isEmpty() {
        if(arrayList.isEmpty()) {
            return true;
        }
        return false;
    }
}
