package com.anoman.machinehistory.config;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.lowagie.text.pdf.PdfPageEventHelper;

public class FooterPdfBuilder extends PdfPageEventHelper {

    private  final String exportTime;

    public  FooterPdfBuilder() {
        // Format timestamp sekali saat objek dibuat
        this.exportTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        Phrase footer = new Phrase("Di Export: " + exportTime,
                new Font(Font.HELVETICA, 12, Font.ITALIC, new java.awt.Color(100, 100, 100)));

        ColumnText.showTextAligned(
                cb,
                Element.ALIGN_RIGHT,   // rata kanan
                footer,
                document.right() - document.rightMargin(), // posisi X
                document.bottom() - 10, // posisi Y (di bawah margin bawah)
                0
        );
    }

}
