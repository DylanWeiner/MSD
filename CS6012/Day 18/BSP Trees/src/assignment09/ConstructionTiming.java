package assignment09;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class ConstructionTiming extends TimerTemplate{
    int size = (int) (Math.pow(2, 19));

    /**
     * Create a timer
     *
     * @param problemSizes array of N's to use
     * @param timesToLoop  number of times to repeat the tests
     */
    public ConstructionTiming(int[] problemSizes, int timesToLoop) {
        super(problemSizes, timesToLoop);
    }
    ArrayList<Segment> segmentsBulkList = new ArrayList<>();
    BSPTree bulkBspTree = new BSPTree();

    @Override
    protected void setup(int n) {
        segmentsBulkList.clear();
        for (int i = 1; i <= n; i++) {
            Segment s = new Segment(i, 0, i, 10);
            segmentsBulkList.add(s);
        }
    }

    @Override
    protected void timingIteration(int n) {
        bulkBspTree.bulkBuild(segmentsBulkList);
    }

    @Override
    protected void compensationIteration(int n) {
    }

    public static void main() throws IOException {
        System.out.println("running");
        int[] problemSize = new int[5];
        int index = 0;
        for (int i = 9; i < 14; i++) {
            problemSize[index] = (int) (Math.pow(2, i));
            index++;
        }

        var timer = new ConstructionTiming(problemSize, 100);
        var results = timer.run();

        String fileName = "BulkBSPTiming.csv";
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
