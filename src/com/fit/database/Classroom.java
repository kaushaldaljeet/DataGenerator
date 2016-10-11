package com.fit.database;

import java.util.ArrayList;
import java.util.List;

public class Classroom extends Table 
{

	@Override
	public void generateData() 
	{
		int capacity = 0;
		
		List<String> lines=  new ArrayList<String>();
		
		List<Integer> departmentBuildings = Department.getDepartmentBuildings();
		for (Integer buildingNo : departmentBuildings) 
		{
			int noOfRooms = getRandomNumber(1, 30);
			for (int roomNo = 1; roomNo <= noOfRooms; roomNo++) 
			{
				capacity = getRandomNumber(15,200);
				lines.add(buildingNo + "," + roomNo + "," + capacity);
			}
		}
		writeToFile(lines);
	}
}
