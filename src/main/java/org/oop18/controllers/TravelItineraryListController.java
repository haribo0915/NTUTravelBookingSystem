package org.oop18.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.oop18.entities.Product;
import org.oop18.entities.TravelCode;
import org.oop18.entities.User;
import org.oop18.exceptions.EntryNotFoundException;
import org.oop18.models.OrderAdapterFactory;
import org.oop18.models.ProductAdapter;
import org.oop18.models.ProductAdapterFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;

/**
 * @author - Haribo
 */
public class TravelItineraryListController implements Initializable {
    private ProductAdapterFactory productAdapterFactory;
    private ProductAdapter productAdapter;
    private OrderAdapterFactory orderAdapterFactory;
    private User currentUser;
    private ExecutorService cachedThreadPool = SingletonCachedThreadPool.getInstance();

    @FXML
    private ComboBox<String> travelCodeComboBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button queryProductsBtn;
    @FXML
    private Button createRestaurantBtn;
    @FXML
    private Button queryUserOrdersBtn;
    @FXML
    private Button createOrderBtn;

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, String> productTitleCol;
    @FXML
    private TableColumn<Product, Integer> productPriceCol;
    @FXML
    private TableColumn<Product, Timestamp> productStartDateCol;
    @FXML
    private TableColumn<Product, Timestamp> productEndDateCol;
    @FXML
    private TableColumn<Product, Integer> productLowerBoundCol;
    @FXML
    private TableColumn<Product, Integer> productUpperBoundCol;

    @FXML
    private ObservableList<Product> productTableObservableList = FXCollections.observableArrayList();
    @FXML
    private ObservableList<String> travelCodeComboBoxObservableList = FXCollections.observableArrayList();

    public TravelItineraryListController(ProductAdapterFactory productAdapterFactory, OrderAdapterFactory orderAdapterFactory, User currentUser) {
        this.productAdapterFactory = productAdapterFactory;
        this.productAdapter = productAdapterFactory.create();
        this.orderAdapterFactory = orderAdapterFactory;
        this.currentUser = currentUser;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Product> productList = new ArrayList<>();
        try {
            productList = productAdapter.queryProducts(null, null);
            productTableObservableList.addAll(productList);
            productTable.setItems(productTableObservableList);

            List<TravelCode> travelCodeList = productAdapter.queryTravelCodes();
            for (TravelCode travelCode: travelCodeList) {
                travelCodeComboBoxObservableList.add(travelCode.getTravelCodeName());
            }
            travelCodeComboBox.setItems(travelCodeComboBoxObservableList);

        } catch (EntryNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            initProductTable();
            //queryProductsBtn.setDisable(true);
        }
    }

    private void initProductTable() {
        productTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        productStartDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        productEndDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        productLowerBoundCol.setCellValueFactory(new PropertyValueFactory<>("lowerBound"));
        productUpperBoundCol.setCellValueFactory(new PropertyValueFactory<>("upperBound"));
    }

    private void refreshProductTable(List<Product> productList) {
        productTableObservableList.clear();
        productTableObservableList.addAll(productList);
        productTable.setItems(productTableObservableList);
    }

    public void queryProductsHandler(Event event) {
        cachedThreadPool.execute(() -> {
            String travelCodeName = travelCodeComboBox.getValue();
            String localDateStr = datePicker.getEditor().getText();

            List<Product> productList = new ArrayList<>();
            try {
                DateFormat formatter = new SimpleDateFormat("yyyy/M/d");

                TravelCode travelCode = (travelCodeName == null || travelCodeName.equals(""))? null : productAdapter.queryTravelCode(travelCodeName);
                Timestamp startDate = (localDateStr == null || localDateStr.equals(""))? null : new Timestamp(formatter.parse(localDateStr).getTime());

                productList = productAdapter.queryProducts(travelCode, startDate);
            } catch (EntryNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                refreshProductTable(productList);
            }
        });
    }

    public void createOrderHandler(Event event) {
        cachedThreadPool.execute(() -> {
            try {
                Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
                Platform.runLater(() -> loadOrderDialogView(event, selectedProduct));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void loadOrderDialogView(Event event, Product selectedProduct) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/OrderDialog.fxml"));

            OrderDialogController orderDialogController = new OrderDialogController(orderAdapterFactory, selectedProduct, this.currentUser);
            loader.setController(orderDialogController);

            Parent CommentDialogParent = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("New Order");
            stage.setScene(new Scene(CommentDialogParent));
            stage.sizeToScene();
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void queryUserOrdersHandler(Event event) {
        cachedThreadPool.execute(() -> {
            try {
                Platform.runLater(() -> loadUserOrderListView(event));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void loadUserOrderListView(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserOrderList.fxml"));

            UserOrderListController userOrderListController = new UserOrderListController(productAdapterFactory, orderAdapterFactory, this.currentUser);
            loader.setController(userOrderListController);

            Parent userOrderListParent = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Order List");
            stage.setScene(new Scene(userOrderListParent));
            stage.sizeToScene();
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
