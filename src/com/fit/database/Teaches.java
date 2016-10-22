package com.fit.database;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

public class Teaches extends Table 
{
	@Override
	public void generateData() 
	{
		try
		{
			int maxValue = (int)(getMinCount() * getScalingFactor() * 2.5*3*1.1);
			
			List<String> courseIds = FileUtils.readLines(new File("resources/tables/courseIds.txt"), StandardCharsets.UTF_8);
			int courseSize=courseIds.size();
			List<Integer> instructorIds = Instructor.getInstructorIds();
			int instructorsSize = instructorIds.size();
			int instuctorId=0;
			
			for(int i=0;i<maxValue;i++)
			{
				instuctorId = instructorIds.get(getRandomNumber(instructorsSize));
				addRow(instuctorId,courseIds.get(getRandomNumber(courseSize)),getRandomNumber(1,3),getRandomSemester(),getRandomYear());
				flushData(i);
			}
			writeToFile();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
