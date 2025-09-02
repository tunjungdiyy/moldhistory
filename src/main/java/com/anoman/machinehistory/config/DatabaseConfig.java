package com.anoman.machinehistory.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {

    private static String url;
    private static String user;
    private static String password;
    private static String driver;

    static {
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("dbconnection.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                throw new RuntimeException("db.properties tidak ditemukan di resources!");
            }
            prop.load(input);

            url = prop.getProperty("db.url");
            user = prop.getProperty("db.user");
            password = prop.getProperty("db.password");
            driver = prop.getProperty("db.driver");

            // Load JDBC driver
            Class.forName(driver);

        } catch (Exception e) {
            throw new RuntimeException("Gagal load konfigurasi database", e);
        }
    }


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
