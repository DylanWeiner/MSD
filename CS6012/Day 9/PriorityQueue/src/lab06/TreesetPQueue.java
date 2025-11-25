package lab06;

import java.util.TreeSet;

public class TreesetPQueue<E> implements PriorityQueue<E>{
    TreeSet<E> treeSet = new TreeSet<E>();

    @Override
    public void add(E element) {
        treeSet.add(element);
    }

    @Override
    public E removeMin() {
        E minValue = treeSet.getFirst();
        treeSet.removeFirst();
        return minValue;
    }

    @Override
    public boolean isEmpty() {
        if(treeSet.isEmpty()){
            return true;
        }
        return false;
    }
}
