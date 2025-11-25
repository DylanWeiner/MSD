package lab06;

public interface PriorityQueue<E> {
    void add(E element);

    E removeMin();

    boolean isEmpty();
}
