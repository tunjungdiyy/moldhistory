package com.anoman.machinehistory.controller;

import com.anoman.machinehistory.model.problem.Problem;
import com.anoman.machinehistory.service.problem.ProblemService;
import com.anoman.machinehistory.service.problem.ProblemServiceImpl;
import com.anoman.machinehistory.utility.ConvertionMilistoDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class MenuHomeController {
    public TextField tfSearch;
    public TableView<Problem> tableProblem;
    public TableColumn<Problem, String> idColoumn;
    public TableColumn<Problem, String> ProductNameColumn;
    public TableColumn<Problem, String> cvtColumn;
    public TableColumn<Problem, String> problemColumn;
    public TableColumn<Problem, String> dateColumn;
    public TableColumn<Problem, String> statusColumn;
    @Getter
    @Setter
    public Stage stage;

    ProblemService problemService = new ProblemServiceImpl();

    List<Problem> problemList;
    ObservableList<Problem> problemObservableList;

    public void initialize() {
        showInitial();
    }
    
    public void fncSearch(KeyEvent event) {
        if (!tfSearch.getText().isBlank()) {

            problemList = new ArrayList<>();
            problemList = problemService.findByProductName(tfSearch.getText().toUpperCase());

            addItemTable(problemList);

        } else {
            showInitial();
        }
    }
    
    void showInitial() {
        problemList = new ArrayList<>();
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                
                problemList = problemService.findByStatus("BELUM PROSES");
                
                return null;
            }
        };
        task.setOnSucceeded(event -> addItemTable(problemList));

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    void addItemTable(List<Problem> list) {
        idColoumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getCodeProblem()));
        ProductNameColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty( param.getValue().getProductMold().getProduct().getName() + "-" + param.getValue().getProductMold().getDescription()));
        cvtColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getProductMold().getCvt().toString()));
        problemColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty( param.getValue().getPart() + " : " + param.getValue().getProblem()));
        dateColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(String.valueOf(ConvertionMilistoDate.milistoLocalDate(param.getValue().getProblemDate()))));
        statusColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getStatus()));

        problemObservableList = FXCollections.observableList(list);

        tableProblem.setItems(problemObservableList);
    }
    
    
}
