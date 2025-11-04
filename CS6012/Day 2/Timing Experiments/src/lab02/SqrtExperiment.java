package lab02;

public class SqrtExperiment {
    public static void main(String[] args) {
        long secondsToRun = 1;
        int vals = 1;
        long startTime = System.nanoTime();
        long lastTime = startTime;
        long totalTime = 0;
        double[] savedVals = new double[10];
        int advanceCount = 0;
        int loopCount = 0;

        while (vals <= 10) {
            loopCount++;
            long currentTime = System.nanoTime();
            if (currentTime == lastTime)
                continue;
            totalTime += (currentTime - lastTime);
            lastTime = currentTime;
            savedVals[vals-1] = Math.sqrt(vals);
            advanceCount++;
            vals++;
        }
        double advancesPerSecond = advanceCount / (double) secondsToRun;
        System.out.println("Time advanced " + advancesPerSecond + " times per second.");
        System.out.println("All ten loops took " + totalTime + " ns.");
        for(int i = 0; i < savedVals.length; i++) {
            System.out.println("The square roots are " + savedVals[i] + ".");
        }
    }
}
