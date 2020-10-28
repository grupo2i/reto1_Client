/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;


import java.io.IOException;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;



/**
 *
 * @author Ander
 */

public class LogInController {
    @FXML
    private Stage stage;
    @FXML
    private Button btnAccept;
    @FXML
    private Label lblUsername;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField pwdPassword;
    @FXML
    private Label lblErrorLogin;
    @FXML
    private Label lblErrorPassword;
    @FXML
    public void handleButtonSignUp(ActionEvent event) throws IOException {
        start_signup(stage);
    }
      
    public Stage getStage() {
        return stage;
    }
    

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void initStage(Parent root){
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Log In");
        stage.setResizable(false);
        txtUsername.textProperty().addListener(this::textChangedUser);
        pwdPassword.textProperty().addListener(this::textChangedPassword);
        stage.show();
    }
    public void initialize(){
        btnAccept.setDisable(true);
        lblErrorLogin.setVisible(false);
        lblErrorPassword.setVisible(false);
        btnAccept.setTooltip(
                new Tooltip("Pulse para validar credenciales"));
        
    }
    public void OnClick(){
        String us = txtUsername.getText();
        String pw = pwdPassword.getText();
    }

    private void textChangedPassword(Observable obs){
        Integer pwdLenght = pwdPassword.getText().trim().length();
        boolean error=false;
        
        if(pwdLenght<6 || pwdLenght>255){
            btnAccept.setDisable(true);
            error=true;
            lblErrorPassword.setVisible(true);
        }
        if(error)
            btnAccept.setDisable(true);
        if(!error){
            btnAccept.setDisable(false);   
            lblErrorPassword.setVisible(false);
        }
        if (pwdLenght<6)
            lblErrorPassword.setText("Introduce al menos 6 caracteres");
        if(pwdLenght>255)
            lblErrorPassword.setText("Introduce menos de 255 caracteres");
    }
     private void textChangedUser(Observable obs){
        Integer usLenght = txtUsername.getText().trim().length();

        boolean error=false;
        if(usLenght==0 || usLenght>255){
            error=true;
            lblErrorLogin.setVisible(true);
        }
        if(error)
            btnAccept.setDisable(true);
        if(!error){
            btnAccept.setDisable(false);   
            lblErrorLogin.setVisible(false);
        }
        if (usLenght==0)
            lblErrorLogin.setText("Introduce algun caracter");
        if(usLenght>255)
            lblErrorLogin.setText("Introduce menos de 255 caracteres");
    }
    
    public void start_signup(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignUpWindow.fxml"));
        Parent root = (Parent) loader.load();

        SignUpController controller = (loader.getController());
        controller.setStage(primaryStage);
        controller.initStage(root);
    }

}