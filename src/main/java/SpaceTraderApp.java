import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;

import java.util.List;

//test token = "f3b68ed0-79e6-4dd4-a4d5-79fe74fbaecd"

public class SpaceTraderApp extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        List<String> params = getParameters().getRaw();
        GameManager gameManager = new GameManager(primaryStage, params.get(0));

        // gameManager.run();

        primaryStage.setTitle("SpaceTrader");
        primaryStage.setScene(gameManager.getScene());
        primaryStage.show();
    }
}
