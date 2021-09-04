package gameEngine.pages;

import gameEngine.AlertBox;
import gameEngine.components.Market;
import gameEngine.components.User;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class MarketPage extends Page {

    private final Market market;
    private final User user;
    private final Stage stage;

    public MarketPage(Stage stage, Market market, User user) {
        this.market = market;
        this.user = user;
        this.stage = stage;
    }

    public Scene setSceneBuyFuel(MainPage mainPage) {
        BorderPane paneBuyFuel = new BorderPane();
        VBox vbox = new VBox(10);
        HBox shipIdBox = new HBox(10);
        HBox quantityBox = new HBox(10);
        VBox center = new VBox(0);
        center.setSpacing(10.0);
        center.setAlignment(Pos.CENTER_LEFT);
        vbox.setAlignment(Pos.CENTER);
        shipIdBox.setAlignment(Pos.CENTER);
        quantityBox.setAlignment(Pos.CENTER);

        VBox shipIdsBox = new VBox(10);
        shipIdsBox.setAlignment(Pos.CENTER);
        List<String> shipsIds = this.user.getYourShipsIds();
        if (shipsIds.size() == 0) {
            Label shipIdsLabel = getStandardLabel("You don't have any ships.");
            shipIdsBox.getChildren().add(shipIdsLabel);
        }
        else {
            Label shipIdsLabel = getStandardLabel("Your ship's ids are listed below.");
            shipIdsBox.getChildren().add(shipIdsLabel);
            for (String id: shipsIds) {
                TextField shipIdField = getTextFieldWithText(id);
                setTextField(shipIdField);
                shipIdsBox.getChildren().add(shipIdField);
            }
            shipIdsBox.getChildren().add(getStandardLabel(" "));
        }

        Label shipIdLbl = getStandardLabel("Ship id  ");
        Label quantityLbl = getStandardLabel("Quantity");

        TextField shipIdField = newTextField();
        TextField quantityField = newTextField();

        shipIdBox.getChildren().addAll(shipIdLbl,shipIdField);
        quantityBox.getChildren().addAll(quantityLbl,quantityField);

        Button purchaseButton = getButton("Purchase");
        purchaseButton.setOnAction(e -> {
            String shipId = shipIdField.getText().strip();
            String quantity = quantityField.getText().strip();
            if (shipId != "" && quantity != "") {
                try {
                    int qty = Integer.parseInt(quantity);
                    List<String> fuel = market.tradeGood(shipId, "FUEL", qty, "buy");
                    String successText = "Successful fuel purchase";
                    stage.setScene(this.tradeSuccess(mainPage, fuel, successText));
                }
                catch (IllegalStateException se) {
                    AlertBox.display("Warning", se.getMessage());
                }
                catch (NumberFormatException ne) {
                    AlertBox.display("Warning", "Quantity has to be integer");
                }
            }
            else {
                AlertBox.display("Warning", "Please input Ship id or quantity");
            }
        });
        center.getChildren().addAll(shipIdsBox,shipIdBox,quantityBox);
        vbox.getChildren().addAll(center,purchaseButton);
        paneBuyFuel.setStyle("-fx-background-color: #3e455c");
        paneBuyFuel.setBottom(backMainButton(mainPage));
        paneBuyFuel.setTop(getLabel("Purchase fuel for you ship",paneBuyFuel));
        paneBuyFuel.setCenter(vbox);
        return new Scene(paneBuyFuel, 800, 450);
    }

    public Scene setSceneTradeGood(MainPage mainPage) {
        BorderPane paneTradeGood = new BorderPane();
        VBox vbox = new VBox(10);
        HBox tradesBox = new HBox(10);
        HBox shipIdBox = new HBox(10);
        HBox goodBox = new HBox(10);
        HBox qtyBox = new HBox(10);
        VBox center = new VBox(0);
        center.setSpacing(10.0);
        center.setAlignment(Pos.CENTER_LEFT);
        vbox.setAlignment(Pos.CENTER);
        tradesBox.setAlignment(Pos.CENTER);
        shipIdBox.setAlignment(Pos.CENTER);
        goodBox.setAlignment(Pos.CENTER);
        qtyBox.setAlignment(Pos.CENTER);

        VBox shipIdsBox = new VBox(10);
        shipIdsBox.setAlignment(Pos.CENTER);
        List<String> shipsIds = this.user.getYourShipsIds();
        if (shipsIds.size() == 0) {
            Label shipIdsLabel = getStandardLabel("You don't have any ships.");
            shipIdsBox.getChildren().add(shipIdsLabel);
        }
        else {
            Label shipIdsLabel = getStandardLabel("Your ship's ids are listed below.");
            shipIdsBox.getChildren().add(shipIdsLabel);
            for (String id: shipsIds) {
                TextField shipIdField = getTextFieldWithText(id);
                setTextField(shipIdField);
                shipIdsBox.getChildren().add(shipIdField);
            }
            shipIdsBox.getChildren().add(getStandardLabel(" "));
        }

        Label shipIdLbl = getStandardLabel("Ship id  ");
        Label goodLbl = getStandardLabel("Good     ");
        Label qtyLbl = getStandardLabel("Quantity");

        TextField shipIdField = newTextField();
        TextField goodField = newTextField();
        TextField qtyField = newTextField();

        shipIdBox.getChildren().addAll(shipIdLbl,shipIdField);
        goodBox.getChildren().addAll(goodLbl,goodField);
        qtyBox.getChildren().addAll(qtyLbl,qtyField);

        Button purchaseButton = getButton("Purchase");
        tradeButtonActions(mainPage,purchaseButton,shipIdField,goodField,qtyField,"buy");

        Button sellButton = getButton("Sell");
        tradeButtonActions(mainPage,sellButton,shipIdField,goodField,qtyField,"sell");

        center.getChildren().addAll(shipIdsBox,shipIdBox,goodBox,qtyBox);
        tradesBox.getChildren().addAll(purchaseButton, sellButton);
        vbox.getChildren().addAll(center,tradesBox);
        paneTradeGood.setStyle("-fx-background-color: #3e455c");
        paneTradeGood.setBottom(backMainButton(mainPage));
        paneTradeGood.setTop(getLabel("Purchase or sell good for you ship",paneTradeGood));
        paneTradeGood.setCenter(vbox);
        return new Scene(paneTradeGood, 800, 450);
    }

    public Scene tradeSuccess(MainPage mainPage, List<String> good, String successText) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(300, 300);
        HBox hbox = new HBox(2);
        VBox content = new VBox(2);
        hbox.setAlignment(Pos.CENTER);
        content.setAlignment(Pos.CENTER_LEFT);
        content.getChildren().addAll(backMainButton(mainPage),
                                    getStandardLabel(" "),
                                    getStandardLabel(successText),
                                    getStandardLabel(" "));
        for (String g : good){
            content.getChildren().add(getStandardLabel(g));
        }
        content.getChildren().add(backMainButton(mainPage));
        hbox.getChildren().add(content);

        scrollPane.setContent(hbox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background:#3e455c;");
        return new Scene(scrollPane, 800, 450);
    }

    public Scene setSceneMarketplace(MainPage mainPage) {
        BorderPane paneMarketplace = new BorderPane();
        VBox vbox = new VBox(10);
        VBox center = new VBox(0);
        HBox locationBox = new HBox(10);
        center.setSpacing(10.0);
        center.setAlignment(Pos.CENTER_LEFT);
        vbox.setAlignment(Pos.CENTER);
        locationBox.setAlignment(Pos.CENTER);

        Label locationLbl = getStandardLabel("Location");
        TextField locationField = newTextField();
        locationBox.getChildren().addAll(locationLbl,locationField);

        Button getButton = getButton("Get");
        getButton.setOnAction(e -> {
            String location = locationField.getText().strip();
            if (location != "") {
                try {
                    List<String> marketplaceInfo = market.getMarketplaceInfo(location);
                    stage.setScene(this.getMarketplaceInfo(mainPage, marketplaceInfo));
                }
                catch (IllegalStateException se) {
                    AlertBox.display("Warning", se.getMessage());
                }
            }
            else {
                AlertBox.display("Warning", "Please input Location");
            }
        });
        center.getChildren().add(locationBox);
        vbox.getChildren().addAll(center,getButton);
        paneMarketplace.setStyle("-fx-background-color: #3e455c");
        paneMarketplace.setBottom(backMainButton(mainPage));
        paneMarketplace.setTop(getLabel("Get info on a locations marketplace",paneMarketplace));
        paneMarketplace.setCenter(vbox);
        return new Scene(paneMarketplace, 800, 450);
    }

    public Scene getMarketplaceInfo(MainPage mainPage,List<String> marketInfo) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(300, 300);
        HBox hbox = new HBox(2);
        VBox content = new VBox(2);
        hbox.setAlignment(Pos.CENTER);
        content.setAlignment(Pos.CENTER_LEFT);
        content.getChildren().addAll(backMainButton(mainPage),
                                    getStandardLabel(" "),
                                    getStandardLabel("Marketplace Information"),
                                    getStandardLabel(" "));

        for (String mk : marketInfo){
            content.getChildren().add(getStandardLabel(mk));
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

    private void tradeButtonActions(MainPage mainPage, Button button, TextField shipIdField,
                                        TextField goodField,TextField qtyField,
                                        String tradeAction) {
        button.setOnAction(e -> {
            String shipId = shipIdField.getText().strip();
            String good = goodField.getText().strip();
            String quantity = qtyField.getText().strip();
            if (shipId != "" && quantity != "" && good != "") {
                try {
                    int qty = Integer.parseInt(quantity);
                    String successText;
                    List<String> tradedGood = market.tradeGood(shipId, good, qty, tradeAction);
                    if (tradeAction.equals("sell")) {
                        successText = "Good sold successfully";
                    }
                    else {
                        successText = "Successful good purchase";
                    }
                    stage.setScene(this.tradeSuccess(mainPage, tradedGood, successText));
                }
                catch (IllegalStateException se) {
                    AlertBox.display("Warning", se.getMessage());
                }
                catch (NumberFormatException ne) {
                    AlertBox.display("Warning", "Quantity has to be integer");
                }
            }
            else {
                AlertBox.display("Warning", "Please input Ship id, good or quantity");
            }
        });
    }

}
