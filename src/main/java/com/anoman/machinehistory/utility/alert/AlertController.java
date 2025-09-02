package com.anoman.machinehistory.utility.alert;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AlertController {
    public HBox header;
    public ImageView imgHeader;
    public Label tfjudul;
    public Label tfInformation;
    public Button btnYes;
    public Button btnNo;

    Stage stage;

    private boolean result = false;

    public boolean getResult() {
        return result;
    }

    public void initialize(String titel, String information) {

        /*
        confirm
        warning
        error
         */

        if (titel.equals("confirm")) {
            header.setBackground(new Background(background(Color.AQUA)));
            btnNo.setVisible(true);
            tfjudul.setText("Confirm");
            tfInformation.setText(information);
            imgHeader.setImage(image("/com/anoman/machinehistory/image/alert/information.png"));
        } else if (titel.equals("warning")) {
            header.setBackground(new Background(background(Color.YELLOW)));
            btnNo.setVisible(false);
            tfjudul.setText("Warning");
            tfInformation.setText(information);
            imgHeader.setImage(image("/com/anoman/machinehistory/image/alert/warning.png"));

        } else if (titel.equals("error")) {
            header.setBackground(new Background(background(Color.ORANGERED)));
            btnNo.setVisible(false);
            tfjudul.setText("Error");
            tfInformation.setText(information);
            imgHeader.setImage(image("/com/anoman/machinehistory/image/alert/error.png"));
        } else if (titel.equals("info")) {
            header.setBackground(new Background(background(Color.AQUA)));
            btnNo.setVisible(false);
            tfjudul.setText("Information");
            tfInformation.setText(information);
            imgHeader.setImage(image("/com/anoman/machinehistory/image/alert/information.png"));
        }

    }

    public void btnYes(ActionEvent actionEvent) {
        result = true;
        closeStage();
    }

    public void btnNo(ActionEvent actionEvent) {
        result = false;
        closeStage();
    }

    BackgroundFill background(Color color) {

        return new BackgroundFill(
                color,
                CornerRadii.EMPTY,
                Insets.EMPTY
        );
    }

    private void closeStage() {
        Stage stage1 = (Stage) btnYes.getScene().getWindow();
        stage1.close();
    }

    Image image(String url) {
        Image image = new Image(getClass().getResource(url).toExternalForm());

        return  image;
    }
}
