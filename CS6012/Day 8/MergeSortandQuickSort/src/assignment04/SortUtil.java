// By Alexis Blood and Dylan Weiner

package assignment04;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import static java.util.Collections.swap;

public class SortUtil<T> {
    private ArrayList<T> arr;
    private Comparator<? super T> comparator;
    private static int threshold = 2;

    public SortUtil() {
        this.arr = new ArrayList<>();
        this.comparator = null;
    }

    public SortUtil(ArrayList<T> arr, Comparator<? super T> comp) {
        this.arr = new ArrayList<>();
        this.comparator = comp;
    }

    public Comparator<? super T> comparator() {
        return this.comparator;
    }

    public static <T> void insertionSort(ArrayList<T> arr, Comparator<? super T> comp) {
        for (int i = 1; i < arr.size(); i++) {
            for (int j = i; j > 0; j--) {
                if (comp.compare(arr.get(j), arr.get(j - 1)) < 0) {
                    T temp = arr.get(j);
                    arr.set(j, arr.get(j - 1));
                }
            }
        }
    }

    public static <T> void merge(ArrayList<T> arr, ArrayList<T> left, ArrayList<T> right, Comparator<? super T> comp) {
        int i = 0;
        int l = 0;
        int r = 0;
        while(l < left.size() && r < right.size()) {
            if(comp.compare(left.get(l),right.get(r)) <= 0) {
                arr.set(i, left.get(l));
                i++;
                l++;
            } else {
                arr.set(i, right.get(r));
                i++;
                r++;
            }
        } // leftovers in the left half or right half.
        // copy the remainder to the end of the output array.
        for( ; l < left.size(); l++, i++) {
            arr.set(i, left.get(l));
        }
        for( ; r < right.size(); r++, i++) {
            arr.set(i, right.get(r));
        }
    }

    public static <T> void mergeSort(ArrayList<T> arr, Comparator<? super T> comp) {
        int middle = arr.size() / 2;
        ArrayList<T> left = new ArrayList<>(arr.subList(0, middle));
        ArrayList<T> right = new ArrayList<>(arr.subList(middle, arr.size()));
        if(arr.size() < threshold) {
            insertionSort(arr, comp);
            return;
        }
        mergeSort(left, comp);
        mergeSort(right, comp);
        merge(arr, left, right, comp);
    }

    public static ArrayList<Integer> generateBestCase(int size) {
        ArrayList<Integer> arr = new ArrayList<>(size);
        for(int i = 0; i < size; i++) {
            arr.add(i);
        }
        return arr;
    }

    public static ArrayList<Integer> generateAverageCase(int size) {
        ArrayList<Integer> arr = new ArrayList<>(size);
        Random rand = new Random();
        for(int i = 0; i < size; i++) {
            arr.add(i);
        }
        for(int i = size-1; i > 0; i--) {
            int j = rand.nextInt(i);
            swap(arr, j, i);
        }
        return arr;
    }

    public static ArrayList<Integer> generateWorstCase(int size) {
        ArrayList<Integer> arr = new ArrayList<>(size);
        for(int i = size; i > 0; i--) {
            arr.add(i);
        }
        return arr;
    }

    public static <T> int partition(ArrayList<T> arr, int pivotIndex,  Comparator<? super T> comp) {
        T pivotVal = arr.get(pivotIndex);
        int left = 0;
        int right = arr.size()-1;
        while(left < right) {
            while(left < right && comp.compare(arr.get(left), pivotVal) < 0) {
                left++;
            }
            while(left < right && comp.compare(arr.get(right), pivotVal) > 0) {
                right--;
            }
            if(left < right) {
                swap(arr, left, right);
                left++;
                right--;
            }
        }
        return left;
    }

    public static <T> int getRandom(ArrayList<T> arr, Comparator<? super T> comp) {
        Random rand = new Random();
        return rand.nextInt(arr.size());
    }

    public static <T> void quicksort(ArrayList<T> arr, int low, int high, Comparator<? super T> comp) {
        if(arr.size() < threshold) {
            insertionSort(arr, comp);
            return;
        }
        if(low >= high) {
            return;
        }

        int pivotIndex = getRandom(arr, comp);
        int partition = partition(arr, pivotIndex, comp);

//        ArrayList<T> left = new ArrayList<>(arr.subList(0, partition));
//        ArrayList<T> right = new ArrayList<>(arr.subList(partition, arr.size()));

        quicksort(arr, low, partition, comp);
        quicksort(arr, partition+1, high, comp);

//        arr.clear();


    }
}
