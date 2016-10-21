package com.fit.database;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;

public abstract class Table extends Thread
{
	private float scalingFactor = 0;
	private List<String> firstNameArray;
	private List<String> lastNameArray;
	private List<String> deptNameArray;
	private int minCount = 2500;
	private List<String> lines = new ArrayList<String>();
	
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
		try
		{
			File firstNameFile = new File("resources/firstNames.txt");
			firstNameArray = FileUtils.readLines(firstNameFile , StandardCharsets.UTF_8);
			
			File lastNameFile = new File("resources/lastNames.txt");
			lastNameArray = FileUtils.readLines(lastNameFile , StandardCharsets.UTF_8 );
			
			File deptNameFile = new File("resources/deptNames.txt");
			deptNameArray = FileUtils.readLines(deptNameFile , StandardCharsets.UTF_8);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public Table() 
	{
		
	}
	protected synchronized void writeToFile(List<String>  lines) 
	{
		this.lines=lines;
		writeToFile();
	}
	protected synchronized void writeToFile() 
	{
		try 
		{
			File outputFile = new File("resources/tables/" + this.getClass().getSimpleName() +".csv");
			FileUtils.writeLines(outputFile,lines,true);
			printFileDetails(outputFile,lines.size());
		}
		catch (IOException e) 
		{
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
	protected synchronized void printFileDetails(File file,int lines) 
	{
		System.out.print(file.getName() + " generated of size " );
		double fileSizeInMB = (file.length() / 1024.0) / 1024.0;
		DecimalFormat f = new DecimalFormat("#0.00###");
		System.out.println(f.format(fileSizeInMB) + " MB");
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
	
	public void generateData()
	{
		
	}


	public float getScalingFactor() 
	{
		return scalingFactor;
	}

	public void setScalingFactor(float scalingFactor) 
	{
		this.scalingFactor = scalingFactor;
	}
	protected String getRandomName()
	{
		return firstNameArray.get(getRandomNumber(firstNameArray.size()))
			+ " " + lastNameArray.get(getRandomNumber(lastNameArray.size()));
	}
	public List<String> getFirstNames() 
	{
		return firstNameArray;
	}

	public void setFirstNames(List<String> firstNameArray) 
	{
		this.firstNameArray = firstNameArray;
	}

	public List<String> getLastNames() 
	{
		return lastNameArray;
	}

	public void setLastNames(List<String> lastNameArray) 
	{
		this.lastNameArray = lastNameArray;
	}
	
	public List<String> getDeptNames() {
		return deptNameArray;
	}

	public void setDeptNames(List<String> deptNameArray) {
		this.deptNameArray = deptNameArray;
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
		 lines.add(line);
	}
	
	@Override
	public void run() {
		generateData();
	}
}
