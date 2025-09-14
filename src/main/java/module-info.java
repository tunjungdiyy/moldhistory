module com.anoman.machinehistory {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires mysql.connector.j;       // MySQL driver
    requires org.apache.poi.ooxml;    // Apache POI (Excel)
    requires com.github.librepdf.openpdf; // OpenPDF
    requires org.slf4j;
    requires java.sql;
    requires static lombok;
    requires commons.math3;
    requires java.desktop;

    opens com.anoman.machinehistory to javafx.fxml;
    exports com.anoman.machinehistory;

    opens com.anoman.machinehistory.config to javafx.fxml;
    exports com.anoman.machinehistory.config;
    opens com.anoman.machinehistory.security to javafx.fxml;
    exports com.anoman.machinehistory.security;
    opens com.anoman.machinehistory.commons to javafx.fxml;
    exports com.anoman.machinehistory.commons;
    opens com.anoman.machinehistory.utility to javafx.fxml;
    exports com.anoman.machinehistory.utility;
    opens com.anoman.machinehistory.utility.alert to javafx.fxml;
    exports com.anoman.machinehistory.utility.alert;

    //model
    //1. user
    opens com.anoman.machinehistory.model.user to javafx.fxml;
    exports com.anoman.machinehistory.model.user;
    opens com.anoman.machinehistory.model.squence to javafx.fxml;
    exports com.anoman.machinehistory.model.squence;

    //repository
    opens com.anoman.machinehistory.repository.user to javafx.fxml;
    exports com.anoman.machinehistory.repository.user;
    opens com.anoman.machinehistory.repository.squence to javafx.fxml;
    exports com.anoman.machinehistory.repository.squence;

    //service
    opens com.anoman.machinehistory.service.user to javafx.fxml;
    exports com.anoman.machinehistory.service.user;
    opens com.anoman.machinehistory.service.squence to javafx.fxml;
    exports com.anoman.machinehistory.service.squence;

    //controller
    opens com.anoman.machinehistory.controller to javafx.fxml;
    exports com.anoman.machinehistory.controller;

}