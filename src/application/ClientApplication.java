/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import view.LogInController;

/**
 *
 * @author aitor
 */

public class ClientApplication extends Application {
       @Override
    public void start(Stage primaryStage) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LogInWindow.fxml"));
        Parent root =(Parent)loader.load();
        LogInController controller = (loader.getController());
        primaryStage.setResizable(Boolean.FALSE);
        controller.setStage(primaryStage);
        controller.initStage(root); 
        
}
        public static void main(String[] args) {
        launch(args);
        
    }


}