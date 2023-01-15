package common.Reports;

import common.connectivity.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The ClientReport class is responsible for holding information about the client report.
 * It includes a hashmap that has a user as the key and the number of orders made by that user as the value.
 * The class also has a default constructor and getters and setters for the userOrderAmount field.
 * @author Ben & Ravid
 */
public class ClientReport implements Serializable{
    private static final long serialVersionUID = 1L;
	/**
	 * A hashmap that holds the user as the key and the number of orders made by that user as the value.
	 */
    HashMap<User, Integer> userOrderAmount;

	/**
	 * A default constructor for the ClientReport class
	 */
	public ClientReport() {
	}

	public HashMap<User, Integer> getUserOrderAmount() {
		return userOrderAmount;
	}

	public void setUserOrderAmount(HashMap<User, Integer> userOrderAmount) {
		this.userOrderAmount = userOrderAmount;
	}

	@Override
	public String toString() {
		return "ClientReport{" +
				"userOrderAmount=" + userOrderAmount +
				'}';
	}
}
