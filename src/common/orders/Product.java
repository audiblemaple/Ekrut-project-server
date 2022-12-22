package common.orders;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Lior Jigalo, Nitsan Maman
 */
public class Product implements Serializable {
    private float price;
    private float discount;
    private String name;
    private int amount;
    private String description;
    private String type;
    private String productId;
    private int criticalAmount;
    private byte[] file;

    //TODO: add a file or byte array.
    public Product() {
    }

    public Product(float price, float discount, String name, int amount, String description, String type, String productId, byte[] file, int criticalAmount) {
        this.price = price;
        this.discount = discount;
        this.name = name;
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.productId = productId;
        this.file = file;
        this.criticalAmount = criticalAmount;
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

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public int getCriticalAmount() {
        return criticalAmount;
    }

    public void setCriticalAmount(int criticalAmount) {
        this.criticalAmount = criticalAmount;
    }

    @Override
    public String toString() {
        return "Product{" +
                "price=" + price +
                ", discount=" + discount +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", productId='" + productId + '\'' +
                ", criticalAmount=" + criticalAmount +
                ", file=" + Arrays.toString(file) +
                '}';
    }
}
