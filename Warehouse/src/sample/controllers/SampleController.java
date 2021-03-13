package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.CommunicationService;
import sample.Switcher;
import sample.Serializers.UserSerializer;

import java.io.FileWriter;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;


public class SampleController {
    Registry registry = LocateRegistry.getRegistry();
    CommunicationService server = (CommunicationService)registry.lookup("WarehouseService");
    String message;
    Switcher switcher;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorUsername;

    @FXML
    private Label errorPassword;

    @FXML
    private Label errorField;

    @FXML
    private Button loginButton;

    @FXML
    private Button signupButton;

    public SampleController() throws RemoteException, NotBoundException {
    }

    @FXML
    public void onEnter() throws SQLException, IOException {
        this.loginValidation();
    }

    private void usernameValidation() {
        if (usernameTextField.getText().equals("")) {
            errorUsername.setText("Write username!");
            errorUsername.setVisible(true);
            errorField.setVisible(false);
        }

        if (!usernameTextField.getText().equals("")) {
            errorUsername.setVisible(false);
        }

    }

    private void passwordValidation() {
        if (passwordField.getText().equals("")) {
            errorPassword.setText("Write password!");
            errorPassword.setVisible(true);
            errorField.setVisible(false);
        }

        if (!passwordField.getText().equals("")) {
            errorPassword.setVisible(false);
        }
    }

    private void authentication(CommunicationService server, String message) throws IOException, SQLException {
        if (message.equals("OK")) {
            errorField.setVisible(false);
            UserSerializer user = server.userSerializer(usernameTextField.getText());

            try {
                FileWriter myWriter = new FileWriter("loggedUser.txt");
                myWriter.write(user.getUsername());
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

            System.out.println(user.getEmail());
            System.out.println("User " + user.getUsername() + " logged in...");

            switcher = new Switcher();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            switcher.switchScene(stage, "home.fxml");
        }
        else {
            errorField.setVisible(true);
            errorField.setText(message);
        }
    }

    private void loginValidation() throws IOException, SQLException {
        if (!usernameTextField.getText().equals("") && !passwordField.getText().equals("")) {
            message = server.login(usernameTextField.getText(), passwordField.getText());
            System.out.println(message);

            this.authentication(server, message);
        }


        this.usernameValidation();
        this.passwordValidation();
    }

    public void loginButtonClicked() throws IOException, SQLException {
        this.loginValidation();
    }

    public void signupButtonClicked() throws IOException {
        switcher = new Switcher();
        Stage stage = (Stage) signupButton.getScene().getWindow();
        switcher.switchScene(stage, "registration.fxml");
    }
}
