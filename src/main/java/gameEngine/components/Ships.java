package gameEngine.components;

import java.util.List;

public interface Ships {

    List<String> getAvailableShips();

    List<String> buyShip(String location, String type);

    List<String> getYourShips();

}
