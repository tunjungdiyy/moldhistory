package com.anoman.machinehistory.controller;

import com.anoman.machinehistory.model.problem.Problem;
import com.anoman.machinehistory.model.repair.RepairMold;
import com.anoman.machinehistory.service.repairmold.RepairMoldService;
import com.anoman.machinehistory.service.repairmold.RepairMoldServiceImpl;
import com.anoman.machinehistory.utility.ConvertionMilistoDate;
import com.anoman.machinehistory.utility.alert.AlertApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
public class SubMenuRepairMoldController {
    public TextField tfSearch;
    public DatePicker tfDateFrom;
    public DatePicker tfDateTo;
    public HBox btnAdd;
    public TableView<RepairMold> tableRepair;
    public TableColumn<RepairMold, String> ProductNameColumn;
    public TableColumn<RepairMold, String> cvtColumn;
    public TableColumn<RepairMold, String> problemColumn;
    public TableColumn<RepairMold, String> repairAction;
    public TableColumn<RepairMold, String> teamColumn;
    public TableColumn<RepairMold, String> dateRepairColumn;
    public TableColumn<RepairMold, Void> actionCokumn;
    public Stage stage;
    public HBox btnSearch;


    RepairMoldService repairMoldService = new RepairMoldServiceImpl();

    List<RepairMold> repairMoldList;
    ObservableList<RepairMold> repairMoldObservableList;

    List<RepairMold> list;

    AlertApp alertApp = new AlertApp();

    public void initialize() {
        showInitial();
    }

    public void fncSearch(KeyEvent event) {

        if (!tfSearch.getText().isBlank()) {
            repairMoldList = repairMoldService.findByProductNameContaint(tfSearch.getText().toUpperCase());

            tfDateFrom.setValue(null);
            tfDateTo.setValue(null);
            addDataTable(repairMoldList);
        } else {
            showInitial();
        }

    }

    public void fncAdd(MouseEvent mouseEvent) throws IOException {

        showDialog("add", null);

        showInitial();

    }

    void showInitial() {

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Long from = ConvertionMilistoDate.LocalDateToMilis(LocalDate.now().minusDays(30));
                Long to = ConvertionMilistoDate.LocalDateToMilis(LocalDate.now().plusDays(2));
                list = repairMoldService.findByDate(from, to);
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            addDataTable(list);
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    void addDataTable(List<RepairMold> list) {
        ProductNameColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getProblem().getProductMold().getProduct().getName() + "-" + param.getValue().getProblem().getProductMold().getDescription()) );
        cvtColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(String.valueOf(param.getValue().getProblem().getProductMold().getCvt())));
        problemColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getProblem().getPart() + " : " + param.getValue().getProblem().getProblem()));
        repairAction.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getAction()));
        teamColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getTeamRepair()));
        dateRepairColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(String.valueOf(ConvertionMilistoDate.milistoLocalDate(param.getValue().getRepairStart())) + " - " + String.valueOf(ConvertionMilistoDate.milistoLocalDate(param.getValue().getReapirEnd()))));

        Callback<TableColumn<RepairMold, Void>, TableCell<RepairMold, Void>> cellFactory = new Callback<TableColumn<RepairMold, Void>, TableCell<RepairMold, Void>>() {
            @Override
            public TableCell<RepairMold, Void> call(TableColumn<RepairMold, Void> param) {
                return new TableCell<>() {
                    public final Button btnEdit = new Button("EDIT");
                    {
                        btnEdit.setOnAction(event -> {
                            RepairMold repairMold = getTableView().getItems().get(getIndex());
                            try {
                                showDialog("edit", repairMold);
                                showInitial();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });

                    }

                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox hBox = new HBox(5, btnEdit);
                            setGraphic(hBox);
                            hBox.setAlignment(Pos.CENTER);
                            hBox.setPrefHeight(10);
                        }
                    }
                };
            }
        };

        actionCokumn.setCellFactory(cellFactory);

        repairMoldObservableList = FXCollections.observableList(list);

        tableRepair.setItems(repairMoldObservableList);
    }

    void showDialog(String title, RepairMold repairMold) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource("/com/anoman/machinehistory/dialog-repair-view.fxml"));
        Parent root = fxmlLoader.load();

        Stage dialogStage = new Stage();
        dialogStage.setScene(new Scene(root));
        DialogRepairMoldController controller = fxmlLoader.getController();
        controller.setTitle(title, repairMold);
        dialogStage.initStyle(StageStyle.UNDECORATED);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(btnAdd.getScene().getWindow());
        dialogStage.showAndWait();
    }

    public void fncbtnSearch(MouseEvent mouseEvent) {
        log.info("function search", getClass());
        if (tfSearch.getText().isBlank()) {
            if (tfDateFrom.getValue() == null || tfDateTo.getValue() == null) {
                alertApp.showAlert("warning", "textfield tangal dari dan sampai Tidak Boleh kosong");
            } else {
                repairMoldList = repairMoldService.findByDate(ConvertionMilistoDate.LocalDateToMilis(tfDateFrom.getValue()), ConvertionMilistoDate.LocalDateToMilis(tfDateTo.getValue()));
                addDataTable(repairMoldList);
            }
        } else {
            if (tfDateFrom.getValue() == null || tfDateTo.getValue() == null) {
                alertApp.showAlert("warning", "textfield tangal dari dan sampai Tidak Boleh kosong");
            } else {
                repairMoldList = repairMoldService.findBynameProductAndRepairdate(tfSearch.getText().toUpperCase(), ConvertionMilistoDate.LocalDateToMilis(tfDateFrom.getValue()), ConvertionMilistoDate.LocalDateToMilis(tfDateTo.getValue()));
                addDataTable(repairMoldList);
            }
        }
    }
}
