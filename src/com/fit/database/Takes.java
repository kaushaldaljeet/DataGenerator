package com.fit.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Takes extends Table 
{
	Scanner studentIds;
	public Takes() 
	{
		try 
		{
			studentIds = new Scanner(new File("resources/tables/studentIds.txt"));
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public synchronized String getStudentId() throws Exception
	{
		if(studentIds.hasNext())
			return  studentIds.next();
		else
		{
			studentIds = new Scanner(new File("resources/tables/studentIds.txt"));
			return studentIds.next();
		}
	}
	
	@Override
	public void generateData() 
	{
		try
		{
			int maxValue = (int)(getMinCount() * getScalingFactor());
			
			Scanner section = new Scanner(new File("resources/tables/Section.txt"));
			
			String[] sectionString = null;
			String sem="",studentId="";
			int year=0;
			for(int i=0;i<maxValue;i++)
			{
				int courses = getRandomNumber(1,7);
				
				studentId = getStudentId();
				sem=getRandomSemester();
				year=getRandomYear();
				
				for (int j = 0; j < courses; j++) 
				{
					if (section.hasNext())
					{
						sectionString=section.next().split(",");
					}
					else
					{
						System.out.println("aaya");
						section = new Scanner(new File("resources/tables/Section.txt"));
						sectionString=section.next().split(",");
					}
					addRow(studentId,sectionString[0],sectionString[1],sem,year,getRandomGrade());
				}
				flushData(i);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	private char getRandomGrade() 
	{
		return  (char)('A' + getRandomNumber(5));
	}
	
	@Override
	public void run() 
	{	
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
			bufferedWritter.close();
		}
		catch (Exception e) 
		{
			// TODO: handle exception
		}
	}
}
