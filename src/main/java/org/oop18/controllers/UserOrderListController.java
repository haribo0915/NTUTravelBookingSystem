package org.oop18.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.oop18.entities.Order;
import org.oop18.entities.Product;
import org.oop18.entities.User;
import org.oop18.models.OrderAdapter;
import org.oop18.models.OrderAdapterFactory;
import org.oop18.models.ProductAdapter;
import org.oop18.models.ProductAdapterFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author - Haribo
 */
public class UserOrderListController implements Initializable {
    private ProductAdapterFactory productAdapterFactory;
    private ProductAdapter productAdapter;
    private OrderAdapterFactory orderAdapterFactory;
    private OrderAdapter orderAdapter;
    private User currentUser;

    @FXML
    private Button querySelectedUserOrderBtn;
    @FXML
    private Button deleteSelectedUserOrderBtn;

    @FXML
    private TableView<Order> orderTable;
    @FXML
    private TableColumn<Order, Integer> orderIdCol;
    @FXML
    private TableColumn<Product, Integer> orderProductIdCol;
    @FXML
    private TableColumn<Product, Integer> orderAdultCountCol;
    @FXML
    private TableColumn<Product, Integer> orderChildrenCountCol;
    @FXML
    private TableColumn<Product, Integer> orderTotalPriceCol;
    @FXML
    private TableColumn<Product, Timestamp> orderCreatedTimeCol;

    @FXML
    private ObservableList<Order> orderTableObservableList = FXCollections.observableArrayList();

    public UserOrderListController(ProductAdapterFactory productAdapterFactory, OrderAdapterFactory orderAdapterFactory, User currentUser) {
        this.productAdapterFactory = productAdapterFactory;
        this.productAdapter = productAdapterFactory.create();
        this.orderAdapterFactory = orderAdapterFactory;
        this.orderAdapter = orderAdapterFactory.create();
        this.currentUser = currentUser;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Order> orderList = orderAdapter.queryOrders(currentUser.getId());

        orderTableObservableList.addAll(orderList);
        orderTable.setItems(orderTableObservableList);

        initOrderTable();
    }

    private void initOrderTable() {
        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderProductIdCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        orderAdultCountCol.setCellValueFactory(new PropertyValueFactory<>("adultCount"));
        orderChildrenCountCol.setCellValueFactory(new PropertyValueFactory<>("childrenCount"));
        orderTotalPriceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        orderCreatedTimeCol.setCellValueFactory(new PropertyValueFactory<>("createdTime"));
    }

    private void refreshOrderTable(List<Order> orderList) {
        orderTableObservableList.clear();
        orderTableObservableList.addAll(orderList);
        orderTable.setItems(orderTableObservableList);
    }

    public void querySelectedUserOrderHandler(Event event) {
        try {
            Order selectedUserOrder = orderTable.getSelectionModel().getSelectedItem();
            loadUserOrderDetailsView(event, selectedUserOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUserOrderDetailsView(Event event, Order selectedUserOrder) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/OrderDialog.fxml"));

            UserOrderDetailsController userOrderDetailsController = new UserOrderDetailsController(productAdapterFactory, orderAdapterFactory, selectedUserOrder, this.currentUser);
            loader.setController(userOrderDetailsController);

            Parent UserCommentDetailsParent = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Order Details");
            stage.setScene(new Scene(UserCommentDetailsParent));
            stage.sizeToScene();
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteSelectedUserOrderHandler(Event event) {
        List<Order> orderList = new ArrayList<>();
        try {
            Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
            selectedOrder = orderAdapter.deleteOrder(selectedOrder);

            orderList = orderAdapter.queryOrders(currentUser.getId());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            refreshOrderTable(orderList);
        }
    }
}
