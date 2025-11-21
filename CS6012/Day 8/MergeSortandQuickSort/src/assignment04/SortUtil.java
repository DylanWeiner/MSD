// By Alexis Blood and Dylan Weiner

package assignment04;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import static java.util.Collections.swap;

public class SortUtil {
    private static int threshold_ = 5;
    private static int strat = 0;

    public static <T> void insertionSort(ArrayList<T> arr, int low, int high, Comparator<? super T> comp) { // add a begin and an end a parameter to pass.
        for (int i = low + 1; i <= high; i++) {
            T temp = arr.get(i);
            int j = i - 1;
            while(j >= low && comp.compare(arr.get(j), temp) > 0) {
                arr.set(j + 1, arr.get(j));
                j--;
            }
            arr.set(j + 1, temp);
        }
    }

    private static <T> int getEndpointPivot(int low) {
        return low;
    }

    public static <T> void merge(ArrayList<T> arr, ArrayList<T> temp, int left, int mid, int right, Comparator<? super T> comp) {
        int i = left;
        int l = mid + 1;
        int r = left;

        while (i <= mid && l <= right) {
            if (comp.compare(arr.get(i), arr.get(l)) <= 0) {
                temp.set(r++, arr.get(i++));
            } else {
                temp.set(r++, arr.get(l++));
            }
        }
        while (i <= mid) {
            temp.set(r++, arr.get(i++));
        }
        while (l <= right) {
            temp.set(r++, arr.get(l++));
        }
        for(i = left; i <= right; i++){
            arr.set(i, temp.get(i));
        }
    }

    public static void setThreshold(int threshold) {
        threshold_ = threshold;
    }

    public static int getThreshold(int threshold) {
        return threshold;
    }

    private static <T> void merger(ArrayList<T> arr, ArrayList<T> temp, int left, int right, Comparator<? super T> comp) {
        if (right - left + 1 <= threshold_) {
            insertionSort(arr, left, right, comp);
            return;
        }

        int middle = (left + right) / 2;

        merger(arr, temp, left, middle, comp);
        merger(arr, temp, middle + 1, right, comp);

        merge(arr, temp, left, middle, right, comp);
    }

    public static <T> void mergesort(ArrayList<T> arr, Comparator<? super T> comp) {
        ArrayList<T> temp = new ArrayList<>(arr);
        merger(arr, temp, 0, arr.size()-1, comp);
    }

    public static ArrayList<Integer> generateBestCase(int size) {
        ArrayList<Integer> arr = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            arr.add(i);
        }
        return arr;
    }

    public static ArrayList<Integer> generateAverageCase(int size) {
        ArrayList<Integer> arr = new ArrayList<>(size);
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            arr.add(i);
        }
        for (int i = size - 1; i > 0; i--) {
            int j = rand.nextInt(i);
            swap(arr, j, i);
        }
        return arr;
    }

    public static ArrayList<Integer> generateWorstCase(int size) {
        ArrayList<Integer> arr = new ArrayList<>(size);
        for (int i = size; i > 0; i--) {
            arr.add(i);
        }
        return arr;
    }

    public static <T> void sort(ArrayList<T> arr, int low, int high, Comparator<? super T> comp, int strat) {
        if(low >= high) {
            return;
        }

        if (high - low < threshold_) {
            insertionSort(arr, low, high, comp);
            return;
        }

        int pivotIndex = 0;

        if (strat == 0) {
            pivotIndex = getRandom(arr, low, high, comp);
        } else if (strat == 1) {
            pivotIndex = getEndpointPivot(low);
        } else if (strat == 2) {
            pivotIndex = getEndpointPivot((low+high)/2);
        }

        int partition = partition(arr, low, high, pivotIndex, comp);

        sort(arr, low, partition - 1, comp, strat);
        sort(arr, partition + 1, high, comp, strat);
    }

    public static <T> int partition(ArrayList<T> arr, int low, int high, int pivotIndex, Comparator<? super T> comp) {
        T pivotVal = arr.get(pivotIndex);
        swap(arr, pivotIndex, high);
        int left = low;
        int right = high;

        while (left < right) {
            while (comp.compare(arr.get(left), pivotVal) < 0) {
                left++;
            }
            while (comp.compare(arr.get(right), pivotVal) > 0) {
                right--;
            }
            if (left < right) {
                swap(arr, left, right);
            }
        }
//        swap(arr, pivotIndex, high);
        return left;
    }

    public static <T> int getRandom(ArrayList<T> arr, int low, int high, Comparator<? super T> comp) {
        return low + new Random().nextInt(high - low + 1);
    }

    public static <T> void setStrat(int strat) {
        SortUtil.strat = strat;
    }

    public static <T> void quicksort(ArrayList<T> arr, Comparator<? super T> comp) {
        sort(arr, 0, arr.size() - 1, comp, strat);
    }
}
