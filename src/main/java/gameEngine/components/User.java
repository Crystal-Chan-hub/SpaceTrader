package gameEngine.components;

import java.util.List;

public interface User {

    String[] newUser(String username);

    String[] login(String uname, String token);

    List<String> getUserInfo();

    String getToken();

    String getUsername();

    List<String> getYourShipsIds();
}
