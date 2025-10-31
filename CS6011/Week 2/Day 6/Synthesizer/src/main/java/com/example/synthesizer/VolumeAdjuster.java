package com.example.synthesizer;

import static com.example.synthesizer.AudioClip.FULL_SAMPLE_RATE;

public class VolumeAdjuster implements AudioComponent {
    private double volume_;
    private AudioComponent input_;

    public VolumeAdjuster() {
        volume_ = 1.0;
    }

    public void setVolume(double volume) {
        volume_ = volume;
    }


    @Override
    public AudioClip getClip() {
        AudioClip original = input_.getClip();
        AudioClip adjust = new AudioClip();
        for (int j = 0; j < FULL_SAMPLE_RATE; j++) {
            int newVal = original.getSample(j) + adjust.getSample(j);
            newVal = Math.max(Short.MIN_VALUE, Math.min(Short.MAX_VALUE, newVal));
            adjust.setSample(j, newVal);
        }
        return adjust;
    }

    @Override
    public boolean hasInputs() {
        return input_ != null;
    }

    @Override
    public void connectInput(AudioComponent ac, int inputIndex) {
        if(!hasInputs()) {
            input_ = ac;
        }
        else {
            assert(false);
        }
    }
}
