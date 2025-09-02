package com.anoman.machinehistory;

import com.anoman.machinehistory.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApp.class.getResource("/com/anoman/machinehistory/login-view.fxml"));

        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        LoginController loginController = fxmlLoader.getController();
        loginController.setStage(primaryStage);
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
