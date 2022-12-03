package Application.server;
import Data.UserData.Subscriber;

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
	public boolean addUser(String firstname,  String lastname, String id, String phonenumber, String emailaddress, String creditcardnumber){
		// adding 1 to subscriber number to save the incremented number of subscribers
		Integer subscribernumber = getSubscriberNum() + 1;
		PreparedStatement stmt;
		String query = "INSERT INTO " +  this.dataBasename + ".subscriber(firstname, lastname, id, phonenumber, emailaddress, creditcardnumber, subscribernumber) VALUES(?, ?, ?, ?, ?, ?,?)";

		if (!checkUserExists(id)){
			try{
				stmt = connection.prepareStatement(query);
				stmt.setString(1,firstname);
				stmt.setString(2,lastname);
				stmt.setString(3,id);
				stmt.setString(4,phonenumber);
				stmt.setString(5,emailaddress);
				stmt.setString(6,creditcardnumber);
				stmt.setString(7,subscribernumber.toString());

				stmt.executeUpdate();
				if(checkUserExists(id)){
					System.out.printf("user added successfully");
					return true;
				}
			}
			catch (SQLException e){
				e.printStackTrace();
				return false;
			}
		}
		System.out.printf("user already exists");
		return false;
	}
	public boolean checkUserExists(String id){
		PreparedStatement stmt;
		ResultSet res;
		String query = "SELECT * FROM " + this.dataBasename +".subscriber WHERE (id) = (?)";

		try{
			stmt = connection.prepareStatement(query);
			stmt.setString(1,id);
			res = stmt.executeQuery();
			if (res.next()){
				if (res.getString("id").equals(id)){

					return true;
				}
			}
			stmt.close();
			res.close();
		}
		catch (SQLException e){
			e.printStackTrace();
		}
		return false;
	}

	private int getSubscriberNum(){
		String query = "SELECT COUNT(*) FROM " + this.dataBasename + ".subscriber";
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

	public boolean deleteUser(String id, String username, String password){
		PreparedStatement stmt;
		String query = "DELETE FROM " + this.dataBasename +  ".subscriber WHERE id=? username=? password=?";
		if(!checkUserExists(id))
			return false;
		try {
			stmt = connection.prepareStatement(query);
			stmt.setString(1, id);
			stmt.setString(2, username);
			stmt.setString(3, password);
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void updateUser(String id, String creditcardnumber, String subscribernumber){
		PreparedStatement stmt;
		String query = "UPDATE " + this.dataBasename + ".subscriber SET creditcardnumber = ?, subscribernumber = ? WHERE id = ?";
		try {
			stmt = connection.prepareStatement(query);
			stmt.setString(1, creditcardnumber);
			stmt.setString(2, subscribernumber);
			stmt.setString(3, id);
			stmt.executeUpdate();
			System.out.println("update done successfully");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getAllDB(){
		String query = "SELECT * FROM " + this.dataBasename + ".subscriber";
		String database = "";
		try{
			Statement stmt = connection.createStatement();
			ResultSet res = stmt.executeQuery(query);
			while(res.next()){
				database += res.getString("firstname");
				database +=" ";
				database += res.getString("lastname");
				database +=" ";
				database += res.getString("id");
				database +=" ";
				database += res.getString("phonenumber");
				database +=" ";
				database += res.getString("emailaddress");
				database +=" ";
				database += res.getString("creditcardnumber");
				database +=" ";
				database += res.getString("subscribernumber");
				database +="\n";
			}
			return database;
		}catch (SQLException exception){
			exception.printStackTrace();
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

	protected String getname(){
		try{
			return connection.getCatalog();
		}
		catch (SQLException e){
			return "null";
		}
	}

}