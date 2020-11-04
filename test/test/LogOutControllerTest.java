/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import application.ClientApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.testfx.framework.junit.ApplicationTest;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isFocused;
import static org.testfx.matcher.base.NodeMatchers.isInvisible;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import view.LogOutController;

/**
 * Testing class for Log Out and controller. TestFX framework.
 *
 * @author Lorena
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LogOutControllerTest extends ApplicationTest {

    /**
     * Starts application to be tested.
     *
     * @param stage Primary Stage object
     * @throws Exception if we have any error
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LogOutWindow.fxml"));
        Parent root = (Parent) loader.load();
        LogOutController controller = (loader.getController());
        controller.setStage(stage);
        controller.initStage(root);
    }

    @Test
    public void test1() {
        clickOn("#btnLogOut");
        verifyThat("#btnSignUp", isEnabled());

    }
}
