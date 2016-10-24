package com.fit.database;

import java.util.ArrayList;
import java.util.List;

public class Instructor extends Table 
{
	private static List<Integer> instructorIds;
	static int maxValue=0;
	public static boolean generationCompleted = false;
	protected void incrementMaxValue()
	{
		maxValue++;
	}
	protected static int getMaxValue()
	{
		return maxValue;
	}
	public Instructor(ThreadGroup group,int minCount,float scalingFactor) 
	{
		super(group, group.getName());
		setMinCount(minCount);
		setScalingFactor(scalingFactor);
		instructorIds=new ArrayList<Integer>();
	}

	public static List<Integer> getInstructorIds() 
	{
		return instructorIds;
	}
	
	@Override
	public void generateData() 
	{
		if(generationCompleted)
			return;
		
		int maxValue = (int)(getMinCount() * getScalingFactor());
		
		String instructorName="";
		String deptName="";
		
		List<String> deptNames = Department.getDeptNames();
		int size = deptNames.size();
		
		for (int id = 0; id < maxValue; instructorIds.add(id++)) 
		{
			instructorName = Utils.getInstance().getRandomName();
			deptName = deptNames.get(Utils.getInstance().getRandomNumber(size));
			incrementMaxValue();
			addRow(id,instructorName,deptName,Utils.getInstance().getRandomNumber(30000,150000));
		}
		writeToFile();
		generationCompleted=true;
	}
}
