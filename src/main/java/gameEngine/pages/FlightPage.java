package gameEngine.pages;

import gameEngine.AlertBox;
import gameEngine.components.Flight;
import gameEngine.components.User;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class FlightPage extends Page {

    private final Flight flight;
    private final User user;
    private final Stage stage;
    private List<String> flightPlan;

    public FlightPage(Stage stage, Flight flight, User user) {
        this.flightPlan = new ArrayList<>();
        this.flight = flight;
        this.stage = stage;
        this.user = user;
    }

    public Scene setSceneNewFlightPlan(MainPage mainPage) {
        BorderPane paneNewFlight = new BorderPane();
        VBox vbox = new VBox(10);
        HBox shipIdBox = new HBox(10);
        HBox destBox = new HBox(10);
        VBox center = new VBox(0);
        center.setSpacing(10.0);
        center.setAlignment(Pos.CENTER_LEFT);
        vbox.setAlignment(Pos.CENTER);
        shipIdBox.setAlignment(Pos.CENTER);
        destBox.setAlignment(Pos.CENTER);

        VBox shipsIdsBox = new VBox(10);
        shipsIdsBox.setAlignment(Pos.CENTER);
        List<String> shipsIds = this.user.getYourShipsIds();
        if (shipsIds.size() == 0) {
            Label shipIdsLabel = getStandardLabel("You don't have any ships.");
            shipsIdsBox.getChildren().add(shipIdsLabel);
        }
        else {
            Label shipIdsLabel = getStandardLabel("Your ship's ids are listed below.");
            shipsIdsBox.getChildren().add(shipIdsLabel);
            for (String id: shipsIds) {
                TextField shipIdField = getTextFieldWithText(id);
                setTextField(shipIdField);
                shipsIdsBox.getChildren().add(shipIdField);
            }
            shipsIdsBox.getChildren().add(getStandardLabel(" "));
        }

        Label shipIdLbl = getStandardLabel("Ship id         ");
        Label destLbl = getStandardLabel("Destination ");

        TextField shipIdField = newTextField();
        TextField destField = newTextField();

        shipIdBox.getChildren().addAll(shipIdLbl,shipIdField);
        destBox.getChildren().addAll(destLbl,destField);

        Button createPlanButton = getButton("Create Plan");
        createPlanButton.setMinWidth(150);
        createPlanButton.setOnAction(e -> {
            String shipId = shipIdField.getText().strip();
            String dest = destField.getText().strip();
            if (shipId != "" && dest != "") {
                try {
                    this.flightPlan = flight.newFlightPlan(shipId, dest);
                    stage.setScene(this.sceneFlightPlan(mainPage));
                }
                catch (IllegalStateException se) {
                    AlertBox.display("Warning", se.getMessage());
                }
            }
            else {
                AlertBox.display("Warning", "Please input Ship id or destination");
            }
        });

        Button viewPlanButton = getButton("View current flight plan");
        viewPlanButton.setMinWidth(150);
        viewPlanButton.setOnAction(e -> {
            if (this.flightPlan.size() != 0) {
                stage.setScene(this.sceneFlightPlan(mainPage));
            }
            else {
                AlertBox.display("Warning", "You have no current flight plan");
            }
        });

        center.getChildren().addAll(shipsIdsBox,shipIdBox,destBox);
        vbox.getChildren().addAll(center,createPlanButton,viewPlanButton);
        paneNewFlight.setStyle("-fx-background-color: #3e455c");
        paneNewFlight.setBottom(backMainButton(mainPage));
        paneNewFlight.setTop(getLabel("Create or View Flight Plan",paneNewFlight));
        paneNewFlight.setCenter(vbox);
        return new Scene(paneNewFlight, 800, 450);
    }

    public Scene sceneFlightPlan(MainPage mainPage) {
        BorderPane paneFlightPlan = new BorderPane();
        HBox hbox = new HBox(2);
        VBox vbox = new VBox(2);
        hbox.setAlignment(Pos.CENTER);
        vbox.setAlignment(Pos.CENTER_LEFT);
        if (this.flightPlan.size() == 0) {
            vbox.getChildren().add(getStandardLabel("You have no current flight plan"));
        }
        else {
            for (String fp : this.flightPlan){
                vbox.getChildren().add(getStandardLabel(fp));
            }
        }
        hbox.getChildren().add(vbox);

        Button refreshFlightButton = getButton("Refresh current flight plan");
        refreshFlightButton.setOnAction(e -> {
            this.flightPlan = this.flight.getCurrentFlight();
            stage.setScene(this.sceneFlightPlan(mainPage));
        });

        paneFlightPlan.setStyle("-fx-background-color: #3e455c");
        paneFlightPlan.setTop(getLabel("Current Flight Plan",paneFlightPlan));
        paneFlightPlan.setCenter(hbox);
        HBox bottom = backMainButton(mainPage);
        bottom.getChildren().add(refreshFlightButton);
        paneFlightPlan.setBottom(bottom);
        return new Scene(paneFlightPlan, 800, 450);
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
