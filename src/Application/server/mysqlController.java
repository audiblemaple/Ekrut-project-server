package Application.server;
import java.sql.*;


public class mysqlController {
	private static mysqlController sqlInstance = null;
	private Connection connection;
	private mysqlController(String IP, String username, String password){
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error*/
			System.out.println("Driver definition failed");
		}

		try {
			//String jdbcURL = "jdbc:mysql://localhost:3306?serverTimezone=UTC";
			String jdbcURL = "jdbc:mysql://" + IP + ":3306?serverTimezone=UTC";
			connection = DriverManager.getConnection(jdbcURL,username,password);
			System.out.println("SQL connection succeed");

		} catch (SQLException ex) {
			/* handle any errors*/
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public static mysqlController getSQLInstance(){
		return sqlInstance;
	}

	public static mysqlController getSQLInstance(String IP, String username, String password){
		if (sqlInstance == null)
			sqlInstance = new mysqlController(IP, username, password);

		return sqlInstance;
	}

	public boolean addUser(String ID,  String username, String password, String name, String lastname, String phonenumber, String email){
		PreparedStatement stmt;
		String query = "INSERT INTO userdata.users(ID, username, password, name, lastname, phonenumber, email) VALUES(?, ?, ?, ?, ?, ?,?)";
		if (!checkUserExists(username, password).equals("")){
			try{
				stmt = connection.prepareStatement(query);
				stmt.setString(1,ID);
				stmt.setString(2,username);
				stmt.setString(3,password);
				stmt.setString(4,name);
				stmt.setString(5,lastname);
				stmt.setString(6,phonenumber);
				stmt.setString(7,email);
				stmt.executeUpdate();
				if(checkUserExists(username, password).equals("")){
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
	public String checkUserExists(String username, String password){
		PreparedStatement stmt;
		ResultSet res;
		String query = "SELECT ID,username,password FROM userdata.users WHERE (username, password) = (?, ?)";
		try{
			stmt = connection.prepareStatement(query);
			stmt.setString(1,username);
			stmt.setString(2,password);
			res = stmt.executeQuery();
			if (res.next()){
				if (res.getString("username").equals(username) && res.getString("password").equals(password)){
					return res.getString("ID");
				}
			}
			stmt.close();
			res.close();
		}
		catch (SQLException e){
			e.printStackTrace();
		}
		return "";
	}

	public boolean deleteUser(String ID, String username, String password){
		PreparedStatement stmt;
		String query = "DELETE FROM userdata.users WHERE ID=? username=? password=?";
		if(!checkUserExists(username, password).equals(""))
			return false;
		try {
			stmt = connection.prepareStatement(query);
			stmt.setString(1, ID);
			stmt.setString(2, username);
			stmt.setString(3, password);
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// TODO: write functionality of update user
	public void updateUser(){
		PreparedStatement stmt;
		String query = "DELETE FROM table_name WHERE username=? ;";
		try {
			stmt = connection.prepareStatement(query);
			stmt.setString(1, "");
			stmt.executeUpdate();
			System.out.println("updateStatuses done successfully");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}



	protected void disconnect(){
		try{
			connection.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
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



// @localhost

//	SPARE  PARTS:

// String query = "SELECT * FROM userdata.users WHERE (ID, username, password) = (?, ?, ?)";
// String query = "SELECT * FROM userdata.users WHERE ID=? username=? and password=?";
// String query = "DELETE FROM userdata.users WHERE ID=? and username=? and password=?";
// String query = "SELECT * FROM userdata.users WHERE username = '" + username + "' and password = '" + password + "'";
// SELECT EXISTS(SELECT 1 FROM mysql.user WHERE user = 'username')
// SELECT EXISTS(SELECT 1 FROM mysql.user WHERE user = ?)
// String query = "SELECT * FROM userdata.users WHERE ID=?";

//	public static void printCourses(Connection con) {
//		Statement stmt;
//		try {
//			// stmt = con.createStatement();
//			stmt = con.prepareStatement("INSERT INTO userdata.users(ID,username,approvalstatus) VALUES('Master',\"abc\",true), ('Designe',\"egf\",false);\n");
//			ResultSet rs = stmt.executeQuery("SELECT * FROM userdata.users;");
//	 		while(rs.next()) {
//				 // Print out the values
//				 System.out.println(rs.getString(1)+"  " +rs.getString(2));
//			}
//			rs.close();
//			//stmt.executeUpdate("UPDATE course SET semestr=\"W08\" WHERE num=61309");
//		} catch (SQLException e) {e.printStackTrace();}
//	}


