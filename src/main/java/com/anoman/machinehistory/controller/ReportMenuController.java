package com.anoman.machinehistory.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ReportMenuController {
    public VBox btnMold;
    public VBox btnProduct;
    public VBox btnProblem;
    public VBox btnrepair;
    public VBox btnRekapMold;

    public void fncMold(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource("/com/anoman/machinehistory/filter/report-mold-review.fxml"));
        Parent root = fxmlLoader.load();

        Stage dialogStage = new Stage();
        dialogStage.setScene(new Scene(root));
        ReviewReportMoldController controller = fxmlLoader.getController();
        dialogStage.initStyle(StageStyle.UNDECORATED);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(btnMold.getScene().getWindow());
        dialogStage.showAndWait();
    }

    public void fncProduct(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource("/com/anoman/machinehistory/filter/report-produk-review.fxml"));
        Parent root = fxmlLoader.load();

        Stage dialogStage = new Stage();
        dialogStage.setScene(new Scene(root));
        ReviewProductController controller = fxmlLoader.getController();
        dialogStage.initStyle(StageStyle.UNDECORATED);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(btnMold.getScene().getWindow());
        dialogStage.showAndWait();
    }

    public void fncProbleMold(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource("/com/anoman/machinehistory/filter/report-problem-review.fxml"));
        Parent root = fxmlLoader.load();

        Stage dialogStage = new Stage();
        dialogStage.setScene(new Scene(root));
        ReviewRepostProblemController controller = fxmlLoader.getController();
        dialogStage.initStyle(StageStyle.UNDECORATED);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(btnMold.getScene().getWindow());
        dialogStage.showAndWait();
    }

    public void fncRepairMold(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource("/com/anoman/machinehistory/filter/report-repair-review.fxml"));
        Parent root = fxmlLoader.load();

        Stage dialogStage = new Stage();
        dialogStage.setScene(new Scene(root));
        ReviewReportRepairMoldController controller = fxmlLoader.getController();
        dialogStage.initStyle(StageStyle.UNDECORATED);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(btnMold.getScene().getWindow());
        dialogStage.showAndWait();
    }

    public void fncRekapMold(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource("/com/anoman/machinehistory/filter/report-reviewproblemmold-review.fxml"));
        Parent root = fxmlLoader.load();

        Stage dialogStage = new Stage();
        dialogStage.setScene(new Scene(root));
        ReviewTotalRepairMoldController controller = fxmlLoader.getController();
        dialogStage.initStyle(StageStyle.UNDECORATED);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(btnMold.getScene().getWindow());
        dialogStage.showAndWait();
    }
}
