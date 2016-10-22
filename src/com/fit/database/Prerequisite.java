package com.fit.database;

import java.io.File;
import java.util.Scanner;

public class Prerequisite extends Table
{
	@Override
	public void generateData() 
	{
		try
		{
			int maxValue = (int)(getMinCount() * getScalingFactor() * 2.5*0.9);
			Scanner courseIds = new Scanner(new File("resources/tables/courseIds.txt"));
			int courseId=0;
			int prereq_id=0;
			courseIds.nextInt();
			for(int i=0;courseIds.hasNextInt();i++)
			{
				courseId = courseIds.nextInt();
				if (courseId==1)
					continue;
				prereq_id=getRandomNumber(1,courseId);
				addRow(courseId,prereq_id);
				flushData(i);
			}
			writeToFile();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
