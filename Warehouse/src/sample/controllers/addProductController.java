package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
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

public class addProductController implements Initializable {

    @FXML
    private ComboBox<String> productComboBoxField;

    @FXML
    private TextField productNameTextField;

    @FXML
    private Label productNameError;

    @FXML
    private Button productBackButton;

    @FXML
    private Label productSuccessLabel;

    @FXML
    private TextField productQuantityTextField;

    @FXML
    private Label productQuantityError;

    @FXML
    private TextField productPriceTextField;

    @FXML
    private Label productPriceError;

    Registry registry = LocateRegistry.getRegistry();
    CommunicationService server = (CommunicationService)registry.lookup("WarehouseService");
    Switcher switcher;
    ArrayList<Boolean> next = null;
    boolean if_next = false;

    public addProductController() throws RemoteException, NotBoundException {
    }

    private boolean validateName() throws RemoteException, SQLException {
        boolean helper = server.productNameValidation(productNameTextField.getText());

        if (productNameTextField.getText().equals("")){
            productNameError.setVisible(true);
            productNameError.setText("Write product name!");
            return false;
        }
        else if (helper) {
            productNameError.setText("Product name exists");
            productNameError.setVisible(true);
            return false;
        } else {
            productNameError.setVisible(false);
            return true;
        }
    }

    private boolean isNumber(String text) {
        return text.matches("([0-9])*");
    }

    private boolean numberError(Label error, boolean helper) {
        if (helper) {
            error.setVisible(false);
            return true;
        }
        else {
            error.setVisible(true);
            error.setText("Must be a number");
            return false;
        }
    }

    private boolean validateQuantity() {
        boolean helper = isNumber(productQuantityTextField.getText());

        if (productQuantityTextField.getText().equals("")){
            productQuantityError.setVisible(true);
            productQuantityError.setText("Write quantity!");
            return false;
        }
        else return numberError(productQuantityError, helper);
    }

    private boolean validatePrice() {
        boolean helper = isNumber(productPriceTextField.getText());

        if (productPriceTextField.getText().length() == 0){
            productPriceError.setVisible(true);
            productPriceError.setText("Write product price!");
            return false;
        }
        else return numberError(productPriceError, helper);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        productComboBoxField.setValue(categories.get(0));
        System.out.println(productComboBoxField.getValue());
    }

    public void addButtonClicked() throws RemoteException, SQLException {
        next = new ArrayList<>();
        if_next = true;

        next.add(validateName());

        next.add(validateQuantity());

        next.add(validatePrice());

        for (boolean i : next) {
            if (!i) {
                if_next = false;
                productSuccessLabel.setVisible(false);
                break;
            }

        }
        if (if_next) {
            productSuccessLabel.setText("Product added to database");
            productSuccessLabel.setVisible(true);
            ProductSerializer product = new ProductSerializer(productNameTextField.getText(), Integer.parseInt(productQuantityTextField.getText()), Integer.parseInt(productPriceTextField.getText()), productComboBoxField.getValue());
            server.addProduct(product);
        }
    }

    public void backButtonClicked() throws IOException {
        switcher = new Switcher();
        Stage stage = (Stage) productBackButton.getScene().getWindow();
        switcher.switchScene(stage, "home.fxml");
    }

}
