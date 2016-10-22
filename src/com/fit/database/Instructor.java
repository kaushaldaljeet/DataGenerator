package com.fit.database;

import java.util.ArrayList;
import java.util.List;

public class Instructor extends Table 
{

	int minCount=100;
	private static List<Integer> instructorIds;
	
	public Instructor(ThreadGroup group) 
	{
		super(group, "Instructor");
		instructorIds=new ArrayList<Integer>();
	}

	public static List<Integer> getInstructorIds() 
	{
		return instructorIds;
	}
	
	@Override
	public void generateData() 
	{
		int maxValue = (int)(getMinCount() * getScalingFactor());
		String instructorName="";
		String deptName="";
		
		for (int id = 0; id < maxValue; instructorIds.add(id++)) 
		{
			instructorName = getRandomName();
			deptName = getDeptNames().get(getRandomNumber(getDeptNames().size()));
			addRow(id,instructorName,deptName,getRandomNumber(30000,150000,100));
		}
		writeToFile();
	}

	public int getMinCount() 
	{
		return minCount;
	}
}
