package com.anoman.machinehistory.controller;

import com.anoman.machinehistory.model.mold.ProductMold;
import com.anoman.machinehistory.model.problem.Problem;
import com.anoman.machinehistory.model.problem.ProblemUpdate;
import com.anoman.machinehistory.service.problem.ProblemService;
import com.anoman.machinehistory.service.problem.ProblemServiceImpl;
import com.anoman.machinehistory.service.productmold.ProductMoldService;
import com.anoman.machinehistory.service.productmold.ProductMoldServiceImpl;
import com.anoman.machinehistory.utility.ConvertionMilistoDate;
import com.anoman.machinehistory.utility.Navigation;
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
public class DialogProblemController {
    public Label titleLable;
    public HBox btnCanceled;
    public TextField tfProductName;
    public TextField tfPart;
    public TextField tfProblem;
    public TextArea tfDescription;
    public DatePicker tfDate;
    public Button btnSave;

    @Getter
    @Setter
    public Stage stage;

    Problem problem;

    AlertApp alertApp = new AlertApp();

    ProductMoldService productMoldService = new ProductMoldServiceImpl();
    ProblemService problemService = new ProblemServiceImpl();

    // for drop down
    ListView<ProductMold> productMoldListView = new ListView<>();
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

        productMoldListView.setOnMouseClicked(event -> {
            ProductMold selected = productMoldListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                tfProductName.setText(selected.getProduct().getName() + "#" +selected.getId());

                popupControl.hide();

            }
        });
    }

    public void fncExit(MouseEvent mouseEvent) {
        closeDialog();
    }

    public void navProduct(KeyEvent event) {
        nav(tfProductName, event);
    }

    public void fncSearch(KeyEvent event) {
            popupControl.setAutoHide(true);
            popupControl.setAutoFix(true);
            popupControl.getScene().setRoot(productMoldListView);

            String input = tfProductName.getText().toUpperCase();

            if (input.isEmpty()) {
                popupControl.hide();
                tfProductName.setText("");
//                clearCheckboxes();
                return;
            }

            List<ProductMold> filtered = productMoldService.findByKeyword(tfProductName.getText().toUpperCase());

            if (filtered.isEmpty()) {
                popupControl.hide();
                return;
            }

            productMoldListView.getItems().setAll(filtered);
            productMoldListView.setPrefHeight(Math.min(150, filtered.size()) * 30);

            if (!popupControl.isShowing()) {
                Bounds bounds = tfProductName.localToScreen(tfProductName.getBoundsInLocal());
                popupControl.show(tfProductName, bounds.getMinX(), bounds.getMaxY());
            }

            productMoldListView.setCellFactory(productUpdateAndReadListView -> new ListCell<>() {
                @Override
                protected void updateItem(ProductMold response, boolean empty) {
                    super.updateItem(response, empty);
                    setText(empty || response == null ? null : response.getProduct().getName() + "/" + response.getDescription());
                }
            });
    }

    public void navPart(KeyEvent event) {
        nav(tfPart, event);
    }

    public void navProblem(KeyEvent event) {
        nav(tfProblem, event);
    }

    public void navDescription(KeyEvent event) {
        nav(tfDescription, event);
    }

    public void fncSave(ActionEvent actionEvent) {
        if (titleLable.getText().equals("FORM TAMBAH DATA")) {
            if (tfProductName.getText().isEmpty() || tfPart.getText().isEmpty() ||
                    tfProblem.getText().isEmpty() || tfDescription.getText().isEmpty() || tfDate.getValue() == null) {
                alertApp.showAlert("error", "Textfield Tidak Boleh Kosong ");
            } else {

                String[] strings = tfProductName.getText().split("#");
                ProductMold productMold = productMoldService.findById(strings[1]);

                log.info(productMold.getId().toString(), getClass());

                Problem problem = new Problem();
                problem.setProblemDate(ConvertionMilistoDate.LocalDateToMilis(tfDate.getValue()));
                problem.setProblem(tfProblem.getText());
                problem.setPart(tfPart.getText());
                problem.setProductMold(productMold);
                problem.setStatus("BELUM PROSES".toUpperCase());
                problem.setDescriptionProblem(tfDescription.getText());


                problemService.create(problem);

                closeDialog();
            }
        } else if (titleLable.getText().equals("FORM EDIT DATA")) {
            log.info(String.valueOf(problem.getId()), getClass());

            ProblemUpdate update = new ProblemUpdate();
            update.setId(problem.getId());
            update.setPart(tfPart.getText());
            update.setProblem(tfProblem.getText());
            update.setProblemDate(ConvertionMilistoDate.LocalDateToMilis(tfDate.getValue()));
            update.setDescription(tfDescription.getText());
            update.setProductMold(problem.getProductMold());
            log.info(String.valueOf(problem.getProductMold().getId()), getClass());
            update.setStatus(problem.getStatus());

            problemService.update(update);

            closeDialog();

        }

    }

    public void setTitile(String title, Problem problem) {

        if (title.equals("add")) {
            titleLable.setText("FORM TAMBAH DATA");
            setEditable();

        } else if (title.equals("edit")) {
            titleLable.setText("FORM EDIT DATA");
            tfProductName.setEditable(false);
            tfProductName.setText(problem.getProductMold().getProduct().getName() + "(" + problem.getProductMold().getDescription() + ")");
            tfPart.setText(problem.getPart());
            tfProblem.setText(problem.getProblem());
            tfDate.setValue(ConvertionMilistoDate.milistoLocalDate(problem.getProblemDate()));
            tfDescription.setText(problem.getDescriptionProblem());
            this.problem = problem;
        }

    }

    void setEditable() {
        tfProductName.setEditable(true);
        tfPart.setEditable(true);
        tfProblem.setEditable(true);
        tfDate.setEditable(true);
        tfDescription.setEditable(true);
    }

    void closeDialog() {
        Stage stage = (Stage) btnCanceled.getScene().getWindow();
        stage.close();
    }

    void nav(javafx.scene.Node node, KeyEvent event) {
        javafx.scene.Node[] nodes = {tfProductName, tfPart, tfProblem, tfDescription};

        Navigation navigation = new Navigation();

        navigation.upDownNav(nodes, node, event);
    }

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
}
