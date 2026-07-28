package assignment06;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class UnorderedBSTTiming extends TimerTemplate {
    BinarySearchTree<Integer> set = new BinarySearchTree<>();
    ArrayList<Integer> list = new ArrayList<>();

    public UnorderedBSTTiming(int[] problemSizes, int timesToLoop) {
        super(problemSizes, timesToLoop);
    }

    @Override
    protected void setup(int n) {
        list.clear();
        for (int i = 1; i <= n; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
    }

    @Override
    protected void timingIteration(int n) {
        set.addAll(list);
    }

    @Override
    protected void compensationIteration(int n) {
    }

    public static void main() throws IOException {
        System.out.println("running");
        int[] problemSize = new int[6];
        int index = 0;
        for (int i = 10; i < 16; i++) {
            problemSize[index] = (int) (Math.pow(2, i));
            index++;
        }

        var timer = new UnorderedBSTTiming(problemSize, 10);
        var results = timer.run();

        String fileName = "UnorderedBSTTiming.csv";
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
