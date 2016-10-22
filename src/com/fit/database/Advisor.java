package com.fit.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class Advisor extends Table 
{
	Scanner studentIds;
	
	public Advisor()
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
				instuctorId = instructorIds.get(getRandomNumber(instructorsSize));
				addRow(studentId,instuctorId);
				
				flushData(i);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() 
	{	
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
			bufferedWritter.close();
		}
		catch (Exception e) 
		{
			// TODO: handle exception
		}
	}

	
}
