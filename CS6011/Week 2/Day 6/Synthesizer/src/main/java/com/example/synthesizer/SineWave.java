package com.example.synthesizer;

import static com.example.synthesizer.AudioClip.FULL_SAMPLE_RATE;
import static java.lang.Math.sin;
import static java.lang.Math.unsignedPowExact;

import javax.sound.sampled.*;

public class SineWave implements AudioComponent {
    AudioClip clip = new AudioClip();
    int frequency = 0;
    AudioClip input = new AudioClip();

    public SineWave(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public AudioClip getClip() {
        int result = 0;
        for(int i = 0; i < FULL_SAMPLE_RATE; i++) {
            result = (int) (Short.MAX_VALUE * sin(2 * Math.PI * frequency * i / FULL_SAMPLE_RATE));
            clip.setSample(i, result);
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
