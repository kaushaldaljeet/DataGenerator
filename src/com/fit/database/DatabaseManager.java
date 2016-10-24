package com.fit.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;

public class DatabaseManager
{
	static Connection conn;
	static String username="root",password;
	
	protected static boolean init() 
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver"); 
			conn=DriverManager.getConnection(getConnectionURL(),username,password);  
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
			conn=DriverManager.getConnection(getConnectionURL(),username,password); 
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	public static String getConnectionURL()
	{
		String connectionURL="jdbc:mysql://localhost:3306/University?useSSL=false";
		return connectionURL;
	}
	

}
