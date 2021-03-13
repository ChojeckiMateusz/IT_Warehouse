package sample;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import sample.Serializers.CustomerSerializer;
import sample.Serializers.OrderSerializer;
import sample.Serializers.ProductSerializer;
import sample.Serializers.UserSerializer;
import sample.models.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommunicationServiceImplementation extends UnicastRemoteObject implements CommunicationService {
    String path;
    ConnectionSource connectionSource;
    Dao<User, Integer> userDao;
    Dao<Category, Integer> categoryDao;
    Dao<Customer, Integer> customerDao;
    Dao<Order, Integer> orderDao;
    Dao<Product, Integer> productDao;

    ProductSerializer productModify;
    CustomerSerializer customerModify;
    OrderSerializer orderSerializer;

    CommunicationServiceImplementation() throws RemoteException, SQLException {
        super();

        path = "jdbc:sqlite:C:\\Inf\\Java\\WarehouseServer\\warehouse.db";

        connectionSource = new JdbcConnectionSource(path);
        userDao = DaoManager.createDao(connectionSource, User.class);
        categoryDao = DaoManager.createDao(connectionSource, Category.class);
        customerDao = DaoManager.createDao(connectionSource, Customer.class);
        orderDao = DaoManager.createDao(connectionSource, Order.class);
        productDao = DaoManager.createDao(connectionSource, Product.class);
    }

    private boolean empty(List list) {
        if (list.isEmpty()) {
            System.out.println("Not exist");
            return false;
        }
        else {
            System.out.println("Exist");
            return true;
        }
    }

    @Override
    public String login(String username, String password) throws RemoteException, SQLException {

        List<User> users = this.userDao.queryForEq("username", username);
        if (empty(users)) {
            for (User user : users) {
                System.out.println(user.getId() + " " + user.getUsername());

                if (user.getPassword().equals(password)) {
                    return "OK";
                }
                else {
                    return "Incorrect password";
                }
            }
        }
        else {
            return "Incorrect username";
        }
        return "";
    }

    @Override
    public UserSerializer userSerializer(String username) throws RemoteException, SQLException {
        List<User> users = this.userDao.queryForEq("username", username);

        for (User user : users) {
            return new UserSerializer(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getPhoneNumber());
        }
        return null;
    }

    @Override
    public String communicate(String message) throws RemoteException {
        if (message.equals(new StringBuilder(message).reverse().toString())) {
            System.out.println("OK");
            return "Ok";
        }
        else {
            System.out.println("Err");
            return "Err";
        }
    }

    @Override
    public boolean userValidation(String username) throws RemoteException, SQLException {
        List<User> users = this.userDao.queryForEq("username", username);

        return empty(users);
    }

    @Override
    public boolean emailValidation(String email) throws RemoteException, SQLException {
        List<User> users = this.userDao.queryForEq("email", email);

        return empty(users);
    }

    @Override
    public boolean phoneValidation(String phone) throws RemoteException, SQLException {
        List<User> users = this.userDao.queryForEq("phoneNumber", phone);

        return empty(users);
    }

    @Override
    public void addUser(UserSerializer user) throws RemoteException, SQLException {
        User new_user = new User(user.getUsername(), user.getPassword(), user.getEmail(), user.getPhoneNumber());
        userDao.create(new_user);
    }

    @Override
    public ArrayList<String> getCategories() throws RemoteException, SQLException {
        ArrayList<String> result = new ArrayList<>();

        List<Category> cat = categoryDao.queryForAll();
        for (Category c : cat) {
            result.add(c.getDescription());
        }

        return result;
    }

    @Override
    public boolean productNameValidation(String name) throws RemoteException, SQLException {
        List<Product> products = this.productDao.queryForEq("name", name);

        return empty(products);
    }

    @Override
    public void addProduct(ProductSerializer product) throws RemoteException, SQLException {
        Category result = null;

        List<Category> cat = categoryDao.queryForEq("description", product.getCategory());
        for (Category c : cat) {
            result = c;
        }

        Product new_product = new Product(product.getName(), product.getQuantity(), product.getPrice(), result);
        productDao.create(new_product);
    }

    @Override
    public boolean customerEmailValidation(String email) throws RemoteException, SQLException {
        List<Customer> customers = this.customerDao.queryForEq("email", email);

        return empty(customers);
    }

    @Override
    public boolean customerPhoneValidation(String phone) throws RemoteException, SQLException {
        List<Customer> customers = this.customerDao.queryForEq("phoneNumber", phone);

        return empty(customers);
    }

    @Override
    public void addCustomer(CustomerSerializer customer) throws RemoteException, SQLException {
        Customer new_customer = new Customer(customer.getName(), customer.getSurname(), customer.getEmail(), customer.getPhoneNumber(), customer.getAddress());
        customerDao.create(new_customer);
    }

    @Override
    public ArrayList<String> getCustomers() throws RemoteException, SQLException {
        ArrayList<String> result = new ArrayList<>();

        List<Customer> customers = customerDao.queryForAll();
        for (Customer customer : customers) {
            result.add(customer.getEmail());
        }

        return result;
    }

    @Override
    public ArrayList<String> getProducts() throws RemoteException, SQLException {
        ArrayList<String> result = new ArrayList<>();

        List<Product> products = productDao.queryForAll();
        for (Product product : products) {
            result.add(product.getName());
        }
        return result;
    }

    @Override
    public void addOrder(OrderSerializer order) throws RemoteException, SQLException {
        Customer customer = null;
        Product product = null;
        User user = null;

        List<Customer> customers = customerDao.queryForEq("email", order.getCustomer());
        for (Customer c : customers) {
            customer = c;
        }

        List<Product> products = productDao.queryForEq("name", order.getProduct());
        for (Product p : products) {
            product = p;
        }

        List<User> users = this.userDao.queryForEq("username", order.getUser());
        for (User u : users) {
            user = u;
        }

        Order new_order = new Order(Objects.requireNonNull(user), Objects.requireNonNull(customer), Objects.requireNonNull(product), order.getQuantity(),order.getComments());
        orderDao.create(new_order);
    }

    @Override
    public ProductSerializer getProduct(String name) throws RemoteException, SQLException {
        List<Product> products = productDao.queryForEq("name", name);
        ProductSerializer result = null;

        for (Product product : products) {
            result = new ProductSerializer(product.getName(), product.getQuantity(), product.getPrice(), product.getCategory().getDescription());
        }
        return result;
    }

    @Override
    public void orderProductQuantity(String productName, String quantity) throws RemoteException, SQLException {
        List<Product> products = productDao.queryForEq("name", productName);

        for (Product product : products) {
            int helper = product.getQuantity() - Integer.parseInt(quantity);
            System.out.println(helper);
            product.setQuantity(helper);
            productDao.update(product);
        }
    }

    @Override
    public ArrayList<ProductSerializer> allProducts() throws RemoteException, SQLException {
        ArrayList<ProductSerializer> result = new ArrayList<>();
        List<Product> products = productDao.queryForAll();
        ProductSerializer prodS;

        for (Product product : products) {
            prodS = new ProductSerializer(product.getName(), product.getQuantity(), product.getPrice(), product.getCategory().getDescription());
            result.add(prodS);
        }

        return result;
    }

    @Override
    public void deleteProduct(String name) throws RemoteException, SQLException {
        List<Product> products = productDao.queryForEq("name", name);

        for (Product product : products) {
            List<Order> orders = orderDao.queryForEq("product_id", product);
            System.out.println(orders.size());
            for (Order order : orders) {
                System.out.println("DELETE order");
                orderDao.delete(order);
            }
            productDao.delete(product);
        }
    }

    @Override
    public void modifyProduct(String name) throws RemoteException, SQLException {
        List<Product> products = productDao.queryForEq("name", name);

        for (Product product : products) {
            productModify = new ProductSerializer(product.getName(), product.getQuantity(), product.getPrice(), product.getCategory().getDescription());
        }
    }

    @Override
    public ProductSerializer productToModify() throws RemoteException {
        return productModify;
    }

    @Override
    public void modifiedProduct(ProductSerializer product) throws RemoteException, SQLException {
        List<Product> products = productDao.queryForEq("name", productModify.getName());

        for (Product prod : products) {

            prod.setName(product.getName());
            prod.setQuantity(product.getQuantity());
            prod.setPrice(product.getPrice());

            List<Category> categories = categoryDao.queryForEq("description", product.getCategory());
            for (Category category : categories) {
                prod.setCategory(category);
            }
            System.out.println(prod.getName());
            productDao.update(prod);
        }
    }

    @Override
    public ArrayList<CustomerSerializer> allCustomers() throws RemoteException, SQLException {

        ArrayList<CustomerSerializer> result = new ArrayList<>();
        List<Customer> customers = customerDao.queryForAll();
        CustomerSerializer cust;

        for (Customer customer : customers) {
            cust = new CustomerSerializer(customer.getName(), customer.getSurname(), customer.getEmail(), customer.getPhoneNumber(), customer.getAddress());
            result.add(cust);
        }

        return result;

    }

    @Override
    public void deleteCustomer(String email) throws RemoteException, SQLException {
        List<Customer> customers = customerDao.queryForEq("email", email);
        System.out.println(customers.size());

        for (Customer customer : customers) {
            List<Order> orders = orderDao.queryForEq("customer_id", customer);
            System.out.println(orders.size());
            for (Order order : orders) {
                System.out.println("DELETE order");
                orderDao.delete(order);
            }

            customerDao.delete(customer);
        }
    }

    @Override
    public void modifyCustomer(String email) throws RemoteException, SQLException {
        List<Customer> customers = customerDao.queryForEq("email", email);

        for (Customer customer : customers) {
            customerModify = new CustomerSerializer(customer.getName(), customer.getSurname(), customer.getEmail(), customer.getPhoneNumber(), customer.getAddress());
        }
    }

    @Override
    public CustomerSerializer customerToModify() throws RemoteException {
        return customerModify;
    }

    @Override
    public void modifiedCustomer(CustomerSerializer customer) throws RemoteException, SQLException {
        List<Customer> customers = customerDao.queryForEq("email", customerModify.getEmail());

        for (Customer cust : customers) {
            cust.setName(customer.getName());
            cust.setSurname(customer.getSurname());
            cust.setEmail(customer.getEmail());
            cust.setPhoneNumber(customer.getPhoneNumber());
            cust.setAddress(customer.getAddress());

            customerDao.update(cust);
        }
    }

    @Override
    public ArrayList<OrderSerializer> allOrders() throws RemoteException, SQLException {

        ArrayList<OrderSerializer> result = new ArrayList<>();
        List<Order> orders = orderDao.queryForAll();
        OrderSerializer orderSerializer;

        for (Order order : orders) {
            orderSerializer = new OrderSerializer(order.getUser().getUsername(), order.getCustomer().getEmail(), order.getProduct().getName(), order.getQuantity(), order.getComments());
            orderSerializer.setPrice(order.getPrice());
            orderSerializer.setId(order.getID());
            result.add(orderSerializer);
        }

        return result;

    }

    @Override
    public void deleteOrder(int ID) throws RemoteException, SQLException {
        List<Order> orders = orderDao.queryForEq("id", ID);

        for (Order order : orders) {
            orderDao.delete(order);
        }
    }

    @Override
    public void modifyOrder(int ID) throws RemoteException, SQLException {
        List<Order> orders = orderDao.queryForEq("id", ID);

        for (Order order : orders) {
            orderSerializer = new OrderSerializer(order.getUser().getUsername(), order.getCustomer().getEmail(), order.getProduct().getName(), order.getQuantity(), order.getComments());
            orderSerializer.setId(order.getID());
            orderSerializer.setPrice(order.getPrice());
        }
    }

    @Override
    public OrderSerializer orderToModify() throws RemoteException {
        return orderSerializer;
    }

    @Override
    public void modifiedOrder(OrderSerializer order) throws RemoteException, SQLException {
        List<Order> orders = orderDao.queryForEq("id", orderSerializer.getId());

        for (Order ord : orders) {

            List<Customer> customers = customerDao.queryForEq("email", order.getCustomer());
            for (Customer customer : customers) {
                ord.setCustomer(customer);
            }

            List<Product> products = productDao.queryForEq("name", order.getProduct());
            for (Product product : products) {
                ord.setProduct(product);
            }

            ord.setQuantity(order.getQuantity());
            ord.setComments(order.getComments());

            orderDao.update(ord);
        }

    }
}