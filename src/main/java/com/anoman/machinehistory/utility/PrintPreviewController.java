package com.anoman.machinehistory.utility;

import javafx.geometry.Pos;
import javafx.print.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.Setter;

public class PrintPreviewController {
    public HBox btnCanceled;
    public ImageView printViewpane;
    public Button btnprint;

    @Setter
    public Node node;

    public void initialize(Node node) {

        this.node = node;

        WritableImage snapshot = node.snapshot(new SnapshotParameters(), null);

        printViewpane.setImage(snapshot);
    }

    public void fncExit(MouseEvent mouseEvent) {

        Stage stage = (Stage) btnCanceled.getScene().getWindow();
        stage.close();

    }

    public void fncPrint(MouseEvent mouseEvent) {


        Printer printer = Printer.getDefaultPrinter();
        if (printer == null) {
            System.out.println("Tidak ada printer ditemukan.");
            return;
        }

        double margin = 5 * 72 / 25.4;

        PrinterJob job = PrinterJob.createPrinterJob(printer);
        if (job != null && job.showPrintDialog(node.getScene().getWindow())) {
            PageLayout pageLayout = printer.createPageLayout(
                    Paper.A5, PageOrientation.LANDSCAPE,
                    margin,
                    margin,
                    margin,
                    margin
            );


            double scaleX = pageLayout.getPrintableWidth() / node.getBoundsInParent().getWidth();
            double scaleY = pageLayout.getPrintableHeight() / node.getBoundsInParent().getHeight();
            double scale = Math.min(scaleX, scaleY); // supaya tidak keluar batas

            node.setScaleX(scale);
            node.setScaleY(scale);

            // Hitung posisi agar pas di tengah
            double x = (pageLayout.getPrintableWidth() - node.getBoundsInParent().getWidth() * scale) / 2;
            double y = (pageLayout.getPrintableHeight() - node.getBoundsInParent().getHeight() * scale) / 2;

            Group centered = new Group(node);
            centered.setTranslateX(x);
            centered.setTranslateY(y);

            boolean success = job.printPage(pageLayout, centered);
            if (success) {
                job.endJob();
            }
        }

        Stage stage = (Stage) btnCanceled.getScene().getWindow();
        stage.close();
    }

}
