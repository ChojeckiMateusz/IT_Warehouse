package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.CommunicationService;
import sample.Serializers.OrderSerializer;
import sample.Serializers.ProductSerializer;
import sample.Switcher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class modifyOrderController  implements Initializable {

    @FXML
    private ComboBox<String> orderCustomerComboBoxField;

    @FXML
    private ComboBox<String> orderProductComboBoxField;

    @FXML
    private Label testLabel;

    @FXML
    private TextField orderQuantityTextField;

    @FXML
    private Label orderQuantityError;

    @FXML
    private Label orderSuccessLabel;

    @FXML
    private TextArea orderCommentsTextField;

    @FXML
    private Label orderPriceLabel;

    @FXML
    private Button orderBackButton;

    Registry registry = LocateRegistry.getRegistry();
    CommunicationService server = (CommunicationService)registry.lookup("WarehouseService");
    Switcher switcher;
    boolean if_next = false;

    public modifyOrderController() throws RemoteException, NotBoundException {
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        OrderSerializer order = null;
        try {
            order = server.orderToModify();
        } catch (RemoteException | SQLException e) {
            e.printStackTrace();
        }
        ArrayList<String> customers = null;
        try {
            customers = server.getCustomers();
        } catch (RemoteException | SQLException e) {
            e.printStackTrace();
        }

        assert customers != null;
        for (String customer : customers) {
            orderCustomerComboBoxField.getItems().add(customer);
        }

        orderCustomerComboBoxField.setValue(order.getCustomer());

        ArrayList<String> products = null;
        try {
            products = server.getProducts();
        } catch (RemoteException | SQLException e) {
            e.printStackTrace();
        }

        for (String product : products) {
            orderProductComboBoxField.getItems().add(product);
        }

        orderProductComboBoxField.setValue(order.getProduct());

        orderQuantityTextField.setText(String.valueOf(order.getQuantity()));
        orderCommentsTextField.setText(order.getComments());

        testLabel.setText(orderProductComboBoxField.getValue());
        ProductSerializer product = null;
        try {
            product = server.getProduct(orderProductComboBoxField.getValue());
        } catch (RemoteException | SQLException e) {
            e.printStackTrace();
        }
        testLabel.setText("Quantity: " + String.valueOf(product.getQuantity() + " Price: " + String.valueOf(product.getPrice())));

    }

    private boolean validateQuantity() throws RemoteException, SQLException {
        boolean helper = orderQuantityTextField.getText().matches("([0-9])*");

        if (orderQuantityTextField.getText().equals("")){
            orderQuantityError.setVisible(true);
            orderQuantityError.setText("Write quantity!");
            return false;
        }
        else if (helper) {
            ProductSerializer product = server.getProduct(orderProductComboBoxField.getValue());

            if (Integer.valueOf(orderQuantityTextField.getText()) > product.getQuantity()) {
                orderQuantityError.setVisible(true);
                orderQuantityError.setText("There are not that many products in warehouse");
                return false;
            }
            orderQuantityError.setVisible(false);
            return true;
        }
        else {
            orderQuantityError.setVisible(true);
            orderQuantityError.setText("Must be a number");
            return false;
        }
    }

    public void backButtonClicked() throws IOException {
        switcher = new Switcher();
        Stage stage = (Stage) orderBackButton.getScene().getWindow();
        switcher.switchScene(stage, "tableOrder.fxml");
    }

    public void onAction() throws RemoteException, SQLException {
        testLabel.setText(orderProductComboBoxField.getValue());
        ProductSerializer product = server.getProduct(orderProductComboBoxField.getValue());
        testLabel.setText("Quantity: " + String.valueOf(product.getQuantity() + " Price: " + String.valueOf(product.getPrice())));
    }

    public void setPrice() throws RemoteException, SQLException {
        ProductSerializer product = server.getProduct(orderProductComboBoxField.getValue());
        orderPriceLabel.setText(String.valueOf(product.getPrice() * Integer.valueOf(orderQuantityTextField.getText())));
    }

    public void modifyButtonClicked() throws IOException, SQLException {
        if_next = validateQuantity();

        if (if_next == true) {
            orderSuccessLabel.setText("Order modified successfully");
            String user = "";
            orderSuccessLabel.setVisible(true);
            try(BufferedReader br = new BufferedReader(new FileReader("loggedUser.txt"))) {
                for(String line; (line = br.readLine()) != null; ) {
                    user = line;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            OrderSerializer order = new OrderSerializer(user, orderCustomerComboBoxField.getValue(), orderProductComboBoxField.getValue(), Integer.valueOf(orderQuantityTextField.getText()), orderCommentsTextField.getText());
            server.modifiedOrder(order);
        }
        else {
            orderSuccessLabel.setVisible(false);
        }
    }

}
