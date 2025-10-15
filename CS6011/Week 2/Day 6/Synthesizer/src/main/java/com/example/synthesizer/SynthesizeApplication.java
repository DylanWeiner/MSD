package com.example.synthesizer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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
        Button mixBtn = new Button("Create a Mixer"); // This section is responsible for the create widget section.

        menu.getChildren().addAll(swBtn, vcBtn, mixBtn); // adds create widgets into a shared menu.

        borderPane.setTop(menu);
        borderPane.setCenter(ap);

        Button pBtn = new Button("Play");
        pBtn.setOnAction( e -> {
            try {
                play(speaker.getAudioClip());
            } catch (LineUnavailableException ex) {
                System.out.println("Line unavailable");;
            }
        }); // Allows audio to play from speaker output.

        borderPane.setBottom(pBtn);

        swBtn.setOnAction(e -> CreateSineWaveWidget());
        vcBtn.setOnAction(e -> CreateVolumeControlWidget());
        mixBtn.setOnAction(e -> CreateMixerWidget());

        Scene scene = new Scene(borderPane, 720, 640);
        stage.setTitle("Synthesizer Program");
        stage.setScene(scene);
        stage.show();
    }

    private void CreateMixerWidget() {
        Mixer mix = new Mixer();
        MixerWidget mixerWidget = new MixerWidget(mix, ap);
        comps.add(mixerWidget);
    } // Mixer Widget Connection

    private void CreateSineWaveWidget() {
        SineWave sine = new SineWave(440);
        SineWaveWidget sineWave = new SineWaveWidget(ap, sine);
        comps.add(sineWave);
    } // Sinewave Widget Connection

    private void CreateVolumeControlWidget() {
        VolumeAdjuster  vol = new VolumeAdjuster();
        VolumeWidget volume = new VolumeWidget(ap, vol);
        comps.add(volume);
    } // Volume Control Widget Connection

}
