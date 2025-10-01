//package com.example.synthesizer;
//import javafx.scene.control.Slider;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.Pane;
//import javafx.scene.text.Text;
//
//double vol;
//public void handleSliderVol(Slider slider, Text text) {
//    text.setText("Volume Slider: " + String.format("%.0f", (slider.getValue()*100)) + "%");
//    vol = slider.getValue();
//}
//
//public class VolumeWidget extends Pane {
//    private int volNumber = 0;
//    private int yVolNumber = 0;
//    private void createVolume(AnchorPane ap) {
//        if(volNumber < 6) {
//            HBox swBox = new HBox(10);
//            Slider s = new Slider();
//            Text name = new Text("Volume Slider: ");
//
//            s.setMin(0.0);
//            s.setMax(1.0);
//            s.setValue(1.0);
//
//            s.setOnMouseDragged(e -> handleSliderVol(s, name));
//
//            swBox.getChildren().add(name);
//            swBox.getChildren().add(s);
//
//            ap.getChildren().add(name);
//            ap.getChildren().add(s);
//
//            s.setLayoutX(50 + volNumber * 100);
//            s.setLayoutY(450 + yVolNumber * 100);
//            if (yVolNumber == 0) {
//                yVolNumber++;
//            } else if (volNumber >= 1) {
//                yVolNumber = 0;
//            }
//            volNumber++;
//        }
//    }
//}
