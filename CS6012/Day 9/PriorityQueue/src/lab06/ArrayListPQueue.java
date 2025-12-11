package lab06;

import java.util.ArrayList;
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

    public void percolateUp(int index) {
        while(index > 0 && arrayList.get((index - 1) / 2).compareTo(arrayList.get(index)) > 0) {
            System.out.println(("currParent:"+ (index - 1) / 2));
            swap(arrayList, index, (index - 1) / 2);
            index = (index - 1) / 2;
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
            return arrayList.removeFirst();
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
        return arrayList.isEmpty();
    }
}
