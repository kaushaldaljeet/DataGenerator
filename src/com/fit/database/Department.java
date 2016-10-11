package com.fit.database;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Department extends Table
{
	List<String> deptNameArray;
	private static List<Integer> departmentBuildings;
	
	public Department() 
	{
		try
		{
			File deptNameFile = new File("resources/deptNames.txt");
			deptNameArray = FileUtils.readLines(deptNameFile , StandardCharsets.UTF_8);
			departmentBuildings = new ArrayList<Integer>();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}

	@Override
	public void generateData() 
	{
		List<String> lines=  new ArrayList<String>();
		int buildingNo = 0;
		for (int i = 0; i < getDeptNames().size(); i++) 
		{
			buildingNo = getRandomNumber(20);
			lines.add(getDeptNames().get(i)+"," + buildingNo +"," + getRandomNumber(500000,2000000));
			if( !departmentBuildings.contains(buildingNo))
			{
				departmentBuildings.add(buildingNo);
			}
		}
		writeToFile(lines);		
	}
	

	public List<String> getDeptNames() 
	{
		return deptNameArray;
	}

	public void setDeptNames(List<String> deptNameArray) 
	{
		this.deptNameArray = deptNameArray;
	}
	
	public static List<Integer> getDepartmentBuildings() 
	{
		return departmentBuildings;
	}

}
