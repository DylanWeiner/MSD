package assignment03;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class BinarySearchSetAddTiming extends TimerTemplate {
    BinarySearchSet<Integer> set = new BinarySearchSet<>();

    public BinarySearchSetAddTiming(int[] problemSizes, int timesToLoop) {
        super(problemSizes, timesToLoop);
    }

    @Override
    protected void setup(int n) {
        for (int i = 1; i <= n; i++) {
            set.add(i);
        }
    }

    @Override
    protected void timingIteration(int n) {
        set.add(n/2);
        set.remove(n/2);
    }

    @Override
    protected void compensationIteration(int n) {
        set.remove(n/2);
    }

    public static void main() throws IOException {
        System.out.println("running");
        int[] problemSize = new int[7];
        int index = 0;
        for (int i = 10; i < 17; i++) {
            problemSize[index] = (int) (Math.pow(2, i));
            index++;
        }

        var timer = new BinarySearchSetTiming(problemSize, 10);
        var results = timer.run();

        String fileName = "additionData.csv";
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