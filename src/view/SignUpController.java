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
 * @author Aitor Fidalgo
 */
public class SignUpController {

    @FXML
    private Stage stage;

    @FXML
    private Button btnCancel;
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
     * Initializes the stage and asigns listeners to its components.
     *
     * @param root Parent object with signUpWindow loaded in it.
     */
    public void initStage(Parent root) {
        //Hiding stage so the window showing can be handled.
        stage.hide();
        //Setting stage properties...
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Sign Up");
        stage.setResizable(false);
        stage.setOnShowing(this::handleWindowShowing);

        //Setting text change listener to the text fields...
        txtUsername.textProperty().addListener(this::handleTextChangeUsername);
        txtEmail.textProperty().addListener(this::handleTextChangeEmail);
        txtName.textProperty().addListener(this::handleTextChangeName);
        pwdPassword.textProperty().addListener(this::handleTextChangePassword);
        pwdConfirmPassword.textProperty().addListener(this::handleTextChangeConfirmPassword);

        //Showing stage executes handleWindowShowing event.
        stage.show();
        Logger.getLogger(SignUpController.class.getName()).log(Level.INFO, "Switched to Sign Up window.");
    }

    /**
     * Handles the OnShowing event of the stage.
     *
     * @param event Specifies the event that is being handled by the stage.
     */
    private void handleWindowShowing(WindowEvent event) {
        //Setting window focus on first text field.
        txtUsername.requestFocus();

        //Setting Accept button properties...
        btnAccept.setDisable(true);
        btnAccept.setTooltip(
                new Tooltip("Pulse para validar credenciales"));
        btnAccept.setDefaultButton(true);

        //Hidding all error labels...
        lblErrorConfirmPassword.setVisible(false);
        lblErrorEmail.setVisible(false);
        lblErrorName.setVisible(false);
        lblErrorPassword.setVisible(false);
        lblErrorUsername.setVisible(false);

        //Initializing textFieldErrors to true to control input errors.
        textFieldErrors.put("txtUsernameError", true);
        textFieldErrors.put("txtEmailError", true);
        textFieldErrors.put("txtNameError", true);
        textFieldErrors.put("pwdPasswordError", true);
        textFieldErrors.put("pwdConfirmPasswordError", true);
    }

    /**
     * Switches to the LogIn window when Cancel button is clicked.
     *
     * @param event Specifies the event that is being handled by the Cancel
     * button.
     */
    @FXML
    public void handleButtonCancel(ActionEvent event) {
        try {
            Logger.getLogger(SignUpController.class.getName()).log(Level.INFO, "Cancel button pressed.");

            //Switching to logInWindow...
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LogInWindow.fxml"));
            Parent root = (Parent) loader.load();
            LogInController controller = (loader.getController());
            controller.setStage(stage);
            controller.initStage(root);
        } catch (IOException e) {
            //Showing error message on Alert window.
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not change to Log In window.", ButtonType.OK);
            alert.showAndWait();
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, "Could not change to Log In window.");
        }

    }

    /**
     * Calls signUp method in Signer sending the users data to make a sign up
     * request to the server, this will check if the data is already registered
     * or not.
     *
     * @param event Specifies the event that is being handled by the Accept
     * button.
     */
    @FXML
    public void handleButtonAccept(ActionEvent event) {
        try {
            Logger.getLogger(SignUpController.class.getName()).log(Level.INFO, "Accept button pressed.");

            //Getting users data stated in the text fields...
            User user = new User();
            user.setLogin(txtUsername.getText());
            user.setEmail(txtEmail.getText());
            user.setFullName(txtName.getText());
            user.setPassword(pwdPassword.getText());

            /*Calling signUp method in Signer to make a sign up request to the server.
              This method will check if the users data is already registered or not.*/
            user = SignableFactory.getSignable().signUp(user);

            switchToLogOutWindow();
        } catch (UserAlreadyExistsException | EmailAlreadyExistsException | UnexpectedErrorException ex) {
            /*Showing error message on Alert window if the username or email
              are already registered or if an unexpected error occures*/
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
    }

    /**
     * Switches to log out window.
     */
    private void switchToLogOutWindow() {
        try {
            //Switching to log out window...
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LogOutWindow.fxml"));
            Parent root = (Parent) loader.load();
            LogOutController controller = (loader.getController());
            controller.setStage(stage);
            controller.initStage(root);
        } catch (IOException e) {
            //Showing error message on Alert window if anything goes wrong.
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not change to Sign Up window.", ButtonType.OK);
            alert.showAndWait();
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, "Could not change to Sign Up window.");
        }
    }

    /**
     * Tests if there is any error on every txtUsername text propertie change.
     * The text must be between 1 and 255 characters.
     *
     * @param obs Represents the object handleling the event.
     */
    private void handleTextChangeUsername(Observable obs) {
        Integer txtUsernameLength = txtUsername.getText().trim().length();

        //If there is any error...
        if (txtUsernameLength == 0 || txtUsernameLength > 255) {
            //Sets the error message when the fiel is empty.
            if (txtUsernameLength == 0) {
                lblErrorUsername.setText("* Field must not be empty");
            } //Sets the error message when the field is longer than 255 characters.
            else if (txtUsernameLength > 255) {
                lblErrorUsername.setText("* Must be less than 255 characters");
            }

            //Marks there is an error on txtUsername and shows the error message.
            textFieldErrors.put("txtUsernameError", true);
            lblErrorUsername.setVisible(true);
        } else {
            //Marks there is NOT any error on txtUsername and hides the error message.
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
        //Checks if there is any input errors in the text fields... 
        if (textFieldErrors.get("txtUsernameError") || textFieldErrors.get("txtEmailError")
                || textFieldErrors.get("txtNameError") || textFieldErrors.get("pwdPasswordError")
                || textFieldErrors.get("pwdConfirmPasswordError")) {

            btnAccept.setDisable(true);
        } else {
            btnAccept.setDisable(false);
        }
    }

    /**
     * Tests if there is any error on every txtEmail text propertie change. The
     * text must be between 1 and 255 character and have a correct format.
     *
     * @param obs Represents the object handleling the event.
     */
    private void handleTextChangeEmail(Observable obs) {
        Integer txtEmailLength = txtEmail.getText().trim().length();
        //Pattern used to validate the email format.
        Pattern patternEmail = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@"
                + "[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        //Used to check if the email matches the pattern.
        Matcher matcherEmail = patternEmail.matcher(txtEmail.getText());

        //If there is any error...
        if (txtEmailLength == 0 || txtEmailLength > 255 || !matcherEmail.matches()) {
            //Sets the error message when the fiel is empty.
            if (txtEmailLength == 0) {
                lblErrorEmail.setText("* Field must not be empty");
            } //Sets the error message when the field is longer than 255 characters.
            else if (txtEmailLength > 255) {
                lblErrorEmail.setText("* Must be less than 255 characters");
            } //Sets the error message when the field does not match the pattern.
            else if (!matcherEmail.matches()) {
                lblErrorEmail.setText("* Must match the pattern example@example.com");
            }

            //Marks there is an error on txtEmail and shows the error message.
            textFieldErrors.put("txtEmailError", true);
            lblErrorEmail.setVisible(true);
        } else {
            //Marks there is NOT any error on txtEmail and hides the error message.
            textFieldErrors.put("txtEmailError", false);
            lblErrorEmail.setVisible(false);
        }

        //Tests if there is any input error and disables btnAccept if so.
        testInputErrors();
    }

    /**
     * Tests if there is any error on every txtName text propertie change. The
     * text must be between 1 and 255 character and have a correct format.
     *
     * @param obs Represents the object handleling the event.
     */
    private void handleTextChangeName(Observable obs) {
        Integer txtNameLength = txtName.getText().trim().length();
        //Pattern used to validate the names format.
        Pattern patternName = Pattern.compile("^([A-Za-z]+[ ]?)+$");
        //Used to check if the name matches the pattern.
        Matcher matcherName = patternName.matcher(txtName.getText());

        //If there is any error...
        if (txtNameLength == 0 || txtNameLength > 255 || !matcherName.matches()) {
            //Sets the error message when the fiel is empty.
            if (txtNameLength == 0) {
                lblErrorName.setText("* Field must not be empty");
            } //Sets the error message when the field is longer than 255 characters.
            else if (txtNameLength > 255) {
                lblErrorName.setText("* Must be less than 255 characters");
            } //Sets the error message when the field does not match the pattern.
            else if (!matcherName.matches()) {
                lblErrorName.setText("* Must only contain letters");
            }

            //Marks there is an error on txtName and shows the error message.
            textFieldErrors.put("txtNameError", true);
            lblErrorName.setVisible(true);
        } else {
            //Marks there is NOT any error on txtName and hides the error message.
            textFieldErrors.put("txtNameError", false);
            lblErrorName.setVisible(false);
        }

        //Tests if there is any input error and disables btnAccept if so.
        testInputErrors();
    }

    /**
     * Tests if there is any error on every pwdPassword text propertie change.
     * The text must be between 6 and 255 characters.
     *
     * @param obs Represents the object handleling the event.
     */
    private void handleTextChangePassword(Observable obs) {
        Integer pwdPasswordLength = pwdPassword.getText().trim().length();

        //If there is any error with pwdPassword...
        if (pwdPasswordLength < 6 || pwdPasswordLength > 255) {
            //Sets the error message when the fiel is shorter than 6 characters.
            if (pwdPasswordLength < 6) {
                lblErrorPassword.setText("* Must be at least 6 characters");
            } //Sets the error message when the field is longer than 255 characters.
            else if (pwdPasswordLength > 255) {
                lblErrorPassword.setText("* Must be less than 255 characters");
            }

            //Marks there is an error on pwdPassword and shows the error message.
            textFieldErrors.put("pwdPasswordError", true);
            lblErrorPassword.setVisible(true);
        } else {
            //Marks there is NOT any error on pwdPassword and hides the error message.
            textFieldErrors.put("pwdPasswordError", false);
            lblErrorPassword.setVisible(false);
        }

        //If pwdConfirmPassword does not match with pwdPassword due to the text change...
        if (!pwdConfirmPassword.getText().equals(pwdPassword.getText())) {
            //Marks there is an error on pwdConfirmPassword and shows the error message.
            textFieldErrors.put("pwdConfirmPasswordError", true);
            lblErrorConfirmPassword.setVisible(true);
        } else {
            //Marks there is NOT any error on pwdConfirmPassword and hides the error message.
            textFieldErrors.put("pwdConfirmPasswordError", false);
            lblErrorConfirmPassword.setVisible(false);
        }

        //Tests if there is any input error and disables btnAccept if so.
        testInputErrors();
    }

    /**
     * Tests if there is any error on every pwdConfirmPassword text propertie
     * change. The text must match the text of pwdPassword.
     *
     * @param obs Represents the object handleling the event.
     */
    private void handleTextChangeConfirmPassword(Observable obs) {
        //If pwdConfirmPassword does not match with pwdPassword...
        if (!pwdConfirmPassword.getText().equals(pwdPassword.getText())) {
            //Marks there is an error on pwdConfirmPassword and shows the error message.
            textFieldErrors.put("pwdConfirmPasswordError", true);
            lblErrorConfirmPassword.setVisible(true);
        } else {
            //Marks there is NOT any error on pwdConfirmPassword and hides the error message.
            textFieldErrors.put("pwdConfirmPasswordError", false);
            lblErrorConfirmPassword.setVisible(false);
        }

        //Tests if there is any input error and disables btnAccept if so.
        testInputErrors();
    }

    /**
     * Retuns the stage attribute of this class.
     *
     * @return Stage object used in the class.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Sets the value of the attribute stage
     *
     * @param primaryStage Value that is going to be set.
     */
    public void setStage(Stage primaryStage) {
        stage = primaryStage;
    }
}
