package common.connectivity;

import java.io.Serializable;

public class User implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	// CLASS FIELDS ***********************************************
	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private String id;
	private String phonenumber;
	private String emailaddress;
	private String isLoggedIn;
	private String department;


	//****************************************************************

	// CLASS Constructors ********************************************
	public User() {
		this.firstname = null;
		this.lastname = null;
		this.id = null;
		this.phonenumber = null;
		this.emailaddress = null;

		this.password = null;
		this.username = null;
	}
	public User(String firstname, String lastname, String id, String phonenumber, String emailaddress,
				String creditcardnumber, String subscribernumber) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.id = id;
		this.phonenumber = phonenumber;
		this.emailaddress = emailaddress;

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


	public String getIsLoggedIn() {
		return isLoggedIn;
	}

	public void setIsLoggedIn(String isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	//  **************************************************************

	// CLASS TO-STRING ***********************************************
	@Override
	public String toString(){
		return String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s\n"
				,username,password,firstname,lastname,id,phonenumber, emailaddress,isLoggedIn, department);
	}
	//  **************************************************************
}
