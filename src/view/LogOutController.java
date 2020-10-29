/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import logic.SignableFactory;
import user.User;

/**
 *
 * @author Ander
 */
public class LogOutController {

    @FXML
    private Stage stage;
    @FXML
    private Button btnLogOut;
    @FXML
    private Button btnExit;

    @FXML
    public void handleButtonLogOut(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LogInWindow.fxml"));
        Parent root = (Parent) loader.load();
        LogInController controller = (loader.getController());
        controller.setStage(stage);
        controller.initStage(root);
    }

    @FXML
    public void handleButtonExit(ActionEvent event) {
        stage.close();
    }

    boolean errorUsername = true;
    boolean errorPassword = true;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Log Out");
        stage.setResizable(false);
        stage.show();
    }

    public void initialize() {
        btnLogOut.setTooltip(
                new Tooltip("Click to log out"));
        btnExit.setTooltip(
                new Tooltip("Click to close app"));

    }


}
