package com.anoman.machinehistory.controller;

import com.anoman.machinehistory.model.problem.Problem;
import com.anoman.machinehistory.repository.problem.ProblemRepository;
import com.anoman.machinehistory.repository.problem.ProblemRepositoryImpl;
import com.anoman.machinehistory.service.problem.ProblemService;
import com.anoman.machinehistory.service.problem.ProblemServiceImpl;
import com.anoman.machinehistory.utility.ConvertionMilistoDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
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
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SubMenuProblemMoldController {
    public TextField tfSearch;
    public HBox btnAdd;
    public TableView<Problem> tableProblem;
    public TableColumn<Problem, String> idColoumn;
    public TableColumn<Problem, String> ProductNameColumn;
    public TableColumn<Problem, String> cvtColumn;
    public TableColumn<Problem, String> problemColumn;
    public TableColumn<Problem, String> dateColumn;
    public TableColumn<Problem, String> statusColumn;
    public TableColumn<Problem, Void> actionColumn;

    @Getter
    @Setter
    public Stage stage;
    public ComboBox cmBoxStatus;

    ProblemService problemService = new ProblemServiceImpl();

    List<Problem> problemList;
    ObservableList<Problem> problemObservableList;

    List<Problem> list;

    public void initialize() {

        setDataCmBox();

        showinitial();

    }

    public void fncSearch(KeyEvent event) {

        if (!tfSearch.getText().isBlank()) {

            problemList = problemService.findByProductName(tfSearch.getText().toUpperCase());

            addItemTable(problemList);

        } else {

            showinitial();
        }

    }

    public void fncAdd(MouseEvent mouseEvent) throws IOException {
        showDialog("add", null);

        showinitial();

    }

    void addItemTable(List<Problem> list) {
        idColoumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getCodeProblem()));
        ProductNameColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty( param.getValue().getProductMold().getProduct().getName() + "-" + param.getValue().getProductMold().getDescription()));
        cvtColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getProductMold().getCvt().toString()));
        problemColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty( param.getValue().getPart() + " : " + param.getValue().getProblem()));
        dateColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(String.valueOf(ConvertionMilistoDate.milistoLocalDate(param.getValue().getProblemDate()))));
        statusColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getStatus()));

        Callback<TableColumn<Problem, Void>, TableCell<Problem, Void>> cellFactory = new Callback<TableColumn<Problem, Void>, TableCell<Problem, Void>>() {
            @Override
            public TableCell<Problem, Void> call(TableColumn<Problem, Void> param) {
                return new TableCell<>() {
                    public final Button btnEdit = new Button("EDIT");
                    public final Button btnPrint = new Button("PRINT");
                    {
                        btnEdit.setOnAction(event -> {
                            Problem problem = getTableView().getItems().get(getIndex());
                            try {
                                System.out.println("Function Edit");
                                showDialog("edit", problem);

                                showinitial();

                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });

                        btnPrint.setOnAction(event -> {
                            Problem problem = getTableView().getItems().get(getIndex());
                            try {
                                // action
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
                            HBox hBox = new HBox(5, btnEdit, btnPrint);
                            setGraphic(hBox);
                            hBox.setAlignment(Pos.CENTER);
                            hBox.setPrefHeight(10);
                        }
                    }
                };
            }
        };

        actionColumn.setCellFactory(cellFactory);

        problemObservableList = FXCollections.observableList(list);

        tableProblem.setItems(problemObservableList);
    }

    void showDialog(String title, Problem problem) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource("/com/anoman/machinehistory/dialog-problem-view.fxml"));
        Parent root = fxmlLoader.load();

        Stage dialogStage = new Stage();
        dialogStage.setScene(new Scene(root));
        DialogProblemController controller = fxmlLoader.getController();
        controller.setTitile(title, problem);
        dialogStage.initStyle(StageStyle.UNDECORATED);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(btnAdd.getScene().getWindow());
        dialogStage.showAndWait();
    }

    public void filterStatus(ActionEvent actionEvent) {

        if (cmBoxStatus.getValue().equals("SEMUA")) {
            LocalDate now = LocalDate.now();

            List<Problem> problems = problemService.findByDate(ConvertionMilistoDate.LocalDateToMilis(now.minusDays(30)), ConvertionMilistoDate.LocalDateToMilis(now.plusDays(2)));

            addItemTable(problems);
        } else {
            problemList = problemService.findByStatus(cmBoxStatus.getValue().toString());

            addItemTable(problemList);
        }
    }

    void setDataCmBox() {
        List<String> stringList = new ArrayList<>();
        stringList.add("SEMUA");
        stringList.add("BELUM PROSES");
        stringList.add("SELESAI");

        ObservableList<String> stringObservableList = FXCollections.observableList(stringList);

        cmBoxStatus.setItems(stringObservableList);
    }

    void showinitial() {

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                LocalDate now = LocalDate.now();

                 list = problemService.findByStatusAndDate( "BELUM PROSES",ConvertionMilistoDate.LocalDateToMilis(now.minusDays(30)), ConvertionMilistoDate.LocalDateToMilis(now.plusDays(2)));
                return null;
            }
        };

        task.setOnSucceeded(event -> addItemTable(list));

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

}
