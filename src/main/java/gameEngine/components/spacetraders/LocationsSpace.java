package gameEngine.components.spacetraders;

import gameEngine.components.Locations;
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

public class LocationsSpace implements Locations {

    private final User user;
    private final HttpClient client;
    static List<String> nearbyLocations;
    static String errorMessage;

    public LocationsSpace(HttpClient client, User user) {
        this.client = client;
        this.user = user;
    }

    @Override
    public List<String> getNearbyLocations() {
        nearbyLocations = new ArrayList<>();
        String auth = String.format("Bearer %s", this.user.getToken());
        String url = String.format("https://api.spacetraders.io/game/systems/%s/locations", "OE");
        HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .setHeader("Authorization", auth)
                    .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenApply(LocationsSpace::parseNearbyLocations)
            .join();
        return nearbyLocations;
    }

    @SuppressWarnings("unchecked")
    public static String parseNearbyLocations(String responseBody) {
        try {
            JSONParser parse = new JSONParser();
            JSONObject jsonObject = (JSONObject)parse.parse(responseBody);
            JSONObject error = (JSONObject) jsonObject.get("error");
            if (error == null) {
                errorMessage = null;
                JSONArray locations = (JSONArray) jsonObject.get("locations");
                Iterator<JSONObject> iterator = (Iterator<JSONObject>) locations.iterator();
                while (iterator.hasNext()) {
                    JSONObject location = (JSONObject) iterator.next();
                    String symbol = (String) location.get("symbol");
                    String type = (String) location.get("type");
                    String name = (String) location.get("name");
                    long x = (long) location.get("x");
                    long y = (long) location.get("y");
                    nearbyLocations.add("-------------LOCATION------------");
                    nearbyLocations.add(String.format("Symbol : %s",symbol));
                    nearbyLocations.add(String.format("Type : %s",type));
                    nearbyLocations.add(String.format("Name : %s",name));
                    nearbyLocations.add(String.format("x : %d",x));
                    nearbyLocations.add(String.format("y : %d",y));
                    boolean allowsConstruction = (boolean) location.get("allowsConstruction");
                    String allowsConstruct;
                    if (allowsConstruction) {
                        allowsConstruct = "yes";
                    }
                    else {
                        allowsConstruct = "no";
                    }
                    nearbyLocations.add(String.format("Allows construction : %s",allowsConstruct));
                    JSONArray structures = (JSONArray) location.get("structures");
                    if(structures != null && structures.size() > 0 ){
                        nearbyLocations.add("Structures : ");
                        Iterator<JSONObject> structuresIterator = (Iterator<JSONObject>) structures.iterator();
                        while (structuresIterator.hasNext()) {
                            JSONObject structure = (JSONObject) structuresIterator.next();
                            String id = (String) structure.get("id");
                            String structType = (String) structure.get("type");
                            String structlocation = (String) structure.get("location");
                            nearbyLocations.add("\t-------------STRUCTURE-------------");
                            nearbyLocations.add(String.format("\tid : %s",id));
                            nearbyLocations.add(String.format("\tType : %s",structType));
                            nearbyLocations.add(String.format("\tLocation : %s",structlocation));

                            JSONObject ownedBy = (JSONObject) structure.get("ownedBy");
                            if(ownedBy != null){
                                String ownedByUser = (String) ownedBy.get("username");
                                nearbyLocations.add(String.format("\tOwned by : %s",ownedByUser));
                            }

                        }
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

}
