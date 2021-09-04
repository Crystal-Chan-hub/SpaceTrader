import gameEngine.DummyEngine;
import gameEngine.GameEngine;
import gameEngine.SpaceTraderEngine;
import gameEngine.pages.Page;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GameManager extends Page {

    private final GameEngine engine;
    private final BorderPane pane;
    private final Scene scene;

    public GameManager(Stage stage, String line) {

        this.pane = new BorderPane();

        if (line.equals("online")) {
            this.engine = new SpaceTraderEngine(stage);
        }
        else if (line.equals("offline")) {
            this.engine = new DummyEngine(stage);
        }
        else {throw new IllegalArgumentException("argument must be online or offline");}

        this.init(stage);
        this.scene = new Scene(pane, 800, 450);

    }

    public Scene getScene() {
        return this.scene;
    }

    private void init(Stage stage) {
        ImageView imageView = new ImageView("logo.png");
        imageView.setX(300);
        imageView.setY(125);
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);

        VBox center = new VBox(10);
        center.setSpacing(10.0);
        center.setAlignment(Pos.CENTER);
        
        pane.setCenter(center);
        pane.setBottom(getLabel(this.engine.getStatus(), pane));
        pane.setStyle("-fx-background-color: #3e455c");

        Button newUserButton = getButtons("Create Account");
        newUserButton.setOnAction(e -> {
            stage.setScene(engine.setSceneSignUp(this.scene));
        });
        Button oldUserButton = getButtons("Login with Account");
        oldUserButton.setOnAction(e -> {
            stage.setScene(engine.setSceneLogin(this.scene));
        });

        newUserButton.setMinWidth(150);
        oldUserButton.setMinWidth(150);

        center.getChildren().addAll(imageView,newUserButton,oldUserButton);

    }

    private static Button getButtons(String text) {
        Button button = new Button(text);
        button.setFont(new Font("Gill Sans", 15));
        button.setStyle("-fx-background-color: #E7F0FD;"
                + "-fx-border-color: white;");
        return button;
    }
}
