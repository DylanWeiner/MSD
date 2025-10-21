package com.example.threadinclass;

public class CounterRunnable implements Runnable {
    private int myId = -1;

    public CounterRunnable(int i) {
        myId = i;
    }

    @Override
    public void run() {
        for(int i = 0; i < 100; i++) {
            System.out.println(myId + ": " + i);
        }
        Thread outputter = new Thread( () -> {
            System.out.println("My parent thread is done.");
        });
        outputter.start();
    }
}