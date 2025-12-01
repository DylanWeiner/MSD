package lab06;

import java.util.ArrayList;
import java.util.Comparator;

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
        int minIndex = MinFinder(index);
        if(arrayList.get(index).compareTo(arrayList.get(minIndex)) > 0) {
            swap(arrayList, index, minIndex);
            percolateDown(minIndex);
        }

//        if(left.compareTo(root) < 0 && right.compareTo(root) < arrayList.size()) {
//            if (rightChild > leftChild) {
//
//                percolateDown(rightChild);
//            } else {
//                swap(arrayList, index, leftChild);
//                percolateDown(leftChild);
//            }
//        }
        else {
            return;
        }
    }

    public void percolateUp(int item) {
        int parentIndex = 2*item-1;
        E itemVal = arrayList.get(item);
        E parent = arrayList.get(parentIndex);

        while(parent.compareTo(itemVal) > 0) {
            swap(arrayList, item, parentIndex);
        }
    }

    @Override
    public void add(E element) {
        arrayList.add(element);
        percolateUp(arrayList.size()-1);
    }

    @Override
    public E removeMin() {
        E removedValue = arrayList.get(0);
        arrayList.removeFirst();
        arrayList.set(0, arrayList.get(arrayList.size()-1));
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
