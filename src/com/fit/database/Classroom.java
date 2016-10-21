package com.fit.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Classroom extends Table 
{
	static Map<Integer,Integer> classroomDetails;
	
	public Classroom(ThreadGroup group) 
	{
		super(group,"Classroom");
		classroomDetails = new HashMap<Integer, Integer>();
	}

	@Override
	public void generateData() 
	{
		int capacity = 0;
		
		List<Integer> departmentBuildings = Department.getDepartmentBuildings();
		for (Integer buildingNo : departmentBuildings) 
		{
			int noOfRooms = getRandomNumber(2, 30);
			classroomDetails.put(buildingNo, noOfRooms);
			for (int roomNo = 1; roomNo <= noOfRooms; roomNo++) 
			{
				capacity = getRandomNumber(15,200);
				addRow(buildingNo ,roomNo ,capacity);
			}
		}
		writeToFile();
	}

	public static Map<Integer, Integer> getClassroomDetails() 
	{
		return classroomDetails;
	}

}
