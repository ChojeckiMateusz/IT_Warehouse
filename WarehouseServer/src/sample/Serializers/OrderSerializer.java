package sample.Serializers;

import java.io.Serializable;

public class OrderSerializer implements Serializable {

    private int id;

    private String user;

    private String customer;

    private String product;

    private int quantity;

    private int price;

    private String comments;


    public OrderSerializer() {

    }

    public OrderSerializer(String user, String customer, String product, String quantity, String comments) {
        this.user = user;
        this.customer = customer;
        this.product = product;
        this.quantity = Integer.parseInt(quantity);
        this.comments = comments;
    }

    public OrderSerializer(String user, String customer, String product, int quantity, String comments) {
        this.user = user;
        this.customer = customer;
        this.product = product;
        this.quantity = quantity;
        this.comments = comments;
    }

    public String getUser() {
        return user;
    }

    public String getCustomer() {
        return customer;
    }

    public String getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getComments() {
        return comments;
    }

    public void setId (int id) {
        this.id = id;
    }

    public int getId () {
        return id;
    }
}
