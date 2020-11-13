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
     * Switches to the SignUp window. Loads sign up window, gets its controller
     * and loads stage.
     *
     * @param event
     * @throws IOException case it cant switch to sign up window
     */
    @FXML
    public void handleButtonSignUp(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignUpWindow.fxml"));
            Parent root = (Parent) loader.load();
            SignUpController controller = (loader.getController());
            controller.setStage(stage);
            Logger.getLogger(LogInController.class.getName()).log(Level.INFO, "Loading Sign up stage...");
            // Loads Sign Up window.
            controller.initStage(root);
            // If window switch not succesful, shows alert and notifies the user.
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not change to Sign Up window.", ButtonType.OK);
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, "IOException: {0}", e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Loads log out window, gets its controller and loads stage. If introduced
     * fields enters log out.
     *
     * @param event
     * @throws UserNotFoundException case user is not found.
     * @throws PasswordDoesNotMatchException case password does not match.
     * @throws UnexpectedErrorException case something unexpected happens.
     * @throws IOException case it cant switch to log out window.
     */
    @FXML
    public void handleButtonAccept(ActionEvent event) throws UserNotFoundException, PasswordDoesNotMatchException, UnexpectedErrorException, IOException {
        try {
            // Creates new object user type and assigns username and password introduced by user.
            User user = new User();
            user.setLogin(txtUsername.getText());
            user.setPassword(pwdPassword.getText());
            // Validates that username and password exist and match with saved in database.
            SignableFactory.getSignable().signIn(user);
            Logger.getLogger(LogInController.class.getName()).log(Level.INFO, "Loading log out window...");
            // If data is OK switches to log out.
            switchToLogOutWindow();
            // If data NOT OK shows alert and notifies the user.
        } catch (UserNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "User was not found.", ButtonType.OK);
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, "An error occurred: {0}", e.getMessage());
            alert.showAndWait();
        } catch (PasswordDoesNotMatchException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Password does not match.", ButtonType.OK);
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, "PasswordDoesNotMatchException {0}", e.getMessage());
            alert.showAndWait();
        } catch (UnexpectedErrorException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Unexpected error.", ButtonType.OK);
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, "UnexpectedErrorException {0}", e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Loads log out view, sets it's controller and inits stage. if introduced
     * data ok switch to log out.
     *
     * @throws IOException case it can't switch to log out window.
     */
    private void switchToLogOutWindow() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LogOutWindow.fxml"));
            Parent root = (Parent) loader.load();
            LogOutController controller = (loader.getController());
            controller.setStage(stage);
            Logger.getLogger(LogInController.class.getName()).log(Level.INFO, "Loading log out stage...");
            controller.initStage(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not change to Log Out window.", ButtonType.OK);
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, "An error occurred: {0}", e.getMessage());
            alert.showAndWait();
        }
    }

    boolean errorUsername = true;
    boolean errorPassword = true;

    /**
     * Gets stage.
     *
     * @return stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Sets stage.
     *
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
        Logger.getLogger(LogInController.class.getName()).log(Level.INFO, "Initializing stage...");
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
        Logger.getLogger(LogInController.class.getName()).log(Level.INFO, "Showing stage...");
        stage.show();
    }

    /**
     * Tests if there is any error on every pwdPassword text propertie change.
     *
     * @param obs
     */
    private void textChangedPassword(Observable obs) {
        // Comprobar que la longitud del texto introducido es como mínimo de 6 caracteres y no supera los 255.
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
