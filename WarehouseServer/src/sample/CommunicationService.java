package sample;

import sample.Serializers.CustomerSerializer;
import sample.Serializers.OrderSerializer;
import sample.Serializers.ProductSerializer;
import sample.Serializers.UserSerializer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface CommunicationService extends Remote {
    public String login(String username, String password) throws RemoteException, SQLException;

    public UserSerializer userSerializer(String username) throws RemoteException, SQLException;

    public String communicate(String message) throws RemoteException;

    public boolean userValidation(String username)  throws RemoteException, SQLException;

    public boolean emailValidation(String email) throws RemoteException, SQLException;

    public boolean phoneValidation(String phone) throws RemoteException, SQLException;

    public void addUser(UserSerializer user) throws RemoteException, SQLException;

    public ArrayList<String> getCategories() throws RemoteException, SQLException;

    public boolean productNameValidation(String name)  throws RemoteException, SQLException;

    public void addProduct(ProductSerializer product) throws RemoteException, SQLException;

    public boolean customerEmailValidation(String email) throws RemoteException, SQLException;

    public boolean customerPhoneValidation(String phone) throws RemoteException, SQLException;

    public void addCustomer(CustomerSerializer customer) throws RemoteException, SQLException;

    public ArrayList<String> getCustomers() throws RemoteException, SQLException;

    public ArrayList<String> getProducts() throws RemoteException, SQLException;

    public void addOrder(OrderSerializer order) throws RemoteException, SQLException;

    public ProductSerializer getProduct(String name) throws RemoteException, SQLException;

    public void orderProductQuantity(String productName, String quantity) throws RemoteException, SQLException;

    public ArrayList<ProductSerializer> allProducts() throws RemoteException, SQLException;

    public void deleteProduct(String name) throws RemoteException, SQLException;

    public void modifyProduct(String name) throws RemoteException, SQLException;

    public ProductSerializer productToModify() throws RemoteException, SQLException;

    public void modifiedProduct(ProductSerializer product) throws RemoteException, SQLException;

    public ArrayList<CustomerSerializer> allCustomers() throws RemoteException, SQLException;

    public void deleteCustomer(String email) throws RemoteException, SQLException;

    public void modifyCustomer(String email) throws RemoteException, SQLException;

    public CustomerSerializer customerToModify() throws RemoteException, SQLException;

    public void modifiedCustomer(CustomerSerializer customer) throws RemoteException, SQLException;

    public ArrayList<OrderSerializer> allOrders() throws RemoteException, SQLException;

    public void deleteOrder(int ID) throws RemoteException, SQLException;

    public void modifyOrder(int ID) throws RemoteException, SQLException;

    public OrderSerializer orderToModify() throws RemoteException, SQLException;

    public void modifiedOrder(OrderSerializer order) throws RemoteException, SQLException;

}