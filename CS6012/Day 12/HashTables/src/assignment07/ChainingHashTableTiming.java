package assignment07;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ChainingHashTableTiming extends TimerTemplate {
    int size = (int) (Math.pow(2, 19));

    /**
     * Create a timer
     *
     * @param problemSizes array of N's to use
     * @param timesToLoop  number of times to repeat the tests
     */
    public ChainingHashTableTiming(int[] problemSizes, int timesToLoop) {
        super(problemSizes, timesToLoop);
    }
    private final int Capacity = 100000;
    // for analysis use enum for testFunc to separate good, mediocre, and bad
    // Use another hashmap to store n values and strings (HashMap<Integer, ArrayList<String>>
    // We can use hashmaps to track collisions.
    private static final GoodHashFunctor GoodHashFunctor = new GoodHashFunctor();
    private static final BadHashFunctor BadHashFunctor = new BadHashFunctor();
    private static final MediocreHashFunctor MediocreHashFunctor = new MediocreHashFunctor();

    HashMap<Integer, String> N_Items = new HashMap<>();
    private final int wordLength = 1000;
    private final Random rand = new Random();

    private static ChainingHashTable instance;

    public enum HashType {
        BAD,
        MEDIOCRE,
        GOOD
    }

    private static HashType hashFunctors;

    @Override
    protected void setup(int n) {
        if(N_Items.containsKey(n)) {
            return;
        }

        String n_str = "";

        while(n_str.length() < n) {
            n_str += rand.nextInt(Character.MAX_VALUE);
        }
        N_Items.put(n, n_str);
    }

    @Override
    protected void timingIteration(int n) {
        switch(hashFunctors) {
            case BAD:
                BadHashFunctor.hash(N_Items.get(n));
                break;
            case MEDIOCRE:
                MediocreHashFunctor.hash(N_Items.get(n));
                break;
            case GOOD:
                GoodHashFunctor.hash(N_Items.get(n));
                break;
        }
    }

    @Override
    protected void compensationIteration(int n) {
        switch(hashFunctors) {
            case BAD:
                N_Items.get(n);
                break;
            case MEDIOCRE:
                N_Items.get(n);
                break;
            case GOOD:
                N_Items.get(n);
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

        var timer = new ChainingHashTableTiming(size, 1);

        Result[] goodTime = new Result[size.length];
        Result[] mediocreTime = new Result[size.length];
        Result[] badTime = new Result[size.length];

        for (HashType hashType : HashType.values()) {
            hashFunctors = hashType;

            switch (hashFunctors) {
                case BAD:
                    badTime = timer.run();
                    break;
                case MEDIOCRE:
                    mediocreTime = timer.run();
                    break;
                case GOOD:
                    goodTime = timer.run();
                    break;
            }
        }

        String fileName = "HashFunctorTiming.csv";
        String COMMA_DELIMITER = ",";
        String NEW_LINE_SEPARATOR = "\n";
        BufferedWriter writerTime = new BufferedWriter(new FileWriter(fileName));

        System.out.println("Function Timing:");
        String headers = "N, Good, Mediocre, Bad" + NEW_LINE_SEPARATOR;
        writerTime.write(headers);

        for (int i = 0; i < goodTime.length; i++) {
            writerTime.write(size[i]
                    + COMMA_DELIMITER
                    + goodTime[i].avgNanoSecs()
                    + COMMA_DELIMITER
                    + mediocreTime[i].avgNanoSecs()
                    + COMMA_DELIMITER
                    + badTime[i].avgNanoSecs()
                    + NEW_LINE_SEPARATOR);
        }
        writerTime.close();
    }
}