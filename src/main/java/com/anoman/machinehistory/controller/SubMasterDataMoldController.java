package com.anoman.machinehistory.controller;

import com.anoman.machinehistory.model.mold.ProductMold;
import com.anoman.machinehistory.service.productmold.ProductMoldService;
import com.anoman.machinehistory.service.productmold.ProductMoldServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;

public class SubMasterDataMoldController {
    public TextField tfSearch;
    public HBox btnAdd;
    public TableView<ProductMold> tableModl;
    public TableColumn<ProductMold, String> idColoumn;
    public TableColumn<ProductMold, String> noSeriColumn;
    public TableColumn<ProductMold, String> ProductNameColumn;
    public TableColumn<ProductMold, String> cvtColumn;
    public Stage stage;
    public TableColumn<ProductMold, String> descriptionColumn;

    List<ProductMold> productMoldList;
    ObservableList<ProductMold> productMoldObservableList;

    ProductMoldService productMoldService = new ProductMoldServiceImpl();

    public void initialize() {

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                productMoldList = productMoldService.findAll();
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            addDataTable(productMoldList);
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public void fncSearch(KeyEvent event) {
        List<ProductMold> productMolds = productMoldService.findAll();

        if(!tfSearch.getText().isBlank()) {
            List<ProductMold> filter = productMoldList.stream().filter(productMold ->
                    productMold.getProduct().getName().contains(tfSearch.getText().toUpperCase())).toList();

            addDataTable(filter);
        } else {
            addDataTable(productMolds);
        }
    }

    public void fncAdd(MouseEvent mouseEvent) throws IOException {

        showDialog();
        addDataTable(productMoldService.findAll());
    }

    public void addDataTable(List<ProductMold> productMoldList) {
        idColoumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getCode()));
        noSeriColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getMold().getSerialNumber()));
        ProductNameColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getProduct().getName()));
        cvtColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(String.valueOf(param.getValue().getCvt())));
        descriptionColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getDescription()));

        productMoldObservableList = FXCollections.observableList(productMoldList);

        tableModl.setItems(productMoldObservableList);

    }

    void showDialog() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource("/com/anoman/machinehistory/dialog-mold-view.fxml"));
        Parent root = fxmlLoader.load();

        Stage dialogStage = new Stage();
        dialogStage.setScene(new Scene(root));
        DialogMoldController controller = fxmlLoader.getController();
        dialogStage.initStyle(StageStyle.UNDECORATED);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(btnAdd.getScene().getWindow());
        dialogStage.showAndWait();
    }

}
