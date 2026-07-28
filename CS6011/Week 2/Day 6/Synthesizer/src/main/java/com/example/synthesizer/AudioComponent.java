package com.example.synthesizer;

public interface AudioComponent {
        AudioClip getClip();
        boolean hasInputs();
        void connectInput(AudioComponent ac, int inputIndex);
}
