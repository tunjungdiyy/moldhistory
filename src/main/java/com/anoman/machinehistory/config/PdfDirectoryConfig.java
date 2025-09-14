package com.anoman.machinehistory.config;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PdfDirectoryConfig {

    private static final String DIRECTORY_NAME = "mechine";

    /**
     * Ambil direktori default untuk penyimpanan PDF
     */
    public static File getPdfDirectory() {
        String userHome = System.getProperty("user.home");
        File dir = new File(userHome, DIRECTORY_NAME);

        if (!dir.exists()) {
            dir.mkdirs(); // buat folder kalau belum ada
        }

        return dir;
    }

    /**
     * Tambahkan timestamp pada nama file
     */
    private static String addTimestamp(String filename) {
        String baseName = filename.replace(".pdf", "");
        String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        return baseName + "-" + timestamp + ".pdf";
    }

    private static String addTimestampXlxs(String filename) {
        String baseName = filename.replace(".xlsx", "");
        String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        return baseName + "-" + timestamp + ".xlsx";
    }

    /**
     * Ambil file PDF dengan nama + timestamp di folder mechine
     */
    public static File getPdfFile(String filename) {
        return new File(getPdfDirectory(), addTimestamp(filename));
    }

    public static File getXlxs(String filename) {
        return new File(getPdfDirectory(), addTimestampXlxs(filename));
    }

}
