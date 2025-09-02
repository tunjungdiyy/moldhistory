package com.anoman.machinehistory;

import com.anoman.machinehistory.controller.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MenuApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(MenuApp.class.getResource("/com/anoman/machinehistory/home-view.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        MenuController controller = fxmlLoader.getController();
        controller.setStage(primaryStage);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.centerOnScreen();
        primaryStage.setScene(scene);

        primaryStage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
