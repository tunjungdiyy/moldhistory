package com.anoman.machinehistory.controller;

import com.anoman.machinehistory.config.FooterPdfBuilder;
import com.anoman.machinehistory.config.PdfDirectoryConfig;
import com.anoman.machinehistory.model.mold.ProductMold;
import com.anoman.machinehistory.service.productmold.ProductMoldService;
import com.anoman.machinehistory.service.productmold.ProductMoldServiceImpl;
import com.anoman.machinehistory.utility.alert.AlertApp;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Arrays;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ReviewReportMoldController {
    public HBox btnCanceled;
    public TableView<ProductMold> tableMold;
    public TableColumn<ProductMold, String> columnName;
    public TableColumn<ProductMold, String> columSerialNumber;
    public TableColumn<ProductMold, String> columnCvt;
    public TableColumn<ProductMold, String> columnVertical;
    public TableColumn<ProductMold, String> columnHorizontal;
    public TableColumn<ProductMold, String> columnThickness;
    public TableColumn<ProductMold, String> columnDescription;
    public Button btnPdf;
    public Button btnExcel;

    List<ProductMold> productMoldList = new ArrayList<>();
    ProductMoldService productMoldService = new ProductMoldServiceImpl();
    ObservableList<ProductMold> productMoldObservableList;

    AlertApp alertApp = new AlertApp();

    public void initialize() {
        productMoldList = productMoldService.findAll();

        addDataTableProductMold(productMoldList);
    }

    public void fncCanceled(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnCanceled.getScene().getWindow();
        stage.close();
    }

    public void fncPdf(ActionEvent actionEvent) {
        try {

            File pdfFile = PdfDirectoryConfig.getPdfFile("daftar-mold.pdf");

            Font normal = FontFactory.getFont(FontFactory.HELVETICA, 12);                  // normal
            Font bold   = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);             // tebal
            Font italic = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 12);          // miring
            Font big    = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);

            Document document = new Document(PageSize.A4.rotate());
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();

            writer.setPageEvent(new FooterPdfBuilder());

            Paragraph title = new Paragraph("DAFTAR MOLD", big);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);

            Paragraph space = new Paragraph("\n\n");
            document.add(space);

            // Buat tabel 4 kolom
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3f, 2f, 1f, 2f, 2f, 2f, 3f});

            String[] titleTable = {"MOLD", "NO SERI", "CVT", "VERTICAL", "HORIZONTAL", "THICKNESS", "KETERANGAN"};

            for (String s : titleTable) {
                PdfPCell cell = new PdfPCell(new Paragraph(s, normal));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); // horizontal: LEFT, CENTER, RIGHT
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                table.addCell(cell);
            }

            for (ProductMold productMold : productMoldList) {
                table.addCell(productMold.getProduct().getName() + " / " + productMold.getDescription());
                table.addCell(productMold.getMold().getSerialNumber());
                table.addCell(String.valueOf(productMold.getCvt()));
                table.addCell(String.valueOf(productMold.getMold().getVertical()));
                table.addCell(String.valueOf(productMold.getMold().getHorizontal()));
                table.addCell(String.valueOf(productMold.getMold().getThickness()));
                table.addCell(productMold.getDescription());
            }

            Paragraph count = new Paragraph("Jumlah Mold : " + String.valueOf(productMoldList.size()), normal);
            count.setAlignment(Paragraph.ALIGN_LEFT);

            document.add(table);
            document.add(count);
            document.close();

            alertApp.showAlert("info", "Berhasil Export PDF");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void fncExcel(ActionEvent actionEvent) throws IOException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("daftar-mold");

        setTitle(sheet, workbook);

        // ======= Style Header =======
        CellStyle headerStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        // ======= Header =======
        Row header = sheet.createRow(3);
        String[] columns = {"MOLD", "NO SERI", "CVT", "VERTICAL", "HORIZONTAL", "THICKNESS", "KETERANGAN"};;
        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        // ======= Isi Data =======
        int rowIdx = 4;
        for (ProductMold productMold : productMoldList) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(productMold.getProduct().getName() + " / " + productMold.getDescription());
            row.createCell(1).setCellValue(productMold.getMold().getSerialNumber());
            row.createCell(2).setCellValue(productMold.getCvt());
            row.createCell(3).setCellValue(productMold.getMold().getVertical());
            row.createCell(4).setCellValue(productMold.getMold().getHorizontal());
            row.createCell(5).setCellValue(productMold.getMold().getThickness());
            row.createCell(6).setCellValue(productMold.getDescription());
        }

        // ======= Auto-size kolom =======
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // set count data table
        countDataTable(sheet, productMoldList, workbook);

        // date export
        setFooterdateExport(sheet, workbook);

        // ======= Tentukan direktori tujuan =======
        File fileexcel = PdfDirectoryConfig.getXlxs("daftar-mold.xlsx");

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


    void addDataTableProductMold(List<ProductMold> list) {
        columnName.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getProduct().getName()));
        columSerialNumber.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getMold().getSerialNumber()));
        columnCvt.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(String.valueOf(param.getValue().getCvt())));
        columnVertical.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(String.valueOf(param.getValue().getMold().getVertical())));
        columnHorizontal.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(String.valueOf(param.getValue().getMold().getHorizontal())));
        columnThickness.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(String.valueOf(param.getValue().getMold().getThickness())));
        columnDescription.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getDescription()));

        productMoldObservableList = FXCollections.observableList(list);

        tableMold.setItems(productMoldObservableList);
    }

    void setFooterdateExport(Sheet sheet, Workbook workbook) {
        // ambil baris setelah data terakhir
        int lastRow = sheet.getLastRowNum() + 2; // kasih jarak 1 baris
        Row footerRow = sheet.createRow(lastRow);

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

    void countDataTable(Sheet sheet, List<ProductMold> list, Workbook workbook) {
        // ambil baris setelah data terakhir
        int lastRow = sheet.getLastRowNum() + 2; // +2 biar ada jarak

        Row footerRow = sheet.createRow(lastRow);
        Cell footerCell = footerRow.createCell(0);
        footerCell.setCellValue("Total Mold: " + list.size());

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
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("DAFTAR MOLD");

        // Style judul
        CellStyle titleStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16); // lebih besar
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleCell.setCellStyle(titleStyle);

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
