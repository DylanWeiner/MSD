package assignment05;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ArrayStackPushTiming extends TimerTemplate {
    ArrayStack<Integer> set = new ArrayStack<>();

    public ArrayStackPushTiming(int[] problemSizes, int timesToLoop) {
        super(problemSizes, timesToLoop);
    }

    @Override
    protected void setup(int n) {
        set.clear();
    }

    @Override
    protected void timingIteration(int n) {
        for (int i = 1; i <= n; i++) {
            set.push(i);
        }
    }

    @Override
    protected void compensationIteration(int n) {
    }

    public static void main() throws IOException {
        System.out.println("running");
        int[] problemSize = new int[7];
        int index = 0;
        for (int i = 10; i < 17; i++) {
            problemSize[index] = (int) (Math.pow(2, i));
            index++;
        }

        var timer = new ArrayStackPushTiming(problemSize, 10);
        var results = timer.run();

        String fileName = "ArrayStackPush.csv";
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
