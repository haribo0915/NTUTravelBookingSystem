package org.oop18.controllers;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.oop18.entities.Order;
import org.oop18.entities.Product;
import org.oop18.entities.User;
import org.oop18.exceptions.CreateException;
import org.oop18.models.OrderAdapter;
import org.oop18.models.OrderAdapterFactory;
import org.oop18.models.ProductAdapter;
import org.oop18.models.ProductAdapterFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

/**
 * @author - Haribo
 */
public class OrderDialogController implements Initializable {
    private ProductAdapterFactory productAdapterFactory;
    private ProductAdapter productAdapter;
    private OrderAdapterFactory orderAdapterFactory;
    private OrderAdapter orderAdapter;
    private Product product;
    private User currentUser;
    private Order order;

    @FXML
    private TextField productTitleTextField;
    @FXML
    private TextField adultCountTextField;
    @FXML
    private TextField childrenCountTextField;
    @FXML
    private TextField productStartDateTextField;
    @FXML
    private TextField productEndDateTextField;

    public OrderDialogController(OrderAdapterFactory orderAdapterFactory, Product product, User currentUser) {
        this.orderAdapterFactory = orderAdapterFactory;
        this.orderAdapter = orderAdapterFactory.create();
        this.product = product;
        this.currentUser = currentUser;
        this.order = new Order();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productTitleTextField.setText(product.getTitle());
        productStartDateTextField.setText(String.valueOf(product.getStartDate()));
        productEndDateTextField.setText(String.valueOf(product.getEndDate()));
    }

    public void saveOrderHandler(Event event) {
        try {
            Integer adultCount = Integer.valueOf(adultCountTextField.getText());
            Integer childrenCount = Integer.valueOf(childrenCountTextField.getText());
            Integer totalPrice = (adultCount + childrenCount) * product.getPrice();

            order.setUserId(currentUser.getId());
            order.setProductId(product.getId());
            order.setAdultCount(adultCount);
            order.setChildrenCount(childrenCount);
            order.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            order.setTotalPrice(totalPrice);

            order = orderAdapter.createOrder(order);

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.close();
        } catch (CreateException e) {
            loadErrorBoxView((ActionEvent) event, e.getMessage());
        } catch (Exception e) {
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
