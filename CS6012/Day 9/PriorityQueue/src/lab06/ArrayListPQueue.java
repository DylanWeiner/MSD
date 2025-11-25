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
        for(int i = arrayList.size()/2; i > 0; i++) { // Everything right of size/2 is a leaf and can't percolate down
            percolateDown(i);
        }
    }

    public void percolateDown(int item) {
        int leftChild = 2*item+1;
        int rightChild = 2*item+2;
        E root = arrayList.get(item);
        E left = arrayList.get(leftChild);
        E right = arrayList.get(rightChild);

        while(root.compareTo(left) > 0 || root.compareTo(right) > 0) {
            if(right.compareTo(left) > 0) {
                swap(arrayList, item, rightChild);
            }
            else {
                swap(arrayList, item, leftChild);
            }
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
