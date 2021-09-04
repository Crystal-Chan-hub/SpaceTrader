package gameEngine.components.dummy;

import gameEngine.components.Ships;

import java.util.List;
import java.util.ArrayList;

public class ShipsDum implements Ships {

    private List<String> availableShips,purchasedShip,yourShips;

    public ShipsDum() {
        this.yourShips = new ArrayList<>();
    }

    @Override
    public List<String> getAvailableShips() {
        this.availableShips = new ArrayList<>();
        availableShips.add("-----------SHIP-----------");
        availableShips.add("Type : EM-MK-III");
        availableShips.add("Class : MK-III");
        availableShips.add("Max Cargo : 100");
        availableShips.add("Speed : 3");
        availableShips.add("Manufacturer : Electrum");
        availableShips.add("Plating : 10");
        availableShips.add("Weapons : 15");
        availableShips.add("Purchase Locations :");
        availableShips.add("\tSystem : OE");
        availableShips.add("\tLocation : OE-A1");
        availableShips.add("\tPrice : 40000");
        availableShips.add("\t-----------------------");
        return availableShips;
    }

    @Override
    public List<String> buyShip(String location, String type) {
        this.buy(location, type);
        this.purchasedShip = new ArrayList<>();
        purchasedShip.add("Remains credits : 178875");
        purchasedShip.add("cargo : []");
        purchasedShip.add("class : MK-I");
        purchasedShip.add("id : ckno9324k0079iiop71j5nrob");
        purchasedShip.add(String.format("location : %s",location));
        purchasedShip.add("manufacturer : Jackshaw");
        purchasedShip.add("maxCargo : 50");
        purchasedShip.add("plating : 5");
        purchasedShip.add("spaceAvailable : 50");
        purchasedShip.add("speed : 1");
        purchasedShip.add(String.format("type : %s", type));
        purchasedShip.add("weapons : 5");
        purchasedShip.add("x : 23");
        purchasedShip.add("y : -2");
        return purchasedShip;
    }

    @Override
    public List<String> getYourShips() {
        return this.yourShips;
    }

    private void buy(String location, String type) {
        yourShips.add("----------------SHIP-----------------");
        yourShips.add("id : ckno9324k0079iiop71j5nrob");
        yourShips.add(String.format("location : %s",location));
        yourShips.add("x : 23");
        yourShips.add("y : -2");
        yourShips.add("cargo : []");
        yourShips.add("class : MK-I");
        yourShips.add("manufacturer : Jackshaw");
        yourShips.add("maxCargo : 50");
        yourShips.add("plating : 5");
        yourShips.add("spaceAvailable : 50");
        yourShips.add("speed : 1");
        yourShips.add(String.format("type : %s", type));
        yourShips.add("weapons : 5");
    }

}
