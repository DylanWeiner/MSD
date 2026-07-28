//package com.example.hellothreads;
//
//import java.util.ArrayList;
//
//public class SayHello implements Runnable {
//    private int myId = -1;
//
//    public SayHello(int i) {
//        myId = i;
//    }
//
//    @Override
//    public void run() {
//        ArrayList<String> threads = new ArrayList<>();
//        for(int i = 0; i < 100; i++) {
//            threads.add("Hello number " + (i + 1) + " from thread number " + myId + ". ");
//            if((i % 10) == 0) {
//                System.out.print("\n" + threads.get(i));
//            }
//            else {
//                System.out.print(threads.get(i));
//            }
//        }
//    }
//}
