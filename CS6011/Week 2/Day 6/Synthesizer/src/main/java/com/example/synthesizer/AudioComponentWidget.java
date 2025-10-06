package com.example.synthesizer;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import java.util.ArrayList;

public class AudioComponentWidget extends Pane {
    Circle output_ = new Circle(0, 0, 10);
    Circle input_ = new Circle(0, 0, 10);
    Line connectionLine_ = new Line(0, 0, 10, 10);
    boolean connected = false;
    boolean drawConnection_ = false;
    AudioComponent ac;
    AudioComponentWidget parent;
    double startY_ = 0;
    double startX_ = 0;
    AudioComponentWidget connection = null;

    HBox hBox_ = new HBox();
    VBox vBoxLeft = new VBox(); //input
    VBox vBoxRight = new VBox();//output

    public AudioComponentWidget(AudioComponent audioComponent, AnchorPane ap) {
        this.ac = audioComponent;
        output_.setOnMousePressed(e -> beginDrawConnectionLine(e, output_));
        output_.setOnMouseReleased(e -> endDrawConnectionLine(e));
        input_.setOnMousePressed(e -> beginDrawConnectionLine(e, input_));
        input_.setOnMouseReleased(e -> endDrawInputConnectionLine(e));

        this.setOnMousePressed(e -> handleBeginDrag(e));
        this.setOnMouseDragged(e -> handleDrag(e));

        vBoxRight.getChildren().add(output_);
        vBoxLeft.getChildren().add(input_);

        hBox_.getChildren().add(vBoxLeft);
        hBox_.getChildren().add(vBoxRight);

        hBox_.setLayoutX(200);
        hBox_.setLayoutY(200);
        hBox_.setStyle("-fx-background-color: lightblue;" + "-fx-border-color: darkblue;" + "-fx-border-width: 1px;");
        this.getChildren().add(hBox_);
        ap.getChildren().add(this);

        connectionLine_.setMouseTransparent(true); // If a line is on the screen, we cannot click on it.
        connectionLine_.setStroke(Color.BLACK);
        connectionLine_.setStrokeWidth(4);
    }

    private void endDrawInputConnectionLine(MouseEvent e) {
        drawConnection_ = false;
        ArrayList<AudioComponentWidget> comps = SynthesizeApplication.getComponents();
        for(AudioComponentWidget comp : comps) {
            if( comp != this ) {
                Point2D mousePosition = new Point2D( e.getSceneX(), e.getSceneY() );
                Point2D input = comp.output_.localToScene(0, 0);
                double distance = input.distance(mousePosition);
                if(distance < 20) {
                    System.out.println("connection");
                    connected = true;
                    connection = comp;
                    break;
                }
                else {
                    System.out.println("no connection: dist: " + distance);
                } // Needs to determine if the mouse is over another port.
            }
        }
        if(!connected) {
            this.getChildren().remove(connectionLine_);
        }
        else{
            if(connection instanceof Speaker speaker) {
                speaker.setSound(this);
            }
            ac.connectInput(connection.ac, 0);
        }
    }

    public void endDrawConnectionLine(MouseEvent e) {
        drawConnection_ = false;

        ArrayList<AudioComponentWidget> comps = SynthesizeApplication.getComponents();
        for(AudioComponentWidget comp : comps) {
            if( comp != this ) {
                Point2D mousePosition = new Point2D( e.getSceneX(), e.getSceneY() );
                Point2D input = comp.input_.localToScene(0, 0);
                double distance = input.distance(mousePosition);
                if(distance < 30) {
                    System.out.println("connection");
                    connected = true;
                    connection = comp;
                    break;
                }
			else {
                    System.out.println("no connection: dist: " + distance);
                } // Needs to determine if the mouse is over another port.
            }
        }
        if(!connected) {
            this.getChildren().remove(connectionLine_);
        }
        else {
            if(connection instanceof Speaker speaker) {
                speaker.setSound(this);
            }
            connection.getAudioComponent().connectInput(this.ac, 0);
        }
    }

    private void beginDrawConnectionLine(MouseEvent e, Circle output_) {
        e.consume ();
        drawConnection_ = true;

        Point2D outputScreenCoord = output_.localToScene (0, 0);
        Point2D childInThis = this.sceneToLocal( outputScreenCoord);
        connectionLine_.setStartX(childInThis.getX());
        connectionLine_.setStartY(childInThis.getY());
        connectionLine_.setEndX(childInThis.getX());
        connectionLine_.setEndY(childInThis.getY());
        if(!this.getChildren().contains(connectionLine_)) {
            this.getChildren().add( connectionLine_);
        }
    }

    private void handleBeginDrag(MouseEvent e) {
        this.toFront(); // This makes it so whichever window you are dragging becomes the front window.
        startX_ = e.getSceneX() - getLayoutX();
        startY_ = e.getSceneY() - getLayoutY();
    }

    private void handleDrag(MouseEvent e) {
        if(drawConnection_) {
            Point2D childInThis = this.sceneToLocal(new Point2D(e.getSceneX(), e.getSceneY()));
            connectionLine_.setEndX(childInThis.getX());
            connectionLine_.setEndY(childInThis.getY());
        }
        else {
            setLayoutX(e.getSceneX() - startX_);
            setLayoutY(e.getSceneY() - startY_);
        }
    }

    public AudioComponent getAudioComponent() {
        return ac;
    }
}
