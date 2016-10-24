package com.fit.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class Advisor extends Table 
{
	public static boolean generationCompleted = false;
	Scanner studentIds;
	static int maxValue=0;
	protected synchronized void incrementMaxValue()
	{
		maxValue++;
	}
	protected static int getMaxValue()
	{
		return maxValue;
	}
	public Advisor(int minCount,float scalingFactor) 
	{
		super();
		setMinCount(minCount);
		setScalingFactor(scalingFactor);
		
		try 
		{
			studentIds = new Scanner(new File("resources/tables/studentIds.txt"));
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("Advisor ==> Advisor() -> " + e);
		}
	}	
	public synchronized int getStudentId()
	{
		return studentIds.nextInt();
	}
	public void generateData() 
	{
		int maxValue = (int)(getMinCount() * getScalingFactor() * 0.9);
		int studentId=0;
		int instuctorId=0;
		List<Integer> instructorIds = Instructor.getInstructorIds();

		int instructorsSize=instructorIds.size();
		int i=0;
		try
		{
			for ( i = 0; i < maxValue; i++) 
			{
				studentId =  getStudentId();
				instuctorId = instructorIds.get(Utils.getInstance().getRandomNumber(instructorsSize));
				addRow(studentId,instuctorId);
				incrementMaxValue();
				flushData(i);
			}
		}
		catch (Exception e) 
		{
			Logger.getGlobal().severe("Advisor ==> generateData() -> " + e);
		}
	}
	
	@Override
	public void run() 
	{	
		if(generationCompleted)
			return;
		try
		{
			ThreadGroup group = new ThreadGroup("AdvisorGroup");
			for (int i = 0; i < 10 ; i++) 
			{
				new Thread(group,() -> generateData()).start();
			}
			
			while(group.activeCount()>0)
			{
				Thread.sleep(1000);
			}
			printFileDetails();
			outputFileWritter.close();
			generationCompleted=true;
		}
		catch (Exception e) 
		{
			Logger.getGlobal().severe("Advisor ==> run() -> " + e);
		}
	}

	
}
