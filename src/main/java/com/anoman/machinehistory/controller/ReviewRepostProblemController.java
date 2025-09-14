package com.anoman.machinehistory.controller;

import com.anoman.machinehistory.config.FooterPdfBuilder;
import com.anoman.machinehistory.config.PdfDirectoryConfig;
import com.anoman.machinehistory.model.mold.ProductMold;
import com.anoman.machinehistory.model.mold.ProductMoldReview;
import com.anoman.machinehistory.model.problem.Problem;
import com.anoman.machinehistory.service.problem.ProblemService;
import com.anoman.machinehistory.service.problem.ProblemServiceImpl;
import com.anoman.machinehistory.service.productmold.ProductMoldService;
import com.anoman.machinehistory.service.productmold.ProductMoldServiceImpl;
import com.anoman.machinehistory.utility.ConvertionMilistoDate;
import com.anoman.machinehistory.utility.alert.AlertApp;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReviewRepostProblemController {
    public HBox btnCanceled;
    public TableView<Problem> TableProblem;
    public TableColumn<Problem, String> columnName;
    public TableColumn<Problem, String> columSerialNumber;
    public TableColumn<Problem, String> columnPart;
    public TableColumn<Problem, String> columProblem;
    public TableColumn<Problem, String> columDate;
    public TableColumn<Problem, String> columnDescription;
    public Button btnPdf;
    public Button btnExcel;
    public ComboBox tfFilterType;
    public ComboBox tfStatus;
    public TextField tfMold;
    public DatePicker tfDateFrom;
    public DatePicker tfDateTo;
    public TableColumn<Problem, String> columnCvt;

    // for drop down
    ListView<ProductMold> productMoldListView = new ListView<>();
    PopupControl popupControl = new PopupControl();
    ProductMoldService productMoldService = new ProductMoldServiceImpl();

    //for table
    List<Problem> problemList = new ArrayList<>();
    ObservableList<Problem> problemObservableList;
    ProblemService problemService = new ProblemServiceImpl();

    AlertApp alertApp = new AlertApp();

    public void initialize() {
        setEnabled();

        setDataCmBoxType();

        productMoldListView.setOnMouseClicked(event -> {
            ProductMold selected = productMoldListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                tfMold.setText(selected.getProduct().getName() + ":" + selected.getCode());

                popupControl.hide();

            }
        });

        tfDateFrom.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate object) {
                return (object != null) ? formatter.format(object) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty()) ? LocalDate.parse(string, formatter) : null;
            }
        });

        tfDateTo.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate object) {
                return (object != null) ? formatter.format(object) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty()) ? LocalDate.parse(string, formatter) : null;
            }
        });
    }

    public void fncCanceled(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnCanceled.getScene().getWindow();
        stage.close();
    }

    public void fncPdf(ActionEvent actionEvent) throws FileNotFoundException {

        File pdfFile = PdfDirectoryConfig.getPdfFile("daftar-masalah-mold.pdf");

        Font normal = FontFactory.getFont(FontFactory.HELVETICA, 12);                  // normal
        Font bold   = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);             // tebal
        Font italic = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 12);          // miring
        Font big    = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
        document.open();

        writer.setPageEvent(new FooterPdfBuilder());

        Paragraph title = new Paragraph("DAFTAR KERUSAKAN MOLD", big);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        Paragraph subTite = new Paragraph("Periode : " + String.valueOf(tfDateFrom.getValue()) + " s/d " + String.valueOf(tfDateTo.getValue()), normal);
        subTite.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(subTite);

        Paragraph space = new Paragraph("\n\n");
        document.add(space);

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{4f, 1f, 2f, 3f, 3f, 2f, 4f});

        String[] tableTitle = {"MOLD", "CVT", "NO SERI", "PART", "KERUSAKAN", "TANGGAL", "KETERANGAN"};

        for (String s : tableTitle) {
            PdfPCell cell = new PdfPCell(new Paragraph(s, normal));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            table.addCell(cell);
        }

        for (Problem problem : problemList) {
            table.addCell(problem.getProductMold().getProduct().getName() + " / " + problem.getProductMold().getDescription());
            table.addCell(String.valueOf(problem.getProductMold().getCvt()));
            table.addCell(problem.getProductMold().getMold().getSerialNumber());
            table.addCell(problem.getPart());
            table.addCell(problem.getProblem());
            table.addCell(String.valueOf(ConvertionMilistoDate.milistoLocalDate(problem.getProblemDate())));
            table.addCell(problem.getDescriptionProblem());
        }

        Paragraph count = new Paragraph("Jumlah Kerusakan Mold : " + String.valueOf(problemList.size()));
        count.setAlignment(Paragraph.ALIGN_LEFT);

        document.add(table);
        document.add(count);
        document.close();

        alertApp.showAlert("info", "Berhasil Export Data");

    }

    public void fncExcel(ActionEvent actionEvent) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("daftar-kerusakan.mold");

        setTitle(sheet, workbook);
        // ======= Style Header =======
        CellStyle headerStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        Row header = sheet.createRow(3);
        String[] columns = {"MOLD", "CVT", "NO SERI", "PART", "KERUSAKAN", "TANGGAL", "KETERANGAN"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowIndext = 4;
        for (Problem problem : problemList) {
            Row row = sheet.createRow(rowIndext++);
            row.createCell(0).setCellValue(problem.getProductMold().getProduct().getName() + " / " + problem.getProductMold().getDescription());
            row.createCell(1).setCellValue(problem.getProductMold().getCvt());
            row.createCell(2).setCellValue(problem.getProductMold().getMold().getSerialNumber());
            row.createCell(3).setCellValue(problem.getPart());
            row.createCell(4).setCellValue(problem.getProblem());
            row.createCell(5).setCellValue(ConvertionMilistoDate.milistoLocalDate(problem.getProblemDate()).toString());
            row.createCell(6).setCellValue(problem.getDescriptionProblem());
        }

        // ======= Auto-size kolom =======
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // set count data table
        countDataTable(sheet, problemList, workbook);

        // date export
        setFooterdateExport(sheet, workbook);

        // ======= Tentukan direktori tujuan =======
        File fileexcel = PdfDirectoryConfig.getXlxs("daftar-kerusakan-mold.xlsx");

        try (FileOutputStream fos = new FileOutputStream(fileexcel)) {
            workbook.write(fos);
            System.out.println("File berhasil disimpan di: " + fileexcel);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        workbook.close();

        alertApp.showAlert("info", "berhasil expor excel");

    }

    public void fncTypeFilter(ActionEvent actionEvent) {
        if (tfFilterType.getValue().equals("MOLD")) {
            setEnabled();
            tfMold.setVisible(true);
            tfDateFrom.setVisible(true);
            tfDateFrom.setValue(null);
            tfDateTo.setVisible(true);
            tfDateTo.setValue(null);
        } else if(tfFilterType.getValue().equals("STATUS")) {
            setEnabled();
            tfStatus.setVisible(true);
            setDataCmBoxStatus();
            tfDateFrom.setVisible(true);
            tfDateFrom.setValue(null);
            tfDateTo.setVisible(true);
            tfDateTo.setValue(null);
        }
    }

    public void fncSearch(MouseEvent mouseEvent) {

       if(tfFilterType.getValue().equals("MOLD")) {
            if (tfDateTo.getValue() == null || tfDateFrom.getValue() == null) {
                alertApp.showAlert("error", "Tanggal dari dan Tangal sampai Tidak Boleh Kosong !!");
            } else {

                String[] array = tfMold.getText().split(":");
                problemList = problemService.findByCodeProductMoldAndDate(array[1], ConvertionMilistoDate.LocalDateToMilis(tfDateFrom.getValue()), ConvertionMilistoDate.LocalDateToMilis(tfDateTo.getValue()));

                addDataTable(problemList);

            }


        } else if (tfFilterType.getValue().equals("STATUS")) {
            if (tfDateTo.getValue() == null || tfDateFrom.getValue() == null) {
                alertApp.showAlert("error", "Tanggal dari dan Tangal sampai Tidak Boleh Kosong !!");
            } else {
                if (tfStatus.getValue().equals("SEMUA")) {
                    problemList = problemService.findByDate(ConvertionMilistoDate.LocalDateToMilis(tfDateFrom.getValue()), ConvertionMilistoDate.LocalDateToMilis(tfDateTo.getValue()));

                    addDataTable(problemList);
                } else {
                    problemList = problemService.findByStatusAndDate(tfStatus.getValue().toString(), ConvertionMilistoDate.LocalDateToMilis(tfDateFrom.getValue()), ConvertionMilistoDate.LocalDateToMilis(tfDateTo.getValue()));

                    addDataTable(problemList);
                }
            }

        }

    }

    void setEnabled() {
        tfStatus.setVisible(false);
        tfMold.setVisible(false);
        tfDateFrom.setVisible(false);
        tfDateTo.setVisible(false);
    }

    void setDataCmBoxType() {
        List<String> stringList = new ArrayList<>();
        stringList.add("MOLD");
        stringList.add("STATUS");

        ObservableList<String> stringObservableList = FXCollections.observableList(stringList);

        tfFilterType.setItems(stringObservableList);
    }

    void setDataCmBoxStatus() {
        List<String> stringList = new ArrayList<>();
        stringList.add("SEMUA");
        stringList.add("BELUM PROSES");
        stringList.add("SELESAI");

        ObservableList<String> stringObservableList = FXCollections.observableList(stringList);

        tfStatus.setItems(stringObservableList);
    }

    public void fncSerachMold(KeyEvent event) {
        popupControl.setAutoHide(true);
        popupControl.setAutoFix(true);
        popupControl.getScene().setRoot(productMoldListView);

        String input = tfMold.getText().toUpperCase();

        if (input.isEmpty()) {
            popupControl.hide();
            tfMold.setText("");
//                clearCheckboxes();
            return;
        }

        List<ProductMold> filtered = productMoldService.findByKeyword(tfMold.getText().toUpperCase());

        if (filtered.isEmpty()) {
            popupControl.hide();
            return;
        }

        productMoldListView.getItems().setAll(filtered);
        productMoldListView.setPrefHeight(Math.min(150, filtered.size()) * 30);

        if (!popupControl.isShowing()) {
            Bounds bounds = tfMold.localToScreen(tfMold.getBoundsInLocal());
            popupControl.show(tfMold, bounds.getMinX(), bounds.getMaxY());
        }

        productMoldListView.setCellFactory(productUpdateAndReadListView -> new ListCell<>() {
            @Override
            protected void updateItem(ProductMold response, boolean empty) {
                super.updateItem(response, empty);
                setText(empty || response == null ? null : response.getProduct().getName() + "-" + response.getDescription());
            }
        });
    }

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    void addDataTable(List<Problem> list) {
        columnName.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getProductMold().getProduct().getName() + "-" + param.getValue().getProductMold().getDescription()));
        columnCvt.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(String.valueOf(param.getValue().getProductMold().getCvt())));
        columSerialNumber.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getProductMold().getMold().getSerialNumber()));
        columnPart.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getPart()));
        columProblem.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getProblem()));
        columDate.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(String.valueOf(ConvertionMilistoDate.milistoLocalDate(param.getValue().getProblemDate()))));
        columnDescription.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getDescriptionProblem()));

        problemObservableList = FXCollections.observableList(list);

        TableProblem.setItems(problemObservableList);

    }
    void setFooterdateExport(Sheet sheet, Workbook workbook) {
        // ambil baris setelah data terakhir
        int lastRow = sheet.getLastRowNum() + 2; // kasih jarak 1 baris
        org.apache.poi.ss.usermodel.Row footerRow = sheet.createRow(lastRow);

        Cell footerCell = footerRow.createCell(0);
        String waktuExport = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        footerCell.setCellValue("Di Export: " + waktuExport);

        // merge biar jadi 1 cell panjang
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(
                lastRow, lastRow, 0, 2
        ));

        // style biar mirip catatan kaki
        CellStyle footerStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font footerFont = workbook.createFont();
        footerFont.setItalic(true);
        footerFont.setFontHeightInPoints((short) 10);
        footerStyle.setFont(footerFont);
        footerCell.setCellStyle(footerStyle);
    }

    void countDataTable(Sheet sheet, List<Problem> list, Workbook workbook) {
        // ambil baris setelah data terakhir
        int lastRow = sheet.getLastRowNum() + 2; // +2 biar ada jarak

        org.apache.poi.ss.usermodel.Row footerRow = sheet.createRow(lastRow);
        Cell footerCell = footerRow.createCell(0);
        footerCell.setCellValue("Total Kerusakan Mold: " + list.size());

        // Style footer
        CellStyle footerStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font footerFont = workbook.createFont();
        footerFont.setBold(true);
        footerFont.setFontHeightInPoints((short) 12);
        footerStyle.setFont(footerFont);
        footerStyle.setAlignment(HorizontalAlignment.RIGHT);

        // merge cell biar rata kanan ke 3 kolom
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(
                lastRow, lastRow, 0, 2
        ));
        footerCell.setCellStyle(footerStyle);
    }

    void setTitle(Sheet sheet, Workbook workbook) {
        // ======= Judul di atas tabel =======
        org.apache.poi.ss.usermodel.Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("DAFTAR KERUSAKAN MOLD");

        // Style judul
        CellStyle titleStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16); // lebih besar
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleCell.setCellStyle(titleStyle);

        Row subTitleRow = sheet.createRow(1);
        Cell subTitleCell = subTitleRow.createCell(0);
        subTitleCell.setCellValue("Periode : " + tfDateFrom.getValue().toString() + " s/d " + tfDateTo.getValue().toString());

        CellStyle subtitleStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font subtitleFont = workbook.createFont();
        subtitleFont.setBold(false);
        subtitleFont.setFontHeightInPoints((short) 11); // lebih besar
        subtitleStyle.setFont(subtitleFont);
        subtitleStyle.setAlignment(HorizontalAlignment.CENTER);
        subTitleCell.setCellStyle(subtitleStyle);

        // merge cell judul biar rata tengah di atas semua kolom
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(
                0, // baris awal
                0, // baris akhir
                0, // kolom awal
                6  // kolom akhir (sesuaikan jumlah kolom)
        ));

        // Tambahkan sedikit jarak: header mulai di baris ke-2
        Row header = sheet.createRow(2);
    }
}
