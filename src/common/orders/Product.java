package common.orders;

import java.io.Serializable;

public class Product implements Serializable {
    private float price;
    private float discount;
    private String name;
    private int amount;

    public Product() {
    }

    public Product(float price, float discount, String name, int amount) {
        this.price = price;
        this.discount = discount;
        this.name = name;
        this.amount = amount;
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
}
