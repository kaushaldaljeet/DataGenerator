package com.fit.database;

import java.util.List;

public class Section extends Table
{
	public static boolean generationCompleted = false;
	
	public Section(int minCount,float scalingFactor) 
	{
		super();
		setMinCount(minCount);
		setScalingFactor(scalingFactor);
	}
	static int maxValue=0;
	
	protected void incrementMaxValue()
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
			int maxValue = (int)(getMinCount() * getScalingFactor() * 2.5);
			List<Integer> buildingNos = Department.getDepartmentBuildings();
			
			List<Integer> courseIds = Course.getCourseIds();
			Teaches teaches = new Teaches(1000, getScalingFactor());
			List<Integer> instructorIds = Instructor.getInstructorIds();
			int instructorsSize = instructorIds.size();
			
			for (int index = 0; index < maxValue; index++) 
			{
				int buildingNo = buildingNos.get(Utils.getInstance().getRandomNumber(buildingNos.size()));
				int maxRoomNo=Classroom.getClassroomDetails().get(buildingNo);
				
				int maxSectionId = Utils.getInstance().getRandomNumber(1,3);
				int courseId = courseIds.get(index);
				String sem;
				int year,instuctorId;
				
				for (int sectionId = 1; sectionId <= maxSectionId; sectionId++) 
				{
					incrementMaxValue();
					teaches.incrementMaxValue();
					instuctorId = instructorIds.get(Utils.getInstance().getRandomNumber(instructorsSize));
					sem = Utils.getInstance().getRandomSemester();
					year = Utils.getInstance().getRandomYear();
					addRow(courseId,sectionId,sem, year,buildingNo,Utils.getInstance().getRandomNumber(1, maxRoomNo),Utils.getInstance().getRandomNumber(1,17));
					teaches.addRow(instuctorId,courseId,sectionId,sem,year);
				}
				flushData(index);
				teaches.flushData(index);
			}
			writeToFile();
			generationCompleted=true;
			teaches.writeToFile();
		}
		catch (Exception e)
		{
			System.out.println("Section ==> generateData() -> ");
			e.printStackTrace();
		}	
	}
	
}
