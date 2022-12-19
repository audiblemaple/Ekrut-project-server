package Application.server;

import common.connectivity.Message;
import common.connectivity.User;
import common.orders.Product;

import java.sql.*;
import java.util.ArrayList;


public class MysqlController {
	private static MysqlController sqlInstance = null;
	private String dataBasename;
	private String dataBaseusername;
	private String dataBasepassword;
	private String IP;
	private Connection connection;

	public static MysqlController getSQLInstance(){
		if (sqlInstance == null)
			sqlInstance = new MysqlController();
		return sqlInstance;
	}

	public void setDataBaseName(String name) {
		this.dataBasename = name;
	}

	public void setDataBaseUsername(String username) {
		this.dataBaseusername = username;
	}

	public void setDataBasePassword(String password) {
		this.dataBasepassword = password;
	}

	public void setDataBaseIP(String IP) {
		this.IP = IP;
	}

	public  String connectDataBase(){
		String returnStatement = "";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			returnStatement += "Driver definition succeed\n";
		} catch (Exception ex) {
			returnStatement += "Driver definition failed\n";
		}

		try {
			String jdbcURL = "jdbc:mysql://" + this.IP + ":3306?serverTimezone=UTC";
			this.connection = DriverManager.getConnection(jdbcURL, this.dataBaseusername, this.dataBasepassword);
			returnStatement += "SQL connection succeed\n";
			return returnStatement;

		} catch (SQLException ex) {
			/* handle any errors*/
			returnStatement += "SQLException: " + ex.getMessage() + "\n";
			returnStatement += "SQLState: " + ex.getSQLState() + "\n";
			returnStatement += "VendorError: " + ex.getErrorCode() + "\n";
			return returnStatement;
		}
	}

	private int getUserNum(){
		String query = "SELECT COUNT(*) FROM " + this.dataBasename + ".user";
		try{
			Statement stmt = connection.createStatement();
			ResultSet res = stmt.executeQuery(query);
			if (res.next()){
				return res.getInt("count(*)");
			}
		}catch (SQLException exception){
			exception.printStackTrace();
		}
		return 0;
	}




	public ArrayList<Product> getAllProductsForMachine(String machineId){
		if (machineId == null)
			throw new NullPointerException();

		PreparedStatement stmt;
		ResultSet res;
		String loginQuery = "SELECT * FROM " + this.dataBasename + ".products WHERE machineid = ?";
		ArrayList<Product> productList = new ArrayList<Product>();
		try{
			stmt = connection.prepareStatement(loginQuery);
			stmt.setString(1, machineId);
			res = stmt.executeQuery();
			Product product = new Product();

			while(res.next()){
				product.setName(res.getString("name"));
				product.setPrice(res.getFloat("price"));
				product.setDiscount(res.getFloat("discount"));
				product.setAmount(res.getInt("amount"));
				productList.add(product);
			}
			return productList;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return null;
		}
	}

	public ArrayList<Product> getAllProductsForAllMachines(){
		PreparedStatement stmt;
		ResultSet res;
		String loginQuery = "SELECT * FROM " + this.dataBasename + ".products";
		ArrayList<Product> productList = new ArrayList<Product>();
		try{
			stmt = connection.prepareStatement(loginQuery);
			res = stmt.executeQuery();

			while(res.next()){
				Product product = new Product();
				product.setName(res.getString("name"));
				product.setPrice(res.getFloat("price"));
				product.setDiscount(res.getFloat("discount"));
				product.setAmount(res.getInt("amount"));
				product.setDescription(res.getString("description"));
				productList.add(product);
			}
			return productList;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return null;
		}
	}


	public String dataExists(User user){
		PreparedStatement stmt;
		ResultSet res;
		String query = "SELECT * FROM " + this.dataBasename + ".users WHERE username = ? AND id = ?";

		try{
			stmt = connection.prepareStatement(query);
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getId());
			res = stmt.executeQuery();
			while(res.next()){
				User temp = new User();
				temp.setUsername(res.getString("username"));
				temp.setId(res.getString("id"));
				if(temp.getUsername().equals(user.getUsername())){
					return "username already exists.";
				}
				if (temp.getId().equals(user.getId())){
					return "id is already exists.";
				}
			}
			return "";
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return null;
		}
	}

	public boolean addUser(User user){
		String query = "INSERT INTO " +  this.dataBasename + ".users(username, password, firstname, lastname, id, phonenumber, emailaddress, isloggedin, department) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt;
		try{
			stmt = connection.prepareStatement(query);
			stmt.setString(1,user.getUsername());
			stmt.setString(2,user.getPassword());
			stmt.setString(3,user.getFirstname());
			stmt.setString(4,user.getLastname());
			stmt.setString(5,user.getId());
			stmt.setString(6,user.getPhonenumber());
			stmt.setString(7,user.getEmailaddress());
			stmt.setBoolean(8,false);
			stmt.setString(9,user.getDepartment());
			stmt.executeUpdate();

			if(checkUserExists(user.getId())){
				return true;
			}
			return false;
		}
		catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}


	private boolean checkUserExists(String id){
		PreparedStatement stmt;
		ResultSet res;
		String loginQuery = "SELECT id FROM " + this.dataBasename + ".users WHERE id = ?";

		try{
			stmt = connection.prepareStatement(loginQuery);
			stmt.setString(1, id);
			res = stmt.executeQuery();
			String expected = "";

			while(res.next()){
				expected = res.getString("id");
			}

			if(expected.equals(id)){
				return true;
			}
			return false;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return false;
		}
	}



	protected void disconnect(){
		try{
			connection.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
	}

	public Connection getConnection(){
		return this.connection;
	}

	protected String getName(){
		try{
			return connection.getCatalog();
		}
		catch (SQLException e){
			return "null";
		}
	}

	public User logUserIn(ArrayList<String> credentials) {
		boolean userFound = false;
		if (credentials == null)
			throw new NullPointerException();

		PreparedStatement stmt;
		ResultSet res;
		String loginQuery = "SELECT * FROM " + this.dataBasename + ".users WHERE username = ? AND password = ?";
		try{
			stmt = connection.prepareStatement(loginQuery);
			stmt.setString(1, credentials.get(0));
			stmt.setString(2, credentials.get(1));
			res = stmt.executeQuery();
			User user = new User();

			while(res.next()){
				userFound = true;
				user.setUsername(res.getString("username"));
				user.setFirstname(res.getString("firstname"));
				user.setLastname(res.getString("lastname"));
				user.setId(res.getString("id"));
				user.setPhonenumber(res.getString("phonenumber"));
				user.setEmailaddress(res.getString("emailaddress"));
				user.setDepartment(res.getString("department"));
				user.setStatus(res.getString("userstatus"));
			}
			setUserLogInStatus(credentials, "1");
			if(isLoggedIn(credentials) && userFound){
				return user;
			}
			return null;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return null;
		}
	}

	public boolean logUserOut(ArrayList<String> credentials){
		if (credentials == null)
			throw  new NullPointerException();
		if (!isLoggedIn(credentials))
			return false;
		setUserLogInStatus(credentials, "0");
		return true;
	}

	public boolean setUserLogInStatus(ArrayList<String> credentials, String status){
		PreparedStatement stmt;
		String setLoginStatusQuery = "UPDATE " + this.dataBasename + ".users SET isloggedin = ? WHERE username = ?";
		try{
			stmt = connection.prepareStatement(setLoginStatusQuery);
			stmt.setString(1, status);
			stmt.setString(2, credentials.get(0));
			stmt.executeUpdate();
			return true;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return false;
		}
	}

	public boolean isLoggedIn(ArrayList<String> credentials){
		if (credentials == null)
			throw new NullPointerException();

		PreparedStatement stmt;
		ResultSet res;

		String checkUpdated = "SELECT (isloggedin) FROM " + this.dataBasename + ".users WHERE username = ?";
		String expected = "";
		try{
			stmt = connection.prepareStatement(checkUpdated);
			stmt.setString(1, credentials.get(0));
			res = stmt.executeQuery();
			while (res.next()){
				expected = res.getString("isloggedin");
			}
			// if equals 1 then logged in.
			return expected.equals("1");

		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return true;
		}
	}

	public boolean deleteUser(String id){
		PreparedStatement stmt;
		String query = "DELETE FROM " + this.dataBasename + ".users WHERE id=?";
		if(!checkUserExists(id))
			return false;
		try {
			stmt = connection.prepareStatement(query);
			stmt.setString(1, id);
			stmt.executeUpdate();

			return !checkUserExists(id);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public ArrayList<String> getMachineIds(){
		ArrayList<String> machines = new ArrayList<String>();
		PreparedStatement stmt;
		ResultSet res;
		boolean hasResult = false;
		String loginQuery = "SELECT * FROM " + this.dataBasename + ".machines";
		try{
			stmt = connection.prepareStatement(loginQuery);
			res = stmt.executeQuery();

			while(res.next()){
				hasResult = true;
				machines.add(res.getString("machineid"));
			}
			if (hasResult)
				return machines;

			return null;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return null;
		}
	}
}




// OLD FUNCTIONS:
//	public String getAllDB(){
//		String query = "SELECT * FROM " + this.dataBasename + ".user";
//		String database = "";
//		try{
//			Statement stmt = connection.createStatement();
//			ResultSet res = stmt.executeQuery(query);
//			while(res.next()){
//				database += res.getString("firstname");
//				database +=" ";
//				database += res.getString("lastname");
//				database +=" ";
//				database += res.getString("id");
//				database +=" ";
//				database += res.getString("phonenumber");
//				database +=" ";
//				database += res.getString("emailaddress");
//				database +=" ";
//				database += res.getString("creditcardnumber");
//				database +=" ";
//				database += res.getString("subscribernumber");
//				database +="\n";
//			}
//			return database;
//		}catch (SQLException exception){
//			exception.printStackTrace();
//			return null;
//		}
//	}



//	public boolean deleteUser(User user){
//		PreparedStatement stmt;
//		String query = "DELETE FROM " + this.dataBasename + ".user WHERE id=?";
//		if(!checkUserExists(user))
//			return false;
//		try {
//			stmt = connection.prepareStatement(query);
//			stmt.setString(1, user.getId());
//			stmt.executeUpdate();
//			return true;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return false;
//	}


//	public boolean checkUserExists(User clientMessage){
//		PreparedStatement stmt;
//		ResultSet res;
//		String query = "SELECT * FROM " + this.dataBasename +".user WHERE (id) = (?)";
//
//		try{
//			stmt = connection.prepareStatement(query);
//			stmt.setString(1,clientMessage.getId());
//			res = stmt.executeQuery();
//			if (res.next()){
//				if (res.getString("id").equals(clientMessage.getId())){
//
//					return true;
//				}
//			}
//			stmt.close();
//			res.close();
//		}
//		catch (SQLException e){
//			e.printStackTrace();
//		}
//		return false;
//	}




//	public void updateUser(String id, String creditcardnumber, String subscribernumber){
//		PreparedStatement stmt;
//		String query = "UPDATE " + this.dataBasename + ".users SET creditcardnumber = ?, subscribernumber = ? WHERE id = ?";
//		if(creditcardnumber == "null"){
//			creditcardnumber = "";
//		}
//		try {
//			stmt = connection.prepareStatement(query);
//			stmt.setString(1, creditcardnumber);
//			stmt.setString(2, subscribernumber);
//			stmt.setString(3, id);
//			stmt.executeUpdate();
//			System.out.println("update done successfully");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}