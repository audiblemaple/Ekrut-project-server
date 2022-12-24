package common.orders;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderID;
    private float overallPrice;
    private ArrayList<Product> products;
    private String machineID;
    private String orderDate;
    private String estimatedDeliveryTime;
    private String confirmationDate;
    private String orderStatus;
    private String customerID;
    private String supplyMethod;
    private String paidWith;

    public Order() {
    }

    public Order(String orderID, float overallPrice, ArrayList<Product> products, String machineID, String orderDate, String estimatedDeliveryTime, String confirmationDate, String orderStatus, String customerID, String supplyMethod, String paidWith) {
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
}
