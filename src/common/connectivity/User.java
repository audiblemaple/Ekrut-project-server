package common.connectivity;

import java.io.Serializable;

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
	 *
	 */
	// CLASS Constructors ********************************************
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
	 * @param username
	 * @param password
	 * @param firstname
	 * @param lastname
	 * @param id
	 * @param phonenumber
	 * @param emailaddress
	 * @param isLoggedIn
	 * @param department
	 * @param status
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

	/**
	 * @return
	 */
	// CLASS GETTERS/SETTERS *****************************************
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @param lastname
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return
	 */
	public String getPhonenumber() {
		return phonenumber;
	}

	/**
	 * @param phonenumber
	 */
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	/**
	 * @return
	 */
	public String getEmailaddress() {
		return emailaddress;
	}

	/**
	 * @param emailaddress
	 */
	public void setEmailaddress(String emailaddress) {
		this.emailaddress = emailaddress;
	}


	/**
	 * @return
	 */
	public String getIsLoggedIn() {
		return isLoggedIn;
	}

	/**
	 * @param isLoggedIn
	 */
	public void setIsLoggedIn(String isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	/**
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * @param department
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * @return
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	//  **************************************************************

	/**
	 * @return this objects fields as inline stringsstrings
	 */
	// CLASS TO-STRING ***********************************************
	@Override
	public String toString(){
		return String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s %s"
				,username, password, firstname, lastname, id, phonenumber,
				emailaddress, isLoggedIn, department, status);
	}
	//  **************************************************************
}