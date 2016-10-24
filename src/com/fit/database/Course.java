package com.fit.database;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

public class Course extends Table 
{

	List<String> courseTitles;
	static List<Integer> courseIds;
	public static boolean generationCompleted = false;
	static int maxValue=0;
	protected synchronized void incrementMaxValue()
	{
		maxValue++;
	}
	protected static int getMaxValue()
	{
		return maxValue;
	}
	public Course(ThreadGroup group,int minCount,float scalingFactor) 
	{
		super(group, "Course");
		setMinCount(minCount);
		setScalingFactor(scalingFactor);
		courseIds = new ArrayList<Integer>();
		
		try
		{
			File courseTitleFile = new File("resources/courseTitle.txt");
			courseTitles = FileUtils.readLines(courseTitleFile , StandardCharsets.UTF_8);
			
		}
		catch (Exception e) 
		{
			Logger.getGlobal().severe("Course ==> Course() -> " + e);
		}
	}

	public static List<Integer> getCourseIds() 
	{
		return courseIds;
	}

	public synchronized void addCourseIds(int courseId)
	{
		courseIds.add(courseId);
	}	
	
	@Override
	public void run() 
	{	
		if(generationCompleted)
			return;
		try
		{
			ThreadGroup group = new ThreadGroup("CourseGroup");
			for (int i = 0; i < 10 ; i++) 
			{
				final int j=i;
				new Thread(group,() -> generateData(j)).start();;
			}
			
			while(group.activeCount()>0)
			{
				Thread.sleep(1000);
			}
			printFileDetails();
			outputFileWritter.close();
			generationCompleted=true;
		}
		catch (Exception e) 
		{
			Logger.getGlobal().severe("Course ==> run() -> " + e);
		}
	}

	private void generateData(int max) 
	{
		int maxValue = (int)(getMinCount() * getScalingFactor() * 2.5);
		int courseId=(maxValue*max)+1;
		String deptName;
		String courseTitle;
		int courseSize=courseTitles.size();
		List<String> deptNames = Department.getDeptNames();
		
		for (int i = 0; i < maxValue;i++,courseId++)
		{
			addCourseIds(courseId);
			deptName = deptNames.get(Utils.getInstance().getRandomNumber(8));
			courseTitle= courseTitles.get(Utils.getInstance().getRandomNumber(courseSize));
			addRow(courseId,courseTitle,deptName,Utils.getInstance().getRandomNumber(1,4));
			incrementMaxValue();
			flushData(i);
		}
	}

	@Override
	public void generateData() 
	{
		// TODO Auto-generated method stub
	}
}