package common;

import javafx.scene.control.ChoiceBox;

import java.io.Serializable;


/**
 * The Deals class represents a deal offered by the company. It holds information about the deal such as the 
 * deal ID, name, discount, description, type, area, status, start date, and end date.
 * It also implements Serializable interface to make it possible to save the object's state.
 * @author Ravid & Ben
 */
public class Deals implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * The dealID variable holds the unique identification number for the deal.
     */
    private String dealID;
    /**
     * The DealName variable holds the name of the deal.
     */
    private String DealName;
    /**
     * The Discount variable holds the percentage of the discount offered by the deal.
     */
    private float Discount;
    /**
     * The Description variable holds a brief description of the deal.
     */
    private String Description;
    /**
     * The Type variable holds the category of the deal (Drink | SNACKS | ALL).
     */
    private String Type;
    /**
     * The Area_co variable holds a choice box for the area of the deal North | South | UAE  for Manager.
     */
    private ChoiceBox<String> Area_co;
    /**
     * The Status_co variable holds a choice box for the status of the deal Approved | Not Approved  for Manager
     */
    private ChoiceBox<String> Status_co;
    /**
    * The Area variable holds the area of the deal (North, South, UAE) for the employee.
    */
    private String Area;   
    /*
    * The StatusString variable holds the status of the deal (Approved, Not Approved) in string format.
    */
    private String StatusString;
    /*
    * The active variable holds the active status of the deal (active or not active)
    */
    private String active;
    /*
    * The startDate variable holds the start date of the deal.
    */
    private String startDate;
    /*
    * The endDate variable holds the end date of the deal.
    */
    private String endDate;

    /**
     * The default constructor for the Deals class.
     */
    public Deals() {
    }

    /**
     * Constructor for the Deals class that takes in the deal name, discount, description, type, area, status, and active status as arguments.
     * 
     * @param DealName The name of the deal.
     * @param Discount The percentage of the discount offered by the deal.
     * @param Description A brief description of the deal.
     * @param Type The category of the deal (Drink, Snacks, All).
     * @param Area The area of the deal (North, South, UAE).
     * @param StatusString The status of the deal(Approved, Not Approved) in string format.
     * @param active The active status of the deal (active or not active).
     */
    public Deals(String DealName, float Discount, String Description,String Type, String Area , String StatusString, String active) {//result from DB
        this.DealName=DealName;
        this.Discount=Discount;
        this.Description=Description;
        this.Type=Type;
        this.Area=Area;
        this.StatusString=StatusString;
        this.active =active;
    }

    /*
    * A set of getter and setter methods for each variable of the Deals class, 
    * allowing for the retrieval and modification of the information stored in each deal.
    */
    
    public String getDealID() {
        return dealID;
    }

    public void setDealID(String dealID) {
        this.dealID = dealID;
    }

    public String getStatusString() {
        return StatusString;
    }

    public void setStatusString(String statusString) {
        StatusString = statusString;
    }

    public String getDealName() {
        return DealName;
    }

    public void setDealName(String DealName) {
        this.DealName =DealName;
    }

    public float getDiscount() {
        return Discount;
    }

    public void setDiscount(float Discount) {
        this.Discount = Discount;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type =Type;
    }

    public ChoiceBox getArea() {
        return Area_co;
    }

    public void setArea(String area) {
        Area = area;
    }

    public void setArea(ChoiceBox Area_co) {
        this.Area_co=Area_co;
    }

    public String getAreaS() {
        return Area;
    }

    public ChoiceBox getStatus() {
        return Status_co;
    }

    public void setStatus(ChoiceBox Status) {
        this.Status_co =Status;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "\n\t\tDeal: " +
                "\n\t\t\tdealID='" + dealID +
                "\n\t\t\tDealName='" + DealName +
                "\n\t\t\tArea='" + Area;
    }
}
