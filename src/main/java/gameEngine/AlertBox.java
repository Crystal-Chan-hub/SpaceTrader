package gameEngine;

import javafx.scene.Scene;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class AlertBox {

    public static void display(String title, String message) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinWidth(250);

        Label messageLabel = new Label(message);
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> stage.close());

        VBox vb = new VBox(10);
        vb.setPadding(new Insets(20, 0, 20, 20));
        vb.getChildren().addAll(messageLabel, closeButton);
        vb.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vb);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
