package com.example.hellothreads;

import java.util.ArrayList;
import java.util.List;

public class Main {
    static int answer = 0;

    public static void SayHello(int myId) {
        synchronized (System.out) {
            for (int j = 1; j <= 100; j++) {
                System.out.print("Hello number " + j + " from thread " + myId + ". ");
                if ((j % 10) == 0) {
                    System.out.print("\n");
                }
            }
        }
    }

    public static void badSum() throws InterruptedException {
        int maxValue = 40000;
        int numThreads = 10;
        Thread[] badThreads = new Thread[numThreads];
        for(int i = 0; i < numThreads; i++) {
            int finalI = i;
            badThreads[i] = new Thread(() -> {
                int start = finalI * maxValue/numThreads;
                int end = Math.min((finalI + 1) * maxValue/numThreads, maxValue);
                for(int j = start; j <= end; j++) {
                    answer += j;
                }
            });
            badThreads[i].start();
        }
        for (int i = 0; i < numThreads; i++) {
            badThreads[i].join();
        }
        System.out.println("Threaded Answer: " + answer);
        System.out.println("Correct Answer: " + (maxValue * (maxValue + 1) / 2));
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            final int id = i + 1;
            threads[i] = new Thread(() -> SayHello(id));
            threads[i].start();
        }
        for (Thread t : threads) {
            t.join();
        }
        badSum();
    }
}