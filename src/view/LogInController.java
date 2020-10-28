/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;


import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import logic.SignableFactory;
import message.Message;
import message.Message.Type;
import user.User;



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
    public void handleButtonSignUp(ActionEvent event) {
        try{
            start_signup(stage);
        } catch(IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not change to Sign Up window.", ButtonType.OK);
            alert.showAndWait();
        } 
    }
    
    @FXML
    public void handleButtonAccept(ActionEvent event) {
        User user = new User();
        user.setLogin(txtUsername.getText());
        user.setPassword(pwdPassword.getText());
        user.setLastAccess(Date.valueOf(LocalDate.now()));
        SignableFactory.getSignable().signIn(user);
        
    }
    
    boolean errorUsername=true;
    boolean errorPassword=true;
      
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
        
        if(pwdLenght<6 || pwdLenght>255){
            errorPassword=true;
            lblErrorPassword.setVisible(true);
        } else {
            errorPassword=false;
            lblErrorPassword.setVisible(false);
        }
        if (pwdLenght<6)
            lblErrorPassword.setText("* Must be at least 6 characters");
        if(pwdLenght>255)
            lblErrorPassword.setText("* Must be less than 255 characters");
        
        testInputErrors();
    }
     private void textChangedUser(Observable obs){
        Integer usLenght = txtUsername.getText().trim().length();

        if(usLenght==0 || usLenght>255){
            errorUsername=true;
            lblErrorLogin.setVisible(true);
        } else {
            errorUsername=false;
            lblErrorLogin.setVisible(false);
        }
        if (usLenght==0)
            lblErrorLogin.setText("* Field must not be empty");
        if(usLenght>255)
            lblErrorLogin.setText("* Must be less than 255 characters");
        
        testInputErrors();
    }
     
     private void testInputErrors(){
         if(errorPassword || errorUsername){
             btnAccept.setDisable(true);
         } else {
             btnAccept.setDisable(false);
         }
     }
    
    public void start_signup(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignUpWindow.fxml"));
        Parent root = (Parent) loader.load();

        SignUpController controller = (loader.getController());
        controller.setStage(primaryStage);
        controller.initStage(root);
    }

}