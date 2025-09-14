package com.anoman.machinehistory.controller;

import com.anoman.machinehistory.config.FooterPdfBuilder;
import com.anoman.machinehistory.config.PdfDirectoryConfig;
import com.anoman.machinehistory.model.mold.ProductMold;
import com.anoman.machinehistory.model.produk.ProductUpdateAndRead;
import com.anoman.machinehistory.service.product.ProductService;
import com.anoman.machinehistory.service.product.ProductServiceImpl;
import com.anoman.machinehistory.utility.alert.AlertApp;
import com.lowagie.text.*;
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
import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReviewProductController {
    public HBox btnCanceled;
    public TableView<ProductUpdateAndRead> tableProduct;
    public TableColumn<ProductUpdateAndRead, String> columnName;
    public TableColumn<ProductUpdateAndRead, String> columnDescription;
    public Button btnPdf;
    public Button btnExcel;

    List<ProductUpdateAndRead> productList = new ArrayList<>();
    ProductService productService = new ProductServiceImpl();
    ObservableList<ProductUpdateAndRead> productObservableList;

    AlertApp alertApp = new AlertApp();

    public void initialize() {
        productList = productService.findAll();

        addDataTableProduct(productList);
    }

    public void fncCanceled(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnCanceled.getScene().getWindow();
        stage.close();
    }

    public void fncPdf(ActionEvent actionEvent) {

        try {
            File pdfFile = PdfDirectoryConfig.getPdfFile("daftar-product.pdf");

            Font normal = FontFactory.getFont(FontFactory.HELVETICA, 12);                  // normal
            Font bold   = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);             // tebal
            Font italic = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 12);          // miring
            Font big    = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);

            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();

            writer.setPageEvent(new FooterPdfBuilder());

            Paragraph title = new Paragraph("DAFTAR PRODUK", big);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);

            Paragraph space = new Paragraph("\n\n");
            document.add(space);

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);

            String[] tableTitle = {"NAMA PRODUK", "KETERANGAN"};

            for (String s : tableTitle) {
                PdfPCell cell = new PdfPCell(new Paragraph(s, normal));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                table.addCell(cell);
            }

            for (ProductUpdateAndRead productUpdateAndRead : productList) {
                table.addCell(productUpdateAndRead.getName());
                table.addCell(productUpdateAndRead.getDescription());
            }

            Paragraph count = new Paragraph("Jumlah Produk : " + String.valueOf(productList.size()), normal);
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
        Sheet sheet = workbook.createSheet("daftar-produk");

        setTitle(sheet, workbook);

        CellStyle headerStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        org.apache.poi.ss.usermodel.Row header = sheet.createRow(3);
        String[] columns = {"NAMA PRODUK", "KETERANGAN"};;
        for (int i = 0; i < columns.length; i++) {
            org.apache.poi.ss.usermodel.Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowIdx = 4;
        for (ProductUpdateAndRead productUpdateAndRead : productList) {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(productUpdateAndRead.getName());
            row.createCell(1).setCellValue(productUpdateAndRead.getDescription());
        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        countDataTable(sheet, productList, workbook);

        setFooterdateExport(sheet, workbook);

        File fileExcel = PdfDirectoryConfig.getXlxs("daftar-product.xlsx");

        try (FileOutputStream fos = new FileOutputStream(fileExcel)){
            workbook.write(fos);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        workbook.close();

        alertApp.showAlert("info", "Berhasil export excel");
    }

    void addDataTableProduct(List<ProductUpdateAndRead> list) {
        columnName.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getName()));
        columnDescription.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getDescription()));

        productObservableList = FXCollections.observableList(list);

        tableProduct.setItems(productObservableList);


    }

    void setFooterdateExport(Sheet sheet, Workbook workbook) {
        // ambil baris setelah data terakhir
        int lastRow = sheet.getLastRowNum() + 2; // kasih jarak 1 baris
        org.apache.poi.ss.usermodel.Row footerRow = sheet.createRow(lastRow);

        org.apache.poi.ss.usermodel.Cell footerCell = footerRow.createCell(0);
        String waktuExport = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        footerCell.setCellValue("Di Export: " + waktuExport);

        // merge biar jadi 1 cell panjang
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(
                lastRow, lastRow, 0, 1
        ));

        // style biar mirip catatan kaki
        CellStyle footerStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font footerFont = workbook.createFont();
        footerFont.setItalic(true);
        footerFont.setFontHeightInPoints((short) 10);
        footerStyle.setFont(footerFont);
        footerCell.setCellStyle(footerStyle);
    }

    void countDataTable(Sheet sheet, List<ProductUpdateAndRead> list, Workbook workbook) {
        // ambil baris setelah data terakhir
        int lastRow = sheet.getLastRowNum() + 2; // +2 biar ada jarak

        org.apache.poi.ss.usermodel.Row footerRow = sheet.createRow(lastRow);
        org.apache.poi.ss.usermodel.Cell footerCell = footerRow.createCell(0);
        footerCell.setCellValue("Total Produk : " + list.size());

        // Style footer
        CellStyle footerStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font footerFont = workbook.createFont();
        footerFont.setBold(true);
        footerFont.setFontHeightInPoints((short) 12);
        footerStyle.setFont(footerFont);
        footerStyle.setAlignment(HorizontalAlignment.CENTER);

        // merge cell biar rata kanan ke 3 kolom
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(
                lastRow, lastRow, 0, 1
        ));
        footerCell.setCellStyle(footerStyle);
    }

    void setTitle(Sheet sheet, Workbook workbook) {
        // ======= Judul di atas tabel =======
        org.apache.poi.ss.usermodel.Row titleRow = sheet.createRow(0);
        org.apache.poi.ss.usermodel.Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("DAFTAR PRODUK");

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
                1  // kolom akhir (sesuaikan jumlah kolom)
        ));

        // Tambahkan sedikit jarak: header mulai di baris ke-2
        org.apache.poi.ss.usermodel.Row header = sheet.createRow(2);
    }
}
