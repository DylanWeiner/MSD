package lab03;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ArrayListAddition extends TimerTemplate {
    ArrayList<Integer> set = new ArrayList<>();
    ArrayList<Integer> findMe = new ArrayList<>();
    int randomVal = 0;


    public ArrayListAddition(int[] problemSizes, int timesToLoop) {
        super(problemSizes, timesToLoop);
    }

    @Override
    protected void setup(int n) {
        set.clear();
        for (int i = 0; i < n; i++) {
            set.add(i);
        }
        Collections.shuffle(set);
        int[] loops = new int[timesToLoop];
        for (int i = 0; i < timesToLoop; i++) {
            set.add(i);
        }
        for (int i = 1; i <= timesToLoop; i++) {
            findMe.add(set.get(i));
        }
        Collections.shuffle(set);
    }

    @Override
    protected void timingIteration(int n) {
        randomVal = findMe.get(n);
        set.contains(randomVal); // We passed in timesToLoop as n to use to index findMe

    }

    @Override
    protected void compensationIteration(int n) {
        randomVal = findMe.get(n);
    }

    public static void main() throws IOException {
        System.out.println("running");
        int[] problemSize = new int[10];
//        int index = 0;
        for (int i = 0; i < 10; i++) {
            problemSize[i] = (int) (Math.pow(2, i) * 50);
//            index++;
        }

        var timer = new ArrayListAddition(problemSize, 5000);
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



