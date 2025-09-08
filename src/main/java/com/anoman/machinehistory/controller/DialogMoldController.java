package com.anoman.machinehistory.controller;

import com.anoman.machinehistory.model.mold.Mold;
import com.anoman.machinehistory.model.mold.ProductMold;
import com.anoman.machinehistory.model.produk.ProductUpdateAndRead;
import com.anoman.machinehistory.service.product.ProductService;
import com.anoman.machinehistory.service.product.ProductServiceImpl;
import com.anoman.machinehistory.service.productmold.ProductMoldService;
import com.anoman.machinehistory.service.productmold.ProductMoldServiceImpl;
import com.anoman.machinehistory.utility.Navigation;
import com.anoman.machinehistory.utility.alert.AlertApp;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.apache.poi.ss.formula.functions.Mode;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class DialogMoldController {
    public HBox btnCanceled;
    public TextField tfSerialNumber;
    public TextField tfThickness;
    public TextField tfVertical;
    public TextField tfHorizontal;
    public TextField tfTonase;
    public TextField tfProductName;
    public TextField tfCvt;
    public TextField tfCt;
    public TextField tfGrammage;
    public TextArea tfDescription;
    public Button btnSave;
    public Stage stage;

    // for drop down
    ProductService productService = new ProductServiceImpl();
    ListView<ProductUpdateAndRead> productUpdateAndReadListView = new ListView<>();
    PopupControl popupControl = new PopupControl();

    AlertApp alertApp = new AlertApp();

    ProductMoldService productMoldService = new ProductMoldServiceImpl();

    public void initialize() {
        productUpdateAndReadListView.setOnMouseClicked(event -> {
            ProductUpdateAndRead selected = productUpdateAndReadListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                tfProductName.setText(selected.getName());

                popupControl.hide();
//                try {
//                    List<PermissionsResponse> list = filteredPermissionByIDUserRole(selected.getIdUserRole());
//                    updateCheckboxes(list);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }

            }
        });
    }

    public void fncExit(MouseEvent mouseEvent) {
        setEmpty();
        closeDialog();
    }

    public void navSerialNumber(KeyEvent event) {
        nav(tfSerialNumber, event);
    }

    public void navThickness(KeyEvent event) {
        nav(tfThickness, event);
    }

    public void navVertical(KeyEvent event) {
        nav(tfVertical, event);
    }

    public void navHorizontal(KeyEvent event) {
        nav(tfHorizontal, event);
    }

    public void navTonase(KeyEvent event) {
        nav(tfTonase, event);
    }

    public void tfProductname(KeyEvent event) {
        if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
            nav(tfProductName, event);

        } else {
            popupControl.setAutoHide(true);
            popupControl.setAutoFix(true);
            popupControl.getScene().setRoot(productUpdateAndReadListView);

            String input = tfProductName.getText().toUpperCase();

            if (input.isEmpty()) {
                popupControl.hide();
                tfProductName.setText("");
//                clearCheckboxes();
                return;
            }

//            List<ProductUpdateAndRead> responses = productService.findAll();
//            List<ProductUpdateAndRead> filtered = responses.stream().filter(
//                    productUpdateAndRead -> productUpdateAndRead.getName().toUpperCase().contains(input)
//            ).collect(Collectors.toList());

            List<ProductUpdateAndRead> filtered = productService.findByContaint(tfProductName.getText().toUpperCase());

            if (filtered.isEmpty()) {
                popupControl.hide();
                return;
            }

            productUpdateAndReadListView.getItems().setAll(filtered);
            productUpdateAndReadListView.setPrefHeight(Math.min(150, filtered.size()) * 30);

            if (!popupControl.isShowing()) {
                Bounds bounds = tfProductName.localToScreen(tfProductName.getBoundsInLocal());
                popupControl.show(tfProductName, bounds.getMinX(), bounds.getMaxY());
            }

            productUpdateAndReadListView.setCellFactory(productUpdateAndReadListView -> new ListCell<>() {
                @Override
                protected void updateItem(ProductUpdateAndRead response, boolean empty) {
                    super.updateItem(response, empty);
                    setText(empty || response == null ? null : response.getName());
                }
            });
        }
    }

    public void navCvt(KeyEvent event) {
        nav(tfCvt, event);
    }

    public void navCT(KeyEvent event) {
        nav(tfCt, event);
    }

    public void navGrammage(KeyEvent event) {
        nav(tfGrammage, event);
    }

    public void navDescription(KeyEvent event) {
        nav(tfDescription, event);
    }

    public void fncSave(MouseEvent mouseEvent) {

        if (tfProductName.getText().isBlank()) {
            alertApp.showAlert("error", "Nama Produk tidak boleh kosong");
        } else {
            ProductMold productMold = new ProductMold();

            ProductUpdateAndRead product = productService.findByName(tfProductName.getText());

            Mold mold = new Mold();
            if (tfThickness.getText().isBlank()) {
                mold.setThickness(0.0);
            } else {
                mold.setThickness(Double.parseDouble(tfThickness.getText()));
            }

            if (tfVertical.getText().isBlank()) {
                mold.setVertical(0.0);
            } else {
                mold.setVertical(Double.parseDouble(tfVertical.getText()));
            }

            if (tfHorizontal.getText().isBlank()) {
                mold.setHorizontal(0.0);
            } else {
                mold.setHorizontal(Double.parseDouble(tfHorizontal.getText()));
            }
            if (tfTonase.getText().isBlank()) {
                mold.setTonase(0.0);
            } else {
                mold.setTonase(Double.parseDouble(tfTonase.getText()));
            }

            productMold.setMold(mold);
            productMold.setProduct(product);

            if (tfCvt.getText().isBlank()) {
                productMold.setCvt(0);
            } else {
                productMold.setCvt(Integer.parseInt(tfCvt.getText()));
            }
            if (tfCt.getText().isBlank()) {
                productMold.setCt(0.0);
            } else {
                productMold.setCt(Double.parseDouble(tfCt.getText()));
            }
            if (tfGrammage.getText().isBlank()) {
                productMold.setGrammage(0.0);
            } else {
                productMold.setGrammage(Double.parseDouble(tfGrammage.getText()));
            }
            if (tfDescription.getText().isBlank()) {
                productMold.setDescription("");
            } else {
                productMold.setDescription(tfDescription.getText());
            }

            productMoldService.create(productMold);

            log.info("Berhasil menyimpan", getClass());
            setEmpty();
            closeDialog();
        }

    }

    void closeDialog() {
        Stage stage = (Stage) btnCanceled.getScene().getWindow();
        stage.close();
    }

    void nav(javafx.scene.Node node, KeyEvent event) {
        javafx.scene.Node[] nodes = {tfSerialNumber, tfThickness, tfVertical, tfHorizontal, tfTonase, tfProductName, tfCvt, tfCt, tfGrammage, tfDescription};

        Navigation navigation = new Navigation();
        navigation.upDownNav(nodes, node, event);
    }

    void setEmpty() {
        tfSerialNumber.setText("");
        tfThickness.setText("");
        tfVertical.setText("");
        tfHorizontal.setText("");
        tfTonase.setText("");
        tfProductName.setText("");
        tfCvt.setText("");
        tfCt.setText("");
        tfGrammage.setText("");
        tfDescription.setText("");
    }
}
