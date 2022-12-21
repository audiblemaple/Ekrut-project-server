package common.orders;

import java.io.Serializable;

public class Product implements Serializable {
    private float price;
    private float discount;
    private String name;
    private int amount;
    private String description;
    private String type;
    private String productId;

    //TODO: add a file or byte array.
    public Product() {
    }

    public Product(float price, float discount, String name, int amount, String description, String type, String productId) {
        this.price = price;
        this.discount = discount;
        this.name = name;
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.productId = productId;

    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
