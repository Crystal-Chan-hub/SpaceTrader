package gameEngine.pages;

import gameEngine.AlertBox;
import gameEngine.components.User;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserPage extends Page {

    private final User user;
    private final Stage stage;

    public UserPage(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
    }

    public Scene setSceneSignUp(Scene homeScene, MainPage mainPage) {
        BorderPane paneSignUp = new BorderPane();
        VBox center = new VBox(10);
        center.setSpacing(10.0);
        center.setAlignment(Pos.CENTER);
        TextField usernameField = newTextField();

        Button signUpButton = getButton("Create Account");
        signUpButton.setOnAction(e -> {
            String username = usernameField.getText().strip();
            if (username != "") {
                try {
                    String[] unameToken = user.newUser(username);
                    stage.setScene(mainPage.setSceneMain(this, unameToken));
                }
                catch (IllegalStateException se) {
                    AlertBox.display("Warning", se.getMessage());
                }
            }
            else {
                AlertBox.display("Warning", "Invalid Username");
            }
        });
        center.getChildren().addAll(usernameField,signUpButton);

        paneSignUp.setStyle("-fx-background-color: #3e455c");
        paneSignUp.setBottom(this.backHomeButton(homeScene));
        paneSignUp.setTop(getLabel("Welcome to SpaceTrader! Sign up to start",paneSignUp));
        paneSignUp.setCenter(center);
        return new Scene(paneSignUp, 800, 450);
    }

    public Scene setSceneLogin(Scene homeScene, MainPage mainPage) {

        BorderPane paneLogin = new BorderPane();
        VBox vbox = new VBox(10);
        HBox unameBox = new HBox(10);
        HBox tokenBox = new HBox(10);
        VBox center = new VBox(0);
        center.setSpacing(10.0);
        center.setAlignment(Pos.CENTER_LEFT);
        vbox.setAlignment(Pos.CENTER);
        unameBox.setAlignment(Pos.CENTER);
        tokenBox.setAlignment(Pos.CENTER);

        Label usernameLbl = getStandardLabel("Username");
        Label tokenLbl = getStandardLabel("Token        ");

        TextField usernameField = newTextField();
        TextField tokenField = newTextField();

        unameBox.getChildren().addAll(usernameLbl,usernameField);
        tokenBox.getChildren().addAll(tokenLbl,tokenField);

        Button loginButton = getButton("Login");
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().strip();
            String token = tokenField.getText().strip();
            if (username != "" && token != "") {
                try {
                    String[] unameToken = user.login(username, token);
                    stage.setScene(mainPage.setSceneMain(this, unameToken));
                }
                catch (IllegalStateException se) {
                    AlertBox.display("Warning", se.getMessage());
                }
            }
            else {
                AlertBox.display("Warning", "Invalid Username or Token");
            }
        });
        center.getChildren().addAll(unameBox,tokenBox);
        vbox.getChildren().addAll(center,loginButton);
        paneLogin.setStyle("-fx-background-color: #3e455c");
        paneLogin.setBottom(this.backHomeButton(homeScene));
        paneLogin.setTop(getLabel("Welcome to SpaceTrader! Login to start",paneLogin));
        paneLogin.setCenter(vbox);
        return new Scene(paneLogin, 800, 450);
    }

    public Scene setSceneUserInfo(MainPage mainPage) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(300, 300);
        HBox hbox = new HBox(2);
        VBox content = new VBox(2);
        hbox.setAlignment(Pos.CENTER);
        content.setAlignment(Pos.CENTER_LEFT);
        content.getChildren().addAll(backMainButton(mainPage),
                                    getStandardLabel(" "),
                                    getStandardLabel("User Information"),
                                    getStandardLabel(" "));
        for (String i : user.getUserInfo()){
            content.getChildren().add(getStandardLabel(i));
        }
        content.getChildren().add(backMainButton(mainPage));
        hbox.getChildren().add(content);

        scrollPane.setContent(hbox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background:#3e455c;");
        return new Scene(scrollPane, 800, 450);
    }

    private HBox backHomeButton(Scene homeScene) {
        return backButton(homeScene);
    }

    private HBox backMainButton(MainPage mainPage) {
        return backButton(mainPage.getSceneMain());
    }

    private HBox backButton(Scene scene) {
        HBox bkButton = new HBox();
        bkButton.setSpacing(10.0);
        bkButton.setAlignment(Pos.CENTER);

        Button back = getButton("Back");
        back.setOnAction(e -> stage.setScene(scene));
        bkButton.getChildren().add(back);
        return bkButton;
    }

}
