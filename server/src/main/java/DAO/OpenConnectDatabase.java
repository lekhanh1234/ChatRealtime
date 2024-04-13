package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OpenConnectDatabase {
	private static Connection connection;
	public static Connection openConnectDatabase() {
		if(connection != null) return connection;		
	    System.out.println("moi ket noi database");                                           
		connection = null;
		
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String jdbcUrl = "jdbc:sqlserver://127.0.0.1:1433;databaseName=messenger;username=sa;password=123;encrypt=false";
			try {
				connection = DriverManager.getConnection(jdbcUrl);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return connection;
	}
}
