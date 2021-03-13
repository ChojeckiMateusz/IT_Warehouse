package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sample.CommunicationService;
import sample.Serializers.CustomerSerializer;
import sample.Switcher;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;

public class addCustomerController {

    @FXML
    private Button customerBackButton;

    @FXML
    private TextField customerNameTextField;

    @FXML
    private TextField customerSurnameTextField;

    @FXML
    private TextField customerEmailTextField;

    @FXML
    private TextField customerPhoneTextField;

    @FXML
    private TextField customerAddressTextField;

    @FXML
    private Label customerNameError;

    @FXML
    private Label customerSurnameError;

    @FXML
    private Label customerEmailError;

    @FXML
    private Label customerSuccessLabel;

    @FXML
    private Label customerPhoneError;

    @FXML
    private Label customerAddressError;

    Registry registry = LocateRegistry.getRegistry();
    CommunicationService server = (CommunicationService)registry.lookup("WarehouseService");
    Switcher switcher;
    ArrayList<Boolean> next = null;
    boolean if_next = false;

    public addCustomerController() throws RemoteException, NotBoundException {
    }

    private boolean validateName() {

        if (customerNameTextField.getText().equals("")){
            customerNameError.setVisible(true);
            customerNameError.setText("Write Customer name!");
            return false;
        } else {
            customerNameError.setVisible(false);
            return true;
        }
    }

    private boolean validateSurname() {

        if (customerSurnameTextField.getText().equals("")){
            customerSurnameError.setVisible(true);
            customerSurnameError.setText("Write Customer surname!");
            return false;
        } else {
            customerSurnameError.setVisible(false);
            return true;
        }
    }

    private boolean validateEmail() throws RemoteException, SQLException {
        boolean helper = customerEmailTextField.getText().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,6}");
        if (helper) {
            customerEmailError.setVisible(false);

            if (server.customerEmailValidation(customerEmailTextField.getText())) {
                customerEmailError.setText("Email address is already taken");
                customerEmailError.setVisible(true);
                customerSuccessLabel.setText("");
                return false;
            } else {
                customerEmailError.setVisible(false);
                return true;
            }
        } else {
            customerEmailError.setVisible(true);
            customerEmailError.setText("Must be a valid email");
            return false;
        }
    }

    private boolean validatePhone() throws RemoteException, SQLException {
        boolean helper = customerPhoneTextField.getText().matches("[0-9]{9,11}");

        if (helper) {
            customerPhoneError.setVisible(false);
            if (server.customerPhoneValidation(customerPhoneTextField.getText())) {
                customerPhoneError.setText("Phone number is already taken");
                customerPhoneError.setVisible(true);
                return false;
            } else {
                customerPhoneError.setVisible(false);
                return true;
            }
        }
        else {
            customerPhoneError.setVisible(true);
            customerPhoneError.setText("Must be a valid phone number");
            return false;
        }
    }

    private boolean validateAddress() {
        boolean helper = customerAddressTextField.getText().matches("[a-zA-Z]+\\s[0-9]{1,4}");
        if (helper) {
            customerAddressError.setVisible(false);
            return true;
        } else {
            customerAddressError.setVisible(true);
            customerAddressError.setText("Must be a valid address");
            return false;
        }
    }

    public void addButtonClicked() throws RemoteException, SQLException {
        next = new ArrayList<>();
        if_next = true;

        next.add(validateName());

        next.add(validateSurname());

        next.add(validateEmail());

        next.add(validatePhone());

        next.add(validateAddress());

        for (boolean i : next) {
            if (!i) {
                if_next = false;
                customerSuccessLabel.setVisible(false);
                break;
            }

        }
        if (if_next) {
            customerSuccessLabel.setText("Customer added to database");
            customerSuccessLabel.setVisible(true);
            CustomerSerializer customer = new CustomerSerializer(customerNameTextField.getText(), customerSurnameTextField.getText(), customerEmailTextField.getText(), customerPhoneTextField.getText(), customerAddressTextField.getText());
            server.addCustomer(customer);
        }

    }

    public void backButtonClicked() throws IOException {
        switcher = new Switcher();
        Stage stage = (Stage) customerBackButton.getScene().getWindow();
        switcher.switchScene(stage, "home.fxml");
    }
}
