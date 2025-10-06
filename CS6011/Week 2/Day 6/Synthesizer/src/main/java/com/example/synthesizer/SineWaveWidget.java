package com.example.synthesizer;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class SineWaveWidget extends AudioComponentWidget {
    Slider s;
    Text name;

    public SineWaveWidget(AnchorPane ap, SineWave sineWave) {
        super(sineWave, ap, "Frequency: ");
        s = new Slider();
        name = new Text("SineWave");

        s.setMin(-32768);
        s.setMax(32767);
        s.valueProperty().addListener((ov, oldVal, newVal) -> {
            sineWave.setFrequency(newVal.intValue());
            s.setValue(newVal.intValue());
        });

        vBoxLeft.getChildren().add(name);
        vBoxLeft.getChildren().add(s);
    }
}
