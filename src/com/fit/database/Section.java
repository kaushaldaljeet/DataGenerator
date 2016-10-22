package com.fit.database;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class Section extends Table
{
	public Section(int minCount) 
	{
		setMinCount(minCount);
	}
	
	@Override
	public void generateData() 
	{
		try
		{
		int maxValue = (int)(getMinCount() * getScalingFactor() * 2.5);
		List<Integer> buildingNos = Department.getDepartmentBuildings();
		
		Scanner courseIds = new Scanner(new File("resources/tables/courseIds.txt"));
		
		for (int i = 0; i < maxValue; i++) 
		{
			int buildingNo = buildingNos.get(getRandomNumber(buildingNos.size()));
			int maxRoomNo=Classroom.getClassroomDetails().get(buildingNo);
			
			int maxSectionId = getRandomNumber(1,3);
			int courseId = courseIds.nextInt();
			for (int sectionId = 1; sectionId <= maxSectionId; sectionId++) 
			{
				addRow(courseId,sectionId,getRandomSemester(), getRandomYear(),buildingNo,getRandomNumber(1, maxRoomNo),getRandomNumber(1,17));
			}
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
