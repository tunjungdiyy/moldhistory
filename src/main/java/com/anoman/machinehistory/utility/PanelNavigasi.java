package com.anoman.machinehistory.utility;

import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class PanelNavigasi {

    double yOffside;
    double xOffside;

    public PanelNavigasi(double yOffside, double xOffside) {
        this.yOffside = yOffside;
        this.xOffside = xOffside;
    }

    public void drag(Stage stage, MouseEvent event) {

        stage.setX(event.getScreenX() - xOffside);
        stage.setY(event.getScreenY() - yOffside);
    }

    public void press(MouseEvent event) {

        yOffside = event.getSceneY();
        xOffside = event.getSceneX();
    }

    public void minimize(MouseEvent mouseEvent, Stage stage) {
        stage.setIconified(true);
    }

    public void maximize(MouseEvent mouseEvent, Stage stage, double width, double height) {
        if (stage.isMaximized()) {
            stage.setMaximized(false);
            stage.setHeight(height);
            stage.setWidth(width);
        } else {
            stage.setMaximized(true);
        }
    }
}
