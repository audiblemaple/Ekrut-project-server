package Application.server;
import java.sql.*;


public class MysqlController {
	private static MysqlController sqlInstance = null;
	private Connection connection;
	private MysqlController(String IP, String username, String password){
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
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

	public static MysqlController getSQLInstance(){
		return sqlInstance;
	}

	public static MysqlController getSQLInstance(String IP, String username, String password){
		if (sqlInstance == null)
			sqlInstance = new MysqlController(IP, username, password);

		return sqlInstance;
	}

	public boolean addUser(String name,  String lastname, String ID, String phonenumber, String email, String creditcardnumber){
		// adding 1 to subscriber number to save the incremented number of subscribers
		Integer subscriberNum = getSubscriberNum() + 1;
		PreparedStatement stmt;
		String query = "INSERT INTO userdata.subscriber(name, lastname, ID, phonenumber, email, creditcardnumber, subscribernumber) VALUES(?, ?, ?, ?, ?, ?,?)";
		if (!checkUserExists(ID)){
			try{
				stmt = connection.prepareStatement(query);
				stmt.setString(1,name);
				stmt.setString(2,lastname);
				stmt.setString(3,ID);
				stmt.setString(4,phonenumber);
				stmt.setString(5,email);
				stmt.setString(6,creditcardnumber);
				stmt.setString(7,subscriberNum.toString());

				stmt.executeUpdate();
				if(checkUserExists(ID)){
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
	public boolean checkUserExists(String ID){
		PreparedStatement stmt;
		ResultSet res;
		String query = "SELECT * FROM userdata.subscriber WHERE (ID) = (?)";
		try{
			stmt = connection.prepareStatement(query);
			stmt.setString(1,ID);
			res = stmt.executeQuery();
			if (res.next()){
				if (res.getString("ID").equals(ID)){
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
		String query = "SELECT COUNT(*) FROM userdata.subscriber";
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

	public boolean deleteUser(String ID, String username, String password){
		PreparedStatement stmt;
		String query = "DELETE FROM userdata.subscriber WHERE ID=? username=? password=?";
		if(!checkUserExists(ID))
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

	public void updateUser(String ID, String creditcardnum, String subscribernum){
		PreparedStatement stmt;
		//String query = "UPDATE userdata.subscriber set creditcardnumber=? subscribernumber=? WHERE ID=?";
		String query = "UPDATE userdata.subscriber SET creditcardnumber = ?, subscribernumber = ? WHERE ID = ?";
		try {
			stmt = connection.prepareStatement(query);
			stmt.setString(1, creditcardnum);
			stmt.setString(2, subscribernum);
			stmt.setString(3, ID);
			stmt.executeUpdate();
			System.out.println("update done successfully");
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


