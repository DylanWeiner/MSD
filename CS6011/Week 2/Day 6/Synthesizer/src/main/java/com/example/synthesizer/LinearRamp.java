package com.example.synthesizer;

import static com.example.synthesizer.AudioClip.FULL_SAMPLE_RATE;
import static com.example.synthesizer.AudioClip.SAMPLE_RATE;
import static java.lang.Math.PI;
import static java.lang.Math.sin;

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
        float phase = 0;
        for(int i = 0; i < FULL_SAMPLE_RATE; i++) {
                phase += (float) (2 * PI * i / SAMPLE_RATE);
                current = (int) (Short.MAX_VALUE * sin(phase));
                clip.setSample(i, (int) current);
        }
        return clip;
    }

    @Override
    public boolean hasInputs() {
        return false;
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
