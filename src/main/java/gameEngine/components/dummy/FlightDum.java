package gameEngine.components.dummy;

import gameEngine.components.User;
import gameEngine.components.Flight;

import java.util.List;
import java.util.ArrayList;

public class FlightDum implements Flight {

    private List<String> flightPlan;

    public FlightDum() {
        flightPlan = new ArrayList<>();
        flightPlan.add("Flight plan");
    }

    @Override
    public List<String> newFlightPlan(String shipId, String destination) {
        return flightPlan;
    }

    @Override
    public List<String> getCurrentFlight() {
        return flightPlan;
    }
}
