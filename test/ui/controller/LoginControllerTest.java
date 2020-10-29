/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import application.ClientApplication;
import javafx.stage.Stage;
import org.junit.BeforeClass;
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


/**
 * Testing class for Login view and controller. Tests login view behavior using
 * TestFX framework.
 *
 * @author Lorena
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginControllerTest extends ApplicationTest {

    private static final String OVERSIZED_TEXT = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

    /**
     * Starts application to be tested.
     *
     * @param stage Primary Stage object
     * @throws Exception if we have any error
     */
    @Override
    public void start(Stage stage) throws Exception {
        new ClientApplication().start(stage);
    }

    /**
     * Test the login view.
     */
    @Test
    public void test1_InitialState() {
        verifyThat("#txtUsername", isEnabled());
        verifyThat("#pwdPassword", isEnabled());
        verifyThat("#btnAccept", isDisabled());
        verifyThat("#lblErrorLogin", isInvisible());
        verifyThat("#lblErrorPassword", isInvisible());
        verifyThat("#txtUsername", isFocused());

    }

    @Test
    public void test2_TxtUsuarioMaximumLength() {
        doubleClickOn("#txtUsername");
        write(OVERSIZED_TEXT);
        verifyThat("#btnAccept", isDisabled());
        verifyThat("* Must be less than 255 characters", isVisible());
    }

    @Test
    public void test3_PwdPasswordMaximumLength() {
        doubleClickOn("#pwdPassword");
        write(OVERSIZED_TEXT);
        verifyThat("#btnAccept", isDisabled());
        verifyThat("* Must be less than 255 characters", isVisible());
    }

    @Test
    public void test4_TxtUsuarioNotEmpty() {
        doubleClickOn("#txtUsername");
        write("usuario1234");
        verifyThat("#btnAccept", isDisabled());
        eraseText(11);
        verifyThat("* Field must not be empty", isVisible());
    }

    @Test
    public void test5_TxtUsuarioIsCorrect() {
        doubleClickOn("#txtUsername");
        write("usuario1234");
        doubleClickOn("#pwdPassword");
        write("usuario1234");
        verifyThat("#btnAccept", isEnabled());
    }

    @Test
    public void test6_PwdPasswordMinimumLength() {
        doubleClickOn("#pwdPassword");
        write("123");
        verifyThat("#btnAccept", isDisabled());
        verifyThat("* Must be at least 6 characters", isVisible());
    }

    @Test
    public void test7_PwdPasswordIsCorrect() {
        doubleClickOn("#txtUsername");
        write("usuario1234");
        doubleClickOn("#pwdPassword");
        write("123456");
        verifyThat("#btnAccept", isEnabled());
    }
}
