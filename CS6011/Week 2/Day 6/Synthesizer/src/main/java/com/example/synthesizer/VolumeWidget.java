package com.example.synthesizer;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class VolumeWidget extends AudioComponentWidget {
    double vol;

    public void handleSliderVol(Slider slider, Text text) {
        text.setText("Volume Slider: " + String.format("%.0f", (slider.getValue()*100)) + "%");
        vol = slider.getValue();
    }

    public VolumeWidget(AnchorPane ap, VolumeAdjuster vol) {
        super(vol, ap, "Volume: ");
            Slider s = new Slider();
            Text name = new Text("Volume: ");


            s.setMin(0.0);
            s.setMax(1.0);
            s.setValue(1.0);

            s.setOnMouseDragged(e -> handleSliderVol(s, name));


            s.valueProperty().addListener((ov, oldVal, newVal) -> {
                vol.setVolume(newVal.doubleValue());
                s.setValue(newVal.doubleValue());
            });

            vBoxLeft.getChildren().add(name);
            vBoxLeft.getChildren().add(s);
        }
}
