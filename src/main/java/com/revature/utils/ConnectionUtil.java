package com.revature.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionUtil {
	private static final Logger logger = LogManager.getLogger(ConnectionUtil.class);
	private static Connection connectionInstance = null;
	
	private ConnectionUtil () {
		
	}
	
	public static Connection getConnection() {
		if (ConnectionUtil.connectionInstance != null) {
			return logger.traceExit(ConnectionUtil.connectionInstance);
		}
		InputStream in = null;
		try {
			// load information from properties file
			Properties props = new Properties();
			in = new FileInputStream("C:\\Users\\IcedT\\Revature\\Java Workspace\\revature-project0\\src\\main\\resources\\connection.properties");
			props.load(in);
			// get the connection object
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = null;
			
			String endpoint = props.getProperty("jdbc.url");
			String username = props.getProperty("jdbc.username");
			String password = props.getProperty("jdbc.password");
			
			con = DriverManager.getConnection(endpoint, username, password);
			return logger.traceExit(con);
		} catch (Exception e){
			logger.catching(e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				logger.catching(e);
			}
		}
		return null;
	}
}
