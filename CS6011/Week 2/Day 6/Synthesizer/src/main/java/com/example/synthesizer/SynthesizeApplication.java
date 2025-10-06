package com.example.synthesizer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.synthesizer.SoundOutput.play;

public class SynthesizeApplication extends Application {
    AnchorPane ap = new AnchorPane();
    private static ArrayList<AudioComponentWidget> comps = new ArrayList<>();

    public static ArrayList<AudioComponentWidget> getComponents() {
        return comps;
    }

    @Override
    public void start(Stage stage) throws IOException {
        BorderPane borderPane = new BorderPane();
        Speaker speaker = new Speaker(ap, null);
        comps.add(speaker);

        VBox menu = new VBox(10);
        menu.setStyle("-fx-background-color: lightblue");
        Button swBtn = new Button("Create Sine Wave Component");
        Button vcBtn = new Button("Create Volume Control");

        menu.getChildren().addAll(swBtn, vcBtn);

        borderPane.setTop(menu);
        borderPane.setCenter(ap);

        Button pBtn = new Button("Play");
        pBtn.setOnAction( e -> {
            try {
                play(speaker.getAudioClip());
            } catch (LineUnavailableException ex) {
                System.out.println("Line unavailable");;
            }
        });

        borderPane.setBottom(pBtn);

        swBtn.setOnAction(e -> CreateSineWaveWidget());
        vcBtn.setOnAction(e -> CreateVolumeControlWidget());

        Scene scene = new Scene(borderPane, 720, 640);
        stage.setTitle("Synthesizer Program");
        stage.setScene(scene);
        stage.show();
    }

    private void CreateSineWaveWidget() {
        SineWave sine = new SineWave(440);
        SineWaveWidget sineWave = new SineWaveWidget(ap, sine);
        comps.add(sineWave);
    }

    private void CreateVolumeControlWidget() {
        VolumeAdjuster  vol = new VolumeAdjuster();
        VolumeWidget volume = new VolumeWidget(ap, vol);
        comps.add(volume);
    }

}
