package com.example.synthesizer;

import static com.example.synthesizer.AudioClip.FULL_SAMPLE_RATE;

public class VolumeAdjuster implements AudioComponent {
    private double volume_;
    private AudioComponent input_;

    public VolumeAdjuster(double volume) {
        volume_ = volume;
    }


    @Override
    public AudioClip getClip() {
        AudioClip original = input_.getClip();
        AudioClip adjust = new AudioClip();
        for(int i = 0; i < FULL_SAMPLE_RATE; i++) {
            int newVal = (int) (original.getSample(i)*volume_);
            adjust.setSample(i,newVal);
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
