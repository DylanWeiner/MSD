package assignment03;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class BinarySearchSetTiming extends TimerTemplate {
    BinarySearchSet<Integer> set = new BinarySearchSet<>();
//    List<Integer> list = new ArrayList<>();
//    List<Integer> list2 = new ArrayList<>();

    public BinarySearchSetTiming(int[] problemSizes, int timesToLoop) {
        super(problemSizes, timesToLoop);
    }

    @Override
    protected void setup(int n) {
        for (int size = 0; size < n; size++) {
            for (int i = 1; i <= size; i++) {
                set.add(i);
            }
        }
    }

    @Override
    protected void timingIteration(int n) {
        set.contains(n/2);
    }

    @Override
    protected void compensationIteration(int n) {
    }

    public static void main() throws IOException {
        System.out.println("running");
        int[] problemSize = new int[11];
        int index = 0;
        for (int i = 4; i <= 14; i++) {
            problemSize[index] = (int) (Math.pow(2, i));
            index++;
        }

        var timer = new BinarySearchSetTiming(problemSize, 10);
        var results = timer.run();

        String fileName = "data.csv";
        String COMMA_DELIMITER = ",";
        String NEW_LINE_SEPARATOR = "\n";


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Time(ns)" + COMMA_DELIMITER +  "N" + NEW_LINE_SEPARATOR);
            for (var result : results) {
                writer.write(result.n() + COMMA_DELIMITER + result.avgNanoSecs() + NEW_LINE_SEPARATOR);
            }
            writer.flush();
            writer.close();
        }
    }
}



