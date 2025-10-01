package com.example.synthesizer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;

public class SynthesizeApplication extends Application {
    public void handleButtonPress(String text) {
        System.out.println("I was pressed.");
    }

    int frequency;
    public void handleSliderFreq(Slider slider, Text text) {
        text.setText("Frequency Slider: " + String.format("%.0f", (slider.getValue())));
        frequency = (int) slider.getValue();
    }

    @Override
    public void start(Stage stage) throws IOException {
        AnchorPane ap = new AnchorPane();

        VBox menu = new VBox(10);
        menu.setStyle("-fx-background-color: lightblue");
        Button swBtn = new Button("Create Sine Wave Component");
        Button vcBtn = new Button("Create Volume Control");
        swBtn.setOnAction(e -> ap.getChildren().add(new SineWaveWidget(ap)));
//        vcBtn.setOnAction(e -> ap.getChildren().add(new VolumeWidget()));

        menu.getChildren().addAll(swBtn, vcBtn);

        ap.getChildren().add(menu);

        Scene scene = new Scene(ap, 720, 640);
        stage.setTitle("Synthesizer Program");
        stage.setScene(scene);
        stage.show();
    }

}
