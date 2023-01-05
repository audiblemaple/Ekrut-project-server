package common.connectivity;

import java.io.Serializable;

public class Customer extends User implements Serializable{

	private static final long serialVersionUID = 1L;

	// CLASS FIELDS ***********************************************
	private String creditCardNumber;
	private boolean isSub;
	private boolean	isFirstBuyAsSub;

	private int subscriberNumber;
	//****************************************************************


	// CLASS Constructors ********************************************
	public Customer() {
		super();
	}
	public Customer(String username, String password, String firstname,String lastname, String id,
					String phonenumber, String emailaddress, String isLoggedIn, String department, String status, String creditCardNumber) {
		super(username, password, firstname,lastname, id, phonenumber, emailaddress, isLoggedIn, department, status);
		this.creditCardNumber = creditCardNumber;
	}
	//  **************************************************************


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
		return "Customer{" +
				"creditCardNumber='" + creditCardNumber + '\'' +
				", isSub=" + isSub +
				", isFirstBuyAsSub=" + isFirstBuyAsSub +
				", subscriberNumber=" + subscriberNumber +
				'}';
	}
	//  **************************************************************
}
