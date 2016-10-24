package com.fit.database;

import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.logging.Logger;

public class DataLoader extends Thread 
{
	Table generatorThread;
	
	public DataLoader(Table generatorThread) 
	{
		 this.generatorThread=generatorThread;
	}
	public DataLoader(ThreadGroup group,Table generatorThread) 
	{
		super(group,group.getName());
		this.generatorThread=generatorThread;
	}
	protected void startLoading()
	{
		
		try
		{
			waitForComplete(generatorThread);
			executeLoadingQuery();
			waitForQueryToComplete();
		}
		catch (Exception e)
		{
			Logger.getGlobal().severe("DataLoader ==> startLoading() -> " + e);
		}
	}
	
	private void waitForComplete(Table thread) throws Exception
	{
		while(thread.isAlive())
		{
			Thread.sleep(1000);
		}
	}
	
	private void executeLoadingQuery() throws Exception 
	{
		String tableName=generatorThread.getClass().getSimpleName();
		String path = System.getProperty("user.dir");
		path=replaceFileSeperator(path);
		String sql = "LOAD DATA LOCAL INFILE '"+path+"/resources/tables/"+tableName+".txt' INTO TABLE "+tableName +" FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n'";
		//System.out.println("Loading data started for " + tableName + "...");
		Statement st = DatabaseManager.getConnection().createStatement();
		st.executeQuery(sql);
	}
	
	private String replaceFileSeperator(String path) 
	{
		String operatingSystem = System.getProperty("os.name").toLowerCase();
		if(operatingSystem.contains("windows"))
		{
			path=path.replaceAll("\\\\","//");
		}
		return path;
	}
	
	private void waitForQueryToComplete() throws Exception
	{
		Utils utils = new Utils();
		String tableName=generatorThread.getClass().getSimpleName();
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
		Class cls = Class.forName(generatorThread.getClass().getName());
		Method method = cls.getDeclaredMethod("getMaxValue");
		int max = (int) method.invoke(cls);
		return max;
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
