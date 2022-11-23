package Application.server;
import java.sql.*;


public class mysqlController {

	public mysqlController(){
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error*/
			System.out.println("Driver definition failed");
		}

		try {
			String jdbcURL = "jdbc:mysql://localhost:3306?serverTimezone=UTC";
			String username = "root";
			String password = "Aa123456";
			//Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/userdata/users?serverTimezone=IST","root","Aa123456");
			Connection conn = DriverManager.getConnection(jdbcURL,username,password);
			System.out.println("SQL connection succeed");
			//createTableCourses(conn);
			printCourses(conn);
		} catch (SQLException ex) {
			/* handle any errors*/
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	public static void printCourses(Connection con) {
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM userdata.users;");
	 		while(rs.next()) {
				 // Print out the values
				 System.out.println(rs.getString(1)+"  " +rs.getString(2));
			} 
			rs.close();
			//stmt.executeUpdate("UPDATE course SET semestr=\"W08\" WHERE num=61309");
		} catch (SQLException e) {e.printStackTrace();}
	}

	
	public static void createTableCourses(Connection con1){
		Statement stmt;
		try {
			stmt = con1.createStatement();
			stmt.executeUpdate("create table courses(num int, name VARCHAR(40), semestr VARCHAR(10));");
			stmt.executeUpdate("load data local infile \"courses.txt\" into table courses");
	 		
		} catch (SQLException e) {	e.printStackTrace();}
		 		
	}

}