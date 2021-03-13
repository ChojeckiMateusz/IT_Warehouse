package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
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

public class tableProductController implements Initializable{
    @FXML
    private TableView<ProductSerializer> orderTable;

    @FXML
    private TableColumn<ProductSerializer, String> orderColumnName;

    @FXML
    private TableColumn<ProductSerializer, Integer> orderColumnQuantity;

    @FXML
    private TableColumn<ProductSerializer, Integer> orderColumnPrice;

    @FXML
    private TableColumn<ProductSerializer, String> orderColumnCategory;

    @FXML
    private Button productBackButton;

    @FXML
    private Button productModifyButton;

    Registry registry = LocateRegistry.getRegistry();
    CommunicationService server = (CommunicationService)registry.lookup("WarehouseService");
    String message;
    Switcher switcher;

    ArrayList<ProductSerializer> productSerializers = server.allProducts();

    ObservableList<ProductSerializer> products = FXCollections.observableList(productSerializers);

    public tableProductController() throws RemoteException, NotBoundException, SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        orderColumnName.setCellValueFactory(new PropertyValueFactory<ProductSerializer, String>("name"));
        orderColumnQuantity.setCellValueFactory(new PropertyValueFactory<ProductSerializer, Integer>("quantity"));
        orderColumnPrice.setCellValueFactory(new PropertyValueFactory<ProductSerializer, Integer>("price"));
        orderColumnCategory.setCellValueFactory(new PropertyValueFactory<ProductSerializer, String>("category"));

        orderTable.setItems(products);
    }

    public void backButtonClicked() throws IOException {
        switcher = new Switcher();
        Stage stage = (Stage) productBackButton.getScene().getWindow();
        switcher.switchScene(stage, "home.fxml");
    }

    private ObservableList<ProductSerializer> getTable() {
        ObservableList<ProductSerializer> productSelected;
        productSelected = orderTable.getSelectionModel().getSelectedItems();

        return productSelected;
    }

    public void deleteButtonClicked() throws RemoteException, SQLException {
        ObservableList<ProductSerializer> productSelected = getTable();

        for (ProductSerializer a : productSelected) {
            System.out.println(a.getName());
            server.deleteProduct(a.getName());
        }
        ArrayList<ProductSerializer> productSerializers = server.allProducts();

        ObservableList<ProductSerializer> products = FXCollections.observableList(productSerializers);

        orderTable.setItems(products);
    }

    public void modifyButtonClicked() throws IOException, SQLException {
        ObservableList<ProductSerializer> productSelected = getTable();

        if (!productSelected.isEmpty()) {
            for (ProductSerializer a : productSelected) {
                server.modifyProduct(a.getName());
            }

            switcher = new Switcher();
            Stage stage = (Stage) productModifyButton.getScene().getWindow();
            switcher.switchScene(stage, "modifyProduct.fxml");
        }
    }

}
