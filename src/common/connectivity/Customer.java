package common.connectivity;

import java.io.Serializable;

/**
 * The Customer class is a child class of the User class and implements the Serializable 
 * interface to make it possible to save the object's state. 
 * It holds additional information about the customer such as credit card number, 
 * subscription status, first time buy as subscription status, and subscriber number.
 *  @author Ron shahar and Nitsan maman
 */
public class Customer extends User implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	// CLASS FIELDS ***********************************************
	private String creditCardNumber;
	private boolean isSub;
	private boolean	isFirstBuyAsSub;
	private int subscriberNumber;
	//****************************************************************



	public Customer() {
		super();
	}


	/**
	 * A constructor for the Customer class that takes in the customer's username, password, first name, last name, id, phone number, email address, logged in status, department, status, and credit card number as arguments.
	 * @param username The username of the customer.
	 * @param password The password of the customer.
	 * @param firstname The first name of the customer.
	 * @param lastname The last name of the customer.
	 * @param id The id of the customer.
	 * @param phonenumber The phone number of the customer.
	 * @param emailaddress The email address of the customer.
	 * @param isLoggedIn The logged in status of the customer.
	 * @param department The department of the customer.
	 * @param status The status of the customer.
	 * @param creditCardNumber The credit card number of the customer.
	 */
	public Customer(String username, String password, String firstname,String lastname, String id,
				String phonenumber, String emailaddress, String isLoggedIn, String department, String status, String creditCardNumber) {
		super(username, password, firstname,lastname, id, phonenumber, emailaddress, isLoggedIn, department, status);
		this.creditCardNumber = creditCardNumber;
	}

		
		
	// CLASS GETTERS/SETTERS *****************************************
	public String getCreditCardNumber() {
		return creditCardNumber;
	}
	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}
	public boolean isSub() {
		return isSub;
	}
	public void setSub(boolean isSub) {
		this.isSub = isSub;
	}

	public boolean isFirstBuyAsSub() {
		return isFirstBuyAsSub;
	}

	public void setFirstBuyAsSub(boolean firstBuyAsSub) {
		isFirstBuyAsSub = firstBuyAsSub;
	}

	public int getSubscriberNumber() {
		return subscriberNumber;
	}

	public void setSubscriberNumber(int subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}

	//  **************************************************************
		
		// CLASS TO-STRING ***********************************************

	@Override
	public String toString() {
		return "\n\t\t\tCustomer: " + subscriberNumber;
	}
	//  **************************************************************
}
