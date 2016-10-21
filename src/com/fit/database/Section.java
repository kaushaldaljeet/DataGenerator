package com.fit.database;

import java.util.ArrayList;
import java.util.List;

public class Section extends Table
{
	public Section(int minCount) 
	{
		setMinCount(minCount);
	}
	
	@Override
	public void generateData() 
	{
		int maxValue = (int)(getMinCount() * getScalingFactor() * 2.5);
		List<String> lines=  new ArrayList<String>();
		List<Integer> courseIds = Course.getCourseIds();
		List<Integer> buildingNos = Department.getDepartmentBuildings();
		
		for (int i = 0; i < maxValue; i++) 
		{
			int buildingNo = buildingNos.get(getRandomNumber(buildingNos.size()));
			int maxRoomNo=Classroom.getClassroomDetails().get(buildingNo);
			
			int maxSectionId = getRandomNumber(1,3);
			for (int sectionId = 1; sectionId <= maxSectionId; sectionId++) 
			{
				lines.add(courseIds.get(i) + ","+sectionId+"," + getRandomSemester() +"," + getRandomYear()
				+","+ buildingNo+","+getRandomNumber(1, maxRoomNo)+","+getRandomNumber(1,17));
			}
		}
		writeToFile(lines);
		
	}
	protected String getRandomSemester()
	{
		if (getRandomNumber(2) == 0)
		{
		    return "Spring";
		}
	    return "Fall";
	}
	protected int getRandomYear()
	{ 
	  return (2000 + getRandomNumber(16)) ; 
	}
}
