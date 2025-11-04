package lab02;

public class TimingExperiment01 {

    public static void main(String[] args) {
        long lastTime = System.currentTimeMillis();
        int advanceCount = 0;
        while (advanceCount < 100) {
            long currentTime = System.currentTimeMillis();
            if (currentTime == lastTime)
                continue;
            System.out.println("Time advanced " + (currentTime - lastTime) + " milliseconds.");
            lastTime = currentTime;
            advanceCount++;
        }
    }
} // The millisecond timer advances in 1 ms increments, but we cannot say if the continue statement is ever executed.