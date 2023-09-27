package com.example.draggablecircle;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.input.*;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class dragCircle extends Application {

    final Text source = new Text(50, 100, "DRAG ME");
    final Text target = new Text(300, 100, "DROP HERE");

    @Override
    public void start(final Stage primaryStage)
    {
        Pane canvas = new Pane();
        canvas.setPrefSize(600, 400);

        source.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                /* drag was detected, start a drag-and-drop gesture*/
                /* allow any transfer mode : ændret til move */
                Dragboard db = source.startDragAndDrop(TransferMode.MOVE);

                /* Put a string on a dragboard */
                ClipboardContent content = new ClipboardContent();
                content.putImage(source.snapshot(null,null)); // drag ikon, her er det et billede af teksten
                content.putString(source.getText());
                db.setContent(content);

                event.consume();
                System.out.println("OnDragDetected");
            }
        });

        target.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* data is dragged over the target */
                /* accept it only if it is not dragged from the same node
                 * and if it has a string data */
                if (event.getGestureSource() != target &&
                        event.getDragboard().hasString()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }

                event.consume();
                System.out.println("OnDragOver");

            }
        });

        target.setOnDragEntered(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag-and-drop gesture entered the target */
                /* show to the user that it is an actual gesture target */
                if (event.getGestureSource() != target &&
                        event.getDragboard().hasString()) {
                    target.setFill(Color.GREEN);
                }

                event.consume();
                System.out.println("OnDragEntered");

            }
        });

        target.setOnDragExited(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* mouse moved away, remove the graphical cues */
                target.setFill(Color.BLACK);

                event.consume();
                System.out.println("OnDragExited");

            }
        });

        target.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* data dropped */
                /* if there is a string data on dragboard, read it and use it */
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasString()) {
                    target.setText(db.getString());
                    success = true;
                    System.out.println("Modtaget: "+db.getString());
                }
                /* let the source know whether the string was successfully
                 * transferred and used */
                event.setDropCompleted(success);

                event.consume();
                System.out.println("OnDragDropped");

            }
        });

        source.setOnDragDone(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag and drop gesture ended */
                /* if the data was successfully moved, clear it */
                if (event.getTransferMode() == TransferMode.MOVE) {
                    source.setText("");
                    System.out.println("Drag var succes");
                }
                else
                    System.out.println("Mislykket drag");
                event.consume();
                System.out.println("OnDragDone");

            }
        });

        canvas.getChildren().addAll(source, target);
        primaryStage.setScene(new Scene(canvas));
        primaryStage.setTitle("JavaFX draggable text");
        primaryStage.show();
    }

    public static void main(final String[] args)
    {
        launch(args);
    }
}