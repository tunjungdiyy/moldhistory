package com.anoman.machinehistory.controller;

import com.anoman.machinehistory.utility.PanelNavigasi;
import com.anoman.machinehistory.utility.alert.AlertApp;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

public class MenuController {
    public HBox btnMaximize;
    public HBox btnminimize;
    public HBox btnExit;
    public HBox btnReport;
    public HBox btnMaster;
    public HBox btnProcess;
    public HBox btnHome;
    public BorderPane bodyPanel;
    public ScrollPane body;

    @Setter
    @Getter
    public Stage stage;

    double yOffside = 0;
    double xOffside = 0;

    double width = 1200;
    double height = 630;

    PanelNavigasi panelNavigasi = new PanelNavigasi(yOffside, xOffside);

    AlertApp alertApp = new AlertApp();

    public void drag(MouseEvent mouseEvent) {
        panelNavigasi.drag(stage, mouseEvent);
    }

    public void press(MouseEvent mouseEvent) {
        panelNavigasi.press(mouseEvent);
    }

    public void fncMaximize(MouseEvent mouseEvent) {
        panelNavigasi.maximize(mouseEvent, stage, width, height);
    }

    public void fncMinimize(MouseEvent mouseEvent) {
        panelNavigasi.minimize(mouseEvent, stage);
    }

    public void functionExit(MouseEvent mouseEvent) {
        boolean result = alertApp.showAlert("confirm", "Continue Exit Program ???");

        if (result) {
            Platform.exit();
        }

    }

    public void showrRepostMenu(MouseEvent mouseEvent) {
    }

    public void showMasterDataMenu(MouseEvent mouseEvent) {
        showContent("/com/anoman/machinehistory/masterdata-view.fxml");
    }

    public void showProcessMenu(MouseEvent mouseEvent) {
        showContent("/com/anoman/machinehistory/transactioninput-view.fxml");
    }

    public void showHomeMenu(MouseEvent mouseEvent) {
    }

    void showContent(String url) {
        try {
            BorderPane borderPane = FXMLLoader.load(getClass().getResource(url));
            body.setContent(borderPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
