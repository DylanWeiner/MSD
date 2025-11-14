package assignment03;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class BinarySearchSetTiming extends TimerTemplate {
    BinarySearchSet<Integer> set = new BinarySearchSet<>();

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
        int[] problemSize = new int[5];
        int index = 0;
        for (int i = 10; i < 15; i++) {
            problemSize[index] = (int) (Math.pow(2, i));
            index++;
        }

        var timer = new BinarySearchSetTiming(problemSize, 10);
        var results = timer.run();

        String fileName = "data.csv";
        String COMMA_DELIMITER = ",";
        String NEW_LINE_SEPARATOR = "\n";


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("N" + COMMA_DELIMITER +  "Time(ns)" + NEW_LINE_SEPARATOR);
            for (var result : results) {
                writer.write(result.n() + COMMA_DELIMITER + result.avgNanoSecs() + NEW_LINE_SEPARATOR);
            }
            writer.flush();
        }
    }
}



