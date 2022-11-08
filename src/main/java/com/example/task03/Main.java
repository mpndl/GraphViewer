package com.example.task03;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("View.fxml"));
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setTitle("Graph Viewer");
        stage.show();
    }
}
