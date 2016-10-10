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
	
	public Department() 
	{
		try
		{
			File deptNameFile = new File("resources/deptNames.txt");
			deptNameArray = FileUtils.readLines(deptNameFile , StandardCharsets.UTF_8);
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
		
		for (int i = 0; i < getDeptNames().size(); i++) 
		{
			lines.add(getDeptNames().get(i)+","+getRandomNumber(20)+"," + getRandomNumber(500000,2000000));
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

}
