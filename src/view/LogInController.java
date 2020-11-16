package view;

import exceptions.PasswordDoesNotMatchException;
import exceptions.UnexpectedErrorException;
import exceptions.UserNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import logic.Signer;
import user.User;

/**
 * Controls the SignIn window behaviour.
 *
 * @author Ander Vicente
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
        try {
            Logger.getLogger(LogInController.class.getName()).log(Level.INFO, "Sign Up button pressd.");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignUpWindow.fxml"));
            Parent root = (Parent) loader.load();
            SignUpController controller = (loader.getController());
            controller.setStage(stage);
            controller.initStage(root);
        } catch (IOException e) {
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, "Sign up button error: {0}", e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not change to Sign Up window.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Tries to log in, if introduced fields ok enters to app.
     *
     * @param event
     */
    @FXML
    public void handleButtonAccept(ActionEvent event) {
        try {
            Logger.getLogger(LogInController.class.getName()).log(Level.INFO, "Accept button pressed");

            User user = new User();
            user.setLogin(txtUsername.getText());
            user.setPassword(pwdPassword.getText());
            SignableFactory.getSignable().signIn(user);
            switchToLogOutWindow();
        } catch (UserNotFoundException | PasswordDoesNotMatchException | UnexpectedErrorException e) {
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, "Accept button error: {0}", e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * if introduced data ok switch to log out.
     */
    private void switchToLogOutWindow() {
        try {
            Logger.getLogger(LogInController.class.getName()).log(Level.INFO, "LogOut button pressed.");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LogOutWindow.fxml"));
            Parent root = (Parent) loader.load();
            LogOutController controller = (loader.getController());
            controller.setStage(stage);
            controller.initStage(root,txtUsername.getText().toString());
        } catch (IOException e) {
            //traza al pulsar un botón
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, "Switch to log out error: {0}", e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not change to Log Out window.", ButtonType.OK);
            alert.showAndWait();
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, "Could not change to Log Out window.");
        }
    }
    /**
     * set True in the errors
    */
    boolean errorUsername = true;
    boolean errorPassword = true;

    /**
     * Get the acctual Stage
     * @return the stage
    */
    public Stage getStage() {
        return stage;
    }

    /**
     * Set the Stage
     * @param stage
    */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the scene and its components
     *
     * @param root
     */
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Log In");
        stage.setResizable(false);
        txtUsername.requestFocus();
        txtUsername.textProperty().addListener(this::textChangedUser);
        pwdPassword.textProperty().addListener(this::textChangedPassword);
        btnAccept.setDisable(true);
        lblErrorLogin.setVisible(false);
        lblErrorPassword.setVisible(false);
        btnAccept.setTooltip(
                new Tooltip("Click to send credentials "));
        btnSignUp.setTooltip(
                new Tooltip("Click to navigate to Sign Up"));
        btnAccept.setDefaultButton(true);
        stage.show();
        Logger.getLogger(LogInController.class.getName()).log(Level.INFO, "Switched to Log In window.");
    }

    /**
     * Tests if there is any error on every pwdPassword text propertie change.
     *
     * @param obs
     */
    private void textChangedPassword(Observable obs) {
        Integer pwdLenght = pwdPassword.getText().trim().length();
        if (pwdLenght < 6 || pwdLenght > 255) {
            errorPassword = true;
            lblErrorPassword.setVisible(true);
        } else {
            errorPassword = false;
            lblErrorPassword.setVisible(false);
        }
        //if typing and then erasing, reminder that field must be greater than 6
        if (pwdLenght < 6) {
            lblErrorPassword.setText("* Must be at least 6 characters");
        }
        //if typing and then erasing and exceeding max characters, reminding not to do it 
        if (pwdLenght > 255) {
            lblErrorPassword.setText("* Must be less than 255 characters");
        }

        testInputErrors();
    }

    /**
     * Tests if there is any error on every txtUsername text propertie change.
     *
     * @param obs
     */
    private void textChangedUser(Observable obs) {
        Integer usLenght = txtUsername.getText().trim().length();
        //if username =0 or <255= error
        if (usLenght == 0 || usLenght > 255) {
            errorUsername = true;
            lblErrorLogin.setVisible(true);
        } else {
            errorUsername = false;
            lblErrorLogin.setVisible(false);
        }
        //if typing and then erasing, reminder that field must not be empty
        if (usLenght == 0) {
            lblErrorLogin.setText("* Field must not be empty");
        }
        //if typing and then erasing and exceeding max characters, reminding not to do it      
        if (usLenght > 255) {
            lblErrorLogin.setText("* Must be less than 255 characters");
        }

        testInputErrors();
    }

    /**
     * Checks if there is any input error and disables btnAccept if so.
     */
    private void testInputErrors() {
        if (errorPassword || errorUsername) {
            btnAccept.setDisable(true);
        } else {
            btnAccept.setDisable(false);
        }
    }
}
