package tfm.MissingFlights;


import java.sql.Connection;
import java.sql.DriverManager;

import tfm.MissingFlights.PropertiesReader;

public enum DaoConnectionFactory {
	INSTANCE;

	private Connection con = null;

	private DaoConnectionFactory() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
		  	String url = PropertiesReader.getProperties().getProperty("connection");
			
			con = DriverManager.getConnection(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return con;
	}
}

