package gameEngine.components.dummy;

import gameEngine.components.User;
import gameEngine.components.Loans;
import gameEngine.components.Ships;

import java.util.List;
import java.util.ArrayList;

public class UserDum implements User {

    private Loans loans;
    private Ships ships;
    private String token;
    private int userCredits;
    private String username;
    private List<String> userInfo,yourShipsIds;

    public UserDum(Loans loans,Ships ships) {
        this.loans = loans;
        this.ships = ships;
        this.userCredits = 0;
    }

    @Override
    public String[] newUser(String username) {
        this.token = generateToken();
        this.username = username;
        String[] arr = {this.username, this.token};
        return arr;
    }

    @Override
    public String[] login(String uname, String token) {
        this.username = uname;
        String[] unameToken = {uname, token};
        return unameToken;
    }

    @Override
    public List<String> getUserInfo() {
        userInfo = new ArrayList<>();
        List<String> yourLoans = this.loans.getYourLoans();
        List<String> yourShips = this.ships.getYourShips();
        userInfo.add(String.format("User : %s", username));
        if (yourLoans.size() != 0) {
            userCredits = 200000;
            userInfo.add(String.format("Credits : %d", userCredits));
            for (String s: yourLoans) {
                userInfo.add(s);
            }
        }
        else {
            userCredits = 0;
            userInfo.add(String.format("Credits : %d", userCredits));
            userInfo.add("Loans: You have no loans");
        }
        if (yourShips.size() != 0) {
            for (String s: yourShips) {
                userInfo.add(s);
            }
        }
        else {
            userInfo.add("Ships: You have no ship");
        }
        return userInfo;
    }

    private String generateToken() {
        String string = "0123456789abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(9);

        for (int i = 0; i < 9; i++) { // Append 9 char with random index
            int randomInt = (int)(string.length()* Math.random());
            sb.append(string.charAt(randomInt));
        }
        return sb.toString();
    }

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public List<String> getYourShipsIds() {
        this.yourShipsIds = new ArrayList<>();
        this.yourShipsIds.add("id_of_your_ship");
        return this.yourShipsIds;
    }
}
