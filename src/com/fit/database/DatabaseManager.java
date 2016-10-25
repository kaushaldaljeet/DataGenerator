package com.fit.database;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.logging.Logger;

public class DatabaseManager
{
	static Connection conn;
	static String username="root",password;
	static String connectionURL = null;
	
	protected static boolean init() 
	{
		try
		{
			
			Properties prop = new Properties();
			InputStream input = new FileInputStream("resources/SQLConfig.properties");
			prop.load(input);
			Class.forName(prop.getProperty("jdbcDriverClass"));
			connectionURL = "jdbc:"+prop.getProperty("dbURL")+":"
							+prop.getProperty("portNo")+"/"
							+prop.getProperty("instanceName")
							+prop.getProperty("connectionProperty");

			username=prop.getProperty("user");
			password=prop.getProperty("password");
			
			conn=DriverManager.getConnection(connectionURL,username,password);  
			return true;
		}
		catch (Exception e)
		{
			Logger.getGlobal().severe("DatabaseManager ==> init() -> " + e);
			return false;
		}
	}
	public static Connection getConnection() 
	{
		if (conn==null)
		{
			if(!init())
			{
				return null;
			}
		}
		return conn;
	}
	public static Connection getNewConnection() 
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver"); 
			return DriverManager.getConnection(getConnectionURL(),username,password); 
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;	
	}
	public static String getConnectionURL()
	{
		if(connectionURL==null)
		{
			init();
		}
		return connectionURL;
	}
	

}
