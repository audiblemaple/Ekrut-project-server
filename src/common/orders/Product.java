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
    private String productName;
    private int amount;
    private String description;
    private String type;
    private String productId;
    private int criticalAmount;
    private String machineID;
    private int numOfTimesBelowCritical;
    private byte[] file;

    public Product() {
        this.price = 0;
        this.discount = 0;
        this.name = null;
        this.amount = 0;
        this.description = null;
        this.type = null;
        this.productId = null;
        this.file = null;
        this.criticalAmount = 0;
        this.machineID = null;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getMachineID() {
        return machineID;
    }

    public void setMachineID(String machineID) {
        this.machineID = machineID;
    }

    public int getNumOfTimesBelowCritical() {
        return numOfTimesBelowCritical;
    }

    public void setNumOfTimesBelowCritical(int numOfTimesBelowCritical) {
        this.numOfTimesBelowCritical = numOfTimesBelowCritical;
    }

    @Override
    public String toString() {
        return "\n\t\tname: " + name +
                " amount: " + amount + "\n\t\t";
    }
}
