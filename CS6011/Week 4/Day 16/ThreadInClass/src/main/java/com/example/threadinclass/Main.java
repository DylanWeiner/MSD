package com.example.threadinclass;

public class Main {
    public static void main(String[] args) {
        for(int i = 1; i <= 5; i++) {
            CounterRunnable counterRunnable = new CounterRunnable(i);
            Thread counterThread = new Thread(counterRunnable);
            counterThread.start();
        }
    }
}