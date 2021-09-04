package gameEngine.components.spacetraders;

import gameEngine.components.Market;
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

public class MarketSpace implements Market {

    private final User user;
    private final HttpClient client;
    static List<String> tradedGood,marketInfo,soldGood;
    static String errorMessage;

    public MarketSpace(HttpClient client, User user) {
        this.client = client;
        this.user = user;
    }

    @Override
    public List<String> tradeGood(String shipId, String good, int quantity, String tradeAction) {
        tradedGood = new ArrayList<>();
        String auth = String.format("Bearer %s", this.user.getToken());
        String url;
        if (tradeAction.equals("sell")) {
            url = String.format("https://api.spacetraders.io/users/%s/sell-orders",this.user.getUsername());
        }
        else {
            url = String.format("https://api.spacetraders.io/users/%s/purchase-orders",this.user.getUsername());
        }
        String json = new StringBuilder()
                .append("{")
                .append(String.format("\"username\":\"%s\",", this.user.getUsername()))
                .append(String.format("\"shipId\":\"%s\",", shipId))
                .append(String.format("\"good\":\"%s\",", good))
                .append(String.format("\"quantity\":\"%s\"", quantity))
                .append("}").toString();
        HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .setHeader("Authorization", auth)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenApply(MarketSpace::parseTrade)
            .join();
        if (errorMessage == null) {
            return tradedGood;
        }
        else {
            throw new IllegalStateException(errorMessage);
        }
    }

    @Override
    public List<String> getMarketplaceInfo(String location) {
        marketInfo = new ArrayList<>();
        String url = String.format("https://api.spacetraders.io/game/locations/%s/marketplace",location);
        String auth = String.format("Bearer %s", this.user.getToken());
        HttpRequest request = HttpRequest.newBuilder()
                    .setHeader("Authorization", auth)
                    .uri(URI.create(url))
                    .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenApply(MarketSpace::parseMarketInfo)
            .join();

        if (errorMessage == null) {
            return marketInfo;
        }
        else {
            throw new IllegalStateException(errorMessage);
        }
    }

    @SuppressWarnings("unchecked")
    public static String parseTrade(String responseBody) {
        try {
            JSONParser parse = new JSONParser();
            JSONObject jsonObject = (JSONObject)parse.parse(responseBody);
            JSONObject error = (JSONObject) jsonObject.get("error");
            if (error == null) {
                errorMessage = null;
                long credits = (long) jsonObject.get("credits");
                tradedGood.add(String.format("Remains Credits : %d",credits));

                tradedGood.add("Order :");
                JSONObject order = (JSONObject) jsonObject.get("order");
                String goodOrdered = (String) order.get("good");
                long pricePerUnit = (long) order.get("pricePerUnit");
                long quantityOrdered = (long) order.get("quantity");
                long total = (long) order.get("total");
                tradedGood.add(String.format("\tGood : %s",goodOrdered));
                tradedGood.add(String.format("\tPrice per unit : %d",pricePerUnit));
                tradedGood.add(String.format("\tQuantity : %d",quantityOrdered));
                tradedGood.add(String.format("\tTotal : %d",total));

                JSONObject ship = (JSONObject) jsonObject.get("ship");
                JSONArray cargo = (JSONArray) ship.get("cargo");
                tradedGood.add("Ship :");
                tradedGood.add("\tCargo :");
                Iterator<JSONObject> cargoIterator = (Iterator<JSONObject>) cargo.iterator();
                while (cargoIterator.hasNext()) {
                    JSONObject c = (JSONObject) cargoIterator.next();
                    String goodOnCargo = (String) c.get("good");
                    long quantityOnCargo = (long) c.get("quantity");
                    long totalVolume = (long) c.get("totalVolume");
                    tradedGood.add(String.format("\t\tGood : %s",goodOnCargo));
                    tradedGood.add(String.format("\t\tQuantity : %s",quantityOnCargo));
                    tradedGood.add(String.format("\t\tTotal volume : %d",totalVolume));
                    tradedGood.add("\t\t-----------------------");
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
                tradedGood.add(String.format("\tClass : %s",shipClass));
                tradedGood.add(String.format("\tShip id : %s",id));
                tradedGood.add(String.format("\tLocation : %s",location));
                tradedGood.add(String.format("\tManufacturer : %s",manufacturer));
                tradedGood.add(String.format("\tMaxCargo : %d",maxCargo));
                tradedGood.add(String.format("\tPlating : %d",plating));
                tradedGood.add(String.format("\tSpace Available : %d",spaceAvailable));
                tradedGood.add(String.format("\tSpeed : %d",speed));
                tradedGood.add(String.format("\tShip type : %s",type));
                tradedGood.add(String.format("\tWeapons : %d",weapons));
                tradedGood.add(String.format("\tx : %d",x));
                tradedGood.add(String.format("\ty : %d",y));
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
    public static String parseMarketInfo(String responseBody) {
        try {
            JSONParser parse = new JSONParser();
            JSONObject jsonObject = (JSONObject)parse.parse(responseBody);
            JSONObject error = (JSONObject) jsonObject.get("error");
            if (error == null) {
                errorMessage = null;
                JSONObject location = (JSONObject) jsonObject.get("location");

                JSONArray marketplace = (JSONArray) location.get("marketplace");
                marketInfo.add("Marketplace :");
                Iterator<JSONObject> marketplaceIterator = (Iterator<JSONObject>) marketplace.iterator();
                while (marketplaceIterator.hasNext()) {
                    JSONObject m = (JSONObject) marketplaceIterator.next();
                    long quantityAvailable = (long) m.get("quantityAvailable");
                    long volumePerUnit = (long) m.get("volumePerUnit");
                    long pricePerUnit = (long) m.get("pricePerUnit");
                    String mkSymbol = (String) m.get("symbol");
                    marketInfo.add("\t-----------------------");
                    marketInfo.add(String.format("\tQuantity available : %d",quantityAvailable));
                    marketInfo.add(String.format("\tVolume per unit : %d",volumePerUnit));
                    marketInfo.add(String.format("\tPrice per unit : %d",pricePerUnit));
                    marketInfo.add(String.format("\tSymbol : %s",mkSymbol));
                }
                String name = (String) location.get("name");
                String locationSymbol = (String) location.get("symbol");
                String type = (String) location.get("type");
                long x = (long) location.get("x");
                long y = (long) location.get("y");
                marketInfo.add(String.format("\tName : %s",name));
                marketInfo.add(String.format("\tLocation symbol : %s",locationSymbol));
                marketInfo.add(String.format("\tType : %s",type));
                marketInfo.add(String.format("\tx : %d",x));
                marketInfo.add(String.format("\ty : %d",y));
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
