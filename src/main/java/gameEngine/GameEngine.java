package gameEngine;

import javafx.scene.Scene;

public interface GameEngine {

    String getStatus();

    Scene setSceneSignUp(Scene homeScene);

    Scene setSceneLogin(Scene homeScene);
}
