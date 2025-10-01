package com.example.synthesizer;

import static com.example.synthesizer.AudioClip.FULL_SAMPLE_RATE;
import static com.example.synthesizer.AudioClip.SAMPLE_RATE;
import static java.lang.Math.PI;
import static java.lang.Math.sin;

public class VFSineWave implements AudioComponent {
    AudioClip clip = new AudioClip();
    AudioClip input_ = new AudioClip();

    @Override
    public AudioClip getClip() {
        float phase = 0;
        float phaseIncrement = 0;

        for (int i = 0; i < FULL_SAMPLE_RATE; i++) {
            phaseIncrement = (float) (2 * PI * input_.getSample(i) / SAMPLE_RATE);
            phase += phaseIncrement;
            clip.setSample(i, (int) (Short.MAX_VALUE * sin(phase)));
        }
        return clip;
    }

    @Override
    public boolean hasInputs() {
        return input_ != null;
    }

    @Override
    public void connectInput(AudioComponent ac, int inputIndex) {
        if(hasInputs()) {
            input_ = ac.getClip();
        }
        else {
            assert(false);
        }
    }
}
