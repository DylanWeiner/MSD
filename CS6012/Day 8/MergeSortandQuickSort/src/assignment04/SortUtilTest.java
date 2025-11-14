package assignment04;

import java.util.ArrayList;

import static assignment04.SortUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class SortUtilTest {
    ArrayList<Integer> best = new ArrayList<>();
    ArrayList<Integer> avg = new ArrayList<>();
    ArrayList<Integer> worst = new ArrayList<>();
    int size = 500;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        best = generateBestCase(size);
        avg = generateAverageCase(size);
        worst = generateWorstCase(size);
    }

    @org.junit.jupiter.api.Test
    void mergeSortTestOne() {
        SortUtil.mergeSort(best, Integer::compare);
        for(int i = 1; i < best.size(); i++) {
            assertTrue(best.get(i) > best.get(i - 1));
        }
    }

    @org.junit.jupiter.api.Test
    void mergeSortTestTwo() {
        SortUtil.mergeSort(avg, Integer::compare);
        for(int i = 1; i < avg.size(); i++) {
            assertTrue(avg.get(i) > avg.get(i - 1));
        }
    }

    @org.junit.jupiter.api.Test
    void mergeSortTestThree() {
        SortUtil.mergeSort(worst, Integer::compare);
        for(int i = 1; i < worst.size(); i++) {
            assertTrue(worst.get(i) > worst.get(i - 1));
        }
    }

    @org.junit.jupiter.api.Test
    void quicksortTestOne() {
        SortUtil.quicksort(best, Integer::compare);
        for(int i = 1; i < best.size(); i++) {
            assertTrue(best.get(i) > best.get(i - 1));
        }
    }

    @org.junit.jupiter.api.Test
    void quicksortTestTwo() {
        SortUtil.quicksort(avg, Integer::compare);
        for(int i = 1; i < avg.size(); i++) {
            assertTrue(avg.get(i) > avg.get(i - 1));
        }
    }

    @org.junit.jupiter.api.Test
    void quicksortTestThree() {
        SortUtil.quicksort(worst, Integer::compare);
        for(int i = 1; i < worst.size(); i++) {
            assertTrue(worst.get(i) > worst.get(i - 1));
        }
    }
    @org.junit.jupiter.api.Test
    void partitionTestOne() {
        ArrayList<Integer> arr = new ArrayList<>();
        arr = generateAverageCase(5);
        SortUtil.partition(arr, 0, arr.size()-1, (arr.size()-1)/2, Integer::compare);
    }
    @org.junit.jupiter.api.Test
    void partitionTestTwo() {
        ArrayList<Integer> arr = new ArrayList<>();
        arr = generateAverageCase(3);
        SortUtil.partition(arr, 0, arr.size()-1, (arr.size()-1)/2, Integer::compare);
    }
    @org.junit.jupiter.api.Test
    void partitionTestThree() {
        ArrayList<Integer> arr = new ArrayList<>();
        arr = generateAverageCase(1);
        SortUtil.partition(arr, 0, arr.size()-1, arr.size()/2, Integer::compare);
    }
}