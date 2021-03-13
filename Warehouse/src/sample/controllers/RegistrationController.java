package sample.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.CommunicationService;
import sample.Switcher;
import sample.Serializers.UserSerializer;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;


public class RegistrationController {

    @FXML
    private Button signupButton;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private Label usernameError;

    @FXML
    private Label passwordError;

    @FXML
    private Label emailError;

    @FXML
    private Label phoneError;

    @FXML
    private Label successLabel;


    Registry registry = LocateRegistry.getRegistry();
    CommunicationService server = (CommunicationService)registry.lookup("WarehouseService");
    Switcher switcher;
    ArrayList<Boolean> next = null;
    boolean if_next = false;

    private boolean usernameValidation() throws RemoteException, SQLException {
        boolean helper = server.userValidation(usernameTextField.getText());

        if (usernameTextField.getText().equals("")){
            usernameError.setVisible(true);
            usernameError.setText("Write username!");
            return false;
        }
        else if (usernameTextField.getText().length() < 5) {
            usernameError.setText("Username must be at least 5 characters");
            usernameError.setVisible(true);
            return false;
        }
        else if (helper) {
            usernameError.setText("Username is already taken");
            usernameError.setVisible(true);
            return false;
        } else {
            usernameError.setVisible(false);
            return true;
        }

    }

    private boolean passwordValidation() {
        if (passwordTextField.getText().equals("")) {
            passwordError.setText("Write password!");
            passwordError.setVisible(true);
            return false;
        }
        else if (passwordTextField.getText().length() < 5) {
            passwordError.setText("Password must be at least 5 characters");
            passwordError.setVisible(true);
            return false;
        }
        else {
            passwordError.setVisible(false);
            return true;
        }
    }

    private boolean emailValidation() throws RemoteException, SQLException {
        boolean helper = emailTextField.getText().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,6}");
        if (helper) {
            emailError.setVisible(false);

            if (server.emailValidation(emailTextField.getText())) {
                emailError.setText("Email address is already taken");
                emailError.setVisible(true);
                return false;
            } else {
                emailError.setVisible(false);
                return true;
            }
        }
        else {
            emailError.setVisible(true);
            emailError.setText("Must be a valid email");
            return false;
        }
    }

    private boolean phoneValidation() throws RemoteException, SQLException {
        boolean helper = phoneTextField.getText().matches("[0-9]{9,11}");
        if (helper) {
            phoneError.setVisible(false);
            if (server.phoneValidation(phoneTextField.getText())) {
                phoneError.setText("Phone number is already taken");
                phoneError.setVisible(true);
                return false;
            } else {
                phoneError.setVisible(false);
                return true;
            }
        }
        else {
            phoneError.setVisible(true);
            phoneError.setText("Must be a valid phone number");
            return false;
        }
    }

    public RegistrationController() throws RemoteException, NotBoundException {
    }

    public void signupButtonClicked() throws IOException, SQLException{
        if_next = true;

        next = new ArrayList<>();

        next.add(usernameValidation());

        next.add(passwordValidation());

        next.add(emailValidation());

        next.add(phoneValidation());

        System.out.println(next);

        for (boolean i : next) {
            if (!i) {
                if_next = false;
                successLabel.setVisible(false);
                break;
            }

        }
        if (if_next) {
            successLabel.setText("Registered successfully");
            successLabel.setVisible(true);
            UserSerializer user = new UserSerializer(0, usernameTextField.getText(), passwordTextField.getText(), emailTextField.getText(), phoneTextField.getText());
            server.addUser(user);
        }
    }

    public void loginButtonClicked() throws IOException{
        switcher = new Switcher();
        Stage stage = (Stage) signupButton.getScene().getWindow();
        switcher.switchScene(stage, "sample.fxml");
    }
}
