package org.oop18.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.oop18.entities.User;
import org.oop18.exceptions.EntryNotFoundException;
import org.oop18.models.OrderAdapterFactory;
import org.oop18.models.ProductAdapterFactory;
import org.oop18.models.UserAdapter;
import org.oop18.models.UserAdapterFactory;

import java.awt.desktop.SystemSleepEvent;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author - Haribo
 */
public class LoginController {
    private UserAdapterFactory userAdapterFactory;
    private ProductAdapterFactory productAdapterFactory;
    private OrderAdapterFactory orderAdapterFactory;
    private ExecutorService cachedThreadPool = SingletonCachedThreadPool.getInstance();

    @FXML
    private TextField userName;
    @FXML
    private PasswordField password;

    public LoginController() {
    }

    public void setUserAdapterFactory(UserAdapterFactory userAdapterFactory) {
        this.userAdapterFactory = userAdapterFactory;
    }

    public void setProductAdapterFactory(ProductAdapterFactory productAdapterFactory) {
        this.productAdapterFactory = productAdapterFactory;
    }

    public void setOrderAdapterFactory(OrderAdapterFactory orderAdapterFactory) {
        this.orderAdapterFactory = orderAdapterFactory;
    }

    public void loginHandler(ActionEvent event) {
        cachedThreadPool.execute(() -> {
            try {
                UserAdapter userAdapter = userAdapterFactory.create();
                User currentUser = userAdapter.queryUser(userName.getText(), password.getText());
                Platform.runLater(() -> loadTravelItineraryListView(event, currentUser));
            } catch (EntryNotFoundException e) {
                Platform.runLater(() -> loadErrorBoxView(event, e.getMessage()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void loadTravelItineraryListView(ActionEvent event, User currentUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/TravelItineraryList.fxml"));

            TravelItineraryListController travelItineraryListController = new TravelItineraryListController(this.productAdapterFactory, this.orderAdapterFactory, currentUser);
            loader.setController(travelItineraryListController);

            Parent travelItineraryListParent = loader.load();
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setTitle("Travel Itinerary List");
            stage.setScene(new Scene(travelItineraryListParent));
            stage.sizeToScene();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerHandler(ActionEvent event) {
        cachedThreadPool.execute(() -> {
            try {
                Platform.runLater(() -> loadRegisterView(event));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void loadRegisterView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Register.fxml"));

            RegisterController registerController = new RegisterController(userAdapterFactory, productAdapterFactory, orderAdapterFactory);
            loader.setController(registerController);

            Parent registerParent = loader.load();
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(registerParent));
            stage.setTitle("Register");
            stage.sizeToScene();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadErrorBoxView(ActionEvent event, String errorMessage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ErrorBox.fxml"));

            ErrorBoxController errorBoxController = new ErrorBoxController(errorMessage);
            loader.setController(errorBoxController);

            Parent ErrorBoxParent = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Error");
            stage.setScene(new Scene(ErrorBoxParent));
            stage.sizeToScene();
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
