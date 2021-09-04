package gameEngine.components.spacetraders;

import gameEngine.components.Flight;
import gameEngine.components.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class FlightSpace implements Flight {

    private final User user;
    private final HttpClient client;
    static List<String> flightPlan;
    static String errorMessage;
    static String flightId;

    public FlightSpace(HttpClient client, User user) {
        this.client = client;
        this.user = user;
    }

    @Override
    public List<String> newFlightPlan(String shipId, String destination) {
        flightPlan = new ArrayList<>();
        String auth = String.format("Bearer %s", this.user.getToken());
        String url = String.format("https://api.spacetraders.io/users/%s/flight-plans",this.user.getUsername());
        String json = new StringBuilder()
                .append("{")
                .append(String.format("\"username\":\"%s\",", this.user.getUsername()))
                .append(String.format("\"shipId\":\"%s\",", shipId))
                .append(String.format("\"destination\":\"%s\"", destination))
                .append("}").toString();
        HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .setHeader("Authorization", auth)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenApply(FlightSpace::parseFlightPlan)
            .join();
        if (errorMessage == null) {
            return flightPlan;
        }
        else {
            throw new IllegalStateException(errorMessage);
        }
    }

    @Override
    public List<String> getCurrentFlight() {
        flightPlan = new ArrayList<>();
        String auth = String.format("Bearer %s", this.user.getToken());
        if (flightId != null) {
            String url = String.format("https://api.spacetraders.io/users/%s/flight-plans/%s"
                                        ,this.user.getUsername()
                                        , flightId);
            HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .setHeader("Authorization", auth)
                        .build();
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(FlightSpace::parseFlightPlan)
                .join();
        }
        else {
            flightPlan.add("You have no current flight plan");
        }
        return flightPlan;
    }

    @SuppressWarnings("unchecked")
    public static String parseFlightPlan(String responseBody) {
        try {
            JSONParser parse = new JSONParser();
            JSONObject jsonObject = (JSONObject)parse.parse(responseBody);
            JSONObject error = (JSONObject) jsonObject.get("error");
            if (error == null) {
                errorMessage = null;
                JSONObject plan = (JSONObject) jsonObject.get("flightPlan");
                String arrivesAt = parseTime((String) plan.get("arrivesAt"));
                String createdAt = (String) plan.get("createdAt");
                String departure = (String) plan.get("departure");
                String destination = (String) plan.get("destination");
                long distance = (long) plan.get("distance");
                long fuelConsumed = (long) plan.get("fuelConsumed");
                long fuelRemaining = (long) plan.get("fuelRemaining");
                flightId = (String) plan.get("id");
                String shipId = (String) plan.get("shipId");
                String terminatedAt = (String) plan.get("terminatedAt");
                long timeRemainingInSeconds = (long) plan.get("timeRemainingInSeconds");
                flightPlan.add(String.format("Arrives at : %s\n", arrivesAt));
                if (createdAt != null) {
                    flightPlan.add(String.format("Created at : %s\n", parseTime(createdAt)));
                }
                flightPlan.add(String.format("Departure : %s\n", departure));
                flightPlan.add(String.format("Destination : %s\n", destination));
                flightPlan.add(String.format("Distance : %d\n", distance));
                flightPlan.add(String.format("Fuel consumed : %d\n", fuelConsumed));
                flightPlan.add(String.format("Fuel remaining : %d\n", fuelRemaining));
                flightPlan.add(String.format("Flight id : %s\n", flightId));
                flightPlan.add(String.format("Ship id : %s\n", shipId));
                if (terminatedAt != null) {
                    flightPlan.add(String.format("Username : %s\n", terminatedAt));
                }
                flightPlan.add(String.format("timeRemainingInSeconds : %d\n", timeRemainingInSeconds));
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

    public static String parseTime(String t) {
        List<String> dateList = new ArrayList<>();
        List<String> timeList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dateList.add(Character.toString(t.charAt(i)));
        }
        for (int j = 11; j < 23; j++) {
            timeList.add(Character.toString(t.charAt(j)));
        }
        String date = String.join("", dateList);
        String time = String.join("", timeList);
        return date + " " + time;
    }

}
