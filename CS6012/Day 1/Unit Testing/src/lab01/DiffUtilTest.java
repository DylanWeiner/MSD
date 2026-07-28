package lab01;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DiffUtilTest {
    private int[] arr1, arr2, arr3, arr4, arr5, arr6, arr7;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {

    }

    @BeforeEach
    protected void setUp() throws Exception {
        arr1 = new int[0];
        arr2 = new int[] { 3, 3, 3 };
        arr3 = new int[] { 52, 4, -8, 0, -17 };
        arr4 = new int[] { 52, 45, -45, -50, 50 };
        arr5 = new int[] { -52, -4, -8, -2, -17 };
        arr6 = new int[] { -52, -51, 51, 52 };
        arr7 = new int[] { 5, 3 };
    }

    private void someHelperMethodUsedInManyTests() {
    }

    @Test
    public void emptyArray() {
        assertEquals(-1, DiffUtil.findSmallestDiff(arr1));
    }

    @Test
    public void allArrayElementsEqual() {
        assertEquals(0, DiffUtil.findSmallestDiff(arr2));
    }

    @Test
    public void smallRandomArrayElements() {
        assertEquals(4, DiffUtil.findSmallestDiff(arr3));
    }

    @Test
    public void negativeButSameAbsValues() {
        assertEquals(2, DiffUtil.findSmallestDiff(arr4));
    }

    @Test
    public void allNegativeValues() {
        assertEquals(2, DiffUtil.findSmallestDiff(arr5));
    }

    @Test
    public void sameDifferenceDifferentAbsValues() {
        assertEquals(1, DiffUtil.findSmallestDiff(arr6));
    }

    @Test
    public void smallestPossibleArray() {
        assertEquals(2, DiffUtil.findSmallestDiff(arr7));
    }

    @AfterEach
    public void tearDown() throws Exception {
        // happens right after every Test.
    }
}