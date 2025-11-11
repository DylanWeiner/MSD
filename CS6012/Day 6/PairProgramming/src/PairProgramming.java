// By Alexis Blood & Dylan Weiner

import java.util.Arrays;
import java.util.Comparator;

public class PairProgramming {

    public static<T extends Comparable<T>> T findMedian(T[] array) {
        Arrays.sort(array);
        return array[(array.length-1)/2];
    }

    public static <T> T findMedian(Comparator<T> genericSort, T[] array) {
        sort(array, genericSort);
        System.out.println(Arrays.toString(array));

        return array[(array.length-1)/2];
    }

    private static <T> void sort(T[] array, Comparator<T> comp) {
        for(int j = 0; j < array.length; j++) {
            for (int i = 1; i < array.length; i++) {
                if (comp.compare(array[i], array[i - 1]) < 0) {
                    T temp = array[i];
                    array[i] = array[i - 1];
                    array[i - 1] = temp;
                }
            }
        }
    }
}
