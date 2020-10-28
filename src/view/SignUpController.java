/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import javafx.stage.WindowEvent;
import logic.SignableFactory;
import user.User;

/**
 * Class that controls the SignUpWindow behaviour.
 * @author aitor
 */
public class SignUpController {
    @FXML
    private Stage stage;
    
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnAccept;
    
    @FXML
    private PasswordField pwdPassword;
    @FXML
    private PasswordField pwdConfirmPassword;
    
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtUsername;
    
    @FXML
    private Label lblErrorUsername;
    @FXML
    private Label lblErrorEmail;
    @FXML
    private Label lblErrorPassword;
    @FXML
    private Label lblErrorConfirmPassword;
    @FXML
    private Label lblErrorName;
    
    //Used to handle textField input errors.
    HashMap<String, Boolean> textFieldErrors = new HashMap<>();
    
    
    
    
    /**
     * Initializes the scene and its components
     * @param root 
     */
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Sign Up");
        stage.setResizable(false);
        stage.setOnShowing(this::handleWindowShowing);
        
        txtUsername.textProperty().addListener(this::handleTextChangeUsername);
        textFieldErrors.put("txtUsernameError", true);
        txtEmail.textProperty().addListener(this::handleTextChangeEmail);
        textFieldErrors.put("txtEmailError", true);
        txtName.textProperty().addListener(this::handleTextChangeName);
        textFieldErrors.put("txtNameError", true);
        pwdPassword.textProperty().addListener(this::handleTextChangePassword);
        textFieldErrors.put("pwdPasswordError", true);
        pwdConfirmPassword.textProperty().addListener(this::handleTextChangeConfirmPassword);
        textFieldErrors.put("pwdConfirmPasswordError", true);
        
        stage.show();
    }
    
    public void initialize(){
        btnAccept.setDisable(true);
        lblErrorConfirmPassword.setVisible(false);
        lblErrorEmail.setVisible(false);
        lblErrorName.setVisible(false);
        lblErrorUsername.setVisible(false);
        lblErrorPassword.setVisible(false);
        btnAccept.setTooltip(
                new Tooltip("Pulse para validar credenciales"));
        
    }
    
    /**
     * Handles the OnShowing event of the stage.
     * @param event 
     */
    private void handleWindowShowing(WindowEvent event){
        btnAccept.setDisable(true);
        
        txtUsername.requestFocus();
        
        lblErrorConfirmPassword.setVisible(false);
        lblErrorEmail.setVisible(false);
        lblErrorName.setVisible(false);
        lblErrorPassword.setVisible(false);
        lblErrorUsername.setVisible(false);
    }
    
    @FXML
    public void handleButtonLogIn(ActionEvent event) {
        try{
            start_logIn(stage);
        } catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not change to Log In window.", ButtonType.OK);
            alert.showAndWait();
        }
        
    }
    
    @FXML
    public void handleButtonAccept(ActionEvent event) {
        User user = new User();
        user.setLogin(txtUsername.getText());
        user.setEmail(txtEmail.getText());
        user.setFullName(txtName.getText());
        user.setPassword(pwdPassword.getText());
        user.setLastAccess(Date.valueOf(LocalDate.now()));
        SignableFactory.getSignable().signUp(user);
        
    }
    
    /**
     * Tests if there is any error on every txtUsername text propertie change.
     * @param obs 
     */
    private void handleTextChangeUsername(Observable obs){
        Integer txtUsernameLength = txtUsername.getText().trim().length();
        
        //Testing the field length...
        if(txtUsernameLength == 0 || txtUsernameLength > 255){
            lblErrorUsername.setVisible(true);
            textFieldErrors.put("txtUsernameError", true);
        } else {
            lblErrorUsername.setVisible(false);
            textFieldErrors.put("txtUsernameError", false);
        }
        
        //Tests if there is any input error and disables btnAccept if so.
        testInputErrors();
    }
    
    /**
     * Tests if there is any error on every txtEmail text propertie change.
     * @param obs 
     */
    private void handleTextChangeEmail(Observable obs){
        Integer txtEmailLength = txtEmail.getText().trim().length();
        Pattern patternEmail = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@" +
                "[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcherEmail = patternEmail.matcher(txtEmail.getText());
        
        //Testing if it has the correct format...
        if(!matcherEmail.matches()){
            lblErrorEmail.setText("* Must match the pattern example@example.com");
            textFieldErrors.put("txtEmailError", true);
        } else {
            textFieldErrors.put("txtEmailError", false);
        }
        
        //Testing the field length
        if(txtEmailLength > 255 || txtEmailLength == 0){
            lblErrorEmail.setText("* Must be between 0 and 255 characters");
            textFieldErrors.put("txtEmailError", true);
        }
        
        //Setting visible the error label if there is any error
        if(textFieldErrors.get("txtEmailError")){
            lblErrorEmail.setVisible(true);
        } else {
            lblErrorEmail.setVisible(false);
        }
        
        //Tests if there is any input error and disables btnAccept if so.
        testInputErrors();
    }
    
    /**
     * Tests if there is any error on every txtName text propertie change.
     * @param obs 
     */
    private void handleTextChangeName(Observable obs){
        Integer txtNameLength = txtName.getText().trim().length();
        
        //Testing the field length and if it has the correct format...
        Pattern patternName = Pattern.compile("^([A-Za-z]+[ ]?)+$");
        Matcher matcherName = patternName.matcher(txtName.getText());
        if(!matcherName.matches()){
            lblErrorName.setText("* Must only contain letters");
            textFieldErrors.put("txtNameError", true);
        } else {
            textFieldErrors.put("txtNameError", false);
        }
        
        if(txtNameLength > 255 || txtNameLength == 0){
            lblErrorName.setText("* Must be between 0 and 255 characters");
            textFieldErrors.put("txtNameError", true);
        }
        
        if(textFieldErrors.get("txtNameError")){
            lblErrorName.setVisible(true);
        } else {
            lblErrorName.setVisible(false);
        }
        
        //Tests if there is any input error and disables btnAccept if so.
        testInputErrors();
    }
    
    /**
     * Tests if there is any error on every pwdPassword text propertie change.
     * @param obs 
     */
    private void handleTextChangePassword(Observable obs){
        Integer pwdPasswordLength = pwdPassword.getText().trim().length();
        Integer pwdConfirmPasswordLength = pwdConfirmPassword.getText().trim().length();
        
        //Testing the lenght of the field...
        if(pwdPasswordLength < 6 || pwdPasswordLength > 255){
            lblErrorPassword.setVisible(true);
            textFieldErrors.put("pwdPasswordError", true);
        } else {
            lblErrorPassword.setVisible(false);
            textFieldErrors.put("pwdPasswordError", false);
        }
        
        //Testing if the field matches with pwdConfirmPassword...
        if(!pwdConfirmPassword.getText().equals(pwdPassword.getText())){
            lblErrorConfirmPassword.setVisible(true);
            textFieldErrors.put("pwdConfirmPasswordError", true);
        } else {
            lblErrorConfirmPassword.setVisible(false);
            textFieldErrors.put("pwdConfirmPasswordError", false);
        }
        
        //Tests if there is any input error and disables btnAccept if so.
        testInputErrors();
    }
    
    /**
     * Tests if there is any error on every pwdConfirmPassword text propertie change.
     * @param obs 
     */
    private void handleTextChangeConfirmPassword(Observable obs){
        Integer pwdConfirmPasswordLength = pwdConfirmPassword.getText().trim().length();
        
        //Testing the lenght of the field and if it matches with pwdPassword...
        if(!pwdConfirmPassword.getText().equals(pwdPassword.getText())){
            lblErrorConfirmPassword.setVisible(true);
            textFieldErrors.put("pwdConfirmPasswordError", true);
        } else {
            lblErrorConfirmPassword.setVisible(false);
            textFieldErrors.put("pwdConfirmPasswordError", false);
        }
        
        //Tests if there is any input error and disables btnAccept if so.
        testInputErrors();
    }
    
    /**
     * Tests if there is any input error and disables btnAccept if so.
     */
    private void testInputErrors() {
        if(textFieldErrors.get("txtUsernameError") || textFieldErrors.get("txtEmailError") ||
                textFieldErrors.get("txtNameError") || textFieldErrors.get("pwdPasswordError") ||
                textFieldErrors.get("pwdConfirmPasswordError")){

            btnAccept.setDisable(true);  
        } else {
            btnAccept.setDisable(false);
        }
    }
    
    
    public Stage getStage(){
        return stage;
    }
    
    public void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    private void start_logIn(Stage primaryStage) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LogInWindow.fxml"));
        Parent root =(Parent)loader.load();
        
        LogInController controller = (loader.getController());
        controller.setStage(primaryStage);
        controller.initStage(root);
    }
    
}
