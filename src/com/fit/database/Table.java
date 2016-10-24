package com.fit.database;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class Table extends Thread
{
	private float scalingFactor = 0;
	private int minCount = 2500;
	
	BufferedWriter outputFileWritter;
	File outputFile;
	
	public int getMinCount() 
	{
		return minCount;
	}	
	public void setMinCount(int minCount) 
	{
		this.minCount = minCount;
	}
	public Table(ThreadGroup group, String name) 
	{
		super(group, name);
		init();
	}
	public Table() 
	{
		init();
	}
	protected void init() 
	{
		try 
		{
			outputFile = new File("resources/tables/" + this.getClass().getSimpleName() +".txt");
			
    		if(!outputFile.exists())
    		{
    			outputFile.createNewFile();
    		}
			FileWriter fw= new FileWriter(outputFile, true);
			outputFileWritter = new BufferedWriter(fw);	
		} 
		catch (IOException e) 
		{
			System.out.println("Table ==> init() -> " + e);
		}
		
	}
	protected synchronized void flush() 
	{
		try
		{
			outputFileWritter.flush();
		}
		catch (Exception e) 
		{
			System.out.println("Table ==> flush() -> " + e);
		}
	}
	protected synchronized void writeToFile() 
	{
		try 
		{
			outputFileWritter.flush();
			outputFileWritter.close();
			printFileDetails();
		}
		catch (Exception e) 
		{
			System.out.println("Table ==> writeToFile() -> " + e);
		}
	}
	
	protected synchronized void printFileDetails() 
	{
		System.out.print("[FILE] => ");
		System.out.print(outputFile.getName() + " generated of size " );
		System.out.println(Utils.getInstance().getFileSizeInMB(outputFile) + " MB");
	}
	
	public abstract void generateData();
	
	public float getScalingFactor() 
	{
		return scalingFactor;
	}

	public void setScalingFactor(float scalingFactor) 
	{
		this.scalingFactor = scalingFactor;
	}
	
	protected  void addRow(Object... string)
	{
		String line="";
		 for (int i = 0; i < string.length; ++i) 
		 {
			  if (i==string.length-1)
			  {
				  line+=string[i];
			  }
			  else
				  line+=string[i]+",";
		 }
		 try
		 {
			 outputFileWritter.write(line+"\n");
		 }
		 catch (Exception e) 
		 {
			 System.out.println("Table ==> addRow() -> " + e);
		 }
	}
	
	@Override
	public void run() 
	{
		generateData();
	}
	
	protected void flushData(int count)
	{
		if (count%500==0)
		{
			try
			{
				outputFileWritter.flush();
			}
			catch (Exception e)
			{
				System.out.println("Table ==> flushData() -> " + e);
			}
		}
	}
}
