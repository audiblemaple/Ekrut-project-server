package Application.server;

import common.connectivity.User;
import common.orders.Product;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;


/**
 * @author Lior Jigalo
 * This class communcates with the database.
 */
public class MysqlController {
	private static MysqlController sqlInstance = null;
	private String dataBasename;
	private String dataBaseusername;
	private String dataBasepassword;
	private String IP;
	private Connection connection;

	/**
	 * @return a single instance of this class.
	 * This method allows to get an instance of this Singleton class.
	 */
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

	/**
	 * @return returns connection message from database.
	 * This method connects MysqlController to the database.
	 */
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

	/**
	 * @return number of users in the database.
	 * This method counts the number of users in the users table and returns it.
	 */
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


	/**
	 * @param machineId id of a specific machine in the database.
	 * @return Arraylist of products in a specific machine.
	 * This method finds all products that belong to a specific machine id.
	 */
	public ArrayList<Product> getMachineProducts(String machineId, boolean needAll){
		if (machineId == null)
			throw new NullPointerException();

		if (machineId == null)
			throw new NullPointerException();

		PreparedStatement stmt;
		ResultSet res;
		String query;
		ArrayList<Product> productList = new ArrayList<Product>();
		boolean resultFound = false;
		// choose if we need all products or a specific machine
		if (needAll)
			query = "SELECT * FROM " + this.dataBasename + ".productsinmachines";
		else
			query = "SELECT * FROM " + this.dataBasename + ".productsinmachines WHERE machineid = ?";

		try{
			stmt = connection.prepareStatement(query);
			if (!needAll)
				stmt.setString(1, machineId);
			res = stmt.executeQuery();
			ResultSet productRes = null;
			File file = null;
			while(res.next()){
				resultFound = true;
				Product product = new Product();
				productRes = getProductData(res.getString("productid"));

				// add product info from products in table
				product.setProductId(res.getString("productid"));
				product.setDiscount(res.getFloat("discount"));
				product.setAmount(res.getInt("amount"));
				product.setCriticalAmount(res.getInt("criticalamount"));
				if (productRes == null){
					System.out.println("product " + productRes + "is null");
					continue;
				}
				while (productRes.next()){
					// add specific  product info from products table
					product.setName(productRes.getString("name"));
					product.setPrice(productRes.getFloat("price"));
					product.setDescription(productRes.getString("description"));
					product.setType(productRes.getString("type"));

					// create file and streams
					Path path = Paths.get("src/Application/images/", productRes.getString("name") + ".png");
					file = new File(path.toUri());
					FileInputStream fis = null;
					try {
						fis = new FileInputStream(file);
						byte[] outputFile = new byte[(int)file.length()];
						BufferedInputStream bis = new BufferedInputStream(fis);
						bis.read(outputFile,0,outputFile.length);
						// add file to product object
						product.setFile(outputFile);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
				productList.add(product);
			}
			if (resultFound)
				return productList;
			return null;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return null;
		}
	}

	public ArrayList<Product> getWarehouseProducts(){
		PreparedStatement stmt;
		ResultSet res;
		String query;
		ArrayList<Product> productList = new ArrayList<Product>();
		boolean resultFound = false;
		// choose if we need all products or a specific machine

		query = "SELECT * FROM " + this.dataBasename + ".warehouse"; // TODO: change

		try{
			stmt = connection.prepareStatement(query);
			res = stmt.executeQuery();
			ResultSet productRes = null;
			File file = null;
			while(res.next()){
				resultFound = true;
				Product product = new Product();
				productRes = getProductData(res.getString("productid"));
				// add product info from products in table
				product.setProductId(res.getString("productid"));
				product.setDiscount(res.getFloat("discount"));
				product.setAmount(res.getInt("amount"));
				product.setCriticalAmount(res.getInt("criticalamount"));
				if (productRes == null){
					System.out.println("product " + productRes + "is null");
					continue;
				}
				while (productRes.next()){
					// add specific  product info from products table
					product.setName(productRes.getString("name"));
					product.setPrice(productRes.getFloat("price"));
					product.setDescription(productRes.getString("description"));
					product.setType(productRes.getString("type"));

					// create file and streams
					Path path = Paths.get("src/Application/images/", productRes.getString("name") + ".png");
					file = new File(path.toUri());
					FileInputStream fis = null;
					try {
						fis = new FileInputStream(file);
						byte[] outputFile = new byte[(int)file.length()];
						BufferedInputStream bis = new BufferedInputStream(fis);
						bis.read(outputFile,0,outputFile.length);
						// add file to product object
						product.setFile(outputFile);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
				productList.add(product);
			}
			if (resultFound)
				return productList;
			return null;

		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return null;
		}
	}


	/**
	 * @param productId
	 * @return
	 */
	private ResultSet getProductData(String productId){
		if (productId == null)
			throw new NullPointerException();

		PreparedStatement stmt;
		ResultSet res;
		String query = "SELECT * FROM " + this.dataBasename + ".products WHERE productid = ?";
		boolean productfound = false;
		try{
			stmt = connection.prepareStatement(query);
			stmt.setString(1, productId);
			res = stmt.executeQuery();

			return res;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return null;
		}
	}


	/**
	 * @param user whom existence is needed to be checked
	 * @return if exists return error message, else an empty string.
	 * This method checks if id or username of the given user already exists ind the database.
	 */
	public String dataExists(User user){
		PreparedStatement stmt;
		ResultSet res;
		String query = "SELECT * FROM " + this.dataBasename + ".users WHERE username = ? OR id = ?";

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
					return "id already exists.";
				}
			}
			return "";
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return null;
		}
	}

	/**
	 * @param user to add to the database.
	 * @return true on success, false on fail.
	 * This method adds a new user to the database from parameter.
	 */
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


	/**
	 * @param id to check if exists in the database.
	 * @return true if exists, false if not exists.
	 * This method checks if an id exists in the user table.
	 */
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


	/**
	 * This method disconnects the class from the database.
	 */
	protected void disconnect(){
		try{
			connection.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
	}


	/**
	 * @return database name.
	 */
	protected String getName(){
		try{
			return connection.getCatalog();
		}
		catch (SQLException e){
			return "null";
		}
	}

	/**
	 * @param credentials to check if exist in the user table.
	 * @return on success: user object with all user data but the password, null on fail.
	 * This method checks if username and password exist in the users table.
	 */
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

			if (userFound)
				setUserLogInStatus(credentials, "1");
			if(isLoggedIn(credentials) && userFound)
				return user;

			return null;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return null;
		}
	}

	/**
	 * @param credentials username to use to log the user out.
	 * @return false on fail, true on success.
	 * This method logs out the user.
	 */
	public boolean logUserOut(ArrayList<String> credentials){
		if (credentials == null)
			throw  new NullPointerException();
		if (!isLoggedIn(credentials))
			return false;
		setUserLogInStatus(credentials, "0");
		return true;
	}

	/**
	 * @param credentials to user to find the user
	 * @param status to set in user table.
	 * @return true on success, false on SQLException.
	 * This method sets user login status to the parameter value.
	 */
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

	/**
	 * @param credentials to check if user is logged in.
	 * @return true if logged in, else false.
	 * This method checks users isloggedin value in user table.
	 */
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
			// true if logged in.
			return expected.equals("1");

		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return true;
		}
	}

	/**
	 * @param id to find user.
	 * @return true on success, else false.
	 * This method deletes the given user using the id.
	 */
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


	/**
	 * @return Arraylist of all machine ids.
	 * This method gets all machine ids from machines table.
	 */
	public ArrayList<String> getMachineIds(){
		ArrayList<String> machines = new ArrayList<String>();
		PreparedStatement stmt;
		ResultSet res;
		boolean hasResult = false;
		String query = "SELECT machineid FROM " + this.dataBasename + ".machines"; // TODO: add only machine id field
		// DEPRECATED:
		//String query = "SELECT * FROM " + this.dataBasename + ".machines";

		try{
			stmt = connection.prepareStatement(query);
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




// BACKUP:
//	/**
//	 * @param machineId id of a specific machine in the database.
//	 * @return Arraylist of products in a specific machine.
//	 * This method finds all products that belong to a specific machine id.
//	 */
//	public ArrayList<Product> getAllProductsForMachine(String machineId){ // TODO: adapt this method to the new database configuration
//		if (machineId == null)
//			throw new NullPointerException();
//
//		PreparedStatement stmt;
//		ResultSet res;
//		String loginQuery = "SELECT * FROM " + this.dataBasename + ".productsinmachines WHERE machineid = ?";
//		ArrayList<Product> productList = new ArrayList<Product>();
//		try{
//			stmt = connection.prepareStatement(loginQuery);
//			stmt.setString(1, machineId);
//			res = stmt.executeQuery();
//
//			while(res.next()){
//				Product product = new Product();
//				product.setProductId(res.getString("productid"));
//				product.setName(res.getString("name"));
//				product.setPrice(res.getFloat("price"));
//				product.setDiscount(res.getFloat("discount"));
//				product.setAmount(res.getInt("amount"));
//				product.setDescription(res.getString("description"));
//				product.setType(res.getString("type"));
//				productList.add(product);
//			}
//			return productList;
//		}catch (SQLException sqlException){
//			sqlException.printStackTrace();
//			return null;
//		}
//	}