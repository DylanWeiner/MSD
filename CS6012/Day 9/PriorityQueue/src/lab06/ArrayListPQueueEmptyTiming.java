package lab06;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ArrayListPQueueEmptyTiming extends TimerTemplate {

    ArrayListPQueue<Integer> list = new ArrayListPQueue<>();

    public ArrayListPQueueEmptyTiming(int[] problemSizes, int timesToLoop) {
        super(problemSizes, timesToLoop);
    }

    @Override
    protected void setup(int n) {
        for (int i = 1; i <= n; i++) {
            list.add(i);
        }
    }

    @Override
    protected void timingIteration(int n) {
        while (!list.isEmpty()) {
            list.removeMin();
        }
    }

    @Override
    protected void compensationIteration(int n) {
    }

    public static void main() throws IOException {
        System.out.println("running");
        int[] problemSize = new int[10];
        int index = 0;
        for (int i = 10; i < 20; i++) {
            problemSize[index] = (int) (Math.pow(2, i));
            index++;
        }

        var timer = new ArrayListPQueueEmptyTiming(problemSize, 10);
        var results = timer.run();

        String fileName = "ArrayListPQueueEmptyTiming.csv";
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
