package gameEngine.pages;

import gameEngine.components.Locations;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class LocationsPage extends Page {

    private final Locations locations;
    private final Stage stage;

    public LocationsPage(Stage stage, Locations locations) {
        this.locations = locations;
        this.stage = stage;
    }

    public Scene setSceneNearbyLocations(MainPage mainPage) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(300, 300);
        HBox hbox = new HBox(2);
        VBox content = new VBox(2);
        hbox.setAlignment(Pos.CENTER);
        content.setAlignment(Pos.CENTER_LEFT);
        content.getChildren().addAll(backMainButton(mainPage),
                                    getStandardLabel(" "),
                                    getStandardLabel("Nearby Locations"),
                                    getStandardLabel(" "));
        List<String> nearbyLocations = this.locations.getNearbyLocations();
        for (String nl : nearbyLocations){
            content.getChildren().add(getStandardLabel(nl));
        }
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
