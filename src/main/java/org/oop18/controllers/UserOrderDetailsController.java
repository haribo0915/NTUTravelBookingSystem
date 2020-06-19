package org.oop18.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.oop18.entities.Order;
import org.oop18.entities.Product;
import org.oop18.entities.User;
import org.oop18.models.OrderAdapter;
import org.oop18.models.OrderAdapterFactory;
import org.oop18.models.ProductAdapter;
import org.oop18.models.ProductAdapterFactory;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

/**
 * @author - Haribo
 */
public class UserOrderDetailsController implements Initializable {
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


    public UserOrderDetailsController(ProductAdapterFactory productAdapterFactory, OrderAdapterFactory orderAdapterFactory, Order selectedUserOrder, User currentUser) {
        this.productAdapterFactory = productAdapterFactory;
        this.productAdapter = productAdapterFactory.create();
        this.orderAdapterFactory = orderAdapterFactory;
        this.orderAdapter = orderAdapterFactory.create();
        this.currentUser = currentUser;
        this.order = selectedUserOrder;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.product = productAdapter.queryProduct(order.getProductId());
            productTitleTextField.setText(product.getTitle());
            adultCountTextField.setText(String.valueOf(order.getAdultCount()));
            childrenCountTextField.setText(String.valueOf(order.getChildrenCount()));
            productStartDateTextField.setText(String.valueOf(product.getStartDate()));
            productEndDateTextField.setText(String.valueOf(product.getEndDate()));
        } catch (Exception e) {
            e.printStackTrace();
        }
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

            order = orderAdapter.updateOrder(order);

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
