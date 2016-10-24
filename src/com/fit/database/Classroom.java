package com.fit.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Classroom extends Table 
{
	static Map<Integer,Integer> classroomDetails;
	static int maxValue=0;
	public static boolean generationCompleted = false;
	
	protected synchronized void incrementMaxValue()
	{
		maxValue++;
	}
	protected static int getMaxValue()
	{
		return maxValue;
	}
	public Classroom(ThreadGroup group) 
	{
		super(group,"Classroom");
		classroomDetails = new HashMap<Integer, Integer>();
	}
	
	@Override
	public void generateData() 
	{
		if(generationCompleted)
			return;
		
		int capacity = 0;
		
		List<Integer> departmentBuildings = Department.getDepartmentBuildings();
		for (Integer buildingNo : departmentBuildings) 
		{
			int noOfRooms = Utils.getInstance().getRandomNumber(2, 30);
			classroomDetails.put(buildingNo, noOfRooms);
			for (int roomNo = 1; roomNo <= noOfRooms; roomNo++) 
			{
				incrementMaxValue();
				capacity = Utils.getInstance().getRandomNumber(15,200);
				addRow(buildingNo ,roomNo ,capacity);
			}
		}
		writeToFile();
		generationCompleted=true;
	}

	public static Map<Integer, Integer> getClassroomDetails() 
	{
		return classroomDetails;
	}

}
