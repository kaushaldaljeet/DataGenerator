package com.fit.database;

import java.util.List;

public class Prerequisite extends Table
{
	public static boolean generationCompleted = false;
	
	public Prerequisite(int minCount,float scalingFactor) 
	{
		super();
		setMinCount(minCount);
		setScalingFactor(scalingFactor);
	}
	static int maxValue=0;
	protected synchronized void incrementMaxValue()
	{
		maxValue++;
	}
	protected static int getMaxValue()
	{
		return maxValue;
	}
	@Override
	public void generateData() 
	{
		if(generationCompleted)
			return;
		
		try
		{
			int maxValue = (int)(getMinCount() * getScalingFactor() * 2.5*0.9);
			List<Integer> courseIds = Course.getCourseIds();
			int courseId=0;
			int prereqId=1;
			int maxCourseId = courseIds.size();
			for(int i=0;i<maxValue;i++)
			{
				courseId = courseIds.get(i);
				while (courseId==prereqId)
				{
					prereqId=Utils.getInstance().getRandomNumber(maxCourseId-1);
				}
				addRow(courseId,prereqId);
				incrementMaxValue();
				flushData(i);
			}
			writeToFile();
			generationCompleted=true;
		}
		catch (Exception e) 
		{
			System.out.println("Prerequisite ==> generateData() -> ");
			e.printStackTrace();
		}
	}
}
