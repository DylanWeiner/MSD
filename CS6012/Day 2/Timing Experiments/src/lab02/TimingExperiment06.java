package lab02;

public class TimingExperiment06 {

    public static void main(String[] args) {
        long secondsToRun = 10;
        long startTime = System.nanoTime();
        long lastTime = startTime;
        int advanceCount = 0;
        int loopCount = 0;
        long currentTime;
        long totalTime = 1000000000 * secondsToRun;
        long elapsedTime = 0;
        while (elapsedTime < totalTime) {
            loopCount++;
            if ((currentTime = System.nanoTime()) != lastTime) {
                lastTime = currentTime;
                elapsedTime = lastTime - startTime;
                advanceCount++;
            }
        }
        double advancesPerSecond = advanceCount / (double) secondsToRun;
        System.out.println("Time advanced " + advancesPerSecond + " times per second.");
        System.out.println("The loop tested the time " + loopCount + " times.");
    }
}
// Time advanced 2.35056145E7 times per second.
//The loop tested the time 1192599845 times.
//Time advanced 2.34860708E7 times per second.
//The loop tested the time 1190609837 times.
//Time advanced 2.34700509E7 times per second.
//The loop tested the time 1185171283 times.
//Time advanced 2.34889438E7 times per second.
//The loop tested the time 1186160828 times.
//Time advanced 2.34651859E7 times per second.
//The loop tested the time 1182968572 times.