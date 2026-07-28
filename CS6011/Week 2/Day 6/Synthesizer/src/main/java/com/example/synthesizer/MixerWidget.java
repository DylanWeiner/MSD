package com.example.synthesizer;
import javafx.scene.layout.AnchorPane;

public class MixerWidget extends AudioComponentWidget {
    public MixerWidget(Mixer mixer, AnchorPane ap) {
        super(mixer, ap, "Mixer");
        hBox_.setLayoutX(200);
        hBox_.setLayoutY(400);
    }
}

