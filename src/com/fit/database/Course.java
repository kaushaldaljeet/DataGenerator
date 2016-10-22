package com.fit.database;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Course extends Table 
{

	List<String> courseTitleArray;
	static List<Integer> courseIds;
	
	BufferedWriter writter;
	File courseIdFile;
	
	public Course(ThreadGroup group) 
	{
		super(group, "Course");
		courseIds = new ArrayList<Integer>();
		
		try
		{
			File courseTitleFile = new File("resources/courseTitle.txt");
			courseTitleArray = FileUtils.readLines(courseTitleFile , StandardCharsets.UTF_8);
			
			File courseIdFile = new File("resources/tables/courseIds.txt");
			if (!courseIdFile.exists())
				courseIdFile.createNewFile();
			
			FileWriter fw= new FileWriter(courseIdFile, true);
			writter = new BufferedWriter(fw);
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public static List<Integer> getCourseIds() 
	{
		return courseIds;
	}
	
	public List<String> getCourseTitles() 
	{
		return courseTitleArray;
	}

	public synchronized void  addCourseIds(int courseId)
	{
		try 
		{
			writter.write(courseId + "\n");
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() 
	{	
		try
		{
			ThreadGroup group = new ThreadGroup("CourseGroup");
			for (int i = 0; i < 10 ; i++) 
			{
				final int j=i;
				new Thread(group,() -> generateData(j)).start();
			}
			
			while(group.activeCount()>0)
			{
				Thread.sleep(1000);
			}
			bufferedWritter.close();
			writter.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	private void generateData(int max) 
	{
		int maxValue = (int)(getMinCount() * getScalingFactor() * 2.5);
		int courseId=(maxValue*max)+1;
		String deptName;
		String courseTitle;
		int courseSize=getCourseTitles().size();
		List<String> courseTitles = getCourseTitles();
		List<String> deptNames = getDeptNames();
		
		for (int i = 0; i < maxValue;i++)
		{
			addCourseIds(courseId++);
			deptName = deptNames.get(getRandomNumber(8));
			courseTitle= courseTitles.get(getRandomNumber(courseSize));
			//lines.add(courseId+","+courseTitle+","+deptName+","+getRandomNumber(1, 4));
			addRow(courseId,courseTitle,deptName,getRandomNumber(1,4));
			flushData(i);
		}
	}
}