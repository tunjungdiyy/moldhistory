package com.anoman.machinehistory.controller;

import com.anoman.machinehistory.config.FooterPdfBuilder;
import com.anoman.machinehistory.config.PdfDirectoryConfig;
import com.anoman.machinehistory.model.problem.Problem;
import com.anoman.machinehistory.model.repair.RepairMold;
import com.anoman.machinehistory.service.repairmold.RepairMoldService;
import com.anoman.machinehistory.service.repairmold.RepairMoldServiceImpl;
import com.anoman.machinehistory.utility.ConvertionMilistoDate;
import com.anoman.machinehistory.utility.alert.AlertApp;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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

public class ReviewReportRepairMoldController {
    public HBox btnCanceled;
    public DatePicker tfDateFrom;
    public DatePicker tfDateTo;
    public Button btnFilter;
    public TableView<RepairMold> tableRepair;
    public TableColumn<RepairMold, String> columnName;
    public TableColumn<RepairMold, String> columnCvt;
    public TableColumn<RepairMold, String> columnPart;
    public TableColumn<RepairMold, String> columProblem;
    public TableColumn<RepairMold, String> columDateProblem;
    public TableColumn<RepairMold, String> columnRepair;
    public TableColumn<RepairMold, String> columnRepairDate;
    public TableColumn<RepairMold, String> columnTeamRepair;
    public TableColumn<RepairMold, String> columnDescriptionRepair;
    public Button btnPdf;
    public Button btnExcel;

    List<RepairMold> repairMoldList = new ArrayList<>();
    ObservableList<RepairMold> repairMoldObservableList;

    AlertApp alertApp = new AlertApp();
    RepairMoldService repairMoldService = new RepairMoldServiceImpl();

    public void initialize() {
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

    public void fncFilter(ActionEvent actionEvent) {
        if (tfDateFrom.getValue() == null || tfDateTo.getValue() == null) {
            alertApp.showAlert("error", "tanggal dari dan tanggal sampai tidak boleh kosong");
        } else {
            repairMoldList = repairMoldService.findByDate(ConvertionMilistoDate.LocalDateToMilis(tfDateFrom.getValue()), ConvertionMilistoDate.LocalDateToMilis(tfDateTo.getValue()));

            addDataTable(repairMoldList);
        }
    }

    public void fncPdf(ActionEvent actionEvent) throws FileNotFoundException {

        File pdfFile = PdfDirectoryConfig.getPdfFile("perbaikan-mold.pdf");

        Font normal = FontFactory.getFont(FontFactory.HELVETICA, 12);                  // normal
        Font bold   = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);             // tebal
        Font italic = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 12);          // miring
        Font big    = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
        document.open();

        writer.setPageEvent(new FooterPdfBuilder());

        Paragraph title = new Paragraph("DAFTAR PERBAIKAN MOLD", big);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        Paragraph subTitle = new Paragraph("Periode : " + String.valueOf(tfDateFrom.getValue()) + " s/d " + String.valueOf(tfDateTo.getValue()), normal);
        subTitle.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(subTitle);

        Paragraph space = new Paragraph("\n\n");
        document.add(space);

        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{4f, 1f, 3f, 3f, 3f, 4f, 3f, 2f, 4f});

        String[] tableTitle = {"MOLD", "CVT", "PART", "KERUSAKAN", "TANGGAL", "PERBAIKAN", "TGL PERBAIKAN", "TEAM", "KETERANGAN"};

        for (String s : tableTitle) {
            PdfPCell cell = new PdfPCell(new Paragraph(s, normal));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            table.addCell(cell);
        }

        for (RepairMold repairMold : repairMoldList) {
            table.addCell(repairMold.getProblem().getProductMold().getProduct().getCodeProduct() + " / " + repairMold.getProblem().getProductMold().getDescription());
            table.addCell(String.valueOf(repairMold.getProblem().getProductMold().getCvt()));
            table.addCell(repairMold.getProblem().getPart());
            table.addCell(repairMold.getProblem().getProblem());
            table.addCell(String.valueOf(ConvertionMilistoDate.milistoLocalDate(repairMold.getProblem().getProblemDate())));
            table.addCell(repairMold.getAction());
            table.addCell(String.valueOf(ConvertionMilistoDate.milistoLocalDate(repairMold.getReapirEnd()) + " (" + workDuration(repairMold.getRepairStart(), repairMold.getReapirEnd()) + ")"));
            table.addCell(repairMold.getTeamRepair());
            table.addCell(repairMold.getNoteRepair());

        }

        Paragraph count = new Paragraph("Jumlah Perbaikan : " + repairMoldList.size());
        count.setAlignment(Paragraph.ALIGN_LEFT);

        document.add(table);
        document.add(count);
        document.close();

        alertApp.showAlert("info", "Berhasil Exporr PDF");

    }

    public void fncExcel(ActionEvent actionEvent) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("daftar-perbaikan.mold");

        setTitle(sheet, workbook);
        // ======= Style Header =======
        CellStyle headerStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        org.apache.poi.ss.usermodel.Row header = sheet.createRow(3);
        String[] columns = {"MOLD", "CVT", "PART", "KERUSAKAN", "TANGGAL", "PERBAIKAN", "TGL PERBAIKAN", "TEAM", "KETERANGAN"};
        for (int i = 0; i < columns.length; i++) {
            org.apache.poi.ss.usermodel.Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowIndext = 4;
        for (RepairMold repairMold : repairMoldList) {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIndext++);
            row.createCell(0).setCellValue(repairMold.getProblem().getProductMold().getProduct().getCodeProduct() + " / " + repairMold.getProblem().getProductMold().getDescription());
            row.createCell(1).setCellValue(String.valueOf(repairMold.getProblem().getProductMold().getCvt()));
            row.createCell(2).setCellValue(repairMold.getProblem().getPart());
            row.createCell(3).setCellValue(repairMold.getProblem().getProblem());
            row.createCell(4).setCellValue(String.valueOf(ConvertionMilistoDate.milistoLocalDate(repairMold.getProblem().getProblemDate())));
            row.createCell(5).setCellValue(repairMold.getAction());
            row.createCell(6).setCellValue(String.valueOf(ConvertionMilistoDate.milistoLocalDate(repairMold.getReapirEnd()) + " (" + workDuration(repairMold.getRepairStart(), repairMold.getReapirEnd()) + ")"));
            row.createCell(7).setCellValue(repairMold.getTeamRepair());
            row.createCell(8).setCellValue(repairMold.getNoteRepair());
        }

        // ======= Auto-size kolom =======
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // set count data table
        countDataTable(sheet, repairMoldList, workbook);

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

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    void addDataTable(List<RepairMold> list) {
        columnName.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getProblem().getProductMold().getProduct().getName() + " / " + param.getValue().getProblem().getProductMold().getDescription()));
        columnCvt.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(String.valueOf(param.getValue().getProblem().getProductMold().getCvt())));
        columnPart.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getProblem().getPart()));
        columProblem.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getProblem().getProblem()));
        columDateProblem.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(String.valueOf(ConvertionMilistoDate.milistoLocalDate(param.getValue().getProblem().getProblemDate()))));
        columnRepair.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getAction()));
        columnRepairDate.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(String.valueOf(ConvertionMilistoDate.milistoLocalDate(param.getValue().getReapirEnd()) + " ( " + String.valueOf(workDuration(param.getValue().getRepairStart(), param.getValue().getReapirEnd()) + " )"))));
        columnTeamRepair.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getTeamRepair()));
        columnDescriptionRepair.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().getNoteRepair()));

        repairMoldObservableList = FXCollections.observableList(list);

        tableRepair.setItems(repairMoldObservableList);
    }

    int workDuration(Long start, Long end) {

        long value = (end - start) / (1000 * 60 * 60 * 24);

        if(value < 1) {
            return 1;
        } else {
            return (int) value;
        }

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

    void countDataTable(Sheet sheet, List<RepairMold> list, Workbook workbook) {
        // ambil baris setelah data terakhir
        int lastRow = sheet.getLastRowNum() + 2; // +2 biar ada jarak

        org.apache.poi.ss.usermodel.Row footerRow = sheet.createRow(lastRow);
        org.apache.poi.ss.usermodel.Cell footerCell = footerRow.createCell(0);
        footerCell.setCellValue("Total Perbaikan Mold: " + list.size());

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
        org.apache.poi.ss.usermodel.Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("DAFTAR PERBAIKAN MOLD");

        // Style judul
        CellStyle titleStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16); // lebih besar
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleCell.setCellStyle(titleStyle);

        org.apache.poi.ss.usermodel.Row subTitleRow = sheet.createRow(1);
        org.apache.poi.ss.usermodel.Cell subTitleCell = subTitleRow.createCell(0);
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
                8  // kolom akhir (sesuaikan jumlah kolom)
        ));

        // Tambahkan sedikit jarak: header mulai di baris ke-2
        org.apache.poi.ss.usermodel.Row header = sheet.createRow(2);
    }
}
