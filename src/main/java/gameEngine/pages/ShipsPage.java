package gameEngine.pages;

import gameEngine.AlertBox;
import gameEngine.components.Ships;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class ShipsPage extends Page {

    private final Ships ships;
    private final Stage stage;

    public ShipsPage(Stage stage, Ships ships) {
        this.ships = ships;
        this.stage = stage;
    }

    public Scene setSceneAbleShips(MainPage mainPage) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(300, 300);

        HBox locationBox = new HBox(10);
        HBox typeBox = new HBox(10);
        Button buyShip = getButton("Buy Ship");
        Label locationLbl = getStandardLabel("Location to buy");
        Label typeLbl = getStandardLabel("Ship Type           ");
        TextField locationField = newTextField();
        TextField typeField = newTextField();

        locationBox.getChildren().addAll(locationLbl,locationField);
        typeBox.getChildren().addAll(typeLbl,typeField);
        buyShip.setOnAction(e -> {
            String location = locationField.getText().strip();
            String type = typeField.getText().strip();
            if (location != "" && type != "") {
                try {
                    List<String> ship = this.ships.buyShip(location, type);
                    stage.setScene(this.setSuccessfulShip(mainPage, ship));
                }
                catch (IllegalStateException se) {
                    AlertBox.display("Warning", se.getMessage());
                }
            }
            else {
                AlertBox.display("Warning", "Please input location or type");
            }
        });

        HBox hbox = new HBox(2);
        VBox content = new VBox(2);
        hbox.setAlignment(Pos.CENTER);
        content.setAlignment(Pos.CENTER_LEFT);
        List<String> availableShips = this.ships.getAvailableShips();
        content.getChildren().addAll(backMainButton(mainPage),
                                    getStandardLabel(" "),
                                    getStandardLabel("Available Ships"),
                                    getStandardLabel(" "));
        for (String aS : availableShips){
            content.getChildren().add(getStandardLabel(aS));
        }
        HBox bottom = backMainButton(mainPage);
        bottom.getChildren().add(buyShip);
        content.getChildren().addAll(locationBox,typeBox,bottom);
        hbox.getChildren().add(content);

        scrollPane.setContent(hbox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background:#3e455c;");
        return new Scene(scrollPane, 800, 450);
    }

    public Scene setSuccessfulShip(MainPage mainPage, List<String> ship) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(300, 300);
        HBox hbox = new HBox(2);
        VBox content = new VBox(2);
        hbox.setAlignment(Pos.CENTER);
        content.setAlignment(Pos.CENTER_LEFT);
        content.getChildren().addAll(backMainButton(mainPage),
                                    getStandardLabel(" "),
                                    getStandardLabel("Purchase Ship Successful"),
                                    getStandardLabel(" "));
        for (String sp : ship){
            content.getChildren().add(getStandardLabel(sp));
        }
        return getScene(mainPage, scrollPane, hbox, content);
    }

    public Scene setSceneYourShips(MainPage mainPage) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(300, 300);
        HBox hbox = new HBox(2);
        VBox content = new VBox(2);
        hbox.setAlignment(Pos.CENTER);
        content.setAlignment(Pos.CENTER_LEFT);
        content.getChildren().addAll(backMainButton(mainPage),
                                    getStandardLabel(" "),
                                    getStandardLabel("Your Ships"),
                                    getStandardLabel(" "));
        List<String> yourShips = this.ships.getYourShips();
        if (yourShips.size() == 0) {
            content.getChildren().add(getStandardLabel("You have not purchased any ship."));
        }
        else {
            for (String sp : yourShips){
                content.getChildren().add(getStandardLabel(sp));
            }
        }
        return getScene(mainPage, scrollPane, hbox, content);
    }

    private Scene getScene(MainPage mainPage, ScrollPane scrollPane, HBox hbox, VBox content) {
        content.getChildren().add(backMainButton(mainPage));
        hbox.getChildren().add(content);

        scrollPane.setContent(hbox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background:#3e455c;");
        return new Scene(scrollPane, 800, 450);
    }

    private HBox backMainButton(MainPage mainPage) {
        HBox backButton = new HBox();
        backButton.setSpacing(10.0);
        backButton.setAlignment(Pos.CENTER);
        Button backMain = getButton("Back");
        backMain.setOnAction(e -> stage.setScene(mainPage.getSceneMain()));
        backButton.getChildren().add(backMain);
        return backButton;
    }

}
