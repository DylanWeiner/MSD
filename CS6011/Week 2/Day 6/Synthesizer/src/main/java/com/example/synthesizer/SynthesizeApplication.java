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

    double vol;
    public void handleSlider(Slider slider, Text text) {
        text.setText("Volume Slider: " + String.format("%.0f", (slider.getValue()*100)) + "%");
        vol = slider.getValue();
    }

    private int swcNumber = 0;

    private void createSWC(AnchorPane ap) {
        HBox swBox = new HBox(10);
        Slider s = new Slider();
        Text name = new Text("SineWave");

        s.setMin(0.0);
        s.setMax(1.0);
        s.setValue(1.0);

        swBox.getChildren().add(name);

        Button swBtn = new Button("Play");
        swBox.getChildren().add(swBtn);

        ap.getChildren().add(swBtn);

        swBtn.setLayoutX(200 + swcNumber * 100);
        swBtn.setLayoutY(200);
        swcNumber++;
    }

    @Override
    public void start(Stage stage) throws IOException {
        AnchorPane ap = new AnchorPane();

        VBox menu = new VBox(10);
        menu.setStyle("-fx-background-color: lightblue");
        Button swBtn = new Button("Create Sine Wave Component");
        Slider vcSld = new Slider();
        swBtn.setOnAction(e -> createSWC(ap));

        Text text = new Text("Volume Slider: 100%");
        Slider slider = new Slider();

        slider.setOnMouseDragged(e -> handleSlider(slider, text));

        Text text2 = new Text("Frequency Slider: ");
        Slider slider2 = new Slider();
        slider.setMin(-32768);
        slider.setMax(32767);
        slider.setOnMouseDragged(e -> handleSlider(slider2, text2));

        HBox hbox = new HBox(15); // 20 is the number of pixels between each box.
        hbox.getChildren().addAll(text, slider);

        Scene scene = new Scene(hbox, 720, 640);
        stage.setTitle("Synthesizer Program");
        stage.setScene(scene);
        stage.show();
    }

}
