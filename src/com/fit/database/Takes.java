package com.fit.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Takes extends Table 
{
	Scanner studentIds;
	public static boolean generationCompleted = false;
	public Takes(int minCount,float scalingFactor) 
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
			System.out.println("Takes ==> Takes() -> " + e);
		}
		
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
	public synchronized String getStudentId() throws Exception
	{
		if(studentIds.hasNext())
			return  studentIds.next();
		
		return null;
	}
	
	@Override
	public void generateData() 
	{
		try
		{
			int maxValue = (int)(getMinCount() * getScalingFactor());
			
			Scanner section = new Scanner(new File("resources/tables/Section.txt"));
			
			String[] sectionString = null;
			String studentId="";
			
			for(int i=0;i<maxValue;i++)
			{
				int courses = Utils.getInstance().getRandomNumber(1,7);
				
				studentId = getStudentId();
				if (studentId==null)
				{
					System.out.println("i="+i + " max="+maxValue);
					break;
				}	
				for (int j = 0; j < courses; j++) 
				{
					if (section.hasNext())
					{
						sectionString=section.next().split(",");
					}
					else
					{
						section = new Scanner(new File("resources/tables/Section.txt"));
						sectionString=section.next().split(",");
					}
					addRow(studentId,sectionString[0],sectionString[1],sectionString[2],sectionString[3],getRandomGrade());
					incrementMaxValue();
				}
				flushData(i);
			}
		}
		catch (Exception e) 
		{
			System.out.println("Takes ==> generateData() -> " + e);
		}
	}
	private char getRandomGrade() 
	{
		return  (char)('A' + Utils.getInstance().getRandomNumber(5));
	}
	
	@Override
	public void run() 
	{	
		if(generationCompleted)
			return;
		
		try
		{
			ThreadGroup group = new ThreadGroup("TakesGroup");
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
			System.out.println("Takes ==> run() -> " + e);
		}
	}
}
