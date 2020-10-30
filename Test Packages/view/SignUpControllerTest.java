/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isInvisible;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

/**
 * Test class for the SignUpController.
 * @author Martin Angulo <martin.angulo at tartanga.eus>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SignUpControllerTest extends ApplicationTest {
    /** Maximum length of the text field inputs. */
    private static final int MAX_TEXT_LENGTH = 255;
    /** String of size MAX_TEXT_LENGTH + 1 for testing. */
    private static String longString = "";
    
    /**
     * Initializes testing variables.
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void initialize() throws Exception {
        for(int i = 0; i <= MAX_TEXT_LENGTH; ++i)
            longString += "A";
    }
    
    /**
     * Starts the SignUp window.
     * @param stage Primary stage.
     * @throws java.lang.Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignUpWindow.fxml"));
        Parent root =(Parent)loader.load();
        SignUpController controller = (loader.getController());
        controller.setStage(stage);
        controller.initStage(root); 
    }
    
    /**
     * Tests the initial stage of the window.
     */
    @Test
    public void testA_initialState(){
        //Texts
        verifyThat("#txtUsername", hasText(""));
        verifyThat("#txtUsername", isVisible());
        verifyThat("#txtUsername", isEnabled());
        verifyThat("#txtUsername", (TextField t) -> t.isFocused());
        verifyThat("#lblErrorUsername", isInvisible());
        
        verifyThat("#txtName", hasText(""));
        verifyThat("#txtName", isVisible());
        verifyThat("#txtName", isEnabled());
        verifyThat("#lblErrorName", isInvisible());
        
        verifyThat("#txtEmail", hasText(""));
        verifyThat("#txtEmail", isVisible());
        verifyThat("#txtEmail", isEnabled());
        verifyThat("#lblErrorEmail", isInvisible());
        
        verifyThat("#pwdPassword", hasText(""));
        verifyThat("#pwdPassword", isVisible());
        verifyThat("#pwdPassword", isEnabled());
        verifyThat("#lblErrorPassword", isInvisible());
        
        verifyThat("#pwdConfirmPassword", hasText(""));
        verifyThat("#pwdConfirmPassword", isVisible());
        verifyThat("#pwdConfirmPassword", isEnabled());
        verifyThat("#lblErrorConfirmPassword", isInvisible());
        //Buttons
        verifyThat("#btnLogin", isVisible());
        verifyThat("#btnLogin", isEnabled());
        verifyThat("#btnAccept", isVisible());
        verifyThat("#btnAccept", isDisabled());
    }
    
    /**
     * Tests the window when all data is set correctly.
     */
    @Test
    public void testB_fillAllData(){
        clickOn("#txtUsername");
        write("username");
        verifyThat("#btnAccept", isDisabled());
        clickOn("#txtName");
        write("name");
        verifyThat("#btnAccept", isDisabled());
        clickOn("#txtEmail");
        write("email@email.com");
        verifyThat("#btnAccept", isDisabled());
        clickOn("#pwdPassword");
        write("abcd1234");
        verifyThat("#btnAccept", isDisabled());
        clickOn("#pwdConfirmPassword");
        write("abcd1234");
        verifyThat("#btnAccept", isEnabled());
    }
    
    /**
     * Tests that the accept button is disabled when all text fields are empty.
     */
    @Test
    public void testC_emptyTexts() {
        clickOn("#txtUsername");
        write("");
        clickOn("#txtName");
        write("");
        clickOn("#txtEmail");
        write("");
        clickOn("#pwdPassword");
        write("");
        clickOn("#pwdConfirmPassword");
        write("");
        
        verifyThat("#btnAccept", isDisabled());
    }
    
    /**
     * Tests that the error labels are visible when the input texts are longer
     * than MAX_TEXT_LENGTH and that the accept button is disabled.
     */
    @Test
    //@Ignore
    public void testD_maxLengthText() {
        String maxLengthError = "* Must be less than 255 characters";
        clickOn("#txtUsername");
        write(longString);
        verifyThat("#lblErrorUsername", isVisible());
        verifyThat(maxLengthError, isVisible());
        clickOn("#txtName");
        write(longString);
        verifyThat("#lblErrorName", isVisible());
        verifyThat(maxLengthError, isVisible());    
        clickOn("#txtEmail");
        write(longString);
        verifyThat("#lblErrorEmail", isVisible());
        verifyThat(maxLengthError, isVisible());
        clickOn("#pwdPassword");
        write(longString);
        verifyThat("#lblErrorPassword", isVisible());
        verifyThat(maxLengthError, isVisible());
        
        verifyThat("#btnAccept", isDisabled());
    }
    
    /**
     * Tests that the error message is shown when the password is too short .
     */
    @Test
    public void testE_shortPassword() {
        clickOn("#pwdPassword");
        write("12345");
        verifyThat("#lblErrorPassword", isVisible());
        verifyThat("* Must be at least 6 characters", isVisible());
    }
    
    /**
     * Tests that the error messages are shown when the user or email formats
     * are incorrect.
     */
    @Test
    public void testF_userAndEmailFormats() {
        clickOn("#txtName");
        write("12345");
        verifyThat("* Must only contain letters", isVisible());
        clickOn("#txtEmail");
        write("12345");
        verifyThat("* Must match the pattern example@example.com", isVisible());
    }
}
