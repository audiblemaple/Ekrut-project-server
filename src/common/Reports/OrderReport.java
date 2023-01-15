package common.Reports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The OrderReport class is a data structure used to store information about the orders made from a specific vending machine.
 * It contains information such as the machine ID, location, number of orders made, and the month and year of the orders.
 * This class implements the Serializable interface, which allows it to be written to a file or sent over a network.
 * @author Ravid
 */
public class OrderReport implements Serializable {
    private static final long serialVersionUID = 1L;

    String machineid;
    String machineLocation;
    int numberOfOrders;
    String month;
    String year;

    /**
     * Default constructor for the OrderReport class.
     */ 
    public OrderReport() {
    }

    /*
     * Getters and setters for the class variables
    */
    public String getMachineid() {
        return machineid;
    }

    public void setMachineid(String machineid) {
        this.machineid = machineid;
    }

    public String getMachineLocation() {
        return machineLocation;
    }

    public void setMachineLocation(String machineLocation) {
        this.machineLocation = machineLocation;
    }

    public int getNumberOfOrders() {
        return numberOfOrders;
    }

    public void setNumberOfOrders(int numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
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

    @Override
    public String toString() {
        return "OrderReport{" +
                "machineid='" + machineid + '\'' +
                ", machineLocation='" + machineLocation + '\'' +
                ", numberOfOrders=" + numberOfOrders +
                ", month='" + month + '\'' +
                ", year='" + year + '\'' +
                '}';
    }
}
