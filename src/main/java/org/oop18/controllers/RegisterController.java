package org.oop18.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.oop18.entities.User;
import org.oop18.exceptions.EntryExistsException;
import org.oop18.models.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author - Haribo
 */
public class RegisterController implements Initializable {
    private UserAdapterFactory userAdapterFactory;
    private UserAdapter userAdapter;
    private ProductAdapterFactory productAdapterFactory;
    private ProductAdapter productAdapter;
    private OrderAdapterFactory orderAdapterFactory;
    private OrderAdapter orderAdapter;

    @FXML
    private PasswordField userPasswordTextField;
    @FXML
    private TextField userAccountTextField;

    public RegisterController(UserAdapterFactory userAdapterFactory, ProductAdapterFactory productAdapterFactory, OrderAdapterFactory orderAdapterFactory) {
        this.userAdapterFactory = userAdapterFactory;
        this.userAdapter = userAdapterFactory.create();
        this.productAdapterFactory = productAdapterFactory;
        this.productAdapter = productAdapterFactory.create();
        this.orderAdapterFactory = orderAdapterFactory;
        this.orderAdapter = orderAdapterFactory.create();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void registerHandler(ActionEvent event) {
        try {
            User user = new User(userAccountTextField.getText(), userPasswordTextField.getText());
            user = userAdapter.createUser(user);
            loadTravelItineraryListView(event, user);
        } catch (EntryExistsException e) {
            loadErrorBoxView(event, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
