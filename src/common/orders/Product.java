package common.orders;

import java.io.Serializable;

public class Product implements Serializable {
	private String productid;
	private float price;
    private float discount;
    private String name;
    private int amount;
    private String description;

    public Product() {
    }

    public Product(String productid, float price, float discount, String name, int amount, String description) {
        this.productid = productid;
    	this.price = price;
        this.discount = discount;
        this.name = name;
        this.amount = amount;
        this.description = description;
    }
    
    public String getProductId() {
        return productid;
    }

    public void setProductId(String id) {
        this.productid = productid;
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
}
