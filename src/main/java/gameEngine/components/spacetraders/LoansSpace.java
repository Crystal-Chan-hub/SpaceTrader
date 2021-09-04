package gameEngine.components.spacetraders;

import gameEngine.components.Loans;
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

public class LoansSpace implements Loans {

    private final User user;
    private final HttpClient client;
    static List<String> availableLoans,requestedLoans,yourLoans,yourLoansId,paidLoans;
    static String errorMessage;

    public LoansSpace(HttpClient client, User user) {
        this.client = client;
        this.user = user;
    }

    @Override
    public List<String> getAvailableLoans() {
        availableLoans = new ArrayList<>();
        String auth = String.format("Bearer %s", this.user.getToken());
        HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.spacetraders.io/game/loans"))
                    .setHeader("Authorization", auth)
                    .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenApply(LoansSpace::parseAvailableLoans)
            .join();
        return availableLoans;
    }

    @Override
    public List<String> requestLoans(String loanType) {
        requestedLoans = new ArrayList<>();
        String auth = String.format("Bearer %s", this.user.getToken());
        String url = String.format("https://api.spacetraders.io/users/%s/loans",this.user.getUsername());
        String json = new StringBuilder()
                .append("{")
                .append(String.format("\"username\":\"%s\",", this.user.getUsername()))
                .append(String.format("\"type\":\"%s\"", loanType))
                .append("}").toString();
        HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .setHeader("Authorization", auth)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenApply(LoansSpace::parseRequestLoans)
            .join();
        if (errorMessage == null) {
            return requestedLoans;
        }
        else {
            throw new IllegalStateException(errorMessage);
        }
    }

    @Override
    public List<String> payOffLoans(String loanId) {
        paidLoans = new ArrayList<>();
        String auth = String.format("Bearer %s", this.user.getToken());
        String url = String.format(
                            "https://api.spacetraders.io/users/%s/loans/%s"
                            ,this.user.getUsername(),loanId
                            );
        String json = new StringBuilder()
                .append("{")
                .append(String.format("\"username\":\"%s\",", this.user.getUsername()))
                .append(String.format("\"loanId\":\"%s\"", loanId))
                .append("}").toString();
        HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .setHeader("Authorization", auth)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenApply(LoansSpace::parsePayOffLoans)
            .join();
        if (errorMessage == null) {
            return paidLoans;
        }
        else {
            throw new IllegalStateException(errorMessage);
        }
    }

    @Override
    public List<String> getYourLoans() {
        yourLoans = new ArrayList<>();
        String url = String.format("https://api.spacetraders.io/users/%s/loans",this.user.getUsername());
        String auth = String.format("Bearer %s", this.user.getToken());
        HttpRequest request = HttpRequest.newBuilder()
                    .setHeader("Authorization", auth)
                    .uri(URI.create(url))
                    .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenApply(LoansSpace::parseYourLoans)
            .join();

        if (errorMessage == null) {
            return yourLoans;
        }
        else {
            throw new IllegalStateException(errorMessage);
        }
    }

    @Override
    public List<String> getYourLoansId() {
        yourLoansId = new ArrayList<>();
        String url = String.format("https://api.spacetraders.io/users/%s/loans",this.user.getUsername());
        String auth = String.format("Bearer %s", this.user.getToken());
        HttpRequest request = HttpRequest.newBuilder()
                    .setHeader("Authorization", auth)
                    .uri(URI.create(url))
                    .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenApply(LoansSpace::parseYourLoansId)
            .join();
        if (errorMessage == null) {
            return yourLoansId;
        }
        else {
            throw new IllegalStateException(errorMessage);
        }
    }

    @SuppressWarnings("unchecked")
    public static String parseAvailableLoans(String responseBody) {
        try {
            JSONParser parse = new JSONParser();
            JSONObject jsonObject = (JSONObject)parse.parse(responseBody);
            JSONObject error = (JSONObject) jsonObject.get("error");
            if (error == null) {
                errorMessage = null;
                JSONArray loans = (JSONArray) jsonObject.get("loans");

                Iterator<JSONObject> iterator = (Iterator<JSONObject>) loans.iterator();
                // Get loans
                while (iterator.hasNext()) {
                    JSONObject l = (JSONObject) iterator.next();
                    String cr;
                    long amount = (long) l.get("amount");
                    boolean collateralRequired = (boolean) l.get("collateralRequired");
                    long rate = (long) l.get("rate");
                    long termInDays = (long) l.get("termInDays");
                    String type = (String) l.get("type");
                    if (collateralRequired) {
                        cr = "Yes";
                    }
                    else {
                        cr = "No";
                    }
                    availableLoans.add("------------------LOAN-------------------");
                    availableLoans.add(String.format("Amount : %d",amount));
                    availableLoans.add(String.format("Collateral required : %s",cr));
                    availableLoans.add(String.format("Rate : %d",rate));
                    availableLoans.add(String.format("Term in days : %d",termInDays));
                    availableLoans.add(String.format("Loan type : %s",type));
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
    public static String parseRequestLoans(String responseBody) {
        try {
            JSONParser parse = new JSONParser();
            JSONObject jsonObject = (JSONObject)parse.parse(responseBody);
            JSONObject error = (JSONObject) jsonObject.get("error");
            if (error == null) {
                errorMessage = null;
                long credits = (long) jsonObject.get("credits");
                JSONObject loan = (JSONObject) jsonObject.get("loan");
                String due = parseDue((String) loan.get("due"));
                String id = (String) loan.get("id");
                long repaymentAmount = (long) loan.get("repaymentAmount");
                String status = (String) loan.get("status");
                String type = (String) loan.get("type");
                requestedLoans.add(String.format("Credits : %d",credits));
                requestedLoans.add(String.format("Due : %s",due));
                requestedLoans.add(String.format("Loan id : %s",id));
                requestedLoans.add(String.format("Repayment Amount : %d",repaymentAmount));
                requestedLoans.add(String.format("Loan status : %s",status));
                requestedLoans.add(String.format("Loan type : %s",type));
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
    public static String parseYourLoans(String responseBody) {
        try {
            JSONParser parse = new JSONParser();
            JSONObject jsonObject = (JSONObject)parse.parse(responseBody);
            JSONObject error = (JSONObject) jsonObject.get("error");
            if (error == null) {
                errorMessage = null;
                JSONArray loans = (JSONArray) jsonObject.get("loans");
                if(loans != null && loans.size() > 0 ){
                    Iterator<JSONObject> iterator = (Iterator<JSONObject>) loans.iterator();
                    // Get loans
                    while (iterator.hasNext()) {
                        JSONObject l = (JSONObject) iterator.next();
                        String id = (String) l.get("id");
                        String due = parseDue((String) l.get("due"));
                        long repaymentAmount = (long) l.get("repaymentAmount");
                        String status = (String) l.get("status");
                        String type = (String) l.get("type");
                        yourLoans.add("------------------LOAN-------------------");
                        yourLoans.add(String.format("Loan id : %s",id));
                        yourLoans.add(String.format("Due : %s",due));
                        yourLoans.add(String.format("Repayment Amount : %d",repaymentAmount));
                        yourLoans.add(String.format("Loan status : %s",status));
                        yourLoans.add(String.format("Loan type : %s",type));
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
    public static String parseYourLoansId(String responseBody) {
        try {
            JSONParser parse = new JSONParser();
            JSONObject jsonObject = (JSONObject)parse.parse(responseBody);
            JSONObject error = (JSONObject) jsonObject.get("error");
            if (error == null) {
                errorMessage = null;
                JSONArray loans = (JSONArray) jsonObject.get("loans");
                if(loans != null && loans.size() > 0 ){
                    Iterator<JSONObject> loansIterator = (Iterator<JSONObject>) loans.iterator();
                    while (loansIterator.hasNext()) {
                        JSONObject l = (JSONObject) loansIterator.next();
                        String id = (String) l.get("id");
                        yourLoansId.add(id);
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

    @SuppressWarnings("unchecked")
    public static String parsePayOffLoans(String responseBody) {
        try {
            JSONParser parse = new JSONParser();
            JSONObject jsonObject = (JSONObject)parse.parse(responseBody);
            JSONObject error = (JSONObject) jsonObject.get("error");
            if (error == null) {
                errorMessage = null;
                JSONObject user = (JSONObject) jsonObject.get("user");
                String username = (String) user.get("username");
                long userCredits = (long) user.get("credits");
                paidLoans.add(String.format("Username : %s\n", username));
                paidLoans.add(String.format("Credits : %d\n", userCredits));
                JSONArray ships = (JSONArray) user.get("ships");
                if(ships != null && ships.size() > 0 ){
                    Iterator<JSONObject> shipsIterator = (Iterator<JSONObject>) ships.iterator();
                    paidLoans.add("Ships:");
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
                        paidLoans.add("\t------------------SHIP-------------------");
                        paidLoans.add(String.format("\tShip id : %s",id));
                        if (location != null) {
                            paidLoans.add(String.format("\tLocation : %s",location));
                        }
                        if (flightPlanId != null) {
                            paidLoans.add(String.format("\tFlight Plan id : %s",flightPlanId));
                        }
                        if (ship.get("x") != null || ship.get("y") != null) {
                            long x = (long) ship.get("x");
                            long y = (long) ship.get("y");
                            paidLoans.add(String.format("\tx : %d",x));
                            paidLoans.add(String.format("\ty : %d",y));
                        }
                        paidLoans.add("\tCargo : ");
                        JSONArray cargo = (JSONArray) ship.get("cargo");
                        Iterator<JSONObject> cargoIterator = (Iterator<JSONObject>) cargo.iterator();
                        // Get cargo
                        while (cargoIterator.hasNext()) {
                            JSONObject c = (JSONObject) cargoIterator.next();
                            String good = (String) c.get("good");
                            long quantity = (long) c.get("quantity");
                            long totalVolume = (long) c.get("totalVolume");
                            paidLoans.add(String.format("\t\tGood : %s",good));
                            paidLoans.add(String.format("\t\tQuantity : %s",quantity));
                            paidLoans.add(String.format("\t\tTotal volume : %d",totalVolume));
                            paidLoans.add("\t-----------------------");
                        }
                        paidLoans.add(String.format("\tClass : %s",shipClass));
                        paidLoans.add(String.format("\tManufacturer : %s",manufacturer));
                        paidLoans.add(String.format("\tMaxCargo : %d",maxCargo));
                        paidLoans.add(String.format("\tPlating : %d",plating));
                        paidLoans.add(String.format("\tSpace Available : %d",spaceAvailable));
                        paidLoans.add(String.format("\tSpeed : %d",speed));
                        paidLoans.add(String.format("\tShip type : %s",type));
                        paidLoans.add(String.format("\tWeapons : %d",weapons));
                    }
                }
                else {
                    paidLoans.add("Ships: You have no ship");
                }

                JSONArray loans = (JSONArray) user.get("loans");
                if(loans != null && loans.size() > 0 ){
                    Iterator<JSONObject> loansIterator = (Iterator<JSONObject>) loans.iterator();
                    paidLoans.add("Loans:");
                    // Get loans
                    while (loansIterator.hasNext()) {
                        JSONObject loan = (JSONObject) loansIterator.next();
                        String id = (String) loan.get("id");
                        String due = parseDue((String) loan.get("due"));
                        long repaymentAmount = (long) loan.get("repaymentAmount");
                        String status = (String) loan.get("status");
                        String type = (String) loan.get("type");
                        paidLoans.add("\t------------------LOAN-------------------");
                        paidLoans.add(String.format("\tLoan id : %s",id));
                        paidLoans.add(String.format("\tDue : %s",due));
                        paidLoans.add(String.format("\tRepayment Amount : %d",repaymentAmount));
                        paidLoans.add(String.format("\tLoan status : %s",status));
                        paidLoans.add(String.format("\tLoan type : %s",type));
                    }
                }
                else {
                    paidLoans.add("Loans: You have no loans");
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

    public static String parseDue(String due) {
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

}
