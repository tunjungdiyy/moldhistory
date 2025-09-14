package com.anoman.machinehistory.config;

import javafx.scene.Node;
import javafx.print.*;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PrintConfig {

    public static void showPreview(Node node) {
        // Ambil snapshot node
        WritableImage snapshot = node.snapshot(new SnapshotParameters(), null);

        // ImageView untuk menampilkan preview
        ImageView imageView = new ImageView(snapshot);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(600); // supaya pas di layar

        // Tombol print
        Button btnPrint = new Button("Print");
        btnPrint.setOnAction(e -> printNode(node));

        // Layout
        HBox buttonBox = new HBox(10, btnPrint);
        BorderPane root = new BorderPane();
        root.setCenter(imageView);
        root.setBottom(buttonBox);

        // Window preview
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Print Preview");
        stage.setScene(new Scene(root, 650, 500));
        stage.show();
    }

    /**
     * Print Node menggunakan Printer bawaan JavaFX.
     */
    public static void printNode(Node node) {
        Printer printer = Printer.getDefaultPrinter();
        if (printer == null) {
            System.out.println("Tidak ada printer ditemukan.");
            return;
        }

        PrinterJob job = PrinterJob.createPrinterJob(printer);
        if (job != null && job.showPrintDialog(node.getScene().getWindow())) {
            PageLayout pageLayout = printer.createPageLayout(
                    Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT
            );

            boolean success = job.printPage(pageLayout, node);
            if (success) {
                job.endJob();
                System.out.println("Print berhasil 👍");
            }
        }
    }
}
