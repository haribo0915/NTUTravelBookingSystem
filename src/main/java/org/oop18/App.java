package org.oop18;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.oop18.controllers.LoginController;
import org.oop18.models.StubOrderAdapterFactory;
import org.oop18.models.StubProductAdapterFactory;
import org.oop18.models.StubUserAdapterFactory;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
            Parent root = loader.load();

            LoginController loginController = loader.getController();
            loginController.setUserAdapterFactory(new StubUserAdapterFactory());
            loginController.setProductAdapterFactory(new StubProductAdapterFactory());
            loginController.setOrderAdapterFactory(new StubOrderAdapterFactory());

            stage.setTitle("NTU Travel Booking System");
            stage.setScene(new Scene(root));
            stage.sizeToScene();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}