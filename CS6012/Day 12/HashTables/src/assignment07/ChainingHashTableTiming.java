package assignment07;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ChainingHashTableTiming extends TimerTemplate {
    int size = (int) (Math.pow(2, 20));
    ArrayList<Integer> avg = new ArrayList<>(size);

    /**
     * Create a timer
     *
     * @param problemSizes array of N's to use
     * @param timesToLoop  number of times to repeat the tests
     */
    public ChainingHashTableTiming(int[] problemSizes, int timesToLoop) {
        super(problemSizes, timesToLoop);
    }

    @Override
    protected void setup(int n) {
        avg.clear();
        for (int i = 1; i <= n; i++) {
            avg.add(i);
        }
//        Collections.shuffle(avg);
    }

    @Override
    protected void timingIteration(int n) {
    }

    @Override
    protected void compensationIteration(int n) {

    }

    public static void main() throws IOException {
        ArrayList<Integer> problemSizes = new ArrayList<>();
        for(int n = 1000; n <= 4000; n+=20) {
            problemSizes.add(n);
        }

        int[] size = new int[problemSizes.size()];
        for (int i = 0; i < problemSizes.size(); i++) {
            size[i] = problemSizes.get(i);
        }

        var timer = new ChainingHashTableTiming(size, 10);
        var results = timer.run();

        String fileName = "HashFunctorTests.csv";
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
