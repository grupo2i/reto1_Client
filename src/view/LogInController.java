/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;


import exceptions.PasswordDoesNotMatchException;
import exceptions.UnexpectedErrorException;
import exceptions.UserNotFoundException;
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
    private Button btnSignUp;
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
    /**
     * Switches to the SignUp window.
     *
     * @param event
     */
    @FXML
    public void handleButtonSignUp(ActionEvent event) {
        try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignUpWindow.fxml"));
        Parent root = (Parent) loader.load();

        SignUpController controller = (loader.getController());
        controller.setStage(stage);
        controller.initStage(root);
        } catch(IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not change to Sign Up window.", ButtonType.OK);
            alert.showAndWait();
        } 
    }   
    @FXML
    public void handleButtonAccept(ActionEvent event) {
        try{
            User user = new User();
            user.setLogin(txtUsername.getText());
            user.setPassword(pwdPassword.getText());
            user.setLastAccess(Date.valueOf(LocalDate.now()));
            SignableFactory.getSignable().signIn(user);
        } catch(UserNotFoundException | PasswordDoesNotMatchException | UnexpectedErrorException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
        
        
    }
    
    boolean errorUsername=true;
    boolean errorPassword=true;
      
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
        txtUsername.textProperty().addListener(this::textChangedUser);
        pwdPassword.textProperty().addListener(this::textChangedPassword);
        btnAccept.setDisable(true);
        lblErrorLogin.setVisible(false);
        lblErrorPassword.setVisible(false);
        btnAccept.setTooltip(
                new Tooltip("Click to send credentials "));
        btnSignUp.setTooltip(
                new Tooltip("Click to navigate to Sign Up"));
        stage.show();
    }
    /**
     * Tests if there is any error on every pwdPassword text propertie change.
     * @param obs
     */
    private void textChangedPassword(Observable obs){
        Integer pwdLenght = pwdPassword.getText().trim().length();
        //if pwd =0 or <255= error
        if(pwdLenght<6 || pwdLenght>255){
            errorPassword=true;
            lblErrorPassword.setVisible(true);
        } else {
            errorPassword=false;
            lblErrorPassword.setVisible(false);
        }
        //if typing and then erasing, reminder that field must be greater than 6
        if (pwdLenght<6)
            lblErrorPassword.setText("* Must be at least 6 characters");
        //if typing and then erasing and exceeding max characters, reminding not to do it 
        if(pwdLenght>255)
            lblErrorPassword.setText("* Must be less than 255 characters");
        
        testInputErrors();
    }
    /**
     * Tests if there is any error on every txtUsername text propertie change.
     * @param obs
     */
    private void textChangedUser(Observable obs){
        Integer usLenght = txtUsername.getText().trim().length();
        //if username =0 or <255= error
        if(usLenght==0 || usLenght>255){
            errorUsername=true;
            lblErrorLogin.setVisible(true);
        } else {
            errorUsername=false;
            lblErrorLogin.setVisible(false);
        }
        //if typing and then erasing, reminder that field must not be empty
        if (usLenght==0)
            lblErrorLogin.setText("* Field must not be empty");
        //if typing and then erasing and exceeding max characters, reminding not to do it      
        if(usLenght>255)
            lblErrorLogin.setText("* Must be less than 255 characters");
        
        testInputErrors();
    }  
    /**
     * Checks if there is any input error and disables btnAccept if so.
     */
    private void testInputErrors(){
         if(errorPassword || errorUsername){
             btnAccept.setDisable(true);
         } else {
             btnAccept.setDisable(false);
         }
     }    
}