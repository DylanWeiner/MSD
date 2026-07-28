package com.example.synthesizer;

import static com.example.synthesizer.AudioClip.FULL_SAMPLE_RATE;
import static com.example.synthesizer.AudioClip.SAMPLE_RATE;

public class LinearRamp implements AudioComponent {
    AudioClip clip = new AudioClip();
    AudioClip input = new AudioClip();
    float start;
    float stop;
    float current;

    public LinearRamp() {
         start = 50;
         stop = 2000;
         current = 0;
    }



    @Override
    public AudioClip getClip() {
        for (int i = 0; i < FULL_SAMPLE_RATE; i++) {
            if ((current * i / SAMPLE_RATE) % 1 > 0.5) {
                clip.setSample(i, (int) (start * (FULL_SAMPLE_RATE - i) + stop * i) / FULL_SAMPLE_RATE);
            } else {
                clip.setSample(i, (int) -((start * (FULL_SAMPLE_RATE - i) + stop * i) / FULL_SAMPLE_RATE));
            }
        }
        return clip;
    }

    @Override
    public boolean hasInputs() {
        return input != null;
    }

    @Override
    public void connectInput(AudioComponent ac, int inputIndex) {
        if(hasInputs()) {
            input = ac.getClip();
        }
        else {
            assert(false);
        }
    }
}
