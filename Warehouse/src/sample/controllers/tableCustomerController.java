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
import sample.Serializers.CustomerSerializer;
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

public class tableCustomerController implements Initializable {

    @FXML
    private TableView<CustomerSerializer> orderTable;

    @FXML
    private TableColumn<CustomerSerializer, String> customerColumnName;

    @FXML
    private TableColumn<CustomerSerializer, String> customerColumnSurname;

    @FXML
    private TableColumn<CustomerSerializer, String> customerColumnEmail;

    @FXML
    private TableColumn<CustomerSerializer, String> customerColumnPhone;

    @FXML
    private TableColumn<CustomerSerializer, String> customerColumnAddress;

    @FXML
    private Button customerDeleteButton;

    @FXML
    private Button customerModifyButton;

    @FXML
    private Button customerBackButton;

    Registry registry = LocateRegistry.getRegistry();
    CommunicationService server = (CommunicationService)registry.lookup("WarehouseService");
    String message;
    Switcher switcher;

    ArrayList<CustomerSerializer> customerSerializers = server.allCustomers();

    ObservableList<CustomerSerializer> customers = FXCollections.observableList(customerSerializers);


    public tableCustomerController() throws RemoteException, NotBoundException, SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerColumnName.setCellValueFactory(new PropertyValueFactory<CustomerSerializer, String>("name"));
        customerColumnSurname.setCellValueFactory(new PropertyValueFactory<CustomerSerializer, String>("surname"));
        customerColumnEmail.setCellValueFactory(new PropertyValueFactory<CustomerSerializer, String>("email"));
        customerColumnPhone.setCellValueFactory(new PropertyValueFactory<CustomerSerializer, String>("phoneNumber"));
        customerColumnAddress.setCellValueFactory(new PropertyValueFactory<CustomerSerializer, String>("address"));

        orderTable.setItems(customers);
    }

    private ObservableList<CustomerSerializer> getTable() {
        ObservableList<CustomerSerializer> customerSelected;
        customerSelected = orderTable.getSelectionModel().getSelectedItems();

        return customerSelected;
    }

    public void backButtonClicked() throws IOException {
        switcher = new Switcher();
        Stage stage = (Stage) customerBackButton.getScene().getWindow();
        switcher.switchScene(stage, "home.fxml");
    }

    public void deleteButtonClicked() throws RemoteException, SQLException {
        ObservableList<CustomerSerializer> customerSelected = getTable();

        for (CustomerSerializer a : customerSelected) {
            System.out.println(a.getName());
            server.deleteCustomer(a.getEmail());
        }
        ArrayList<CustomerSerializer> productSerializers = server.allCustomers();

        ObservableList<CustomerSerializer> products = FXCollections.observableList(productSerializers);

        orderTable.setItems(products);
    }

    public void modifyButtonClicked() throws IOException, SQLException {
        ObservableList<CustomerSerializer> customerSelected = getTable();

        if (!customerSelected.isEmpty()) {
            for (CustomerSerializer a : customerSelected) {
                server.modifyCustomer(a.getEmail());
            }

            switcher = new Switcher();
            Stage stage = (Stage) customerModifyButton.getScene().getWindow();
            switcher.switchScene(stage, "modifyCustomer.fxml");
        }
    }
}
