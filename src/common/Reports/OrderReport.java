package common.Reports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class OrderReport implements Serializable {
    private static final long serialVersionUID = 1L;

    HashMap<String, Integer> machineAndAmount;
    ArrayList<String> locations;

    public OrderReport() {
    }

    public HashMap<String, Integer> getMachineAndAmount() {
        return machineAndAmount;
    }

    public void setMachineAndAmount(HashMap<String, Integer> machineAndAmount) {
        this.machineAndAmount = machineAndAmount;
    }

    public ArrayList<String> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<String> locations) {
        this.locations = locations;
    }

    @Override
    public String toString() {
        return "OrderReport" +
                "machineAndAmount: " + machineAndAmount +
                ", locations: " + locations;
    }
}
