package gameEngine.components.spacetraders;

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

public class UserSpace implements User {

    static List<String> userInfo,yourShipsIds;
    static String errorMessage;
    private final HttpClient client;
    static String username;
    static String token;


    public UserSpace(HttpClient client) {
        this.client = client;
        userInfo = new ArrayList<>();
    }

    @Override
    public String[] newUser(String username) {
        String url = String.format("https://api.spacetraders.io/users/%s/token",username);
        HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .headers("Content-Type", "text/plain;charset=UTF-8")
                            .POST(HttpRequest.BodyPublishers.ofString(username))
                            .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenApply(UserSpace::parseNewUser)
            .join();
        if (errorMessage == null) {
            String[] arr = {this.username, token};
            return arr;
        }
        else {
            throw new IllegalStateException(errorMessage);
        }
    }

    @Override
    public String[] login(String uname, String token) {
        String url = String.format("https://api.spacetraders.io/users/%s",uname);
        this.token = token;
        String auth = String.format("Bearer %s", token);
        HttpRequest request = HttpRequest.newBuilder()
                    .setHeader("Authorization", auth)
                    .uri(URI.create(url))
                    .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenApply(UserSpace::parseUserInfo)
            .join();

        if (errorMessage == null) {
            return new String[]{this.username, this.token};
        }
        else {
            throw new IllegalStateException(errorMessage);
        }
    }

    @Override
    public List<String> getUserInfo() {
        userInfo = new ArrayList<>();
        String url = String.format("https://api.spacetraders.io/users/%s",this.username);
        String auth = String.format("Bearer %s", token);
        HttpRequest request = HttpRequest.newBuilder()
                    .setHeader("Authorization", auth)
                    .uri(URI.create(url))
                    .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenApply(UserSpace::parseUserInfo)
            .join();

        if (errorMessage == null) {
            return userInfo;
        }
        else {
            throw new IllegalStateException(errorMessage);
        }
    }

    @Override
    public List<String> getYourShipsIds() {
        yourShipsIds = new ArrayList<>();
        String url = String.format("https://api.spacetraders.io/users/%s", username);
        String auth = String.format("Bearer %s", token);
        HttpRequest request = HttpRequest.newBuilder()
                    .setHeader("Authorization", auth)
                    .uri(URI.create(url))
                    .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenApply(UserSpace::parseYourShipsIds)
            .join();
        if (errorMessage == null) {
            return yourShipsIds;
        }
        else {
            throw new IllegalStateException(errorMessage);
        }
    }

    @SuppressWarnings("unchecked")
    public static String parseNewUser(String responseBody) {
        try {
            JSONParser parse = new JSONParser();
            JSONObject jsonObject = (JSONObject)parse.parse(responseBody);
            JSONObject error = (JSONObject) jsonObject.get("error");
            if (error == null) {
                errorMessage = null;
                token = (String) jsonObject.get("token");
                JSONObject userDetails = (JSONObject) jsonObject.get("user");
                username = (String) userDetails.get("username");
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
    public static String parseUserInfo(String responseBody) {
        try {
            JSONParser parse = new JSONParser();
            JSONObject jsonObject = (JSONObject)parse.parse(responseBody);
            JSONObject error = (JSONObject) jsonObject.get("error");
            if (error == null) {
                errorMessage = null;
                JSONObject user = (JSONObject) jsonObject.get("user");
                username = (String) user.get("username");
                long userCredits = (long) user.get("credits");
                userInfo.add(String.format("Username : %s\n", username));
                userInfo.add(String.format("Credits : %d\n", userCredits));
                JSONArray ships = (JSONArray) user.get("ships");
                if(ships != null && ships.size() > 0 ){
                    Iterator<JSONObject> shipsIterator = (Iterator<JSONObject>) ships.iterator();
                    userInfo.add("Ships:");
                    while (shipsIterator.hasNext()) {
                        JSONObject ship = (JSONObject) shipsIterator.next();
                        String shipClass = (String) ship.get("class");
                        String id = (String) ship.get("id");
                        String location = (String) ship.get("location");
                        String flightPlanId = (String) ship.get("flightPlanId");
                        String manufacturer = (String) ship.get("manufacturer");
                        long maxCargo = (long) ship.get("maxCargo");
                        long plating = (long) ship.get("plating");
                        long spaceAvailable = (long) ship.get("spaceAvailable");
                        long speed = (long) ship.get("speed");
                        String type = (String) ship.get("type");
                        long weapons = (long) ship.get("weapons");
                        userInfo.add("\t------------------SHIP-------------------");
                        userInfo.add(String.format("\tShip id : %s",id));
                        if (location != null) {
                            userInfo.add(String.format("\tLocation : %s",location));
                        }
                        if (flightPlanId != null) {
                            userInfo.add(String.format("\tFlight Plan id : %s",flightPlanId));
                        }
                        if (ship.get("x") != null || ship.get("y") != null) {
                            long x = (long) ship.get("x");
                            long y = (long) ship.get("y");
                            userInfo.add(String.format("\tx : %d",x));
                            userInfo.add(String.format("\ty : %d",y));
                        }
                        userInfo.add("\tCargo : ");
                        JSONArray cargo = (JSONArray) ship.get("cargo");
                        Iterator<JSONObject> cargoIterator = (Iterator<JSONObject>) cargo.iterator();
                        // Get cargo
                        while (cargoIterator.hasNext()) {
                            JSONObject c = (JSONObject) cargoIterator.next();
                            String good = (String) c.get("good");
                            long quantity = (long) c.get("quantity");
                            long totalVolume = (long) c.get("totalVolume");
                            userInfo.add(String.format("\t\tGood : %s",good));
                            userInfo.add(String.format("\t\tQuantity : %s",quantity));
                            userInfo.add(String.format("\t\tTotal volume : %d",totalVolume));
                            userInfo.add("\t-----------------------");
                        }
                        userInfo.add(String.format("\tClass : %s",shipClass));
                        userInfo.add(String.format("\tManufacturer : %s",manufacturer));
                        userInfo.add(String.format("\tMaxCargo : %d",maxCargo));
                        userInfo.add(String.format("\tPlating : %d",plating));
                        userInfo.add(String.format("\tSpace Available : %d",spaceAvailable));
                        userInfo.add(String.format("\tSpeed : %d",speed));
                        userInfo.add(String.format("\tShip type : %s",type));
                        userInfo.add(String.format("\tWeapons : %d",weapons));
                    }
                }
                else {
                    userInfo.add("Ships: You have no ship");
                }

                JSONArray loans = (JSONArray) user.get("loans");
                if(loans != null && loans.size() > 0 ){
                    Iterator<JSONObject> loansIterator = (Iterator<JSONObject>) loans.iterator();
                    userInfo.add("Loans:");
                    // Get loans
                    while (loansIterator.hasNext()) {
                        JSONObject loan = (JSONObject) loansIterator.next();
                        String id = (String) loan.get("id");
                        String due = parseDue((String) loan.get("due"));
                        long repaymentAmount = (long) loan.get("repaymentAmount");
                        String status = (String) loan.get("status");
                        String type = (String) loan.get("type");
                        userInfo.add("\t------------------LOAN-------------------");
                        userInfo.add(String.format("\tLoan id : %s",id));
                        userInfo.add(String.format("\tDue : %s",due));
                        userInfo.add(String.format("\tRepayment Amount : %d",repaymentAmount));
                        userInfo.add(String.format("\tLoan status : %s",status));
                        userInfo.add(String.format("\tLoan type : %s",type));
                    }
                }
                else {
                    userInfo.add("Loans: You have no loans");
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
    public static String parseYourShipsIds(String responseBody) {
        try {
            JSONParser parse = new JSONParser();
            JSONObject jsonObject = (JSONObject)parse.parse(responseBody);
            JSONObject error = (JSONObject) jsonObject.get("error");
            JSONObject user = (JSONObject) jsonObject.get("user");
            if (error == null) {
                errorMessage = null;
                JSONArray ships = (JSONArray) user.get("ships");
                if(ships != null && ships.size() > 0 ){
                    Iterator<JSONObject> shipsIterator = (Iterator<JSONObject>) ships.iterator();
                    while (shipsIterator.hasNext()) {
                        JSONObject ship = (JSONObject) shipsIterator.next();
                        String id = (String) ship.get("id");
                        yourShipsIds.add(id);
                    }
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

    public static String parseDue(String due) {
        // "2021-04-21T07:01:42.868Z"
        // "012345678901234567890123"
        List<String> dateList = new ArrayList<>();
        List<String> timeList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dateList.add(Character.toString(due.charAt(i)));
        }
        for (int j = 11; j < 23; j++) {
            timeList.add(Character.toString(due.charAt(j)));
        }
        String date = String.join("", dateList);
        String time = String.join("", timeList);
        return date + " " + time;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
