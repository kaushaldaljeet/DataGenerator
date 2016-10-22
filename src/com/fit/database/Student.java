package com.fit.database;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Student extends Table
{
		
		BufferedWriter writter;
		File studentIdFile;
		
		public Student(ThreadGroup group) 
		{
			super(group, "Student");
			try
			{
				File studentIdFile = new File("resources/tables/studentIds.txt");
				if (!studentIdFile.exists())
					studentIdFile.createNewFile();
				
				FileWriter fw= new FileWriter(studentIdFile, true);
				writter = new BufferedWriter(fw);
			}
			catch (Exception e) 
			{
				
			}
		}
		public synchronized void  addStudent(int id)
		{
			try
			{
				writter.write(id + "\n");
			}
			catch (Exception e) {
			}
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
				addRow(studentID,studentName,deptName,getRandomNumber(1,130));
				flushData(i);
			}
			try
			{
				writter.flush();
			}
			catch (Exception e) {}
			writeToFile(lines);
		}

		@Override
		public void run() 
		{
			try
			{
				ThreadGroup group = new ThreadGroup("StudentGroup");
				for (int i = 0; i < 10 ; i++) 
				{
					final int j=i;
					new Thread(group,() -> generateData(j)).start();
				}
				
				while(group.activeCount()>0)
				{
					Thread.sleep(1000);
				}
				printFileDetails();
				bufferedWritter.close();
				writter.close();
			}
			catch (Exception e) 
			{
				// TODO: handle exception
			}
		}
			
}
