/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;


import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;



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
    /**
     * Switches to the Log in window.
     *
     * @param event
     */
    @FXML
    public void handleButtonLogOut(ActionEvent event) {
        try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LogInWindow.fxml"));
        Parent root = (Parent) loader.load();

        SignUpController controller = (loader.getController());
        controller.setStage(stage);
        controller.initStage(root);
        } catch(IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not change to Sign Up window.", ButtonType.OK);
            alert.showAndWait();
        } 
    }   
    /**
     * Closes application.
     *
     * @param event
     */
    @FXML
    public void handleButtonExit(ActionEvent event) {
        stage.close();
        
    }
    public Stage getStage() {
        return stage;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    /**
     * Initializes the scene and its components
     *
     * @param root
     */
    public void initStage(Parent root){
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Log In");
        stage.setResizable(false);
        btnLogOut.setTooltip(
                new Tooltip("Click to log out "));
        btnExit.setTooltip(
                new Tooltip("Click to close application"));
        stage.show();
    }
 
}