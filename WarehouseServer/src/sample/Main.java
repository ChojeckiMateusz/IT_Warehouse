package sample;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws IOException, SQLException {
        System.out.println("SERVER START");
        CommunicationService server = new CommunicationServiceImplementation();
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("WarehouseService", server);


//        TableUtils.createTable(connectionSource, User.class);
//        TableUtils.createTable(connectionSource, Category.class);
//        TableUtils.createTable(connectionSource, Customer.class);
//        TableUtils.createTable(connectionSource, Order.class);
//        TableUtils.createTable(connectionSource, Product.class);

//        User user = new User("Admin", "Admin", "chojecki8991@gfmail.com", "123456789");
//        userDao.create(user);

//        Category newCategory1 = new Category("Monitor");
//        Category newCategory2 = new Category("Laptop");
//        categoryDao.create(newCategory1);
//        categoryDao.create(newCategory2);

//        List<Category> cat = categoryDao.queryForAll();
//        for (Category c : cat) {
//            System.out.println(c.getId() + " " + c.getDescription());
//        }
//
//        System.out.println(cat.get(0).getDescription());
//
//        connectionSource.close();
    }
}
