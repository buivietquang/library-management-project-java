package quanlythuvien.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
	private static ConnectDB connectDB;
	private final String className = "com.mysql.cj.jdbc.Driver";
	private final String url = "jdbc:mysql://127.0.0.1:3306/library?autoReconnect=true&useSSL=false&characterEncoding=UTF-8";
	private final String user = "root";
	private final String pwd = "root";
	
	public ConnectDB() {
		
	}
	
	public static ConnectDB getInstance() {
		if(connectDB == null)
			connectDB = new ConnectDB();
		
		return connectDB;
	}
	
	public Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName(className);// connect driver
			conn = (Connection) DriverManager.getConnection(url, user, pwd);
			System.out.println("Connect success.");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return conn;
	}

}
