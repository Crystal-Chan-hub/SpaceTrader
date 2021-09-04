package gameEngine.components.dummy;

import gameEngine.components.User;
import gameEngine.components.Locations;

import java.util.List;
import java.util.ArrayList;

public class LocationsDum implements Locations {

    private List<String> nearbyLocations;

    public LocationsDum() {
        nearbyLocations = new ArrayList<>();
        nearbyLocations.add("Nearby Locations");
    }

    @Override
    public List<String> getNearbyLocations() {
        return nearbyLocations;
    }
}
