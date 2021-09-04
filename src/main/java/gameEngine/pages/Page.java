package gameEngine.pages;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public abstract class Page {

    final int SIZE = 60;

    protected void setTextField(TextField field) {
        field.setAlignment(Pos.BASELINE_CENTER);
        field.setPrefWidth(150);
        field.setMaxWidth(150);
    }

    protected TextField newTextField() {
        TextField field = new TextField();
        field.setAlignment(Pos.BASELINE_CENTER);
        field.setPrefWidth(150);
        field.setMaxWidth(150);
        field.setStyle("-fx-background-color: #E7F0FD;"
                + "-fx-border-color: white;");
        field.setFont(new Font("Gill Sans", 15));
        return field;
    }

    protected Label getLabel(String text, Pane pane) {

        Label lbl = new Label(text);
        Color color = Color.rgb(255, 255, 255);
        lbl.setTextFill(color);
        lbl.setAlignment(Pos.BASELINE_CENTER);
        lbl.setPrefHeight(SIZE);
        lbl.prefWidthProperty().bind(pane.widthProperty());
        return lbl;
    }

    protected Label getStandardLabel(String text) {

        Label lbl = new Label(text);
        Color color = Color.rgb(255, 255, 255);
        lbl.setTextFill(color);
        lbl.setAlignment(Pos.BASELINE_CENTER);
        return lbl;
    }

    protected Button getButton(String text) {
        Button button = new Button(text);
        button.setFont(new Font("Gill Sans", 15));
        button.setStyle("-fx-background-color: #E7F0FD;"
                + "-fx-border-color: white;");
        return button;
    }

    protected Button getWideButton(String text) {
        Button button = new Button(text);
        button.setMinWidth(120);
        button.setFont(new Font("Gill Sans", 15));
        button.setStyle("-fx-background-color: #E7F0FD;"
                + "-fx-border-color: white;");
        return button;
    }


    protected TextField getTextFieldWithText(String text) {
        TextField textField = new TextField(text);
        textField.setStyle("-fx-background-color: #E7F0FD;"
                + "-fx-border-color: white;");
        textField.setFont(new Font("Gill Sans", 15));
        return textField;
    }

}
