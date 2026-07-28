package com.example.synthesizer;

import java.util.Arrays;

public class AudioClip {
    static final int DURATION = 2;
    static final int SAMPLE_RATE = 44100;
    static final int FULL_SAMPLE_RATE = SAMPLE_RATE * DURATION;
    byte[] bytes;

    public AudioClip() {
        bytes = new byte[FULL_SAMPLE_RATE *2];
    }

    int getSample(int index) {
        byte byte1 = bytes[2 * index];
        byte byte2 = bytes[2 * index + 1];
        int sample;
        sample = (((byte2<<8) ) | (byte1 & 0xFF ));
        return sample;
    }

    void setSample(int index, int value) {
        bytes[2 * index] = (byte) (value&0xFF);
        bytes[2 * index + 1] = (byte) (value>>8);
    }

    public byte[] getData() {
        return Arrays.copyOfRange(bytes, 0, (FULL_SAMPLE_RATE*2));
    }



}
