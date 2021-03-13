package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.Switcher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private Button logoutButton;

    @FXML
    private Button addProductButton;

    @FXML
    private Label userLabel;

    @FXML
    private Button addCustomerButton;

    @FXML
    private Button tableProductButton;

    Switcher switcher;



    public HomeController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try(BufferedReader br = new BufferedReader(new FileReader("loggedUser.txt"))) {
            for(String line; (line = br.readLine()) != null; ) {
                userLabel.setText("Logged as " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logoutButtonClicked() throws IOException {
        switcher = new Switcher();
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        switcher.switchScene(stage, "sample.fxml");
    }

    public void addProductButtonClicked() throws IOException {
        switcher = new Switcher();
        Stage stage = (Stage) addProductButton.getScene().getWindow();
        switcher.switchScene(stage, "addProduct.fxml");
    }

    public void tableProductButtonClicked() throws IOException {
        switcher = new Switcher();
        Stage stage = (Stage) tableProductButton.getScene().getWindow();
        switcher.switchScene(stage, "tableProduct.fxml");
    }

    public void addCustomerButtonClicked() throws IOException {
        switcher = new Switcher();
        Stage stage = (Stage) addCustomerButton.getScene().getWindow();
        switcher.switchScene(stage, "addCustomer.fxml");
    }

    public void tableCustomerButtonClicked() throws IOException {
        switcher = new Switcher();
        Stage stage = (Stage) tableProductButton.getScene().getWindow();
        switcher.switchScene(stage, "tableCustomer.fxml");
    }

    public void addOrderButtonClicked() throws IOException {
        switcher = new Switcher();
        Stage stage = (Stage) addCustomerButton.getScene().getWindow();
        switcher.switchScene(stage, "addOrder.fxml");
    }

    public void tableOrderButtonClicked() throws IOException {
        switcher = new Switcher();
        Stage stage = (Stage) tableProductButton.getScene().getWindow();
        switcher.switchScene(stage, "tableOrder.fxml");
    }

}
