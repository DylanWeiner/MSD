package assignment07;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ChainingHashTableCollisions extends TimerTemplate {
    int size = (int) (Math.pow(2, 19));

    /**
     * Create a timer
     *
     * @param problemSizes array of N's to use
     * @param timesToLoop  number of times to repeat the tests
     */
    public ChainingHashTableCollisions(int[] problemSizes, int timesToLoop) {
        super(problemSizes, timesToLoop);
    }
    private final int Capacity = 100000;
    // for analysis use enum for testFunc to separate good, mediocre, and bad
    // Use another hashmap to store n values and strings (HashMap<Integer, ArrayList<String>>
    // We can use hashmaps to track collisions.
    static HashMap<Integer, Integer> goodCollisions = new HashMap<>();
    static HashMap<Integer, Integer> mediocreCollisions = new HashMap<>();
    static HashMap<Integer, Integer> badCollisions = new HashMap<>();

    HashMap<Integer, LinkedList<String>> N_Items = new HashMap<>();
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
        switch(hashFunctors) {
            case BAD:
                instance = new ChainingHashTable(Capacity, new BadHashFunctor());
                break;
            case MEDIOCRE:
                instance = new ChainingHashTable(Capacity, new MediocreHashFunctor());
                break;
            case GOOD:
                instance = new ChainingHashTable(Capacity, new GoodHashFunctor());
                break;
        }

        if(N_Items.containsKey(n)) {
            return;
        }
        N_Items.put(n, new LinkedList<>());

        while(N_Items.get(n).size() < n) {
            String current = "";
            for(int i = 0; i < wordLength; i++) {
                current += rand.nextInt(Character.MAX_VALUE);
            }
            N_Items.get(n).add(current);
        }
    }

    @Override
    protected void timingIteration(int n) {
        instance.addAll(N_Items.get(n));
        switch(hashFunctors) {
            case BAD:
              badCollisions.put(n, instance.collisions());
              break;
          case MEDIOCRE:
              mediocreCollisions.put(n, instance.collisions());
              break;
          case GOOD:
              goodCollisions.put(n, instance.collisions());
              break;
        }
    }

    @Override
    protected void compensationIteration(int n) {
        N_Items.get(n);

        switch(hashFunctors) {
            case BAD:
                badCollisions.put(-1, instance.collisions());
                break;
            case MEDIOCRE:
                mediocreCollisions.put(-1, instance.collisions());
                break;
            case GOOD:
                goodCollisions.put(-1, instance.collisions());
                break;
        }
    }

    public static void main() throws IOException {
        ArrayList<Integer> problemSizes = new ArrayList<>();
        for(int n = 7; n < 18; n++) {
            problemSizes.add((int) Math.pow(2,n));
        }

        int[] size = new int[problemSizes.size()];
        for (int i = 0; i < problemSizes.size(); i++) {
            size[i] = problemSizes.get(i);
        }

        var timer = new ChainingHashTableCollisions(size, 1);

        Result[] goodTime = new Result[size.length];
        Result[] mediocreTime = new Result[size.length];
        Result[] badTime = new Result[size.length];

        for(HashType hashType : HashType.values()) {
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

        String COMMA_DELIMITER = ",";
        String NEW_LINE_SEPARATOR = "\n";

        String fileName2 = "HashFunctorCollisions.csv";
        BufferedWriter writerColl = new BufferedWriter(new FileWriter(fileName2));

        System.out.println("Function Timing:");
        String headers2 = "N, Good, Mediocre, Bad" + NEW_LINE_SEPARATOR;
        writerColl.write(headers2);

        for(int i = 0; i < goodTime.length; i++) {
            writerColl.write(size[i]
                    + COMMA_DELIMITER
                    + goodCollisions.get(size[i])
                    + COMMA_DELIMITER
                    + mediocreCollisions.get(size[i])
                    + COMMA_DELIMITER
                    + badCollisions.get(size[i])
                    + NEW_LINE_SEPARATOR);
        }
        writerColl.close();
    }
}