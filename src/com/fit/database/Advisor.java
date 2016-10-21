package com.fit.database;

import java.util.List;

public class Advisor extends Table 
{
	@Override
	public void generateData() 
	{
		int maxValue = (int)(getMinCount() * getScalingFactor() * 0.9);
		int studentId=0;
		int instuctorId=0;
		List<Integer> instructorIds = Instructor.getInstructorIds();
		List<Integer> studentIds = Student.getStudentIds();
		int instructorsSize=instructorIds.size();
		int i=0;
		try
		{
			for ( i = 0; i < maxValue; i++) 
			{
				studentId = studentIds.get(i);
				instuctorId = instructorIds.get(getRandomNumber(instructorsSize));
				addRow(studentId,instuctorId);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		writeToFile();
	}
	
}
