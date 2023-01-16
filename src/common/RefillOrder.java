package common;

import javafx.scene.control.ChoiceBox;

import java.io.Serializable;

/**
 * The RefillOrder class represents an order for refilling a vending machine. It extends the TableRefilOrder class 
 * and implements the Serializable interface to make it possible to save the object's state. It holds information 
 * about the order such as the order ID, product ID, machine ID, creation date, amount at request, new amount, 
 * assigned employee and product name.
 * @author Ravid & Ben
 */
public class RefillOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    private String orderID;
    private String ProductID;
    private String MachineID;
    private String creationDate;
    private int amountAtRequest;
    private int newAmount = 0;
    private String assignedEmployee;
    private String productName;

    /**
     * The default constructor for the RefillOrder class.
     */
    public RefillOrder() {
    }

    /**
     * A set of getter and setter methods for each variable of the RefillOrder class, 
     * allowing for the retrieval and modification of the information stored in each refill order.
     */
    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getMachineID() {
        return MachineID;
    }

    public void setMachineID(String machineID) {
        MachineID = machineID;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public int getAmountAtRequest() {
        return amountAtRequest;
    }

    public void setAmountAtRequest(int amountAtRequest) {
        this.amountAtRequest = amountAtRequest;
    }

    public String getAssignedEmployee() {
        return assignedEmployee;
    }

    public void setAssignedEmployee(String assignedEmployee) {
        this.assignedEmployee = assignedEmployee;
    }

    public int getNewAmount() {
        return newAmount;
    }

    public void setNewAmount(int newAmount) {
        this.newAmount = newAmount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "\n\tRefillOrder:" + "\n" +
                "\t\torderID='" + orderID + '\n' +
                "\t\tProductID='" + ProductID + '\n' +
                "\t\tMachineID='" + MachineID + '\n' +
                "\t\tassignedEmployee='" + assignedEmployee + '\n';
    }
}
