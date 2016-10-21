package com.fit.database;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Course extends Table 
{

	List<String> courseTitleArray;
	static List<Integer> courseIds;
	
	public Course(ThreadGroup group) 
	{
		super(group, "Course");
		courseIds = new ArrayList<Integer>();
		
		try
		{
			File courseTitleFile = new File("resources/courseTitle.txt");
			courseTitleArray = FileUtils.readLines(courseTitleFile , StandardCharsets.UTF_8);
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
		courseIds.add(courseId);
	}
	
	@Override
	public void run() 
	{
		for (int i = 0; i < 5 ; i++) 
		{
			final int j = i;
			new Thread(() -> generateData(j)).start();
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
		List<String> lines=  new ArrayList<String>();
		
		for (int i = 0; i < maxValue;i++)
		{
			addCourseIds(courseId++);
			deptName = deptNames.get(getRandomNumber(8));
			courseTitle= courseTitles.get(getRandomNumber(courseSize));
			lines.add(courseId+","+courseTitle+","+deptName+","+getRandomNumber(1, 4));
		}
		writeToFile(lines);
	}
}