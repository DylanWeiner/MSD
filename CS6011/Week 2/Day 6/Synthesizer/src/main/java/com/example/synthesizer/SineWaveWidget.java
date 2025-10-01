package com.example.synthesizer;

import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

public class SineWaveWidget extends Pane {
    private int swcNumber = 0;
    private HBox swBox;
    private Slider s;
    Text name;
    Button swBtn;
    SineWave sineWave;

        public void play() throws LineUnavailableException {
            Clip c = AudioSystem.getClip();// Your code

            // This is the format that we're following, 44.1 KHz mono audio, 16 bits per sample.
            AudioFormat format16 = new AudioFormat( 44100, 16, 1, true, false );

            c.open( format16, this.sineWave.getClip().getData(), 0, this.sineWave.getClip().getData().length ); // Reads data from our byte array to play it.

            System.out.println( "About to play..." );
            c.start(); // Plays it.
            c.loop( 2 ); // Plays it 2 more times if desired, so 6 seconds total

// Makes sure the program doesn't quit before the sound plays.
            while(c.getFramePosition() < AudioClip.FULL_SAMPLE_RATE || c.isActive() || c.isRunning() ){
                // Do nothing while we wait for the note to play.
            }

            System.out.println( "Done." );
            c.close();
        }

    public SineWaveWidget(AnchorPane ap) {
        swBox = new HBox(10);
        s = new Slider();
        name = new Text("SineWave");

        s.setMin(-32768);
        s.setMax(32767);
        sineWave = new SineWave(440);
        s.valueProperty().addListener((ov, oldVal, newVal) -> {
            sineWave.setFrequency(newVal.intValue());
//            System.out.println(sineWave.frequency);
        });

        swBox.getChildren().add(name);
        swBtn = new Button("Play");
        swBtn.setOnAction( e -> {
            try {
                play();
            } catch (LineUnavailableException ex) {
                System.out.println("Line unavailable");;
            }
        });
        swBox.getChildren().add(swBtn);
        swBox.getChildren().add(s);

        ap.getChildren().add(name);
        ap.getChildren().add(swBtn);
        ap.getChildren().add(s);

        s.setLayoutX(1 + swcNumber * 135);
        s.setLayoutY(235);
        swBtn.setLayoutX(50 + swcNumber * 135);
        swBtn.setLayoutY(200);
        swcNumber++;
    }
}
