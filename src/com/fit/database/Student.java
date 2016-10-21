package com.fit.database;

import java.util.ArrayList;
import java.util.List;

public class Student extends Table
{
		private static List<Integer> studentIDs;
		
		protected static List<Integer> getStudentIds()
		{
			return studentIDs;
		}
		public Student(ThreadGroup group) 
		{
			super(group, "Student");
			studentIDs = new ArrayList<Integer>();
		}
		public synchronized void  addStudent(int id)
		{
			studentIDs.add(id);
		}
		public void generateData(int max)
		{
			int maxValue = (int)(getMinCount() * getScalingFactor());
			
			int studentID = 9000000+(maxValue*max)+1;
			String studentName = "";
			String deptName = "";
			List<String> lines=  new ArrayList<String>();
			List<String> deptNames = getDeptNames();
			
			for (int i = 0; i < maxValue; i++,studentID++)
			{
				addStudent(studentID);
				studentName = getRandomName();
				deptName = deptNames.get(getRandomNumber(8));
				lines.add(studentID+","+studentName+","+deptName+","+getRandomNumber(130));
			}
			writeToFile(lines);
		}

		@Override
		public void run() 
		{
			for (int i = 0; i < 10 ; i++) 
			{
				final int j=i;
				new Thread(() -> generateData(j)).start();
			}
		}
			
}
