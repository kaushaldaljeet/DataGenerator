package com.fit.database;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Department extends Table
{
	static List<String> deptNames;
	private static List<Integer> departmentBuildings;
	static int maxValue=0;
	public static boolean generationCompleted = false;
	protected synchronized void incrementMaxValue()
	{
		maxValue++;
	}
	protected static int getMaxValue()
	{
		return maxValue;
	}
	public Department() 
	{
		try
		{
			File deptNameFile = new File("resources/deptNames.txt");
			deptNames = FileUtils.readLines(deptNameFile , StandardCharsets.UTF_8);
			departmentBuildings = new ArrayList<Integer>();
		}
		catch(IOException e)
		{
			System.out.println("Department ==> Department() -> " + e);
		}
	}

	@Override
	public void generateData() 
	{
		if(generationCompleted)
			return;
		
		int buildingNo = 0;
		for (int i = 0; i < getDeptNames().size(); i++) 
		{
			buildingNo = Utils.getInstance().getRandomNumber(1,20);
			addRow(getDeptNames().get(i),buildingNo,Utils.getInstance().getRandomNumber(500000,2000000));
			incrementMaxValue();
			if( !departmentBuildings.contains(buildingNo))
			{
				departmentBuildings.add(buildingNo);
			}
		}
		writeToFile();	
		generationCompleted=true;
	}
	
	public static List<String> getDeptNames() 
	{
		return deptNames;
	}
	
	public static List<Integer> getDepartmentBuildings() 
	{
		return departmentBuildings;
	}
}
