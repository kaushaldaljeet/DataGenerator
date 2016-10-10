package com.fit.database;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

public class DataGenerator 
{
	static String SET_SCALING_FACTOR = "setScalingFactor";
	static String GENERATE_DATA =  "generateData";
	
	public static void main(String[] args) 
	{
		try
		{
			
			System.out.println("Please enter SF (Scaaling Factor) > 0 :");
			System.out.println("SF 100 gives around 7 MB of Data for Student Table");
			
			Scanner input = new Scanner(System.in);
			
			float scalingFactor = input.nextFloat();
			
			if(scalingFactor<=0)
			{
				main(args);
			}
			generateData(scalingFactor);
			input.close();
		}
		catch (InputMismatchException e) 
		{
			System.out.println("Please enter only numbers :) \n");
			main(args);
		}
	}

	
	
	private static void generateData(float scalingFactor) 
	{
		try 
		{
			long startTime = System.nanoTime();
			
			for (String tableName : getTableNames()) 
			{			
				Class<?> tableClass = Class.forName("com.fit.database." + tableName);
				Object table = tableClass.newInstance();
				
				Method setScalingFactor = tableClass.getSuperclass().getMethod(SET_SCALING_FACTOR,float.class);						 
				setScalingFactor.invoke(table, scalingFactor);
				
				Method  generateData = tableClass.getMethod(GENERATE_DATA);
				generateData.invoke(table);	
			}
			System.out.println("\nTotal Data Generated = "+ Table.getTotalDataGenerated() +" MB");
			
			long endTime = System.nanoTime();
			long duration = (endTime - startTime)/1000000;  //divide by 1000000 to get milliseconds.
			
			System.out.println("\nTotal Time for Generation = " + duration +" ms");
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	
	private static List<String> getTableNames() 
	{
		File tableNameFile =  new File("resources/tableNames.txt");
		try 
		{
			return FileUtils.readLines(tableNameFile , StandardCharsets.UTF_8 );
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return new  ArrayList<String>();
	}

}
