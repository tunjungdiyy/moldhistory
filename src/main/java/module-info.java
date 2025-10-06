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
    opens com.anoman.machinehistory.shared to javafx.fxml;
    exports com.anoman.machinehistory.shared;

    //model
    //1. user
    opens com.anoman.machinehistory.model.user to javafx.fxml;
    exports com.anoman.machinehistory.model.user;
    opens com.anoman.machinehistory.model.squence to javafx.fxml;
    exports com.anoman.machinehistory.model.squence;
    opens com.anoman.machinehistory.model.mold to javafx.fxml;
    exports com.anoman.machinehistory.model.mold;
    opens com.anoman.machinehistory.model.problem to javafx.fxml;
    exports com.anoman.machinehistory.model.problem;
    opens com.anoman.machinehistory.model.produk to javafx.fxml;
    exports com.anoman.machinehistory.model.produk;
    opens com.anoman.machinehistory.model.repair to javafx.fxml;
    exports com.anoman.machinehistory.model.repair;

    //repository
    opens com.anoman.machinehistory.repository.user to javafx.fxml;
    exports com.anoman.machinehistory.repository.user;
    opens com.anoman.machinehistory.repository.squence to javafx.fxml;
    exports com.anoman.machinehistory.repository.squence;
    opens com.anoman.machinehistory.repository.mold to javafx.fxml;
    exports com.anoman.machinehistory.repository.mold;
    opens com.anoman.machinehistory.repository.productmold to javafx.fxml;
    exports com.anoman.machinehistory.repository.productmold;
    opens com.anoman.machinehistory.repository.problem to javafx.fxml;
    exports com.anoman.machinehistory.repository.problem;
    opens com.anoman.machinehistory.repository.product to javafx.fxml;
    exports com.anoman.machinehistory.repository.product;
    opens com.anoman.machinehistory.repository.repair to javafx.fxml;
    exports com.anoman.machinehistory.repository.repair;

    //service
    opens com.anoman.machinehistory.service.user to javafx.fxml;
    exports com.anoman.machinehistory.service.user;
    opens com.anoman.machinehistory.service.squence to javafx.fxml;
    exports com.anoman.machinehistory.service.squence;
    opens com.anoman.machinehistory.service.mold to javafx.fxml;
    exports com.anoman.machinehistory.service.mold;
    opens com.anoman.machinehistory.service.productmold to javafx.fxml;
    exports com.anoman.machinehistory.service.productmold;
    opens com.anoman.machinehistory.service.problem to javafx.fxml;
    exports com.anoman.machinehistory.service.problem;
    opens com.anoman.machinehistory.service.product to javafx.fxml;
    exports com.anoman.machinehistory.service.product;
    opens com.anoman.machinehistory.service.repairmold to javafx.fxml;
    exports com.anoman.machinehistory.service.repairmold;

    //controller
    opens com.anoman.machinehistory.controller to javafx.fxml;
    exports com.anoman.machinehistory.controller;

}