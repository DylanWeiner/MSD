package lab03;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ArrayListAddition extends TimerTemplate {
    ArrayList<Integer> set = new ArrayList<>();

    /**
     * Create a timer
     *
     * @param problemSizes array of N's to use
     * @param timesToLoop  number of times to repeat the tests
     */
    public ArrayListAddition(int[] problemSizes, int timesToLoop) {
        super(problemSizes, timesToLoop);
    }
//    String fileName = "data.csv";
//    String COMMA_DELIMITER = ",";
//    String NEW_LINE_SEPARATOR = "\n";
//    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
//
//
//
//
//        ArrayList<Double> timePerLoop = new ArrayList<>();
//        // Number of iterations to average
//        for(int x = 1; x <= 20; x++) {
//        // Add to array list 1 to problem size
//        for (int size : problemSize) {
//            long startTime = System.nanoTime();
//            for (int i = 1; i <= size; i++) {
//                set.add(i);
//            }
//            long lastTime = System.nanoTime();
//            timePerLoop.add((double) (lastTime - startTime) / (double) size);
//        }
//
////            int elements = 0;
//            int avgTimePerLoop = 0;
//            for (int i = 0; i < timePerLoop.size(); i++) {

    /// /                elements += problemSize.get(i);
//                avgTimePerLoop += timePerLoop.get(i);
//            }
//        writer.append("Average Time(ns)");
//        writer.append(COMMA_DELIMITER);
//        writer.append("" + (avgTimePerLoop/timePerLoop.size()));
//        writer.append(NEW_LINE_SEPARATOR);
//        System.out.println("Iteration: " + x);
//    }
//
//    } catch (IOException e) {
//        System.err.println("Error writing to CSV file: " + e.getMessage());
//        e.printStackTrace();
//    }
    @Override
    protected void setup(int n) {
        for (int size = 0; size < n; size++) {
            for (int i = 1; i <= size; i++) {
                set.add(i);
            }
        }
        Collections.shuffle(set);
    }

    @Override
    protected void timingIteration(int n) {
        int val = n / 2;
        set.contains(val);
        Collections.shuffle(set);
    }

    @Override
    protected void compensationIteration(int n) {
        int val = n / 2;
        Collections.shuffle(set);
    }

    @Override
    public Result[] run() {
        Result[] result = super.run();
        return result;
    }

    public static void main() throws IOException {
        int[] problemSize = new int[10];
        int index = 0;
        for (int i = 10; i <= 20; i++) {
            problemSize[index] = (int) (Math.pow(2, i));
            index++;
        }

//        ArrayListAddition al = new ArrayListAddition(problemSize, 10);
//        al.run();

        String fileName = "data.csv";
        String COMMA_DELIMITER = ",";
        String NEW_LINE_SEPARATOR = "\n";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

        }
    }
}

