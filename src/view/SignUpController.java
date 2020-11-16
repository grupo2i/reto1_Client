package view;

import exceptions.EmailAlreadyExistsException;
import exceptions.UnexpectedErrorException;
import exceptions.UserAlreadyExistsException;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * Controls the SignUpWindow behaviour.
 *
 * @author Aitor Fidalgo, Martin Angulo
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

    /** Used to handle textField input errors. */
    HashMap<String, Boolean> textFieldErrors = new HashMap<>();

    /**
     * Initializes the scene and its components
     *
     * @param root Base node of the scene graph
     */
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Sign Up");
        stage.setResizable(false);
        
        //Add listeners & setup for error handling
        stage.setOnShowing(this::handleWindowShowing);
        stage.setOnCloseRequest((WindowEvent event) -> {
            if(stage.getScene() == scene)
                handleWindowCloseRequest(event);
        });
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

        //Hide error labels
        lblErrorConfirmPassword.setVisible(false);
        lblErrorEmail.setVisible(false);
        lblErrorName.setVisible(false);
        lblErrorUsername.setVisible(false);
        lblErrorPassword.setVisible(false);

        //Disable accept button and set its tooltip
        btnAccept.setDisable(true);
        btnAccept.setTooltip(
                new Tooltip("Pulse para validar credenciales"));
        btnAccept.setDefaultButton(true);

        stage.show();
        Logger.getLogger(SignUpController.class.getName()).log(Level.INFO, "Sign up initialized.");
    }

    /**
     * Handles the CloseRequest event of the sign up so that it goes to log in 
     * instead of closing.
     *
     * @param event WindowEvent of type WINDOW_CLOSE_REQUEST
     */
    private void handleWindowCloseRequest(WindowEvent event) {
        try {
            //Load and switch to log in window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LogInWindow.fxml"));
            Parent root = (Parent) loader.load();
            LogInController controller = (loader.getController());
            controller.setStage(stage);
            controller.initStage(root);
            //Consume the event so that the window is not actually closed.
            event.consume();
        } catch (IOException e) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, "Window close error: {0}", e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not change to Log In window.", ButtonType.OK);
            alert.showAndWait();
        }
    }
    
    /**
     * Handles the OnShowing event of the stage.
     *
     * @param event WindowEvent of type WINDOW_SHOWING
     */
    private void handleWindowShowing(WindowEvent event) {
        btnAccept.setDisable(true);

        txtUsername.requestFocus();

        lblErrorConfirmPassword.setVisible(false);
        lblErrorEmail.setVisible(false);
        lblErrorName.setVisible(false);
        lblErrorPassword.setVisible(false);
        lblErrorUsername.setVisible(false);
    }

    /**
     * Switches to the LogIn window.
     *
     * @param event ActionEvent that triggered the button.
     */
    @FXML
    public void handleButtonLogIn(ActionEvent event) {
        try {
            Logger.getLogger(SignUpController.class.getName()).log(Level.INFO, "Log in button pressed.");
            //Load and switch to log in window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LogInWindow.fxml"));
            Parent root = (Parent) loader.load();
            LogInController controller = (loader.getController());
            controller.setStage(stage);
            controller.initStage(root);
        } catch (IOException e) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, "Log in button error: {0}", e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not change to Log In window.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Sends the user data to the server so that it is inserted into the database.
     *
     * @param event ActionEvent that triggered the button.
     */
    @FXML
    public void handleButtonAccept(ActionEvent event) {
        try{
            Logger.getLogger(SignUpController.class.getName()).log(Level.INFO, "Accept button pressed.");
            //Save the info into a User
            User user = new User();
            user.setLogin(txtUsername.getText());
            user.setEmail(txtEmail.getText());
            user.setFullName(txtName.getText());
            user.setPassword(pwdPassword.getText());
            //Get a signable to actually send the data
            user = SignableFactory.getSignable().signUp(user);
            //Switch to the log out window
            switchToLogOutWindow();
        } catch(UserAlreadyExistsException | EmailAlreadyExistsException | UnexpectedErrorException ex) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, "Sign up error: {0}", ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Helper function to change to the log out window.
     */
    private void switchToLogOutWindow() {
        try{
            //Load and switch to log out window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LogOutWindow.fxml"));
            Parent root =(Parent)loader.load();
            LogOutController controller = (loader.getController());
            controller.setStage(stage);
            controller.initStage(root);
        } catch(IOException e){
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, "Error switching to log out: {0}", e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not change to Sign Up window.", ButtonType.OK);
            alert.showAndWait();
        } 
    }

    /**
     * Tests if there is any error on every txtUsername text property change.
     * @param obs
     */
    private void handleTextChangeUsername(Observable obs) {
        Integer txtUsernameLength = txtUsername.getText().trim().length();

        //If there is any error...
        if (txtUsernameLength == 0 || txtUsernameLength > 255) {
            //Sets the error message when the fiel is empty.
            if (txtUsernameLength == 0) lblErrorUsername.setText("* Field must not be empty");
            //Sets the error message when the field is longer than 255 characters.
            else if (txtUsernameLength > 255) lblErrorUsername.setText("* Must be less than 255 characters");
            
            textFieldErrors.put("txtUsernameError", true);
            lblErrorUsername.setVisible(true);
        } else {
            textFieldErrors.put("txtUsernameError", false);
            lblErrorUsername.setVisible(false);
        }

        //Tests if there is any input error and disables btnAccept if so.
        testInputErrors();
    }

    /**
     * Checks if there is any input error and disables btnAccept if so.
     */
    private void testInputErrors() {
        //Checks if there is any input errors... 
        if (textFieldErrors.get("txtUsernameError") || textFieldErrors.get("txtEmailError")
                || textFieldErrors.get("txtNameError") || textFieldErrors.get("pwdPasswordError")
                || textFieldErrors.get("pwdConfirmPasswordError")) {

            btnAccept.setDisable(true);
        } else {
            btnAccept.setDisable(false);
        }
    }

    /**
     * Tests if there is any error on every txtEmail text property change.
     * @param obs
     */
    private void handleTextChangeEmail(Observable obs) {
        Integer txtEmailLength = txtEmail.getText().trim().length();
        Pattern patternEmail = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@"
                + "[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcherEmail = patternEmail.matcher(txtEmail.getText());

        //If there is any error...
        if (txtEmailLength == 0 || txtEmailLength > 255 || !matcherEmail.matches()) {
            //Sets the error message when the fiel is empty.
            if (txtEmailLength == 0) lblErrorEmail.setText("* Field must not be empty");
            //Sets the error message when the field is longer than 255 characters.
            else if (txtEmailLength > 255) lblErrorEmail.setText("* Must be less than 255 characters");
            //Sets the error message when the field does not match the pattern.
            else if (!matcherEmail.matches()) lblErrorEmail.setText("* Must match the pattern example@example.com");
            
            textFieldErrors.put("txtEmailError", true);
            lblErrorEmail.setVisible(true);
        } else {
            textFieldErrors.put("txtEmailError", false);
            lblErrorEmail.setVisible(false);
        }

        //Tests if there is any input error and disables btnAccept if so.
        testInputErrors();
    }

    /**
     * Tests if there is any error on every txtName text property change.
     * @param obs
     */
    private void handleTextChangeName(Observable obs) {
        Integer txtNameLength = txtName.getText().trim().length();
        Pattern patternName = Pattern.compile("^([A-Za-záéíóúÁÉÍÓÚ]+[ ]?)+$");
        Matcher matcherName = patternName.matcher(txtName.getText());

        //If there is any error...
        if (txtNameLength == 0 || txtNameLength > 255 || !matcherName.matches()) {
            //Sets the error message when the fiel is empty.
            if (txtNameLength == 0) lblErrorName.setText("* Field must not be empty");
            //Sets the error message when the field is longer than 255 characters.
            else if (txtNameLength > 255) lblErrorName.setText("* Must be less than 255 characters");
            //Sets the error message when the field does not match the pattern.
            else if (!matcherName.matches()) lblErrorName.setText("* Must only contain letters");
            
            textFieldErrors.put("txtNameError", true);
            lblErrorName.setVisible(true);
        } else {
            textFieldErrors.put("txtNameError", false);
            lblErrorName.setVisible(false);
        }

        //Tests if there is any input error and disables btnAccept if so.
        testInputErrors();
    }

    /**
     * Tests if there is any error on every pwdPassword text property change.
     * @param obs
     */
    private void handleTextChangePassword(Observable obs) {
        Integer pwdPasswordLength = pwdPassword.getText().trim().length();

        //If there is any error with pwdPassword...
        if (pwdPasswordLength < 6 || pwdPasswordLength > 255) {
            //Sets the error message when the fiel is shorter than 6 characters.
            if (pwdPasswordLength < 6) lblErrorPassword.setText("* Must be at least 6 characters");
            //Sets the error message when the field is longer than 255 characters.
            else if (pwdPasswordLength > 255) lblErrorPassword.setText("* Must be less than 255 characters");
            
            textFieldErrors.put("pwdPasswordError", true);
            lblErrorPassword.setVisible(true);
        } else {
            textFieldErrors.put("pwdPasswordError", false);
            lblErrorPassword.setVisible(false);
        }
        
        //If pwdConfirmPassword does not match with pwdPassword...
        if (!pwdConfirmPassword.getText().equals(pwdPassword.getText())) {
            textFieldErrors.put("pwdConfirmPasswordError", true);
            lblErrorConfirmPassword.setVisible(true);
        } else {
            textFieldErrors.put("pwdConfirmPasswordError", false);
            lblErrorConfirmPassword.setVisible(false);
        }

        //Tests if there is any input error and disables btnAccept if so.
        testInputErrors();
    }

    /**
     * Tests if there is any error on every pwdConfirmPassword text property change.
     * @param obs
     */
    private void handleTextChangeConfirmPassword(Observable obs) {
        //If pwdConfirmPassword does not match with pwdPassword...
        if (!pwdConfirmPassword.getText().equals(pwdPassword.getText())) {
            textFieldErrors.put("pwdConfirmPasswordError", true);
            lblErrorConfirmPassword.setVisible(true);
        } else {
            textFieldErrors.put("pwdConfirmPasswordError", false);
            lblErrorConfirmPassword.setVisible(false);
        }

        //Tests if there is any input error and disables btnAccept if so.
        testInputErrors();
    }

    /**
     * Returns the stage.
     * @return The stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Sets the stage.
     * @param primaryStage Stage to set
     */
    public void setStage(Stage primaryStage) {
        stage = primaryStage;
    }
}