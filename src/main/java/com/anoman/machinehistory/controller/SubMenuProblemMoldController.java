package com.anoman.machinehistory.controller;

import com.anoman.machinehistory.config.PdfDirectoryConfig;
import com.anoman.machinehistory.config.PrintConfig;
import com.anoman.machinehistory.model.problem.Problem;
import com.anoman.machinehistory.repository.problem.ProblemRepository;
import com.anoman.machinehistory.repository.problem.ProblemRepositoryImpl;
import com.anoman.machinehistory.service.problem.ProblemService;
import com.anoman.machinehistory.service.problem.ProblemServiceImpl;
import com.anoman.machinehistory.utility.ConvertionMilistoDate;
import com.anoman.machinehistory.utility.FormRepairMoldController;
import com.anoman.machinehistory.utility.PrintPreviewController;
import com.anoman.machinehistory.utility.alert.AlertApp;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
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

import java.io.File;
import java.io.FileOutputStream;
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
//                            try {
//                                FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource("/com/anoman/machinehistory/util/form-repair-mold.fxml"));
//                                Parent root = fxmlLoader.load();
//
//                                Stage dialogStage = new Stage();
//                                dialogStage.setScene(new Scene(root));
//
//                                FormRepairMoldController controller = fxmlLoader.getController();
//                                controller.setData(problem);
//
////                                PrintConfig.showPreview(controller.getPanePrintRepair());
//
//                                FXMLLoader fxmlLoaderPreview = new FXMLLoader(MenuController.class.getResource("/com/anoman/machinehistory/util/review-print-view.fxml"));
//                                Parent roorReview = fxmlLoaderPreview.load();
//
//                                Stage reviewStage = new Stage();
//                                reviewStage.setScene(new Scene(roorReview));
//                                PrintPreviewController previewController = fxmlLoaderPreview.getController();
//                                previewController.initialize(controller.getPanePrintRepair());
//                                reviewStage.initStyle(StageStyle.UNDECORATED);
//                                reviewStage.initModality(Modality.WINDOW_MODAL);
//                                reviewStage.initOwner(btnAdd.getScene().getWindow());
//                                reviewStage.showAndWait();
//
//                            } catch (Exception e) {
//                                throw new RuntimeException(e);
//                            }

                            try {
                                File pdfFile = PdfDirectoryConfig.getPdfFile("form-perbaikan_mold.pdf");
                                Document document = new Document(PageSize.A4, 40, 40, 50, 50);
                                PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
                                document.open();

                                // Judul
                                Font titleFont = new Font(Font.HELVETICA, 14, Font.BOLD);
                                Paragraph title = new Paragraph("FORM PERBAIKAN MOLD", titleFont);
                                title.setAlignment(Element.ALIGN_CENTER);
                                document.add(title);
                                document.add(Chunk.NEWLINE);

                                // Buat Tabel Utama
                                PdfPTable table = new PdfPTable(2);
                                table.setWidthPercentage(100);
                                table.setWidths(new float[]{1, 1});

                                // Kiri: Detail Masalah Mold
                                PdfPTable leftTable = new PdfPTable(2);
                                leftTable.setWidths(new float[]{2, 5});
                                leftTable.setWidthPercentage(100);

                                addRow(leftTable, "Code", ": " + problem.getCodeProblem(), 40f);
                                addRow(leftTable, "Tanggal", ": " + String.valueOf(ConvertionMilistoDate.milistoLocalDate(problem.getProblemDate())), 40f);
                                addRow(leftTable, "Mold", ": " + problem.getProductMold().getProduct().getName() + "/" + problem.getProductMold().getDescription(), 40f);
                                addRow(leftTable, "Seri Mold", ": " + problem.getProductMold().getMold().getSerialNumber(), 40f);
                                addRow(leftTable, "Part", ": " + problem.getPart(), 40f);
                                addRow(leftTable, "Masalah", ": " + problem.getProblem(), 40f);
                                addRow(leftTable, "Catatan", ": " + problem.getDescriptionProblem(), 40f);

                                PdfPCell leftCell = new PdfPCell(leftTable);
                                leftCell.setBorder(Rectangle.NO_BORDER);
                                table.addCell(leftCell);

                                // Kanan: Detail Perbaikan Mold (Kosong/manual)
                                PdfPTable rightTable = new PdfPTable(2);
                                rightTable.setWidths(new float[]{2, 5});
                                rightTable.setWidthPercentage(100);

                                addRow(rightTable, "Team", ": " , 40f);
                                addRow(rightTable, "Pengerjaan", "", 40f);
                                addRow(rightTable, "Mulai", ": ", 40f);
                                addRow(rightTable, "Selesai", ": ", 40f);
                                addRow(rightTable, "Jenis Perbaikan", ": ", 90f);
                                addRow(rightTable, "Catatan", ": ", 90f);

                                PdfPCell rightCell = new PdfPCell(rightTable);
                                rightCell.setBorder(Rectangle.NO_BORDER);
                                table.addCell(rightCell);

                                document.add(table);
                                document.add(Chunk.NEWLINE);
                                document.add(Chunk.NEWLINE);

                                // Tambahkan Tanda Tangan
                                PdfPTable signTable = new PdfPTable(2);
                                signTable.setWidthPercentage(100);
                                signTable.setWidths(new float[]{1, 1});

                                PdfPCell diketahuiCell = new PdfPCell(new Phrase("Diketahui Oleh,"));
                                diketahuiCell.setBorder(Rectangle.NO_BORDER);
                                diketahuiCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                diketahuiCell.setPaddingTop(30);
                                signTable.addCell(diketahuiCell);

                                PdfPCell diperbaikiCell = new PdfPCell(new Phrase("Diperbaiki Oleh,"));
                                diperbaikiCell.setBorder(Rectangle.NO_BORDER);
                                diperbaikiCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                diperbaikiCell.setPaddingTop(30);
                                signTable.addCell(diperbaikiCell);

                                document.add(signTable);

                                document.close();
                                AlertApp alertApp = new AlertApp();

                                alertApp.showAlert("info", "Berhasil Membuat Form Perbaikan buka di folder mechine");

                            } catch (Exception e) {
                                e.printStackTrace();
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

    // Fungsi untuk menambahkan baris ke sub-tabel
    private static void addRow(PdfPTable table, String key, String value) {
        Font font = new Font(Font.HELVETICA, 10);
        PdfPCell cellKey = new PdfPCell(new Phrase(key, font));
        cellKey.setBorder(Rectangle.NO_BORDER);
        PdfPCell cellValue = new PdfPCell(new Phrase(value, font));
        cellValue.setBorder(Rectangle.NO_BORDER);
        table.addCell(cellKey);
        table.addCell(cellValue);
    }

    private static void addRow(PdfPTable table, String key, String value, float minHeight) {
        Font font = new Font(Font.HELVETICA, 12);
        PdfPCell cellKey = new PdfPCell(new Phrase(key, font));
        cellKey.setBorder(Rectangle.NO_BORDER);
        PdfPCell cellValue = new PdfPCell(new Phrase(value, font));
        cellValue.setBorder(Rectangle.NO_BORDER);
        cellValue.setMinimumHeight(minHeight); // <-- tinggi baris khusus
        table.addCell(cellKey);
        table.addCell(cellValue);
    }

}
