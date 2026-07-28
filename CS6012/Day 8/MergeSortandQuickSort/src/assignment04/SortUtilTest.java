package assignment04;

import java.util.ArrayList;
import java.util.Objects;

import static assignment04.SortUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class SortUtilTest {
    ArrayList<Integer> best = new ArrayList<>();
    ArrayList<Integer> avg = new ArrayList<>();
    ArrayList<Integer> worst = new ArrayList<>();
    ArrayList<Integer> sameVal = new ArrayList<>();
    int size = 500;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        best = generateBestCase(size);
        avg = generateAverageCase(size);
        worst = generateWorstCase(size);
        sameVal = generateSameVal(size);
    }

    @org.junit.jupiter.api.Test
    void mergesortTestOne() {
        SortUtil.mergesort(best, Integer::compare);
        for(int i = 1; i < best.size(); i++) {
            assertTrue(best.get(i) > best.get(i - 1));
        }
    }

    @org.junit.jupiter.api.Test
    void insertionSortTestOne() {
        SortUtil.insertionSort(avg, 0, avg.size()-1, Integer::compare);
        for(int i = 1; i < avg.size(); i++) {
            assertTrue(avg.get(i) > avg.get(i - 1));
        }
    }

    @org.junit.jupiter.api.Test
    void insertionSortTestTwo() {
        SortUtil.insertionSort(worst, 0, worst.size()-1, Integer::compare);
        for(int i = 1; i < worst.size(); i++) {
            assertTrue(worst.get(i) > worst.get(i - 1));
        }
    }

    @org.junit.jupiter.api.Test
    void mergesortTestTwo() {
        SortUtil.mergesort(avg, Integer::compare);
        for(int i = 1; i < avg.size(); i++) {
            assertTrue(avg.get(i) > avg.get(i - 1));
        }
    }

    @org.junit.jupiter.api.Test
    void mergesortTestThree() {
        SortUtil.mergesort(worst, Integer::compare);
        for(int i = 1; i < worst.size(); i++) {
            assertTrue(worst.get(i) > worst.get(i - 1));
        }
    }

    @org.junit.jupiter.api.Test
    void mergesortTestFour() {
        SortUtil.mergesort(sameVal, Integer::compare);
        for(int i = 1; i < sameVal.size(); i++) {
            assertEquals(sameVal.get(i), sameVal.get(i - 1));
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
    void quicksortTestFour() {
        SortUtil.quicksort(sameVal, Integer::compare);
        for(int i = 1; i < sameVal.size(); i++) {
            assertEquals(sameVal.get(i), sameVal.get(i - 1));
        }
    }

    @org.junit.jupiter.api.Test
    void partitionTestOne() {
        ArrayList<Integer> arr = new ArrayList<>();
        arr = generateAverageCase(5);
        int p = SortUtil.partition(arr, 0, arr.size()-1, (arr.size())/2, Integer::compare);
        for(int j = 0; j < p; j++) {
            assertTrue(arr.get(j) < arr.get(p));
        }
    }
    @org.junit.jupiter.api.Test
    void partitionTestTwo() {
        ArrayList<Integer> arr = new ArrayList<>();
        arr = generateAverageCase(3);
        int p = SortUtil.partition(arr, 0, arr.size()-1, (arr.size())/2, Integer::compare);
        for(int j = 0; j < p; j++) {
            assertTrue(arr.get(j) < arr.get(p));
        }
    }
    @org.junit.jupiter.api.Test
    void partitionTestThree() {
        ArrayList<Integer> arr = new ArrayList<>();
        arr = generateAverageCase(1);
        int p = SortUtil.partition(arr, 0, arr.size()-1, (arr.size()-1)/2, Integer::compare);
        for(int j = 1; j < (arr.size()-1)/2; j++) {
            assertTrue(arr.get(j) < arr.get(p));
        }
    }
}