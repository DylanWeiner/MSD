//package com.example.synthesizer;
//import javax.sound.sampled.LineUnavailableException;
//import javax.sound.sampled.*;
//import java.util.ArrayList;
//
//public class Main {
//    public static void main(String[] args){
//        AudioComponent gen = new SineWave(440);
//        AudioComponent gen2 = new SineWave(220);
//        AudioComponent gen3 = new SineWave(110);
//        AudioComponent ramped = new SineWave(50);
//
//        ArrayList<AudioComponent> genList = new ArrayList<>();
//
//
//        double vol = 0.40;
//        VolumeAdjuster v1 = new VolumeAdjuster(vol);
//        v1.connectInput(gen, 0);
//        genList.add(v1);
//
//        VolumeAdjuster v2 = new VolumeAdjuster(vol);
//        v2.connectInput(gen2, 0);
//        genList.add(v2);
//
//        VolumeAdjuster v3 = new VolumeAdjuster(vol);
//        v3.connectInput(gen3, 0);
//        genList.add(v3);
//
//        Mixer m = new Mixer(genList);
//
//        LinearRamp ramp = new LinearRamp();
//        VFSineWave vf = new VFSineWave();
//        vf.connectInput(ramp, 0);
//
//        try {
////            SoundOutput.play(m.getClip());
//            SoundOutput.play(vf.getClip());
//        } catch (LineUnavailableException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
