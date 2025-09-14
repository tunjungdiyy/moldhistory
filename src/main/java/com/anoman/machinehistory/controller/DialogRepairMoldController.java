package com.anoman.machinehistory.controller;

import com.anoman.machinehistory.model.problem.Problem;
import com.anoman.machinehistory.model.repair.RepairMold;
import com.anoman.machinehistory.service.problem.ProblemService;
import com.anoman.machinehistory.service.problem.ProblemServiceImpl;
import com.anoman.machinehistory.service.repairmold.RepairMoldService;
import com.anoman.machinehistory.service.repairmold.RepairMoldServiceImpl;
import com.anoman.machinehistory.utility.ConvertionMilistoDate;
import com.anoman.machinehistory.utility.alert.AlertApp;
import javafx.event.ActionEvent;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class DialogRepairMoldController {
    public Label titleLable;
    public HBox btnCanceled;
    public TextField tfProductName;
    public TextField tfPart;
    public TextField tfProblem;
    public TextArea tfDescription;
    public DatePicker tfDate;
    public TextArea tfRepairAction;
    public DatePicker tfStarDateRepair;
    public DatePicker tfEndDateRepair;
    public TextField tfRepairteam;
    public TextArea tfNote;
    public Button btnSave;
    public TextField tfName;
    @Getter
    @Setter
    public Stage stage;

    RepairMold repairMold;

    AlertApp alertApp = new AlertApp();

    RepairMoldService repairMoldService = new RepairMoldServiceImpl();
    ProblemService problemService = new ProblemServiceImpl();


    // for drop down
    ListView<Problem> problemListView = new ListView<>();
    PopupControl popupControl = new PopupControl();

    public void initialize() {
        tfDate.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate object) {
                return (object != null) ? formatter.format(object) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty()) ? LocalDate.parse(string, formatter) : null;
            }
        });

        tfStarDateRepair.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate object) {
                return (object != null) ? formatter.format(object) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty()) ? LocalDate.parse(string, formatter) : null;
            }
        });

        tfEndDateRepair.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate object) {
                return (object != null) ? formatter.format(object) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty()) ? LocalDate.parse(string, formatter) : null;
            }
        });

        problemListView.setOnMouseClicked( event -> {
            Problem selected = problemListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                tfProductName.setText(selected.getCodeProblem());
                tfName.setText(selected.getProductMold().getProduct().getName() + " - " + selected.getProductMold().getDescription());
                tfPart.setText(selected.getPart());
                tfProblem.setText(selected.getProblem());
                tfDescription.setText(selected.getDescriptionProblem());
                tfDate.setValue(ConvertionMilistoDate.milistoLocalDate(selected.getProblemDate()));

                popupControl.hide();
            }
        });
    }
    
    public void fncExit(MouseEvent mouseEvent) {
        closeDialog();
    }

    public void fncSearch(KeyEvent event) {

        popupControl.setAutoHide(true);
        popupControl.setAutoFix(true);
        popupControl.getScene().setRoot(problemListView);

        String input = tfProductName.getText().toUpperCase();

        if (input.isEmpty()) {
            popupControl.hide();
            tfProductName.setText("");
            return;
        }

        List<Problem> filtered = problemService.findbyCodeContain(tfProductName.getText().toUpperCase());

        if (filtered.isEmpty()) {
            popupControl.hide();
            return;
        }

        problemListView.getItems().setAll(filtered);
        problemListView.setPrefHeight(Math.min(150, filtered.size()) * 30);

        if (!popupControl.isShowing()) {
            Bounds bounds = tfProductName.localToScreen(tfProductName.getBoundsInLocal());
            popupControl.show(tfProductName, bounds.getMinX(), bounds.getMaxY());
        }

        problemListView.setCellFactory(productUpdateAndReadListView -> new ListCell<>() {
            @Override
            protected void updateItem(Problem response, boolean empty) {
                super.updateItem(response, empty);
                setText(empty || response == null ? null : response.getCodeProblem());
            }
        });
    }

    public void fncSave(ActionEvent actionEvent) {

        if (tfRepairAction.getText().isBlank() || tfStarDateRepair.getValue() == null|| tfEndDateRepair.getValue() == null ||
        tfRepairteam.getText().isBlank() || tfProductName.getText().isBlank()) {
            alertApp.showAlert("error", "Textfile Kecuali Catatan tidak boleh kosong !!");
        } else {
            if (titleLable.getText().equals("FORM TAMBAH DATA")) {
                Problem problem = problemService.findByCodeUniqueProblem(tfProductName.getText());
                log.info(problem.getCodeProblem(), getClass());

                repairMold = new RepairMold();

                repairMold.setProblem(problem);
                repairMold.setAction(tfRepairAction.getText());
                repairMold.setRepairStart(ConvertionMilistoDate.LocalDateToMilis(tfStarDateRepair.getValue()));
                repairMold.setReapirEnd(ConvertionMilistoDate.LocalDateToMilis(tfEndDateRepair.getValue()));
                repairMold.setTeamRepair(tfRepairteam.getText());
                repairMold.setNoteRepair(tfNote.getText());

                repairMoldService.save(repairMold);

                closeDialog();
            } else if (titleLable.getText().equals("FORM EDIT DATA")){
                RepairMold update = new RepairMold();

                update.setId(repairMold.getId());
                log.info(String.valueOf(update.getId()), getClass());
                update.setCodeRepair(repairMold.getCodeRepair());
                update.setProblem(repairMold.getProblem());
                update.setAction(tfRepairAction.getText());
                update.setRepairStart(ConvertionMilistoDate.LocalDateToMilis(tfStarDateRepair.getValue()));
                update.setReapirEnd(ConvertionMilistoDate.LocalDateToMilis(tfEndDateRepair.getValue()));
                update.setTeamRepair(tfRepairteam.getText());
                update.setNoteRepair(tfNote.getText());

                repairMoldService.update(update);

                closeDialog();
            }
        }

    }

    public void setTitle(String title, RepairMold repairMold) {

        if (title.equals("add")) {
            titleLable.setText("FORM TAMBAH DATA");

        } else if (title.equals("edit")) {
            titleLable.setText("FORM EDIT DATA");

            tfProductName.setDisable(true);
            tfProductName.setText(repairMold.getProblem().getCodeProblem());
            tfName.setText(repairMold.getProblem().getProductMold().getProduct().getName() + " - " + repairMold.getProblem().getProductMold().getDescription());
            tfPart.setText(repairMold.getProblem().getPart());
            tfProblem.setText(repairMold.getProblem().getProblem());
            tfDescription.setText(repairMold.getProblem().getDescriptionProblem());
            tfDate.setValue(ConvertionMilistoDate.milistoLocalDate(repairMold.getProblem().getProblemDate()));
            tfRepairAction.setText(repairMold.getAction());
            tfStarDateRepair.setValue(ConvertionMilistoDate.milistoLocalDate(repairMold.getRepairStart()));
            tfEndDateRepair.setValue(ConvertionMilistoDate.milistoLocalDate(repairMold.getReapirEnd()));
            tfRepairteam.setText(repairMold.getTeamRepair());
            tfNote.setText(repairMold.getNoteRepair());
            this.repairMold = repairMold;

        }

    }

    void setEditable() {
        tfProductName.setDisable(false);
        tfName.setDisable(false);
        tfRepairAction.setDisable(false);
        tfStarDateRepair.setDisable(false);
        tfEndDateRepair.setDisable(false);
        tfRepairteam.setDisable(false);
        tfNote.setDisable(false);
    }

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    void closeDialog() {
        Stage stage = (Stage) btnCanceled.getScene().getWindow();
        stage.close();
    }
}
