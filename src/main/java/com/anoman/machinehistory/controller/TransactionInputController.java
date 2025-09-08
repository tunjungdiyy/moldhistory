package com.anoman.machinehistory.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

public class TransactionInputController {
    public HBox btnProblem;
    public HBox btnActionRepair;
    public ScrollPane bodyContent;
    public BorderPane mainContent;

    @Getter
    @Setter
    public Stage stage;

    public void  initialize(){
        showContent("/com/anoman/machinehistory/transaction-problem-view.fxml");
    }

    public void fncBtnProblem(MouseEvent mouseEvent) {
        showContent("/com/anoman/machinehistory/transaction-problem-view.fxml");
    }

    public void fncBtnRepair(MouseEvent mouseEvent) {
        showContent("/com/anoman/machinehistory/transaction-repair-view.fxml");
    }

    void showContent(String url) {
        try {
            BorderPane borderPane = FXMLLoader.load(getClass().getResource(url));
            bodyContent.setContent(borderPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
