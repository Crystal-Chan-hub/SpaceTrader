package gameEngine.pages;

import gameEngine.components.Status;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainPage extends Page {

    private final Stage stage;
    private Status status;
    private Scene sceneMain;
    private LoansPage loansPage;
    private ShipsPage shipsPage;
    private FlightPage flightPage;
    private MarketPage marketPage;
    private LocationsPage locationsPage;

    public MainPage(Stage stage) {
        this.stage = stage;
    }

    public Scene setSceneMain(UserPage userPage, String[] unameToken) {

        HBox saveYourToken = new HBox(10);
        saveYourToken.setAlignment(Pos.CENTER);
        TextField tokenField = getTextFieldWithText(unameToken[1]);
        setTextField(tokenField);
        saveYourToken.getChildren().addAll(getStandardLabel("Save token for login :"),tokenField);

        BorderPane paneMain = new BorderPane();
        VBox viewItemsButtons = new VBox(10);
        VBox userInfoButtons = new VBox(10);
        VBox marketButtons = new VBox(10);
        viewItemsButtons.setAlignment(Pos.CENTER);
        userInfoButtons.setAlignment(Pos.CENTER_LEFT);
        marketButtons.setAlignment(Pos.CENTER_RIGHT);
        Button viewUserInfoButton = getWideButton("User Info");
        Button viewYourLoansButton = getWideButton("Your Loans");
        Button viewYourShipsButton = getWideButton("Your Ships");
        Button viewNearbyButton = getWideButton("Nearby Locations");
        Button viewAbleLoansButton = getWideButton("Request Loans");
        Button viewAbleShipsButton = getWideButton("Buy Ships");
        Button viewMarketButton = getWideButton("Marketplace");
        Button flightPlanButton = getWideButton("Flight Plan");
        Button buyFuelButton = getWideButton("Buy Fuel");
        Button tradeGoodButton = getWideButton("Trade Good");
        viewUserInfoButton.setOnAction(e -> stage.setScene(userPage.setSceneUserInfo(this)));
        viewAbleLoansButton.setOnAction(e -> stage.setScene(this.loansPage.setSceneAbleLoans(this)));
        viewYourLoansButton.setOnAction(e -> stage.setScene(this.loansPage.setSceneYourLoans(this)));
        viewYourShipsButton.setOnAction(e -> stage.setScene(this.shipsPage.setSceneYourShips(this)));
        viewNearbyButton.setOnAction(e -> stage.setScene(this.locationsPage.setSceneNearbyLocations(this)));
        viewAbleShipsButton.setOnAction(e -> stage.setScene(this.shipsPage.setSceneAbleShips(this)));
        viewMarketButton.setOnAction(e -> stage.setScene(this.marketPage.setSceneMarketplace(this)));
        flightPlanButton.setOnAction(e -> stage.setScene(this.flightPage.setSceneNewFlightPlan(this)));
        buyFuelButton.setOnAction(e -> stage.setScene(this.marketPage.setSceneBuyFuel(this)));
        tradeGoodButton.setOnAction(e -> stage.setScene(this.marketPage.setSceneTradeGood(this)));
        viewItemsButtons.getChildren().addAll(viewAbleLoansButton,viewAbleShipsButton,flightPlanButton);
        userInfoButtons.getChildren().addAll(viewUserInfoButton,viewYourLoansButton,viewYourShipsButton,viewNearbyButton);
        marketButtons.getChildren().addAll(viewMarketButton,buyFuelButton,tradeGoodButton);

        VBox bottomVbox = new VBox(10);
        bottomVbox.setAlignment(Pos.CENTER);
        Label status = getLabel(this.status.getStatus(), paneMain);
        bottomVbox.getChildren().addAll(saveYourToken, status);

        paneMain.setStyle("-fx-background-color: #3e455c");
        paneMain.setTop(getLabel("Welcome to SpaceTrader!",paneMain));
        paneMain.setRight(marketButtons);
        paneMain.setLeft(userInfoButtons);
        paneMain.setCenter(viewItemsButtons);
        paneMain.setBottom(bottomVbox);
        this.sceneMain = new Scene(paneMain, 800, 450);
        return this.sceneMain;
    }

    public Scene getSceneMain() {
        return this.sceneMain;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setLoansPage(LoansPage loansPage) {
        this.loansPage = loansPage;
    }

    public void setShipsPage(ShipsPage shipsPage) {
        this.shipsPage = shipsPage;
    }

    public void setMarketPage(MarketPage marketPage) {
        this.marketPage = marketPage;
    }

    public void setLocationsPage(LocationsPage locationsPage) {
        this.locationsPage = locationsPage;
    }

    public void setFlightPage(FlightPage flightPage) {
        this.flightPage = flightPage;
    }

}
