package com.example.synthesizer;

import java.util.ArrayList;

import static com.example.synthesizer.AudioClip.FULL_SAMPLE_RATE;

public class Mixer implements AudioComponent {
    private ArrayList<AudioComponent> input_;

    public Mixer(ArrayList<AudioComponent> input) {
        input_ = input;
    }
    @Override
    public AudioClip getClip() {
        AudioClip adjust = new AudioClip();
        for (AudioComponent audioComponent : input_) {
            AudioClip original = audioComponent.getClip();
            for (int j = 0; j < FULL_SAMPLE_RATE; j++) {
                int newVal = (original.getSample(j)) + adjust.getSample(j);
                adjust.setSample(j,newVal);
            }

        }
        return adjust;
    }

    @Override
    public boolean hasInputs() {
            return !input_.isEmpty();
    }

    @Override
    public void connectInput(AudioComponent ac, int inputIndex) {
        if(!hasInputs()) {
            input_.add(inputIndex, ac);
        }
        else {
            assert(false);
        }
    }
}
