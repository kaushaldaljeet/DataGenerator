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
		public Student(ThreadGroup group,int minCount,float scalingFactor) 
		{
			super(group, group.getName());
			setMinCount(minCount);
			setScalingFactor(scalingFactor);
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
				System.out.println("Student ==> Student() -> " + e);
			}
		}
		public synchronized void  addStudent(int id)
		{
			try
			{
				writter.write(id + "\n");
			}
			catch (Exception e) 
			{
				System.out.println("Student ==> addStudent --> writter.write() -> " + e);
			}
		}
		public void generateData(int max)
		{
			int maxValue = (int)(getMinCount() * getScalingFactor());
			
			int studentID = 9000000+(maxValue*max)+1;
			String studentName = "";
			String deptName = "";
			List<String> lines=  new ArrayList<String>();
			List<String> deptNames = Department.getDeptNames();
			int size = deptNames.size();
			
			for (int i = 0; i < maxValue; i++,studentID++)
			{
				addStudent(studentID);
				studentName = Utils.getInstance().getRandomName();
				deptName = deptNames.get(Utils.getInstance().getRandomNumber(size));
				addRow(studentID,studentName,deptName,Utils.getInstance().getRandomNumber(1,130));
				incrementMaxValue();
				flushData(i);
			}
			try
			{
				writter.flush();
			}
			catch (Exception e) 
			{
				System.out.println("Student ==> generateData --> writter.flush() -> " + e);
			}
			flush();
		}

		@Override
		public void run() 
		{
			if(generationCompleted)
				return;
			
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
				outputFileWritter.close();
				writter.close();
				generationCompleted=true;
			}
			catch (Exception e) 
			{
				System.out.println("Student ==> run() -> " + e);
			}
		}
		@Override
		public void generateData() 
		{
			
		}
}
