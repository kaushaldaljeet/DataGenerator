package com.fit.database;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;

public abstract class Table 
{
	private float scalingFactor = 0;
	private int MIN_COUNT = 2500;
	public static double totalDataGenerated = 0;
	
	protected void writeToFile(List<String> lines) 
	{
		try 
		{
			File outputFile = new File("resources/" + this.getClass().getSimpleName() +".csv");
			FileUtils.deleteQuietly(outputFile);
			FileUtils.writeLines(outputFile,lines,true);
			printFileDetails(outputFile,lines.size());
		}
		catch (IOException e) 
		{
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
	protected void printFileDetails(File file,int lines) 
	{
		System.out.print(file.getName() + " generated of size " );
		double fileSizeInMB = (file.length() / 1024.0) / 1024.0;
		DecimalFormat f = new DecimalFormat("#0.00###");
		System.out.println(f.format(fileSizeInMB) + " MB and total records are " + lines + ".");
		totalDataGenerated += fileSizeInMB;
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

	public abstract void generateData();
	
	public int getMinCount() 
	{
		return MIN_COUNT;
	}

	public void setMinCount(int minCount) 
	{
		MIN_COUNT = minCount;
	}

	public float getScalingFactor() 
	{
		return scalingFactor;
	}

	public void setScalingFactor(float scalingFactor) 
	{
		this.scalingFactor = scalingFactor;
	}

	public static String getTotalDataGenerated() 
	{
		DecimalFormat f = new DecimalFormat("#0.00###");
		return f.format(totalDataGenerated);
	}
}
