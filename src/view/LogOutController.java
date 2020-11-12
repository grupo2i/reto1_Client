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
     *
     * @param event
     */
    @FXML
    public void handleButtonLogOut(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LogInWindow.fxml"));
            Parent root = (Parent) loader.load();
            LogInController controller = (loader.getController());
            controller.setStage(stage);
            controller.initStage(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not change to Sign In window.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Closes application.
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
        if (action.get() == ButtonType.OK) {
            this.btnExit.setText("Adiós");
            stage.close();
        } else {
            this.btnExit.setText("Cancelamos");
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
     */
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Log Out");
        stage.setResizable(false);
        lblGreeting.setText("Hola " + user);
        btnLogOut.setTooltip(
                new Tooltip("Click to log out "));
        btnExit.setTooltip(
                new Tooltip("Click to close application"));
        btnLogOut.setDefaultButton(true);
        stage.show();

    }

}
