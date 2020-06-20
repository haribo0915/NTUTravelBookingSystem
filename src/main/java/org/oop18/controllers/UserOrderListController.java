package org.oop18.controllers;

import javafx.application.Platform;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.oop18.entities.Order;
import org.oop18.entities.Product;
import org.oop18.entities.User;
import org.oop18.exceptions.EntryNotFoundException;
import org.oop18.exceptions.UpdateException;
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
import java.util.concurrent.ExecutorService;

/**
 * The user order list controller is used to show all the orders
 * of specific user, delete or select the specific order.
 *
 * @author - Haribo
 */
public class UserOrderListController implements Initializable {
    private ProductAdapterFactory productAdapterFactory;
    private ProductAdapter productAdapter;
    private OrderAdapterFactory orderAdapterFactory;
    private OrderAdapter orderAdapter;
    private User currentUser;
    private ExecutorService cachedThreadPool = SingletonCachedThreadPool.getInstance();

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

    /**
     * Initialize the order table view of specific user.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        List<Order> orderList = new ArrayList<>();
        try {
            orderList = orderAdapter.queryOrders(currentUser.getId());
        } catch (EntryNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            orderTableObservableList.addAll(orderList);
            orderTable.setItems(orderTableObservableList);
            initOrderTable();

            querySelectedUserOrderBtn.setDisable(true);
            deleteSelectedUserOrderBtn.setDisable(true);
        }
    }

    private void initOrderTable() {
        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderProductIdCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        orderAdultCountCol.setCellValueFactory(new PropertyValueFactory<>("adultCount"));
        orderChildrenCountCol.setCellValueFactory(new PropertyValueFactory<>("childrenCount"));
        orderTotalPriceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        orderCreatedTimeCol.setCellValueFactory(new PropertyValueFactory<>("createdTime"));
    }

    public void userClickedOnOrderTable(MouseEvent event) {
        querySelectedUserOrderBtn.setDisable(false);
        deleteSelectedUserOrderBtn.setDisable(false);
    }

    private void refreshAllOrderTable() {
        List<Order> orderList = new ArrayList<>();

        try {
            orderList = orderAdapter.queryOrders(currentUser.getId());
        } catch (EntryNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            refreshOrderTable(orderList);
        }
    }

    private void refreshOrderTable(List<Order> orderList) {
        orderTableObservableList.clear();
        orderTableObservableList.addAll(orderList);
        orderTable.setItems(orderTableObservableList);
    }

    /**
     * Load the specific order details
     *
     * @param event
     */
    public void querySelectedUserOrderHandler(Event event) {
        try {
            Order selectedUserOrder = orderTable.getSelectionModel().getSelectedItem();
            loadUserOrderDetailsView(event, selectedUserOrder);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            refreshAllOrderTable();
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

    /**
     * Delete the specific order if there are at least 10 days left before
     * the start date of the trip; otherwise it will pop up an alert box to
     * restrict the user to delete it.
     *
     * @param event
     */
    public void deleteSelectedUserOrderHandler(Event event) {
        cachedThreadPool.execute(() -> {
            try {
                Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
                selectedOrder = orderAdapter.deleteOrder(selectedOrder);

            } catch (EntryNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (UpdateException e) {
                Platform.runLater(() -> loadErrorBoxView((ActionEvent) event, e.getMessage()));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                refreshAllOrderTable();
            }
        });
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
