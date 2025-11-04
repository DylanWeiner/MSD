package lab02;

public class TimingExperiment05 {

    public static void main(String[] args) {
        long secondsToRun = 10;
        long startTime = System.nanoTime();
        long lastTime = startTime;
        int advanceCount = 0;
        int loopCount = 0;
        while (lastTime - startTime < 1000000000 * secondsToRun) {
            loopCount++;
            long currentTime = System.nanoTime();
            if (currentTime == lastTime)
                continue;
            lastTime = currentTime;
            advanceCount++;
        }
        double advancesPerSecond = advanceCount / (double) secondsToRun;
        System.out.println("Time advanced " + advancesPerSecond + " times per second.");
        System.out.println("The loop tested the time " + loopCount + " times.");
    }
} // The nanosecond's granularity provides the necessary information to infer how many nanoseconds passed within the span, but
// the loop's granularity cannot be confirmed as accurate as there is no concrete information on how many times a loop can
// run in the specified amount of times.