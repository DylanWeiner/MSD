package assignment09;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CollisionTiming extends TimerTemplate {
    int size = (int) (Math.pow(2, 19));

    /**
     * Create a timer
     *
     * @param problemSizes array of N's to use
     * @param timesToLoop  number of times to repeat the tests
     */
    public CollisionTiming(int[] problemSizes, int timesToLoop) {
        super(problemSizes, timesToLoop);
    }
    private final int Capacity = 100000;
    // for analysis use enum for testFunc to separate naive and optimized.
    // We can use hashmaps to track collisions.

    private static Segment COLLIDE_ME = new Segment(0, 15, 100, 20);

    HashMap<Integer, Segment> N_Items = new HashMap<>();
    private final Random rand = new Random();

    private static BSPTree bspTree = new BSPTree();

    public enum Seg {
        NAIVE,
        OPTIMIZED
    }

    private static Seg CollisionCourse;

    @Override
    protected void setup(int n) {
        bspTree = new BSPTree();

        N_Items = new HashMap<>();

        for (int i = 0; i < n; i++) {
            Segment s = new Segment(i, 0, i, 10);
            N_Items.put(i, s);
        }
    }

    @Override
    protected void timingIteration(int n) {
        switch(CollisionCourse) {
            case NAIVE:
                bspTree.naiveCollision(COLLIDE_ME);
                break;
            case OPTIMIZED:
                bspTree.collision(COLLIDE_ME);
                break;
        }
    }

    @Override
    protected void compensationIteration(int n) {
        switch(CollisionCourse) {
            case NAIVE:
                break;
            case OPTIMIZED:
                break;
        }
    }

    public static void main() throws IOException {
        ArrayList<Integer> problemSizes = new ArrayList<>();
        for (int n = 7; n < 18; n++) {
            problemSizes.add((int) Math.pow(2, n));
        }

        int[] size = new int[problemSizes.size()];
        for (int i = 0; i < problemSizes.size(); i++) {
            size[i] = problemSizes.get(i);
        }

        var timer = new CollisionTiming(size, 1);

        Result[] optimizedTime = new Result[size.length];
        Result[] naiveTime = new Result[size.length];

        for (Seg collide : Seg.values()) {
            CollisionCourse = collide;

            switch (CollisionCourse) {
                case NAIVE:
                    naiveTime = timer.run();
                    break;
                case OPTIMIZED:
                    optimizedTime = timer.run();
                    break;
            }
        }

        String fileName = "Collision.csv";
        String COMMA_DELIMITER = ",";
        String NEW_LINE_SEPARATOR = "\n";
        BufferedWriter writerTime = new BufferedWriter(new FileWriter(fileName));

        System.out.println("Function Timing:");
        String headers = "N, Optimized, Naive" + NEW_LINE_SEPARATOR;
        writerTime.write(headers);

        for (int i = 0; i < optimizedTime.length; i++) {
            writerTime.write(size[i]
                    + COMMA_DELIMITER
                    + optimizedTime[i].avgNanoSecs()
                    + COMMA_DELIMITER
                    + naiveTime[i].avgNanoSecs()
                    + NEW_LINE_SEPARATOR);
        }
        writerTime.close();
    }
}