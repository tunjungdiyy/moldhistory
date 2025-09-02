package com.anoman.machinehistory.controller;

import com.anoman.machinehistory.MenuApp;
import com.anoman.machinehistory.model.user.UserAuth;
import com.anoman.machinehistory.service.user.UserService;
import com.anoman.machinehistory.service.user.UserServiceImpl;
import com.anoman.machinehistory.utility.Navigation;
import com.anoman.machinehistory.utility.PanelNavigasi;
import com.anoman.machinehistory.utility.alert.AlertApp;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.Setter;

public class LoginController {


    public HBox btnExit;
    public TextField tfUserName;
    public PasswordField tfPassword;
    public Button btnLogin;
    @Setter
    Stage stage;

    double yOffside = 0;
    double xOffside = 0;

    PanelNavigasi panelNavigasi = new PanelNavigasi(yOffside, xOffside);

    UserService userService = new UserServiceImpl();

    public void drop(MouseEvent mouseEvent) {

        panelNavigasi.drag(stage, mouseEvent);

    }

    public void press(MouseEvent mouseEvent) {
        panelNavigasi.press(mouseEvent);
    }

    public void logout(MouseEvent mouseEvent) {

        AlertApp alertApp = new AlertApp();

        boolean result = alertApp.showAlert("confirm", "Continue exit this Application ?");

        if (result) {
            Platform.exit();
        }
    }

    public void navTfName(KeyEvent event) {
        nav(tfUserName, event);
    }

    public void navTfPassword(KeyEvent event) throws Exception {
        nav(tfPassword, event);

        if (event.getCode() == KeyCode.ENTER) {
            login();
        }
    }

    public void login(ActionEvent actionEvent) throws Exception {
        login();
    }

    void nav (javafx.scene.Node node, KeyEvent event) {

        javafx.scene.Node[] nodes = {tfUserName, tfPassword};

        Navigation navigation = new Navigation();

        navigation.upDownNav(nodes, node, event);

    }

    void login() throws Exception {
        AlertApp alertApp = new AlertApp();

        if (tfUserName.getText().isBlank() || tfPassword.getText().isBlank()) {
            alertApp.showAlert("error", "username & password tidak boleh kosong");
        } else {
            UserAuth userAuth = new UserAuth();
            userAuth.setUsername(tfUserName.getText());
            userAuth.setPassword(tfPassword.getText());

            boolean value = userService.login(userAuth);

            if (value) {
                boolean result = alertApp.showAlert("info", "Berhasil Login");

                if (result) {

                    MenuApp menuApp = new MenuApp();
                    menuApp.start(new Stage());

                    stage.close();
                }
            } else {
                alertApp.showAlert("error", "username / password salah");
            }

        }
    }
}
