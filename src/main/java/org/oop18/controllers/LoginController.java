package org.oop18.controllers;

import javafx.event.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.oop18.entities.User;
import org.oop18.exceptions.QueryException;
import org.oop18.models.*;

import java.io.IOException;

/**
 * @author - Haribo
 */
public class LoginController {
    private UserAdapterFactory userAdapterFactory;
    private ProductAdapterFactory productAdapterFactory;
    private OrderAdapterFactory orderAdapterFactory;

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

        try {
            UserAdapter userAdapter = userAdapterFactory.create();
            User currentUser = userAdapter.queryUser(userName.getText(), password.getText());
            System.out.println("login success!!");
            loadTravelItineraryListView(event, currentUser);
        } catch (QueryException e) {
            System.out.println(e.getMessage());
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

    public void registerHandler(ActionEvent event) {
        try {
            loadRegisterView(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadRegisterView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Register.fxml"));

            RegisterController registerController = new RegisterController(userAdapterFactory, productAdapterFactory, orderAdapterFactory);
            loader.setController(registerController);

            Parent registerParent = loader.load();
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            //stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(registerParent));
            stage.setTitle("Register");
            stage.sizeToScene();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
