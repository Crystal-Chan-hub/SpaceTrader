package gameEngine.components;

import java.util.List;
import java.util.ArrayList;

public interface Flight {

    public List<String> newFlightPlan(String shipId, String destination);

    public List<String> getCurrentFlight();

}
