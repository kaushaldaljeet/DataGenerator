package com.fit.database;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;

public class Utils 
{
	private static Utils utils;
	private List<String >firstNames,lastNames;
	
	public static Utils getInstance()
	{
		if (utils==null)
		{
			utils=new Utils();
		}
		return utils;
	}
	
	public String getFileSizeInMB(File file)
	{
		double fileSizeInMB = (file.length() / 1024.0) / 1024.0;
		DecimalFormat f = new DecimalFormat("#0.00###");
		return f.format(fileSizeInMB);
	}
	public String getFolderSizeInMB(File directory)
	{
		double fileSizeInMB = (FileUtils.sizeOfDirectory(directory) / 1024.0) / 1024.0;
		DecimalFormat f = new DecimalFormat("#0.00###");
		return f.format(fileSizeInMB);
	}
	protected String getRandomSemester()
	{
		if (new Random().nextInt(2) == 0)
		{
		    return "Spring";
		}
	    return "Fall";
	}
	protected int getRandomYear()
	{ 
	  return (2000 + new Random().nextInt(16)) ; 
	}

	public synchronized String getResult(Connection conn,String sql)
	{
		try
		{
			Statement st=conn.createStatement();
			ResultSet result = st.executeQuery(sql);
			if (result.next()) 
			{
				return result.getString(1);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return "";
	}
	public synchronized String getResult(String sql)
	{
		return getResult(DatabaseManager.getConnection(), sql);
	}
	public synchronized String getTableSizeInMB(String tableName) 
	{
		String sql="SELECT sum((data_length+index_length)/power(1024,2)) tablesize_mb FROM information_schema.tables WHERE table_name='"+tableName+"'";
		String resultSize;
		float size;
		int count=0;
		do
		{
			resultSize=getResult(sql);
			size=Float.parseFloat(resultSize);
			count++;
		}while(size==0.03125 || count<=5);
		
		return resultSize;
			
	}
	public synchronized String getDatabaseSizeInMB(String databaseName) 
	{
		String sql="SELECT sum((data_length+index_length)/power(1024,2)) tablesize_mb FROM information_schema.tables WHERE table_schema='"+databaseName+"'";
		String resultSize;
		int count=0;
		do
		{
			resultSize=getResult(sql);
			count++;
		}while(count<=2);
		
		return resultSize;
	}
	
	void initNamesFiles()
	{
		try
		{
			File firstNameFile = new File("resources/firstNames.txt");
			firstNames = FileUtils.readLines(firstNameFile , StandardCharsets.UTF_8);
			
			File lastNameFile = new File("resources/lastNames.txt");
			lastNames = FileUtils.readLines(lastNameFile , StandardCharsets.UTF_8 );
		}
		catch (Exception e) 
		{
			System.out.println(e);
		}
	}
	
	protected int getRandomNumber(int max) 
	{
		return getRandomNumber(0, max);
	}
	
	protected int getRandomNumber(int min, int max) 
	{
		Random r = new Random();
		return r.nextInt(max - min) + min;
	}

	protected int getRandomNumber(int min, int max, int k) 
	{
		return (int) (Math.floor(getRandomNumber(min,max))*k);
	}
	
	protected String getRandomName()
	{
		if (firstNames==null || lastNames==null)
		{
			initNamesFiles();
		}
		
		return firstNames.get(getRandomNumber(firstNames.size()))
			+ " " + lastNames.get(getRandomNumber(lastNames.size()));
	}

	public String getTableCount(Connection conn,String tableName)
	{
		String sql="SELECT count(1) from " + tableName;
		return getResult(conn,sql);
	}
}
