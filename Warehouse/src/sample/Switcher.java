package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Switcher {

    Stage stage;
    Parent root;

    public Switcher() {

    }

    public void switchScene(Stage stage, String fxmlFile) throws IOException {
//        if (fxmlFile.equals("sample.fxml")) {
//            this.stage = stage;
//            root = FXMLLoader.load(getClass().getResource("fxml/sample.fxml"));
//        }
//        else if (fxmlFile.equals("home.fxml")) {
//            this.stage = stage;
//            root = FXMLLoader.load(getClass().getResource("fxml/home.fxml"));
//        }
//        else if (fxmlFile.equals("registration.fxml")) {
//            this.stage = stage;
//            root = FXMLLoader.load(getClass().getResource("fxml/registration.fxml"));
//        }
//        else if (fxmlFile.equals("addProduct.fxml")) {
//            this.stage = stage;
//            root = FXMLLoader.load(getClass().getResource("fxml/addProduct.fxml"));
//        }
//        else if (fxmlFile.equals("addCustomer.fxml")) {
//            this.stage = stage;
//            root = FXMLLoader.load(getClass().getResource("fxml/addCustomer.fxml"));
//        }
//        else if (fxmlFile.equals("addOrder.fxml")) {
//            this.stage = stage;
//            root = FXMLLoader.load(getClass().getResource("fxml/addOrder.fxml"));
//        }
//        else if (fxmlFile.equals("tableProduct.fxml")) {
//            this.stage = stage;
//            root = FXMLLoader.load(getClass().getResource("fxml/tableProduct.fxml"));
//        }
//        else if (fxmlFile.equals("modifyProduct.fxml")) {
//            this.stage = stage;
//            root = FXMLLoader.load(getClass().getResource("fxml/modifyProduct.fxml"));
//        }
//        else if (fxmlFile.equals("tableCustomer.fxml")) {
//            this.stage = stage;
//            root = FXMLLoader.load(getClass().getResource("fxml/tableCustomer.fxml"));
//        }
//        else if (fxmlFile.equals("modifyCustomer.fxml")) {
//            this.stage = stage;
//            root = FXMLLoader.load(getClass().getResource("fxml/modifyCustomer.fxml"));
//        }
//        else if (fxmlFile.equals("tableOrder.fxml")) {
//            this.stage = stage;
//            root = FXMLLoader.load(getClass().getResource("fxml/tableOrder.fxml"));
//        }
//        else if (fxmlFile.equals("modifyOrder.fxml")) {
//            this.stage = stage;
//            root = FXMLLoader.load(getClass().getResource("fxml/modifyOrder.fxml"));
//        }

        this.stage = stage;
        switch (fxmlFile) {
            case "sample.fxml":
                root = FXMLLoader.load(getClass().getResource("fxml/sample.fxml"));
                break;
            case "home.fxml":
                root = FXMLLoader.load(getClass().getResource("fxml/home.fxml"));
                break;
            case "registration.fxml":
                root = FXMLLoader.load(getClass().getResource("fxml/registration.fxml"));
                break;
            case "addProduct.fxml":
                root = FXMLLoader.load(getClass().getResource("fxml/addProduct.fxml"));
                break;
            case "addCustomer.fxml":
                root = FXMLLoader.load(getClass().getResource("fxml/addCustomer.fxml"));
                break;
            case "addOrder.fxml":
                root = FXMLLoader.load(getClass().getResource("fxml/addOrder.fxml"));
                break;
            case "tableProduct.fxml":
                root = FXMLLoader.load(getClass().getResource("fxml/tableProduct.fxml"));
                break;
            case "modifyProduct.fxml":
                root = FXMLLoader.load(getClass().getResource("fxml/modifyProduct.fxml"));
                break;
            case "tableCustomer.fxml":
                root = FXMLLoader.load(getClass().getResource("fxml/tableCustomer.fxml"));
                break;
            case "modifyCustomer.fxml":
                root = FXMLLoader.load(getClass().getResource("fxml/modifyCustomer.fxml"));
                break;
            case "tableOrder.fxml":
                root = FXMLLoader.load(getClass().getResource("fxml/tableOrder.fxml"));
                break;
            case "modifyOrder.fxml":
                root = FXMLLoader.load(getClass().getResource("fxml/modifyOrder.fxml"));
                break;

        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
