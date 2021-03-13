package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.CommunicationService;
import sample.Serializers.OrderSerializer;
import sample.Serializers.ProductSerializer;
import sample.Switcher;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class tableOrderController implements Initializable {

    @FXML
    private TableView<OrderSerializer> orderTable;

    @FXML
    private TableColumn<OrderSerializer, String> orderColumnUer;

    @FXML
    private TableColumn<OrderSerializer, String> orderColumnCustomer;

    @FXML
    private TableColumn<OrderSerializer, String> orderColumnProduct;

    @FXML
    private TableColumn<OrderSerializer, Integer> orderColumnQuantity;

    @FXML
    private TableColumn<OrderSerializer, String> orderColumnComments;

    @FXML
    private TableColumn<OrderSerializer, Integer> orderColumnPrice;

    @FXML
    private Button productDeleteButton;

    @FXML
    private Button productModifyButton;

    @FXML
    private Button productBackButton;

    Registry registry = LocateRegistry.getRegistry();
    CommunicationService server = (CommunicationService)registry.lookup("WarehouseService");
    String message;
    Switcher switcher;

    ArrayList<OrderSerializer> orderSerializers = server.allOrders();

    ObservableList<OrderSerializer> orders = FXCollections.observableList(orderSerializers);


    public tableOrderController() throws RemoteException, NotBoundException, SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        orderColumnUer.setCellValueFactory(new PropertyValueFactory<OrderSerializer, String>("user"));
        orderColumnCustomer.setCellValueFactory(new PropertyValueFactory<OrderSerializer, String>("customer"));
        orderColumnProduct.setCellValueFactory(new PropertyValueFactory<OrderSerializer, String>("product"));
        orderColumnQuantity.setCellValueFactory(new PropertyValueFactory<OrderSerializer, Integer>("quantity"));
        orderColumnComments.setCellValueFactory(new PropertyValueFactory<OrderSerializer, String>("comments"));
        orderColumnPrice.setCellValueFactory(new PropertyValueFactory<OrderSerializer, Integer>("price"));

        orderTable.setItems(orders);
    }

    public void backButtonClicked() throws IOException {
        switcher = new Switcher();
        Stage stage = (Stage) productBackButton.getScene().getWindow();
        switcher.switchScene(stage, "home.fxml");
    }

    private ObservableList<OrderSerializer> getTable() {
        ObservableList<OrderSerializer> productSelected;
        productSelected = orderTable.getSelectionModel().getSelectedItems();

        return productSelected;
    }

    public void deleteButtonClicked() throws RemoteException, SQLException {
        ObservableList<OrderSerializer> orderSelected = getTable();

        for (OrderSerializer a : orderSelected) {
            System.out.println(a.getId());
            server.deleteOrder(a.getId());
        }
        ArrayList<OrderSerializer> orderSerializers = server.allOrders();

        ObservableList<OrderSerializer> orders = FXCollections.observableList(orderSerializers);

        orderTable.setItems(orders);
    }

    public void modifyButtonClicked() throws IOException, SQLException {
        ObservableList<OrderSerializer> orderSelected = getTable();

        if (!orderSelected.isEmpty()) {
            for (OrderSerializer a : orderSelected) {
                server.modifyOrder(a.getId());
            }

            switcher = new Switcher();
            Stage stage = (Stage) productModifyButton.getScene().getWindow();
            switcher.switchScene(stage, "modifyOrder.fxml");
        }
    }

}
