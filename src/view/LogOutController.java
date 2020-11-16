package view;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    /**
     * Switches to the Log in window.
     *
     * @param event
     */
    @FXML
    public void handleButtonLogOut(ActionEvent event) {
        try {
            Logger.getLogger(LogOutController.class.getName()).log(Level.INFO, "Log Out button pressed.");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LogInWindow.fxml"));
            Parent root = (Parent) loader.load();
            LogInController controller = (loader.getController());
            controller.setStage(stage);
            controller.initStage(root);
        } catch (IOException e) {
            Logger.getLogger(LogOutController.class.getName()).log(Level.SEVERE, "Error changing to sign in window: {0}", e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not change to Sign In window.", ButtonType.OK);
            alert.showAndWait();
            Logger.getLogger(LogOutController.class.getName()).log(Level.SEVERE, "Could not change to Sign In window.");
        }
    }

    /**
     * Closes application.
     *
     * @param event
     */
    @FXML
    public void handleButtonExit(ActionEvent event) {
        Logger.getLogger(LogOutController.class.getName()).log(Level.INFO, "Exit button pressed.");
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
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Log Out");
        stage.setResizable(false);
        btnLogOut.setTooltip(
                new Tooltip("Click to log out "));
        btnExit.setTooltip(
                new Tooltip("Click to close application"));
        btnLogOut.setDefaultButton(true);
        stage.show();
        Logger.getLogger(LogOutController.class.getName()).log(Level.INFO, "Switched to Log Out window.");
    }

}
