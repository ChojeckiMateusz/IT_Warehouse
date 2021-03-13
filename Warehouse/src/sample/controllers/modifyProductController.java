package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.CommunicationService;
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

public class modifyProductController implements Initializable {

    @FXML
    private TextField productNameTextField;

    @FXML
    private Label productNameError;

    @FXML
    private TextField productQuantityTextField;

    @FXML
    private Label productQuantityError;

    @FXML
    private Label productSuccessLabel;

    @FXML
    private TextField productPriceTextField;

    @FXML
    private Label productPriceError;

    @FXML
    private ComboBox<String> productComboBoxField;

    @FXML
    private Button productBackButton;

    Registry registry = LocateRegistry.getRegistry();
    CommunicationService server = (CommunicationService)registry.lookup("WarehouseService");
    Switcher switcher;
    ArrayList<Boolean> next = null;
    boolean if_next = false;

    public modifyProductController() throws RemoteException, NotBoundException {
    }

    private boolean validateName() {

        if (productNameTextField.getText().equals("")){
            productNameError.setVisible(true);
            productNameError.setText("Write product name!");
            return false;
        } else {
            productNameError.setVisible(false);
            return true;
        }
    }

    private boolean validateQuantity() {
        boolean helper = productQuantityTextField.getText().matches("([0-9])*");

        if (productQuantityTextField.getText().equals("")){
            productQuantityError.setVisible(true);
            productQuantityError.setText("Write quantity!");
            return false;
        }
        else if (helper) {
            productQuantityError.setVisible(false);
            return true;
        }
        else {
            productQuantityError.setVisible(true);
            productQuantityError.setText("Must be a number");
            return false;
        }
    }

    private boolean validatePrice() {
        boolean helper = productPriceTextField.getText().matches("([0-9])*");

        if (productPriceTextField.getText().equals("")){
            productPriceError.setVisible(true);
            productPriceError.setText("Write product price!");
            return false;
        }
        else if (helper) {
            productPriceError.setVisible(false);
            return true;
        }
        else {
            productPriceError.setVisible(true);
            productPriceError.setText("Must be a number");
            return false;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ProductSerializer product = null;
        try {
            product = server.productToModify();
        } catch (RemoteException | SQLException e) {
            e.printStackTrace();
        }

        assert product != null;
        productNameTextField.setText(product.getName());
        productQuantityTextField.setText(String.valueOf(product.getQuantity()));
        productPriceTextField.setText(String.valueOf(product.getPrice()));

        ArrayList<String> categories = null;
        try {
            categories = server.getCategories();
        } catch (RemoteException | SQLException e) {
            e.printStackTrace();
        }

        assert categories != null;
        for (String category : categories) {
            productComboBoxField.getItems().add(category);
        }

        productComboBoxField.setValue(product.getCategory());
        System.out.println(productComboBoxField.getValue());
    }

    public void backButtonClicked() throws IOException {
        switcher = new Switcher();
        Stage stage = (Stage) productBackButton.getScene().getWindow();
        switcher.switchScene(stage, "tableProduct.fxml");
    }

    public void modifyButtonClicked() throws IOException, SQLException {
        next = new ArrayList<>();
        if_next = true;

        next.add(validateName());

        next.add(validateQuantity());

        next.add(validatePrice());

        System.out.println(next);

        for (boolean i : next) {
            if (!i) {
                if_next = false;
                productSuccessLabel.setVisible(false);
                break;
            }

        }
        if (if_next) {
            productSuccessLabel.setText("Product modified successfully");
            productSuccessLabel.setVisible(true);
            ProductSerializer product = new ProductSerializer(productNameTextField.getText(), Integer.parseInt(productQuantityTextField.getText()), Integer.parseInt(productPriceTextField.getText()), productComboBoxField.getValue());
            server.modifiedProduct(product);
        }
    }

}
