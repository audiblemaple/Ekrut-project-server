package common.Reports;

import common.connectivity.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientReport implements Serializable{
    private static final long serialVersionUID = 1L;
	HashMap<User, Integer> userOrderAmount;

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
