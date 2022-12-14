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
				productList.add(product);
			}
			return productList;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return null;
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
				user.setFirstname(res.getString("firstname"));
				user.setLastname(res.getString("lastname"));
				user.setId(res.getString("id"));
				user.setPhonenumber(res.getString("phonenumber"));
				user.setEmailaddress(res.getString("emailaddress"));
			}

			if(setUserLogInStatus(credentials, "1")){
				return null;
			}
			return user;
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


//	public boolean addUser(String firstname,  String lastname, String id, String phonenumber, String emailaddress, String creditcardnumber){
//		// adding 1 to subscriber number to save the incremented number of subscribers
//		Integer subscribernumber = getSubscriberNum() + 1;
//		PreparedStatement stmt;
//		String query = "INSERT INTO " +  this.dataBasename + ".user(firstname, lastname, id, phonenumber, emailaddress, creditcardnumber, subscribernumber) VALUES(?, ?, ?, ?, ?, ?,?)";
//
////		if (!checkUserExists(id)){
////			try{
////				stmt = connection.prepareStatement(query);
////				stmt.setString(1,firstname);
////				stmt.setString(2,lastname);
////				stmt.setString(3,id);
////				stmt.setString(4,phonenumber);
////				stmt.setString(5,emailaddress);
////				stmt.setString(6,creditcardnumber);
////				stmt.setString(7,subscribernumber.toString());
////
////				stmt.executeUpdate();
////				if(checkUserExists(id)){
////					System.out.printf("user added successfully");
////					return true;
////				}
////			}
////			catch (SQLException e){
////				e.printStackTrace();
////				return false;
////			}
////		}
//		System.out.printf("user already exists");
//		return false;
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