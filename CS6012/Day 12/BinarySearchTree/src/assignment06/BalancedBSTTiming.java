package assignment06;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

public class BalancedBSTTiming extends TimerTemplate {
    TreeSet<Integer> set = new TreeSet<>();

    public BalancedBSTTiming(int[] problemSizes, int timesToLoop) {
        super(problemSizes, timesToLoop);
    }

    @Override
    protected void setup(int n) {
        set.clear();
        for (int i = 1; i <= n; i++) {
            set.add(i);
        }
    }

    @Override
    protected void timingIteration(int n) {
        int randomNumber = (int)(Math.random() * (n - 1 + 1)) + 1;
        set.contains(randomNumber);
    }

    @Override
    protected void compensationIteration(int n) {
        int randomNumber = (int)(Math.random() * (n - 1 + 1)) + 1;
    }

    public static void main() throws IOException {
        System.out.println("running");
        int[] problemSize = new int[6];
        int index = 0;
        for (int i = 10; i < 16; i++) {
            problemSize[index] = (int) (Math.pow(2, i));
            index++;
        }

        var timer = new BalancedBSTTiming(problemSize, 10);
        var results = timer.run();

        String fileName = "BalancedBSTTiming.csv";
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
