package Base;

import java.sql.*;

public class LienBase {

	private static Connection conn = null;

	
	public static Connection OuvertureConnection() throws SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/base_hopital", "root", "");
		} catch (ClassNotFoundException e) {
			throw new SQLException("Impossible de trouver le driver JDBC : " + e.getMessage());
		}
		return conn;
	}

	public static void FermetureConnection(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			// Exception ignoree
		}
	}
}