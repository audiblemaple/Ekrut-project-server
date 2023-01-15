package common.orders;

import java.io.Serializable;
import java.util.Arrays;

/**
*
* The Product class represents a product in the vending machine system. 
* It contains information such as the product's price, discount, name, description, type, productId, 
* criticalAmount, machineID, numOfTimesBelowCritical, and file.
* @author Lior Jigalo, Nitsan Maman, Ron Shahar
* @version 1.0
* @since 1.0
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

    /**
    Creates an empty Product object.
    */
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

    /**
    *Creates a Product object with the given parameters.
    *@param price The price of the product.
    *@param discount The discount of the product.
    *@param name The name of the product.
    *@param amount The amount of the product.
    *@param description The description of the product.
    *@param type The type of the product.
    *@param productId The product ID of the product.
    *@param file The file of the product.
    *@param criticalAmount The critical amount of the product.
    */
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
