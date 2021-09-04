package gameEngine;

import gameEngine.pages.*;
import gameEngine.components.*;
import gameEngine.components.dummy.*;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class DummyEngine implements GameEngine {

    private final Status status;
    private final UserPage userPage;
    private final MainPage mainPage;

    public DummyEngine(Stage stage) {
        this.status = new StatusDum();
        Loans loans = new LoansDum();
        Ships ships = new ShipsDum();
        Flight flight = new FlightDum();
        Market market = new MarketDum();
        Locations locations = new LocationsDum();
        User user = new UserDum(loans, ships);

        LocationsPage locationsPage = new LocationsPage(stage, locations);
        MarketPage marketPage = new MarketPage(stage, market, user);
        FlightPage flightPage = new FlightPage(stage, flight, user);
        ShipsPage shipsPage = new ShipsPage(stage, ships);
        LoansPage loansPage = new LoansPage(stage, loans);
        this.userPage = new UserPage(stage, user);
        this.mainPage = new MainPage(stage);

        this.mainPage.setStatus(this.status);
        this.mainPage.setLoansPage(loansPage);
        this.mainPage.setShipsPage(shipsPage);
        this.mainPage.setFlightPage(flightPage);
        this.mainPage.setMarketPage(marketPage);
        this.mainPage.setLocationsPage(locationsPage);
    }

    @Override
    public String getStatus() {
        return this.status.getStatus();
    }

    @Override
    public Scene setSceneSignUp(Scene homeScene) {
        return this.userPage.setSceneSignUp(homeScene, this.mainPage);
    }

    @Override
    public Scene setSceneLogin(Scene homeScene) {
        return this.userPage.setSceneLogin(homeScene, this.mainPage);
    }
}
