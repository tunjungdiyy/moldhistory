package com.anoman.machinehistory.utility.alert;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class AlertApp {
    public boolean showAlert(String title, String information){

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AlertApp.class.getResource("/com/anoman/machinehistory/util/alert-view.fxml"));
            Parent root =fxmlLoader.load();

            AlertController allertController = fxmlLoader.getController();
            allertController.initialize(title, information);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.centerOnScreen();
            stage.setScene(new Scene(root));
            stage.showAndWait();

            return allertController.getResult();
        } catch (IOException e) {
            return false;
        }


    }
}
