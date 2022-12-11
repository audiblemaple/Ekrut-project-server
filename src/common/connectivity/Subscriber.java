package common.connectivity;

public class Subscriber {

	// CLASS VARIABLES ***********************************************
	private String firstname;
	private String lastname;
	private String id;
	private String phonenumber;
	private String emailaddress;
	private String creditcardnumber;
	private String subscribernumber;
	//                 ***********************************************

	// CLASS Constructors ********************************************
	public Subscriber(String firstname, String lastname, String id, String phonenumber, String emailaddress,
                      String creditcardnumber, String subscribernumber) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.id = id;
		this.phonenumber = phonenumber;
		this.emailaddress = emailaddress;
		this.creditcardnumber = creditcardnumber;
		this.subscribernumber = subscribernumber;
	}
    //  **************************************************************
	
	// CLASS GETTERS/SETTERS *****************************************
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getEmailaddress() {
		return emailaddress;
	}

	public void setEmailaddress(String emailaddress) {
		this.emailaddress = emailaddress;
	}

	public String getCreditcardnumber() {
		return creditcardnumber;
	}

	public void setCreditcardnumber(String creditcardnumber) {
		this.creditcardnumber = creditcardnumber;
	}

	public String getSubscribernumber() {
		return subscribernumber;
	}

	public void setSubscribernumber(String subscribernumber) {
		this.subscribernumber = subscribernumber;
	}
    //  **************************************************************
	
	// CLASS TO-STRING ***********************************************
	@Override
	public String toString(){
		return String.format("%s %s %s %s %s %s %s\n",firstname,lastname,id,phonenumber,
				emailaddress,creditcardnumber,subscribernumber.toString());
	}
    //  **************************************************************
}
