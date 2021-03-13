package sample.Serializers;

import java.io.Serializable;

public class ProductSerializer implements Serializable {
    private String name;

    private int quantity;

    private int price;

    private String category;

    public ProductSerializer() {

    }

    public ProductSerializer(String name, int quantity, int price, String category) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }
}
