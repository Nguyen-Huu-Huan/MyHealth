package com.example.myhealth;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static Scene scene;
    public static Stage stage;


    @Override
    public void start(Stage primaryStage) throws Exception {
        // First start with the UI from Home.fxml
        Main.stage = primaryStage;
        Parent home = FXMLLoader.load(getClass().getResource("fxml/Home.fxml"));
        scene = new Scene(home);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Home");
        primaryStage.show();
    }

    public static void changeScene(String fxml) throws IOException {
        // Function to change scene (load new FXML file) without creating new window
        Parent newFxml = FXMLLoader.load(Main.class.getResource(fxml));
        scene.setRoot(newFxml);
        String windowName = fxml.split("/")[1].split(".fxml")[0];
        stage.setTitle(windowName);
        stage.sizeToScene();
    }

    public static void main(String[] args) {
        launch(args);
    }
}