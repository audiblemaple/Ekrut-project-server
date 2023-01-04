package Application.server;

import common.Deals;
import common.RefillOrder;
import common.Reports.ClientReport;
import common.Reports.InventoryReport;
import common.Reports.OrderReport;
import common.connectivity.Customer;
import common.connectivity.User;
import common.orders.Order;
import common.orders.Product;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;


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

	public InventoryReport getMonthlyInventoryReport(ArrayList<String> monthYearMachine){
		if (monthYearMachine == null)
			throw new NullPointerException();

		PreparedStatement stmt;
		ResultSet res;
		ArrayList<Product> products = new ArrayList<Product>();
		String query = "SELECT * FROM " + this.dataBasename + ".inventoryreports WHERE month = ? AND year = ? AND machineid = ?";
		InventoryReport report = new InventoryReport();
		try{
			stmt = connection.prepareStatement(query);
			stmt.setString(1, monthYearMachine.get(0));
			stmt.setString(2, monthYearMachine.get(1));
			stmt.setString(3, monthYearMachine.get(2));
			res = stmt.executeQuery();
			if (res.next()){
				report.setReportID(res.getString("reportid"));
				report.setArea(res.getString("area"));
				report.setMachineID(res.getString("machineid"));
				products = productDetailsToListExpanded(res.getString("details"));
				if (products == null)
					return null;
				report.setProducts(products);
				report.setMonth(res.getString("month"));
				report.setYear(res.getString("year"));
				report.setTotalValue(res.getInt("overallcost"));
				return report;
			}
			return null;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return null;
		}
	}

	public ArrayList <Product> productDetailsToList(String details){
		String[] splitDetails = details.split(" , ");
		ArrayList<Product> products = new ArrayList<Product>();
		for (int i = 0; i < splitDetails.length; i+=2){
			Product product = new Product();
			product.setDescription(splitDetails[i]);
			product.setAmount(Integer.parseInt(splitDetails[i+1]));
			products.add(product);
		}
		return products;
	}

	public ArrayList <Product> productDetailsToListExpanded(String details){
		String[] splitDetails = details.split(" , ");
		ArrayList<Product> products = new ArrayList<Product>();
		for (int i = 0; i < splitDetails.length; i+=5){
			Product product = new Product();
			product.setProductId(splitDetails[i]);
			product.setDescription(splitDetails[i+1]);
			product.setAmount(Integer.parseInt(splitDetails[i+2]));
			product.setPrice(Float.parseFloat(splitDetails[i+3]));
			product.setType(splitDetails[i+4]);
			products.add(product);
		}
		return products;
	}

	public boolean generateMonthlyInventoryReport(ArrayList<String> areaMachineMonthYear){ // TODO: uncomment this!!!
		String reportID = "REP" + (getNumOfEntriesInTable("inventoryreports") + 1);
		ArrayList<String> mID = new ArrayList<>();
		mID.add(areaMachineMonthYear.get(1));
		mID.add("0");
		ArrayList<Product> products = getMachineProducts(mID);
		String reportDetails = "";
		float overallPrice = 0;

		for (Product prod : products){
			reportDetails += prod.getProductId();
			reportDetails += " , ";
			reportDetails += prod.getDescription();
			reportDetails += " , ";
			reportDetails += prod.getAmount();
			reportDetails += " , ";
			reportDetails += (prod.getPrice() - (prod.getPrice() * prod.getDiscount()));
			reportDetails += " , ";
			reportDetails += prod.getType();
			reportDetails += " , ";
			overallPrice += (prod.getPrice() - (prod.getPrice() * prod.getDiscount())) * prod.getAmount();
		}

		String query = "INSERT INTO " +  this.dataBasename + ".inventoryreports(reportid, area, machineid, details, month, year, overallcost) VALUES(?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt;
		try{
			stmt = connection.prepareStatement(query);
			stmt.setString(1,reportID);
			stmt.setString(2,areaMachineMonthYear.get(0));
			stmt.setString(3,areaMachineMonthYear.get(1));
			stmt.setString(4,reportDetails);
			stmt.setString(5,areaMachineMonthYear.get(2));
			stmt.setString(6,areaMachineMonthYear.get(3));
			stmt.setFloat(7,overallPrice);
			stmt.executeUpdate();

			// check report added successfully.
			ArrayList<String> monthYearMachine = new ArrayList<String>();
			monthYearMachine.add(areaMachineMonthYear.get(2));
			monthYearMachine.add(areaMachineMonthYear.get(3));
			monthYearMachine.add(areaMachineMonthYear.get(1));
			return getMonthlyInventoryReport(monthYearMachine) != null;
		}
		catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}

	private int getNumOfEntriesInTable(String tableName){
		String query = "SELECT COUNT(*) FROM " + this.dataBasename + "." + tableName;
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
	 * @param mIdLocation id or location of machine/s in the database.
	 * @return Arraylist of products in a specific machine.
	 * This method finds all products that belong to a specific machine id.
	 */
	public ArrayList<Product> getMachineProducts(ArrayList<String> mIdLocation){ // TODO: how i can improve this query to make the code more simple
		PreparedStatement stmt;
		ResultSet res;
		String query;
		ArrayList<Product> productList = new ArrayList<Product>();
		// choose if we need all products or a specific machine
		if (mIdLocation == null)
			query = "SELECT * FROM " + this.dataBasename + ".products p JOIN " + this.dataBasename + ".productsinmachines pm ON p.productid = pm.productid";
		else if (mIdLocation.get(0) != null && mIdLocation.get(1).equals("0"))
			query = "SELECT *FROM " + this.dataBasename + ".products p JOIN " + this.dataBasename + ".productsinmachines pm ON p.productid = pm.productid WHERE pm.machineid = ?";
		else
			query = "SELECT * FROM " + this.dataBasename + ".products p JOIN " + this.dataBasename + ".productsinmachines pm ON p.productid = pm.productid JOIN " + this.dataBasename + ".machines m ON pm.machineid = m.machineid WHERE m.machinelocation = ?";

		try{
			stmt = connection.prepareStatement(query);
			if (mIdLocation != null)
				stmt.setString(1, mIdLocation.get(0));
			res = stmt.executeQuery();
			File file = null;
			while (res.next()){
				Product product = new Product();
				product.setPrice(res.getFloat("price"));
				product.setDiscount(res.getFloat("discount"));
				product.setName(res.getString("name"));
				product.setAmount(res.getInt("amount"));
				product.setDescription(res.getString("description"));
				product.setType(res.getString("type"));
				product.setMachineID(res.getString("machineid"));
				product.setProductId(res.getString("productid"));
				product.setCriticalAmount(res.getInt("criticalamount"));
				// create file and streams
				Path path = Paths.get("src/Application/images/" + res.getString("name") + ".png");
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
					e.printStackTrace();
					return null;
				}
				productList.add(product);
			}
			if (!productList.isEmpty())
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
		ArrayList<Product> productList = new ArrayList<Product>();
		boolean resultFound = false;
		// choose if we need all products or a specific machine

		String query = "SELECT * FROM " + this.dataBasename + ".products p INNER JOIN " + this.dataBasename + ".warehouse w ON p.productid = w.productid";

		try {
			stmt = connection.prepareStatement(query);
			res = stmt.executeQuery();
			while (res.next()){
				resultFound = true;
				Product product = new Product();
				product.setPrice(res.getFloat("price"));
				product.setDiscount(res.getFloat("discount"));
				product.setName(res.getString("name"));
				product.setAmount(res.getInt("amount"));
				product.setDescription(res.getString("description"));
				product.setType(res.getString("type"));
				product.setProductId(res.getString("productid"));
				product.setCriticalAmount(res.getInt("criticalamount"));

				// create file and streams
				Path path = Paths.get("src/Application/images/" + res.getString("name") + ".png");
				File file = new File(path.toUri());
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
				productList.add(product);
			}
			if (resultFound)
				return productList;
			return null;

		} catch (SQLException e) {
			e.printStackTrace();
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
	public ArrayList<String> getMachineIds(String location){
		ArrayList<String> machines = new ArrayList<String>();
		PreparedStatement stmt;
		ResultSet res;
		boolean hasResult = false;
		String query = "";
		if (location == null)
			query = "SELECT machineid FROM " + this.dataBasename + ".machines";
		else
			query = "SELECT machineid FROM " + this.dataBasename + ".machines WHERE machinelocation=?";

		try{
			stmt = connection.prepareStatement(query);
			if (!(location == null))
				stmt.setString(1, location);
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

	public ArrayList<String> getAllMachineLocations() {
		ArrayList<String> locations = new ArrayList<String>();
		PreparedStatement stmt;
		ResultSet res;
		boolean hasResult = false;
		String query = "SELECT DISTINCT machinelocation FROM " + this.dataBasename + ".machines";

		try{
			stmt = connection.prepareStatement(query);
			res = stmt.executeQuery();

			while(res.next()){
				hasResult = true;
				locations.add(res.getString("machinelocation"));
			}
			if (hasResult)
				return locations;

			return null;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return null;
		}
	}

	public boolean AddNewOrder(Order order) {
		ArrayList<String> customerAndOrderID = new ArrayList<String>();
		String query = "INSERT INTO " +  this.dataBasename + ".orders" +
				"(orderid, price, products, machineid, orderdate, address, customerid, supplymethod, paidwith, orderstatus)" +
				"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt;
		int updateSuccessful = 0;
		try{
			stmt = connection.prepareStatement(query);
			stmt.setString(1,  order.getOrderID());
			stmt.setFloat (2,  order.getOverallPrice());
			stmt.setString(3,  productListToString(order.getProducts()));
			stmt.setString(4,  order.getMachineID());
			stmt.setString(5,  order.getOrderDate());
			stmt.setString(6,  order.getAddress());
			stmt.setString(7,  order.getCustomerID());
			stmt.setString(8,  order.getSupplyMethod());
			stmt.setString(9,  order.getPaidWith());
			stmt.setString(10, order.getOrderStatus());

			updateSuccessful =  stmt.executeUpdate();

			return updateSuccessful > 0;
		}
		catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateAmountsFromOrder(Order order) {
		PreparedStatement stmt;
		String query;
		int updateStatus = 0;

		if (order.getMachineID() == null)
			query = "UPDATE " + this.dataBasename + ".warehouse SET amount = amount - ? WHERE productid = ?;";
		else
			query = "UPDATE " + this.dataBasename + ".productsinmachines SET amount = amount - ? WHERE productid = ?;";

		try{
			for (Product product : order.getProducts()){
				stmt = connection.prepareStatement(query);
				stmt.setInt(1, product.getAmount());
				stmt.setString(2, product.getProductId());
				updateStatus =  stmt.executeUpdate();
				if (updateStatus == 0)
					return false;
			}
			if (order.getMachineID() != null)
				checkAmount();
			return true;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return false;
		}
	}

	public boolean updateFirstBuyAsSub(String customerID) {
		PreparedStatement stmt;
		String query;
		query = "UPDATE " + this.dataBasename + ".customer SET isfirsttimebuyassub = true WHERE customerid = ?;";
		int updateSuccessfull = 0;
		try{
			stmt = connection.prepareStatement(query);
			stmt.setString(1, customerID);
			updateSuccessfull =  stmt.executeUpdate();

			return updateSuccessfull > 0;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return false;
		}
	}

	public Order getOrderByOrderIdAndCustomerID(ArrayList<String> customerAndOrderID){
		PreparedStatement stmt;
		ResultSet res;
		boolean hasResult = false;
		String query = "SELECT * FROM " + this.dataBasename + ".orders WHERE orderid = ? AND customerid = ?";
		Order order = new Order();
		try{
			stmt = connection.prepareStatement(query);
			stmt.setString( 1,  customerAndOrderID.get(1));
			stmt.setString (2,  customerAndOrderID.get(0));
			res = stmt.executeQuery();

			while(res.next()){
				hasResult = true;
				order.setOrderID(res.getString("orderid"));
				order.setOverallPrice(res.getFloat("price"));

				// convert product details from tuple to array list of product objects
				ArrayList<Product> products = productDetailsToList(res.getString("products"));
				order.setProducts(products);

				order.setMachineID(res.getString("machineid"));
				order.setOrderDate(res.getString("orderdate"));
				order.setEstimatedDeliveryTime(res.getString("estimateddeliverydate"));
				order.setConfirmationDate(res.getString("confirmationdate"));
				order.setOrderStatus(res.getString("orderstatus"));
				order.setCustomerID(res.getString("customerid"));
				order.setSupplyMethod(res.getString("supplymethod"));
				order.setPaidWith(res.getString("paidwith"));
				order.setAddress(res.getString("address"));
			}
			if (hasResult)
				return order;

			return null;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return null;
		}
	}

	public ArrayList<OrderReport> getOrderData(ArrayList<String> monthAndYear){
		ArrayList<OrderReport> machinereports = new ArrayList<>();
		PreparedStatement stmt;
		ResultSet res;
		String query = "SELECT m.machineid, m.machinelocation, COUNT(o.machineid) as 'number_of_orders', MONTH(o.orderdate) as 'month', YEAR(o.orderdate) as 'year' " +
				"FROM " + this.dataBasename + ".machines m " +
				"JOIN " + this.dataBasename + ".orders o " +
				"ON m.machineid = o.machineid AND MONTH(o.orderdate) = ? AND YEAR(o.orderdate) = ?" +
				"GROUP BY m.machineid, m.machinelocation, MONTH(o.orderdate), YEAR(o.orderdate) " +
				"ORDER BY YEAR(o.orderdate), MONTH(o.orderdate);";
		try {
			stmt = connection.prepareStatement(query);
			stmt.setString(1, monthAndYear.get(0));
			stmt.setString(2, monthAndYear.get(1));
			res = stmt.executeQuery();
			while (res.next()){
				OrderReport ordereport = new OrderReport();
				ordereport.setMachineid(res.getString("machineid"));
				ordereport.setMachineLocation(res.getString("machinelocation"));
				ordereport.setNumberOfOrders(res.getInt("number_of_orders"));
				ordereport.setMonth(res.getString("month"));
				ordereport.setYear(res.getString("year"));
				machinereports.add(ordereport);
			}
			if (!machinereports.isEmpty())
				return machinereports;
			return null;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	// TODO: use this to later generate monthly reports.
	public void generateOrderReport(ArrayList<String> monthAndYear){
		String query = "INSERT INTO " +  this.dataBasename + ".orderreports(reportid, location, month, year, machineid, orderamount) VALUES(?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt;
		ArrayList<OrderReport> orderReports = getOrderData(monthAndYear);
		try {
			stmt = connection.prepareStatement(query);
			for (OrderReport ord : orderReports){
				String reportID = "REP" + (getNumOfEntriesInTable("orderreports") + 1);
				stmt.setString(1, reportID);
				stmt.setString(2, ord.getMachineLocation());
				stmt.setString(3, ord.getMonth());
				stmt.setString(4, ord.getYear());
				stmt.setString(5, ord.getMachineid());
				stmt.setInt(6, ord.getNumberOfOrders());
				stmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public ArrayList<OrderReport> getOrderReport(ArrayList<String> monthAndYear){
		ArrayList<OrderReport> orderReports = new ArrayList<>();
		PreparedStatement stmt;
		ResultSet res;
		String query = "SELECT * FROM " + this.dataBasename + ".orderreports WHERE month = ? AND year = ?";

		try {
			stmt = connection.prepareStatement(query);
			stmt.setString(1, monthAndYear.get(0));
			stmt.setString(2, monthAndYear.get(1));

			res = stmt.executeQuery();
			while (res.next()){
				OrderReport orderReport = new OrderReport();
				orderReport.setMachineid(res.getString("machineid"));
				orderReport.setMachineLocation(res.getString("location"));
				orderReport.setNumberOfOrders(res.getInt("orderamount"));
				orderReport.setMonth(res.getString("month"));
				orderReport.setYear(res.getString("year"));
				orderReports.add(orderReport);
			}
			if (!orderReports.isEmpty()){
				return orderReports;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private String productListToString(ArrayList<Product> products){
		String details = "";
		for (Product prod : products){
			details += prod.getDescription() + " , " + prod.getAmount() + " , ";
		}
		return details;
	}


	public boolean updateWarehouseProduct(Product product){
		PreparedStatement stmt;
		ResultSet res;
		String query = "UPDATE ekrutdatabase.warehouse SET discount = ?, amount = ?, criticalamount = ? WHERE productid = ?;";
		try {
			stmt = connection.prepareStatement(query);
			stmt.setFloat(1, product.getDiscount());
			stmt.setInt(2, product.getAmount());
			stmt.setInt(3, product.getCriticalAmount());
			stmt.setString(4, product.getProductId());

			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateMachineProduct(Product product) {
		PreparedStatement stmt;
		ResultSet res;
		String query = "UPDATE ekrutdatabase.productsinmachines SET discount = ?, amount = ?, criticalamount = ? WHERE productid = ? AND machineid = ?;";
		try {
			stmt = connection.prepareStatement(query);
			stmt.setFloat(1, product.getDiscount());
			stmt.setInt(2, product.getAmount());
			stmt.setInt(3, product.getCriticalAmount());
			stmt.setString(4, product.getProductId());
			stmt.setString(5, product.getMachineID());

			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean addNewProductToProductTable(Product product) {
		String query = "INSERT INTO " +  this.dataBasename + ".products(productid, name, price, description, type) VALUES(?, ?, ?, ?, ?)";
		PreparedStatement stmt;
		try{
			stmt = connection.prepareStatement(query);
			stmt.setString(1,product.getProductId());
			stmt.setString(2,product.getName());
			stmt.setFloat(3,product.getPrice());
			stmt.setString(4,product.getDescription());
			stmt.setString(5,product.getType());

			stmt.executeUpdate();
			return checkProductExistsInGivenTable(product, "");
		}
		catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}

	public boolean addNewProductToWarehouse(Product product) {
		String query = "INSERT INTO " +  this.dataBasename + ".warehouse(productid, discount, amount, criticalamount) VALUES(?, ?, ?, ?)";
		PreparedStatement stmt;
		try{
			stmt = connection.prepareStatement(query);
			stmt.setString(1,product.getProductId());
			stmt.setFloat(2,product.getDiscount());
			stmt.setInt(3,product.getAmount());
			stmt.setInt(4,product.getCriticalAmount());

			stmt.executeUpdate();
			return checkProductExistsInGivenTable(product, "warehouse");
		}
		catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}

	public boolean checkProductExistsInGivenTable(Product product, String whereToCheck){
		PreparedStatement stmt;
		ResultSet res;
		String query;
		switch (whereToCheck){
			case "warehouse":
				query = "SELECT productid FROM " + this.dataBasename + ".warehouse WHERE productid = ?";
				break;
			case "machine":
				query = "SELECT productid FROM " + this.dataBasename + ".productsinmachines WHERE productid = ? AND machineid = ?";
				break;
			default:
				query = "SELECT productid FROM " + this.dataBasename + ".products WHERE productid = ?";
		}

		try{
			stmt = connection.prepareStatement(query);
			stmt.setString(1, product.getProductId());
			if (whereToCheck.equals("machine"))
				stmt.setString(2, product.getMachineID());

			res = stmt.executeQuery();
			if (res.next()){
				return res.getString("productid").equals(product.getProductId());
			}
			return false;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return false;
		}
	}

	public boolean addNewProductToMachine(Product product) {
		String query = "INSERT INTO " +  this.dataBasename + ".productsinmachines(productid, machineid, discount, amount, criticalamount) VALUES(?, ?, ?, ?, ?)";
		PreparedStatement stmt;
		try{
			stmt = connection.prepareStatement(query);
			stmt.setString(1,product.getProductId());
			stmt.setString(2,product.getMachineID());
			stmt.setFloat(3,product.getDiscount());
			stmt.setInt(4,product.getAmount());
			stmt.setInt(5, product.getCriticalAmount());

			stmt.executeUpdate();
			return checkProductExistsInGivenTable(product, "machine");
		}
		catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}


	public void generateClientReport(String month, String year){
		PreparedStatement stmt;
		ResultSet res;
		String query;
		query = "SELECT *, COUNT(o.customerid) as num_orders FROM " + this.dataBasename + ".users u JOIN " + this.dataBasename + ".orders o ON o.customerid = u.id WHERE MONTH(o.orderdate) = ? AND YEAR(o.orderdate) = ? GROUP BY u.id";
		try{
			stmt = connection.prepareStatement(query);
			stmt.setString(1, month);
			stmt.setString(2, year);
			PreparedStatement updatestmt;
			String updateQuery = "INSERT INTO " +  this.dataBasename + ".customerreport(customerid, numoforders, month, year, customername, customerlastname) VALUES(?, ?, ?, ?, ?, ?)";

			updatestmt = connection.prepareStatement(updateQuery);
			res = stmt.executeQuery();
			while (res.next()){
				updatestmt.setString(1, res.getString("id"));
				updatestmt.setInt(2, res.getInt("num_orders"));
				updatestmt.setString(3, month);
				updatestmt.setString(4, year);
				updatestmt.setString(5,res.getString("firstname"));
				updatestmt.setString(6, res.getString("lastname"));
				updatestmt.executeUpdate();
			}

		}catch (SQLException sqlException){
			sqlException.printStackTrace();
		}
	}


	public ClientReport getClientReport(ArrayList<String> monthAndYear){
		PreparedStatement stmt;
		ResultSet res;
		String query;
		ClientReport report = new ClientReport();
		HashMap<User, Integer> userOrderData = new HashMap<>();
		User user;
		query = "SELECT * FROM " + this.dataBasename + ".customerreport WHERE month = ? AND year = ?";

		try{
			stmt = connection.prepareStatement(query);
			stmt.setString(1, monthAndYear.get(0));
			stmt.setString(2, monthAndYear.get(1));

			res = stmt.executeQuery();
			while (res.next()){
				user = new User();
				user.setId(res.getString("customerid"));
				user.setFirstname(res.getString("customername"));
				user.setLastname(res.getString("customerLastName"));
				userOrderData.put(user, res.getInt("numoforders"));
			}
			report.setUserOrderAmount(userOrderData);
			if (userOrderData.isEmpty())
				return null;
			return report;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return null;
		}
	}


	public void checkAmount(){
		PreparedStatement stmt;
		ResultSet res;
		String query;
		query = "SELECT * FROM " + this.dataBasename + ".productsinmachines";
		int amount = 0;
		int alertAmount = 0;

		try{
			stmt = connection.prepareStatement(query);
			res = stmt.executeQuery();

			while (res.next()){
				amount = res.getInt("amount");
				alertAmount = res.getInt("criticalamount");
				if (alertAmount >= amount)
					addOrderToDatabase(res.getString("productid"), res.getString("machineid"), res.getInt("criticalamount"));
			}
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
		}
	}

	private void addOrderToDatabase(String productID, String machineID, int amount){
		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy.MM.dd");
		String dateString = currentDate.format(formatter);


		String  uuid = UUID.randomUUID().toString().substring(0, 8);

		String query = "INSERT INTO " +  this.dataBasename + ".refilrequests(requestid, machineid, productid, date, amountatrequest) VALUES(?, ?, ?, ?, ?)";
		PreparedStatement stmt;
		try{
			stmt = connection.prepareStatement(query);
			stmt.setString(1, uuid);
			stmt.setString(2, machineID);
			stmt.setString(3, productID);
			stmt.setString(4, dateString);
			stmt.setInt(5, amount);

			stmt.executeUpdate();
		}
		catch ( SQLIntegrityConstraintViolationException ignored){ // this handles a case where the order is already present

		}catch (SQLException e){
			e.printStackTrace();
		}
	}

	public ArrayList<RefillOrder> getRefillOrders(){
		PreparedStatement stmt;
		ResultSet res;
		String query;
		ArrayList<RefillOrder> refillOrderList = new ArrayList<>();
		query = "SELECT * FROM " + this.dataBasename + ".refilrequests";
		int amount = 0;
		int alertAmount = 0;

		try{
			stmt = connection.prepareStatement(query);
			res = stmt.executeQuery();
			SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy.MM.dd");
			SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy");
			while (res.next()){
				RefillOrder order = new RefillOrder();
				order.setOrderID(res.getString("requestid"));
				order.setMachineID(res.getString("machineid"));
				order.setProductID(res.getString("productid"));

				Date date = inputFormat.parse(res.getString("date"));
				String formattedDate = outputFormat.format(date);
				order.setCreationDate(formattedDate);
				order.setAmountAtRequest(res.getInt("amountatrequest"));
				order.setAssignedEmployee(res.getString("assignedemployeeid"));
				refillOrderList.add(order);
			}

			if (refillOrderList.isEmpty())
				return null;
			return refillOrderList;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return null;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}


	public boolean updateMachineAmount(ArrayList<String> productData) {
		PreparedStatement stmt;
		String query;
		query = "UPDATE " + this.dataBasename + ".productsinmachines SET amount = ? WHERE machineid = ? AND productid = ?;";
		int updateSuccessfull = 0;
		try{
			stmt = connection.prepareStatement(query);
			stmt.setInt(1, Integer.parseInt(productData.get(2)));
			stmt.setString(2, productData.get(0));
			stmt.setString(3, productData.get(1));
			updateSuccessfull =  stmt.executeUpdate();

			return updateSuccessfull > 0;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return false;
		}
	}


	public boolean completeOrderRefil(ArrayList<String> productData) {
		PreparedStatement stmt;
		String query;
		query = "DELETE FROM " + this.dataBasename + ".refilrequests WHERE machineid = ? AND productid = ?;";
		int updateSuccessfull = 0;
		try{
			stmt = connection.prepareStatement(query);
			stmt.setString(1, productData.get(0));
			stmt.setString(2, productData.get(1));
			updateSuccessfull =  stmt.executeUpdate();

			return updateSuccessfull > 0;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return false;
		}
	}

	public boolean assignEmployeeToRefillOrder(RefillOrder order) {
		PreparedStatement stmt;
		String query;
		query = "UPDATE " + this.dataBasename + ".refilrequests SET assignedemployeeid = ? WHERE requestid = ?;";
		int updateSuccessfull = 0;
		try{
			stmt = connection.prepareStatement(query);
			stmt.setString(1, order.getAssignedEmployee());
			stmt.setString(2, order.getOrderID());
			updateSuccessfull =  stmt.executeUpdate();

			return updateSuccessfull > 0;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return false;
		}
	}

	public Customer getCustomerData(String customerID) {
		PreparedStatement stmt;
		ResultSet res;
		String query;
		Customer customer = new Customer();
		query = "SELECT * FROM " + this.dataBasename + ".customer WHERE customerid = ?";
		boolean resultFound = false;

		try{
			stmt = connection.prepareStatement(query);
			stmt.setString(1, customerID);
			res = stmt.executeQuery();

			while (res.next()){
				resultFound = true;
				customer.setCreditCardNumber(res.getString("creditcardnumber"));
				customer.setSub(res.getBoolean("issub"));
				customer.setFirstBuyAsSub(res.getBoolean("isfirsttimebuyassub"));
			}
			if (resultFound)
				return customer;
			return null;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return null;
		}
	}

	public boolean verifyCreditCard(ArrayList<String> creditCardAndID) {
		PreparedStatement stmt;
		ResultSet res;
		String query;

		query = "SELECT * FROM " + this.dataBasename + ".customer WHERE customerid = ? AND creditcardnumber = ?";
		boolean resultFound = false;

		try{
			stmt = connection.prepareStatement(query);
			stmt.setString(1, creditCardAndID.get(0));
			stmt.setString(2, creditCardAndID.get(1));
			res = stmt.executeQuery();

			if (res.next())
				resultFound = true;
			return resultFound;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return false;
		}
	}


	public boolean checkIfCustomerIsSub(String customerID) {
		PreparedStatement stmt;
		ResultSet res;
		String query;

		query = "SELECT issub FROM " + this.dataBasename + ".customer WHERE customerid = ?";
		boolean resultFound = false;

		try{
			stmt = connection.prepareStatement(query);
			stmt.setString(1, customerID);
			res = stmt.executeQuery();

			if (res.next())
				return res.getBoolean("issub");
			return false;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return false;
		}
	}

	public ArrayList<User> getAllCustomerUsers() {
		PreparedStatement stmt;
		ResultSet res;
		String query;
		ArrayList<User> users = new ArrayList<>();

		query = "SELECT * FROM " + this.dataBasename + ".users WHERE department = 'customer' OR department = 'subscriber'";
		boolean resultFound = false;

		try{
			stmt = connection.prepareStatement(query);
			res = stmt.executeQuery();

			while (res.next()){
				User user = new User();
				user.setFirstname(res.getString("firstname"));
				user.setLastname(res.getString("lastname"));
				user.setId(res.getString("id"));
				user.setStatus(res.getString("userstatus"));
				users.add(user);
			}

			if (users.isEmpty())
				return null;
			return users;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return null;
		}
	}

	public boolean updateUsersStatuses(ArrayList<User> data) {
		PreparedStatement stmt;
		String query;
		query = "UPDATE " + this.dataBasename + ".users SET userstatus = ? WHERE id = ?;";
		int updateSuccessfull = 0;
		try{
			for (User user : data){
				stmt = connection.prepareStatement(query);
				stmt.setString(1, user.getStatus());
				stmt.setString(2, user.getId());
				updateSuccessfull =  stmt.executeUpdate();

				if (updateSuccessfull == 0)
					return false;
			}
			return true;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return false;
		}
	}


	public ArrayList<Deals> getAllDiscounts() { // return discount
		PreparedStatement stmt;
		ResultSet res;
		String query;
		ArrayList<Deals> dealList = new ArrayList<>();

		query = "SELECT * FROM " + this.dataBasename + ".deals";

		try{
			stmt = connection.prepareStatement(query);
			res = stmt.executeQuery();

			while (res.next()){
				Deals deal = new Deals();
				deal.setDealID(res.getString("id"));
				deal.setDealName(res.getString("name"));
				deal.setDiscount(res.getFloat("discount"));
				deal.setDescription(res.getString("description"));
				deal.setType(res.getString("type"));
				deal.setArea(res.getString("area"));
				deal.setStatusString(res.getString("status"));
				deal.setActive(res.getString("isactive"));
				dealList.add(deal);
			}

			if (dealList.isEmpty())
				return null;
			return dealList;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return null;
		}

	}

	public boolean updateDeal(Deals dealToUpdate) {
		PreparedStatement stmt;
		String query;
		query = "UPDATE " + this.dataBasename + ".deals SET name = ?, discount = ?, description = ?, type = ?, area = ?, status = ?, isactive = ? WHERE id = ?;";
		int updateSuccessfull = 0;

		try{
			stmt = connection.prepareStatement(query);
			stmt.setString(1, dealToUpdate.getDealName());
			stmt.setFloat(2, dealToUpdate.getDiscount());
			stmt.setString(3, dealToUpdate.getDescription());
			stmt.setString(4, dealToUpdate.getType());
			stmt.setString(5, dealToUpdate.getAreaS());
			stmt.setString(6, dealToUpdate.getStatusString());
			stmt.setString(7, dealToUpdate.getActive());
			stmt.setString(8, dealToUpdate.getDealID());
			updateSuccessfull =  stmt.executeUpdate();

		return updateSuccessfull != 0;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return false;
		}
	}


	public boolean updateOrderStatus(ArrayList<String> orderIdAndStatus) {
		PreparedStatement stmt;
		String query;
		query = "UPDATE " + this.dataBasename + ".orders SET orderstatus = ? WHERE orderid = ?;";
		int updateSuccessfull = 0;

		try{
			stmt = connection.prepareStatement(query);
			stmt.setString(1,orderIdAndStatus.get(1));
			stmt.setString(2, orderIdAndStatus.get(0));

			updateSuccessfull =  stmt.executeUpdate();

			return updateSuccessfull != 0;
		}catch (SQLException sqlException){
			sqlException.printStackTrace();
			return false;
		}
	}


	public boolean applyDeals() {
		PreparedStatement stmt;
		String query;
		ArrayList<Deals> dealList = getAllDiscounts();
		ArrayList<String> areas = getAllMachineLocations();
		float discount = 0;


		query = "UPDATE " + this.dataBasename + ".productsinmachines SET discount = ? " +
				"WHERE machineid IN (SELECT machineid FROM " + this.dataBasename + ".machines " +
				"WHERE machines.machinelocation = ? OR machines.machinelocation = ? OR machines.machinelocation = ?) AND productid IN (SELECT productid FROM " + this.dataBasename + ".products " +
				"WHERE products.type = ? OR products.type = ?)";

		for (Deals deal : dealList){
			if ( deal.getDealID().equals("005"))
				continue;
			if (!deal.getActive().equals("active"))
				discount = 0;
			else
				discount = deal.getDiscount();

			try{
				stmt = connection.prepareStatement(query);
				stmt.setFloat(1, discount);

				if (deal.getAreaS().equals("all")){
					stmt.setString(2, "uae");
					stmt.setString(3, "north");
					stmt.setString(4, "south");
				}
				else{
					stmt.setString(2, deal.getAreaS());
					stmt.setString(3, deal.getAreaS());
					stmt.setString(4, deal.getAreaS());
				}
				if (deal.getType().equals("ALL")){
					stmt.setString(5, "SNACK");
					stmt.setString(6, "DRINK");
				}
				else{
					stmt.setString(5, deal.getType());
					stmt.setString(6, deal.getType());
				}
				stmt.executeUpdate();
			}catch (SQLException sqlException){
				sqlException.printStackTrace();
				return false;
			}
		}

		return true;
	}
}


// get ALL PRODUCTS that need a refil order
//	public ArrayList<Product> getRefillOrderProducts(){
//		PreparedStatement stmt;
//		ResultSet res;
//		String query;
//		ArrayList<Product> productList = new ArrayList<>();
//		query = "SELECT * FROM " + this.dataBasename + ".products JOIN "
//				+ this.dataBasename + ".productsinmachines ON products.productid = productsinmachines.productid JOIN "
//				+ this.dataBasename + ".refilrequests ON productsinmachines.machineid = refilrequests.machineid AND products.productid = refilrequests.productid " +
//				"WHERE productsinmachines.machineid = refilrequests.machineid AND products.productid = refilrequests.productid;";
//		int amount = 0;
//		int alertAmount = 0;
//
//		try{
//			stmt = connection.prepareStatement(query);
//			res = stmt.executeQuery();
//
//			while (res.next()){
//				Product product = new Product();
//				product.setProductId(res.getString("productid"));
//				product.setName(res.getString("name"));
//				product.setPrice(res.getFloat("price"));
//				product.setDescription(res.getString("description"));
//				product.setType(res.getString("type"));
//				product.setMachineID(res.getString("machineid"));
//				product.setAmount(res.getInt("amount"));
//
//				productList.add(product);
//			}
//
//			if (productList.isEmpty())
//				return null;
//			return productList;
//		}catch (SQLException sqlException){
//			sqlException.printStackTrace();
//			return null;
//		}
//	}

// SCRAP DEAL UPDATE METHOD
//	public boolean applyDeals() {
//		ArrayList<Deals> dealList = getAllDiscounts();
//		float maxUae = 0;
//		float maxNorth = 0;
//		float maxSouth = 0;
//		float maxAll = 0;
//
//		for (Deals deal : dealList){
//			switch (deal.getAreaS()){
//				case "uae":
//					if (maxUae < deal.getDiscount() && deal.getActive().equals("active") && !deal.getDealID().equals("005"))
//						maxUae = deal.getDiscount();
//					break;
//
//				case "north":
//					if (maxNorth < deal.getDiscount() && deal.getActive().equals("active") && !deal.getDealID().equals("005"))
//						maxNorth = deal.getDiscount();
//					break;
//
//				case "south":
//					if (maxSouth < deal.getDiscount() && deal.getActive().equals("active") && !deal.getDealID().equals("005"))
//						maxSouth = deal.getDiscount();
//					break;
//
//				case "all":
//					if (maxAll < deal.getDiscount() && deal.getActive().equals("active") && !deal.getDealID().equals("005"))
//						maxAll = deal.getDiscount();
//					break;
//
//			}
//		}
//
//		ArrayList<String> uaeMachineIDs = getMachineIds("uae");
//		ArrayList<String> northMachineIDs = getMachineIds("north");
//		ArrayList<String> southMachineIDs = getMachineIds("south");
//
//
//		// THIS IS THE QUERY I NEED:
//		// todo: change this method to use this query...
//		// UPDATE productsinmachines
//		//SET discount = (SELECT discount FROM deals WHERE deals.id = productsinmachines.dealid)
//		//WHERE machineid IN (SELECT machineid FROM machines WHERE machines.machinelocation = 'south') AND productid IN (SELECT productid FROM products WHERE products.type = 'SNACK')
//
//		PreparedStatement stmt;
//		String query;
//		query = "UPDATE " + this.dataBasename + ".productsinmachines SET discount = ? WHERE machineid = ?;";
//
//		for (String str :uaeMachineIDs){
//			try{
//				stmt = connection.prepareStatement(query);
//				stmt.setFloat(1, maxUae);
//				stmt.setString(2,str);
//				stmt.executeUpdate();
//			}catch (SQLException sqlException){
//				sqlException.printStackTrace();
//				return false;
//			}
//		}
//
//		for (String str :northMachineIDs){
//			try{
//				stmt = connection.prepareStatement(query);
//				stmt.setFloat(1, maxNorth);
//				stmt.setString(2,str);
//				stmt.executeUpdate();
//			}catch (SQLException sqlException){
//				sqlException.printStackTrace();
//				return false;
//			}
//		}
//
//		for (String str :southMachineIDs){
//			try{
//				stmt = connection.prepareStatement(query);
//				stmt.setFloat(1, maxSouth);
//				stmt.setString(2,str);
//				stmt.executeUpdate();
//			}catch (SQLException sqlException){
//				sqlException.printStackTrace();
//				return false;
//			}
//		}
//		if (maxAll == 0)
//			return true;
//		query = "UPDATE " + this.dataBasename + ".productsinmachines SET discount = ?";
//		try{
//			stmt = connection.prepareStatement(query);
//			stmt.setFloat(1, maxAll);
//			stmt.executeUpdate();
//		}catch (SQLException sqlException){
//			sqlException.printStackTrace();
//			return false;
//		}
//		return true;
//	}