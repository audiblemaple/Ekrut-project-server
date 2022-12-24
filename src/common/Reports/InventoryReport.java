package common.Reports;

import common.orders.Product;
import java.io.Serializable;
import java.util.ArrayList;

public class InventoryReport implements Serializable {
    private static final long serialVersionUID = 1L;

    String reportID;
    String area;
    String machineID;
    ArrayList<Product> products;
    String month;
    String year;
    float totalValue;

    public InventoryReport() {
    }

    public InventoryReport(String reportID, String area, String machineID, ArrayList<Product> products, String month, String year, float totalValue) {
        this.reportID = reportID;
        this.area = area;
        this.machineID = machineID;
        this.products = products;
        this.month = month;
        this.year = year;
        this.totalValue = totalValue;
    }

    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getMachineID() {
        return machineID;
    }

    public void setMachineID(String machineID) {
        this.machineID = machineID;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
    public float getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(float totalValue) {
        this.totalValue = totalValue;
    }

//    @Override
//    public String toString() {
//        return "InventoryReport{" +
//                "reportID='" + reportID + '\'' +
//                ", area='" + area + '\'' +
//                ", machineID='" + machineID + '\'' +
//                ", products=" + products +
//                ", month='" + month + '\'' +
//                ", year='" + year + '\'' +
//                ", totalValue=" + totalValue +
//                '}';
//    }
}
