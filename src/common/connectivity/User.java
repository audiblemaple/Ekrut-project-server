package common.connectivity;

import java.io.Serializable;

/**
 * The User class implements the Serializable interface to make it possible to save the object's state. 
 * It holds information about a user such as username, password, first name, last name, id, phone number, and email address.
 * @author Ron
 */
public class User implements Serializable{

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
	private String status;


//****************************************************************

    /**
     * The default constructor for the User class. 
     * It sets all the variables to null.
     */
	public User() {
		this.firstname 	  = null;
		this.lastname 	  = null;
		this.id 		  = null;
		this.phonenumber  = null;
		this.emailaddress = null;
		this.password 	  = null;
		this.username 	  = null;
		this.status		  = null;
	}
	
    /**
     * A constructor for the User class that takes in the user's username, password, first name, last name, id, phone number, email address, logged in status, department, and status as arguments.
     * 
     * @param username The username of the user.
     * @param password The password of the user.
     * @param firstname The first name of the user.
     * @param lastname The last name of the user.
     * @param id The id of the user.
     * @param phonenumber The phone number of the user.
     * @param emailaddress The email address of the user.
     * @param isLoggedIn The logged in status of the user.
     * @param department The department of the user.
     * @param status The status of the user.
     */
	public User(String username, String password, String firstname,String lastname, String id,
				String phonenumber, String emailaddress, String isLoggedIn, String department, String status) {
		this.username 	  = username;
		this.password 	  = password;
		this.firstname 	  = firstname;
		this.lastname 	  = lastname;
		this.id 		  = id;
		this.phonenumber  = phonenumber;
		this.emailaddress = emailaddress;
		this.isLoggedIn   = isLoggedIn;
		this.department   = department;
		this.status 	  = status;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	//  **************************************************************

	// CLASS TO-STRING ***********************************************
	@Override
	public String toString(){
		return String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s\n"
				,username, password, firstname, lastname, id, phonenumber,
				emailaddress, isLoggedIn, department);
	}
	//  **************************************************************
}