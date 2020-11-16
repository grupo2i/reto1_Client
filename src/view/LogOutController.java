package view;

import java.io.IOException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import user.User;

/**
 * Controls the logOut window behaviou.
 *
 * @author Ander Vicente
 */
public class LogOutController {

    @FXML
    private Stage stage;
    @FXML
    private Button btnLogOut;
    @FXML
    private Button btnExit;
    @FXML
    private Label lblGreeting;
    private User user;

    /**
     * Switches to the Log in window.
     * And the alert confirmation is showed
     * @param event
     */
    @FXML
    public void handleButtonLogOut(ActionEvent event) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setTitle("Confirmación");
            alert.setContentText("¿Estas seguro de que quieres salir?");
            Optional<ButtonType> action = alert.showAndWait();
            if (action.get() != ButtonType.OK) {
                this.btnExit.setText("Cancelamos");
            } else {
                this.btnExit.setText("Adiós");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LogInWindow.fxml"));
                Parent root = (Parent) loader.load();
                LogInController controller = (loader.getController());
                controller.setStage(stage);
                controller.initStage(root);
            }

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not change to Sign In window.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Closes application with the alert confirmation.
     *
     * @param event
     */
    @FXML
    public void handleButtonExit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirmación");
        alert.setContentText("¿Estas seguro de que quieres salir?");
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() != ButtonType.OK) {
            this.btnExit.setText("Cancelamos");
        } else {
            this.btnExit.setText("Adiós");
            stage.close();
        }
    }

    /**
     * Get the acctual Stage
     *
     * @return the stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Set the acctual Stage
     *
     * @param stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Get the User
     *
     * @param user
     */
    public void getUser(User user) {
        this.user = user;
    }

    /**
     * Initializes the scene and its components
     *
     * @param root
     * @param user
     */
    public void initStage(Parent root, String user) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Log Out");
        stage.setResizable(false);
        stage.setOnCloseRequest((event) -> this.exitApplication(event));
        lblGreeting.setText("Hola " + user);
        btnLogOut.setTooltip(
                new Tooltip("Click to log out "));
        btnExit.setTooltip(
                new Tooltip("Click to close application"));
        btnLogOut.setDefaultButton(true);
        stage.show();

    }

    /**
     * Initializates the Stage
     *
     * @param root
     */
    void initStage(Parent root) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Put the confirmation alert on the button exit of the application
     *
     * @param event
     */
    private void exitApplication(WindowEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirmación");
        alert.setContentText("¿Estas seguro de que quieres salir?");
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() != ButtonType.OK) {
            this.btnExit.setText("Cancelamos");
        } else {
            this.btnExit.setText("Adiós");
            stage.close();
        }
    }

}
