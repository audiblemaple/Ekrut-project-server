package common.orders;

import common.connectivity.User;

import java.io.Serializable;
import java.util.HashMap;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    private String customerId;
    private HashMap<Product, Integer> products;
    private float price;
    private String machineId;
    private int supplyMethod;
    private int paidWith;
    public Order() {
    }
    public Order(String customerId, HashMap<Product, Integer> products, float price, String machineId, int supplyMethod, int paidWith) {
        this.customerId = customerId;
        this.products = products;
        this.price = price;
        this.machineId = machineId;
        this.supplyMethod = supplyMethod;
        this.paidWith = paidWith;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public HashMap<Product, Integer> getProducts() {
        return products;
    }

    public void setProducts(HashMap<Product, Integer> products) {
        this.products = products;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public int getSupplyMethod() {
        return supplyMethod;
    }

    public void setSupplyMethod(int supplyMethod) {
        this.supplyMethod = supplyMethod;
    }

    public int getPaidWith() {
        return paidWith;
    }

    public void setPaidWith(int paidWith) {
        this.paidWith = paidWith;
    }

}
