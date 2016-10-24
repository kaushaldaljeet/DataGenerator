package com.fit.database;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.logging.Logger;

public class DataLoader extends Thread 
{
	Table thread;
	
	public DataLoader(Table thread) 
	{
		 this.thread=thread;
	}
	public DataLoader(ThreadGroup group,Table thread) 
	{
		super(group,group.getName());
		this.thread=thread;
	}
	protected void startLoading()
	{
		
		try
		{
			thread.waitForComplete(1000);
			executeLoadingQuery();
			waitForQueryToComplete();
				
			}
		catch (Exception e)
		{
			Logger.getGlobal().severe("DataLoader ==> startLoading() -> " + e);
		}
	}
	private void executeLoadingQuery() throws Exception 
	{
		String tableName=thread.getClass().getSimpleName();
		String path = System.getProperty("user.dir");
		String sql = "LOAD DATA LOCAL INFILE '"+path+"/resources/tables/"+tableName+".txt' INTO TABLE "+tableName +" FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n'";
		//System.out.println("Loading data started for " + tableName + "...");
		Statement st = DatabaseManager.getConnection().createStatement();
		st.executeQuery(sql);
	}
	
	private void waitForQueryToComplete() throws Exception
	{
		Utils utils = new Utils();
		String tableName=thread.getClass().getSimpleName();
		int currentCount;
		int max = getMaxRecords();
		
		while(true)
		{
			currentCount = Integer.parseInt(utils.getTableCount(tableName));
			if (max==currentCount)
			{
				break;
			}
			Thread.sleep(1000);
		}
		
		String tableSize=utils.getTableSizeInMB(tableName);
		System.out.println("[DATABASE] => Data Loaded for " + tableName + " with size = " + tableSize + " MB");
	}
	private int getMaxRecords() throws Exception 
	{
		Class cls = Class.forName(thread.getClass().getName());
		Method method = cls.getDeclaredMethod("getMaxValue");
		int max = (int) method.invoke(cls);
		return max;
	}
	
	public void waitForComplete()
	{
		try
		{
			while(this.isAlive())
			{
				Thread.sleep(1000);
			}
		}
		catch(Exception e)
		{
			Logger.getGlobal().severe("DataLoader ==> waitForComplete() ->"+e);
		}
	}
	
	@Override
	public void run() 
	{
		if(DataGenerator.isLoadData())
		{
			startLoading();
		}
	}
}
