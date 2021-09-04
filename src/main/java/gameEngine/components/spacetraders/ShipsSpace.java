package gameEngine.components.spacetraders;

import gameEngine.components.Ships;
import gameEngine.components.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShipsSpace implements Ships {

    private final User user;
    private final HttpClient client;
    static List<String> availableShips, purchasedShip, yourShips;
    static String errorMessage;

    public ShipsSpace(HttpClient client, User user) {
        this.client = client;
        this.user = user;
    }

    @Override
    public List<String> getAvailableShips() {
        availableShips = new ArrayList<>();
        String auth = String.format("Bearer %s", this.user.getToken());
        HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.spacetraders.io/game/ships"))
                    .setHeader("Authorization", auth)
                    .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenApply(ShipsSpace::parseAvailableShips)
            .join();
        return availableShips;
    }

    @Override
    public List<String> buyShip(String location, String type) {
        purchasedShip = new ArrayList<>();
        String auth = String.format("Bearer %s", this.user.getToken());
        String url = String.format("https://api.spacetraders.io/users/%s/ships",this.user.getUsername());
        String json = new StringBuilder()
                .append("{")
                .append(String.format("\"username\":\"%s\",", this.user.getUsername()))
                .append(String.format("\"location\":\"%s\",", location))
                .append(String.format("\"type\":\"%s\"", type))
                .append("}").toString();
        HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .setHeader("Authorization", auth)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenApply(ShipsSpace::parseBuyShip)
            .join();
        if (errorMessage == null) {
            return purchasedShip;
        }
        else {
            throw new IllegalStateException(errorMessage);
        }
    }

    @Override
    public List<String> getYourShips() {
        yourShips = new ArrayList<>();
        String url = String.format("https://api.spacetraders.io/users/%s/ships",this.user.getUsername());
        String auth = String.format("Bearer %s", this.user.getToken());
        HttpRequest request = HttpRequest.newBuilder()
                    .setHeader("Authorization", auth)
                    .uri(URI.create(url))
                    .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenApply(ShipsSpace::parseYourShips)
            .join();

        if (errorMessage == null) {
            return yourShips;
        }
        else {
            throw new IllegalStateException(errorMessage);
        }
    }

    @SuppressWarnings("unchecked")
    public static String parseAvailableShips(String responseBody) {
        try {
            JSONParser parse = new JSONParser();
            JSONObject jsonObject = (JSONObject)parse.parse(responseBody);
            JSONObject error = (JSONObject) jsonObject.get("error");
            if (error == null) {
                errorMessage = null;
                JSONArray ships = (JSONArray) jsonObject.get("ships");

                Iterator<JSONObject> shipsIterator = (Iterator<JSONObject>) ships.iterator();
                // Get ships
                while (shipsIterator.hasNext()) {
                    JSONObject s = (JSONObject) shipsIterator.next();
                    String type = (String) s.get("type");
                    String shipClass = (String) s.get("class");
                    long maxCargo = (long) s.get("maxCargo");
                    long speed = (long) s.get("speed");
                    String manufacturer = (String) s.get("manufacturer");
                    long plating = (long) s.get("plating");
                    long weapons = (long) s.get("weapons");
                    availableShips.add("------------------SHIP-------------------");
                    availableShips.add(String.format("Type : %s",type));
                    availableShips.add(String.format("Class : %s",shipClass));
                    availableShips.add(String.format("Max Cargo : %d",maxCargo));
                    availableShips.add(String.format("Speed : %d",speed));
                    availableShips.add(String.format("Manufacturer : %s",manufacturer));
                    availableShips.add(String.format("Plating : %d",plating));
                    availableShips.add(String.format("Weapons : %d",weapons));
                    availableShips.add("Purchase Locations :");
                    JSONArray purchaseLocations = (JSONArray) s.get("purchaseLocations");
                    Iterator<JSONObject> locationsIterator = (Iterator<JSONObject>) purchaseLocations.iterator();
                    // Get purchaseLocations
                    while (locationsIterator.hasNext()) {
                        JSONObject l = (JSONObject) locationsIterator.next();
                        String system = (String) l.get("system");
                        String location = (String) l.get("location");
                        long price = (long) l.get("price");
                        availableShips.add(String.format("\tSystem : %s",system));
                        availableShips.add(String.format("\tLocation : %s",location));
                        availableShips.add(String.format("\tPrice : %d",price));
                        availableShips.add("\t-----------------------");
                    }
                }
            }
            else {
                errorMessage = (String) error.get("message");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static String parseBuyShip(String responseBody) {
        try {
            JSONParser parse = new JSONParser();
            JSONObject jsonObject = (JSONObject)parse.parse(responseBody);
            JSONObject error = (JSONObject) jsonObject.get("error");
            if (error == null) {
                errorMessage = null;
                long credits = (long) jsonObject.get("credits");
                JSONObject ship = (JSONObject) jsonObject.get("ship");
                JSONArray cargo = (JSONArray) ship.get("cargo");
                purchasedShip.add(String.format("Remains Credits : %d",credits));
                purchasedShip.add("Cargo : ");
                Iterator<JSONObject> cargoIterator = (Iterator<JSONObject>) cargo.iterator();
                while (cargoIterator.hasNext()) {
                    JSONObject c = (JSONObject) cargoIterator.next();
                    String good = (String) c.get("good");
                    long quantity = (long) c.get("quantity");
                    long totalVolume = (long) c.get("totalVolume");
                    purchasedShip.add(String.format("\tGood : %s",good));
                    purchasedShip.add(String.format("\tQuantity : %s",quantity));
                    purchasedShip.add(String.format("\tTotal volume : %d",totalVolume));
                    purchasedShip.add("\t-----------------------");
                }
                String shipClass = (String) ship.get("class");
                String id = (String) ship.get("id");
                String location = (String) ship.get("location");
                String manufacturer = (String) ship.get("manufacturer");
                long maxCargo = (long) ship.get("maxCargo");
                long plating = (long) ship.get("plating");
                long spaceAvailable = (long) ship.get("spaceAvailable");
                long speed = (long) ship.get("speed");
                String type = (String) ship.get("type");
                long weapons = (long) ship.get("weapons");
                long x = (long) ship.get("x");
                long y = (long) ship.get("y");

                purchasedShip.add(String.format("Class : %s",shipClass));
                purchasedShip.add(String.format("Ship id : %s",id));
                purchasedShip.add(String.format("Location : %s",location));
                purchasedShip.add(String.format("Manufacturer : %s",manufacturer));
                purchasedShip.add(String.format("MaxCargo : %d",maxCargo));
                purchasedShip.add(String.format("Plating : %d",plating));
                purchasedShip.add(String.format("Space Available : %d",spaceAvailable));
                purchasedShip.add(String.format("Speed : %d",speed));
                purchasedShip.add(String.format("Ship type : %s",type));
                purchasedShip.add(String.format("Weapons : %d",weapons));
                purchasedShip.add(String.format("x : %d",x));
                purchasedShip.add(String.format("y : %d",y));
            }
            else {
                String err = (String) error.get("message");
                JSONObject data = (JSONObject) error.get("data");
                if (data != null) {
                    JSONArray typeError = (JSONArray) data.get("type");
                    errorMessage =(String) typeError.get(0);
                }
                else {
                    errorMessage = err;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static String parseYourShips(String responseBody) {
        try {
            JSONParser parse = new JSONParser();
            JSONObject jsonObject = (JSONObject)parse.parse(responseBody);
            JSONObject error = (JSONObject) jsonObject.get("error");
            if (error == null) {
                errorMessage = null;
                JSONArray ships = (JSONArray) jsonObject.get("ships");
                Iterator<JSONObject> shipsIterator = (Iterator<JSONObject>) ships.iterator();
                // Get ships
                while (shipsIterator.hasNext()) {
                    JSONObject ship = (JSONObject) shipsIterator.next();
                    String shipClass = (String) ship.get("class");
                    String id = (String) ship.get("id");
                    String manufacturer = (String) ship.get("manufacturer");
                    long maxCargo = (long) ship.get("maxCargo");
                    long plating = (long) ship.get("plating");
                    long spaceAvailable = (long) ship.get("spaceAvailable");
                    long speed = (long) ship.get("speed");
                    String type = (String) ship.get("type");
                    long weapons = (long) ship.get("weapons");
                    String location = (String) ship.get("location");
                    String flightPlanId = (String) ship.get("flightPlanId");
                    yourShips.add("------------------SHIP-------------------");
                    yourShips.add(String.format("Ship id : %s",id));
                    if (location != null) {
                        yourShips.add(String.format("Location : %s",location));
                    }
                    if (flightPlanId != null) {
                        yourShips.add(String.format("Flight Plan id : %s",flightPlanId));
                    }
                    if (ship.get("x") != null || ship.get("y") != null) {
                        long x = (long) ship.get("x");
                        long y = (long) ship.get("y");
                        yourShips.add(String.format("x : %d",x));
                        yourShips.add(String.format("y : %d",y));
                    }
                    yourShips.add("Cargo : ");
                    JSONArray cargo = (JSONArray) ship.get("cargo");
                    Iterator<JSONObject> cargoIterator = (Iterator<JSONObject>) cargo.iterator();
                    // Get cargo
                    while (cargoIterator.hasNext()) {
                        JSONObject c = (JSONObject) cargoIterator.next();
                        String good = (String) c.get("good");
                        long quantity = (long) c.get("quantity");
                        long totalVolume = (long) c.get("totalVolume");
                        yourShips.add(String.format("\tGood : %s",good));
                        yourShips.add(String.format("\tQuantity : %s",quantity));
                        yourShips.add(String.format("\tTotal volume : %d",totalVolume));
                        yourShips.add("\t-----------------------");
                    }
                    yourShips.add(String.format("Class : %s",shipClass));
                    yourShips.add(String.format("Manufacturer : %s",manufacturer));
                    yourShips.add(String.format("MaxCargo : %d",maxCargo));
                    yourShips.add(String.format("Plating : %d",plating));
                    yourShips.add(String.format("Space Available : %d",spaceAvailable));
                    yourShips.add(String.format("Speed : %d",speed));
                    yourShips.add(String.format("Ship type : %s",type));
                    yourShips.add(String.format("Weapons : %d",weapons));
                }
            }
            else {
                String err = (String) error.get("message");
                JSONObject data = (JSONObject) error.get("data");
                if (data != null) {
                    JSONArray typeError = (JSONArray) data.get("type");
                    errorMessage =(String) typeError.get(0);
                }
                else {
                    errorMessage = err;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
