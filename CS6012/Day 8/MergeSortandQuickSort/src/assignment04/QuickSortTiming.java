package assignment04;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class QuickSortTiming extends TimerTemplate {
    private final int pivotStrat;
    ArrayList<Integer> avg = new ArrayList<>();

    /**
     * Create a timer
     *
     * @param problemSizes array of N's to use
     * @param timesToLoop  number of times to repeat the tests
     */
    public QuickSortTiming(int[] problemSizes, int timesToLoop, int strat) {
        super(problemSizes, timesToLoop);
        pivotStrat = strat;
    }

    @Override
    protected void setup(int n) {
        SortUtil.setStrat(pivotStrat);
        avg.clear();
        for (int i = 1; i <= n; i++) avg.add(i);
//        Collections.shuffle(avg);
    }

    @Override
    protected void timingIteration(int n) {
        SortUtil.quicksort((ArrayList<Integer>) avg.clone(), Integer::compare);
    }

    @Override
    protected void compensationIteration(int n) {
        ArrayList<Integer> avgClone = (ArrayList<Integer>) avg.clone();
    }

    public static void main() throws IOException {
        int[] size = new int[5];
        for (int i = 0; i < size.length; i++) {
            size[i] = (int) Math.pow(2, 10 + i);
        }

            var timer = new QuickSortTiming(size, 10, 0);

            var results = timer.run();

            String fileName = "QuickSortBest.csv";
            String COMMA_DELIMITER = ",";
            String NEW_LINE_SEPARATOR = "\n";


            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                writer.write("N" + COMMA_DELIMITER + "Time(ns)" + NEW_LINE_SEPARATOR);
                for (var r : results) {
                    writer.write(r.n() + COMMA_DELIMITER + r.avgNanoSecs() + NEW_LINE_SEPARATOR);
                }
                writer.flush();
            }
    }
}
