package com.anoman.machinehistory.controller;

import com.anoman.machinehistory.model.produk.ProductUpdateAndRead;
import com.anoman.machinehistory.service.product.ProductService;
import com.anoman.machinehistory.service.product.ProductServiceImpl;
import com.anoman.machinehistory.utility.Navigation;
import com.anoman.machinehistory.utility.alert.AlertApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.util.List;

public class SubMenuProduct {
    public TextField tfId;
    public TextField tfNama;
    public TextArea tfDescription;
    public Button btnDaftar;
    public Button btnSetEmpty;
    public TextField tfSearch;
    public TableView<ProductUpdateAndRead> tableDevision;
    public TableColumn<ProductUpdateAndRead, String> idColoumn;
    public TableColumn<ProductUpdateAndRead, String> nameColoumn;
    public TableColumn<ProductUpdateAndRead, String> descriptionColumn;
    public TextField tfCode;

    List<ProductUpdateAndRead> productUpdateAndReadList;
    ObservableList<ProductUpdateAndRead> productUpdateAndReadObservableList;

    ProductService productService = new ProductServiceImpl();

    public void initialize() {
        List<ProductUpdateAndRead> list = productService.findAll();

        addTableData(list);

        tableDevision.setRowFactory(param -> {
            TableRow<ProductUpdateAndRead> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    ProductUpdateAndRead response = row.getItem();
                    tfId.setText(String.valueOf(response.getId()));
                    tfCode.setText(response.getCodeProduct());
                    tfNama.setText(response.getName());
                    tfDescription.setText(response.getDescription());
                }
            });
            return row;
        });
    }

    public void fncSearch(KeyEvent event) {

        if (!tfSearch.getText().isEmpty()) {

            List<ProductUpdateAndRead> productUpdateAndReadList1 = productService.findAll();

            List<ProductUpdateAndRead> filter = productUpdateAndReadList1.stream().filter(productUpdateAndRead ->
                    productUpdateAndRead.getName().toUpperCase().contains(tfSearch.getText().toUpperCase())).toList();

            addTableData(filter);
        } else {
            List<ProductUpdateAndRead> productUpdateAndReadList1 = productService.findAll();

            addTableData(productUpdateAndReadList1);
        }

    }

    public void fncsetEmptyTf(ActionEvent actionEvent) {
        setEmpty();
    }

    public void fncRegister(ActionEvent actionEvent) {
        AlertApp alertApp = new AlertApp();

        if (tfNama.getText().isBlank()) {
            alertApp.showAlert("error", "nama tidak boleh kosong");
        }

        Boolean value = productService.register(tfNama.getText(), tfDescription.getText());

        if (value) {
            productUpdateAndReadList = productService.findAll();

            addTableData(productUpdateAndReadList);
            setEmpty();
        } else {
            alertApp.showAlert("error", "Terjadi Kesalahan Sistem");
        }

    }

    public void navtfDescription(KeyEvent event) {
        nav(tfDescription, event);
    }

    public void navTfNameProduct(KeyEvent event) {
        nav(tfNama, event);
    }

    void nav(javafx.scene.Node node, KeyEvent event) {
        javafx.scene.Node[] nodes = {tfNama, tfDescription};

        Navigation navigation = new Navigation();
        navigation.upDownNav(nodes, node, event);
    }

    void addTableData(List<ProductUpdateAndRead> list) {
        idColoumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getCodeProduct()));
        nameColoumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getName()));
        descriptionColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getDescription()));

        productUpdateAndReadObservableList = FXCollections.observableList(list);

        tableDevision.setItems(productUpdateAndReadObservableList);
    }

    void setEmpty() {
        tfId.setText("");
        tfCode.setText("");
        tfNama.setText("");
        tfDescription.setText("");
        tfSearch.setText("");
    }
}
