package com.example.synthesizer;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

public class Speaker extends AudioComponentWidget {
    private AudioComponentWidget sound;
    public Speaker(AnchorPane ap, AudioComponent ac) {
        super(ac, ap);
        this.sound = null;
        vBoxRight.getChildren().remove(output_);
    }

    public AudioClip getAudioClip() {
        return this.sound.getAudioComponent().getClip();
    }

    public void setSound(AudioComponentWidget sound) {
        this.sound = sound;
    }
}
