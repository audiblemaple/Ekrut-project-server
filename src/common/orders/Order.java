package common.orders;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The Order class is used to store information about an order. It implements the Serializable interface
 * to make it possible to save the object's state.
 * @author Lior & Ron & Nitsan
 */
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderID;
    private float overallPrice;
    private ArrayList<Product> products;
    private String machineID;
    private String area;
    private String orderDate;
    private String estimatedDeliveryTime;
    private String confirmationDate;
    private String orderStatus;
    private String customerID;
    private String supplyMethod;
    private String paidWith;
    private String address;

    /**
     * The default constructor for the Order class. 
     * It sets all the variables to null.
     */
    public Order() {
    }

    /**
     * A constructor for the Order class that takes in the order's ID, overall price, products, machine ID, order date, estimated delivery time, confirmation date, order status, customer ID, supply method, paid with, and address as arguments.
     * 
     * @param orderID The unique identifier for the order.
     * @param overallPrice The overall price of the order.
     * @param products The products included in the order.
     * @param machineID The machine ID that the order was placed at.
     * @param orderDate The date the order was placed.
     * @param estimatedDeliveryTime The estimated delivery time for the order.
     * @param confirmationDate The date the order was confirmed.
     * @param orderStatus The current status of the order.
     * @param customerID The ID of the customer that placed the order.
     * @param supplyMethod The method used to supply the order (e.g. delivery or pickup).
     * @param paidWith The method used to pay for the order (e.g. credit card or cash).
     * @param address The delivery address for the order.
     */
    public Order(String orderID, float overallPrice, ArrayList<Product> products, String machineID, String orderDate, String estimatedDeliveryTime, String confirmationDate, String orderStatus, String customerID, String supplyMethod, String paidWith, String address) {
        this.orderID = orderID;
        this.overallPrice = overallPrice;
        this.products = products;
        this.machineID = machineID;
        this.orderDate = orderDate;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
        this.confirmationDate = confirmationDate;
        this.orderStatus = orderStatus;
        this.customerID = customerID;
        this.supplyMethod = supplyMethod;
        this.paidWith = paidWith;
        this.address = address;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public float getOverallPrice() {
        return overallPrice;
    }

    public void setOverallPrice(float overallPrice) {
        this.overallPrice = overallPrice;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public String getMachineID() {
        return machineID;
    }

    public void setMachineID(String machineID) {
        this.machineID = machineID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    public void setEstimatedDeliveryTime(String estimatedDeliveryTime) {
        this.estimatedDeliveryTime = estimatedDeliveryTime;
    }

    public String getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(String confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getSupplyMethod() {
        return supplyMethod;
    }

    public void setSupplyMethod(String supplyMethod) {
        this.supplyMethod = supplyMethod;
    }

    public String getPaidWith() {
        return paidWith;
    }

    public void setPaidWith(String paidWith) {
        this.paidWith = paidWith;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderID='" + orderID + '\'' +
                ", overallPrice=" + overallPrice +
                ", products=" + products +
                ", machineID='" + machineID + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", estimatedDeliveryTime='" + estimatedDeliveryTime + '\'' +
                ", confirmationDate='" + confirmationDate + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", customerID='" + customerID + '\'' +
                ", supplyMethod='" + supplyMethod + '\'' +
                ", paidWith='" + paidWith + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
