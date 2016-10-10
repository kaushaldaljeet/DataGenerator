package com.fit.database;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Student extends Table
{
		private List<String> firstNameArray;
		private List<String> lastNameArray;
		private List<String> deptNameArray;
		
		public Student() 
		{
			try
			{
				File deptNameFile = new File("resources/deptNames.txt");
				deptNameArray = FileUtils.readLines(deptNameFile , StandardCharsets.UTF_8);
				
				File firstNameFile = new File("resources/firstNames.txt");
				firstNameArray = FileUtils.readLines(firstNameFile , StandardCharsets.UTF_8);
				
				File lastNameFile = new File("resources/lastNames.txt");
				lastNameArray = FileUtils.readLines(lastNameFile , StandardCharsets.UTF_8 );
			}
			catch(IOException e)
			{
				System.out.println(e);
			}
		}
		
		@Override
		public void generateData()
		{
			int maxValue = (int)(getMinCount() * getScalingFactor());
			
			int studentID = 9000000;
			String studentName = "";
			String deptName = "";
			
			List<String> lines=  new ArrayList<String>();
		
			for (int i = 0; i < maxValue; i++,studentID++) 
			{
				studentName = getRandomStudentName();
				deptName = getDeptNames().get(getRandomNumber(getDeptNames().size()));
				lines.add(studentID+","+studentName+","+deptName+","+getRandomNumber(130));
			}
			writeToFile(lines);
		}
		private String getRandomStudentName()
		{
			return firstNameArray.get(getRandomNumber(firstNameArray.size())) 
					+ " " + lastNameArray.get(getRandomNumber(lastNameArray.size()));
		}
		
		public List<String> getFirstNames() 
		{
			return firstNameArray;
		}

		public void setFirstNames(List<String> firstNameArray) 
		{
			this.firstNameArray = firstNameArray;
		}

		public List<String> getLastNames() 
		{
			return lastNameArray;
		}

		public void setLastNames(List<String> lastNameArray) 
		{
			this.lastNameArray = lastNameArray;
		}

		public List<String> getDeptNames() {
			return deptNameArray;
		}

		public void setDeptNames(List<String> deptNameArray) {
			this.deptNameArray = deptNameArray;
		}
}
