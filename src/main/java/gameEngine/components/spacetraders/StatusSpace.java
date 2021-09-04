package gameEngine.components.spacetraders;

import gameEngine.components.Status;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class StatusSpace implements Status {

    private final HttpClient client;
    static String status;

    public StatusSpace(HttpClient client) {
        this.client = client;
    }

    @Override
    public String getStatus() {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.spacetraders.io/game/status")).build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenApply(StatusSpace::parseStatus)
            .join();
        return status;
    }

    public static String parseStatus(String responseBody) {
        try {
            JSONParser parse = new JSONParser();
            JSONObject jsonObject = (JSONObject)parse.parse(responseBody);
            status = (String) jsonObject.get("status");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
