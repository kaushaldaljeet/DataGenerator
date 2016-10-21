package com.fit.database;

import java.util.InputMismatchException;
import java.util.Scanner;

public class DataGenerator 
{
	static String SET_SCALING_FACTOR = "setScalingFactor";
	static String GENERATE_DATA =  "";
	
	public static void main(String[] args) 
	{
		try
		{
			
			System.out.println("Please enter SF (Scaaling Factor) > 0 :");
			System.out.println("SF 100 gives around 7 MB of Data for Student Table");
			
			Scanner input = new Scanner(System.in);
			
			float scalingFactor = input.nextFloat();
			
			if(scalingFactor<=0)
			{
				main(args);
			}
			generateData(scalingFactor);
			input.close();
		}
		catch (InputMismatchException e) 
		{
			System.out.println("Please enter only numbers :) \n");
			main(args);
		}
	}

	
	
	private static void generateData(float scalingFactor) 
	{
		try 
		{
			long startTime = System.nanoTime();
			
			Department dept = new Department();
			dept.start();
			
			while(dept.isAlive())
			{
				Thread.sleep(100);
			}
			
			ThreadGroup group = new ThreadGroup("2");
			
			Student student =  new Student(group);
			student.setMinCount(250);
			student.setScalingFactor(scalingFactor);
			student.start();
			
			Classroom classroom =  new Classroom(group);
			classroom.start();
			
			Course course =  new Course(group);
			course.setScalingFactor(scalingFactor);
			course.setMinCount(20);
			course.start();
			
			Instructor instructor = new Instructor(group);
			instructor.setScalingFactor(scalingFactor);
			instructor.start();
			
			while(group.activeCount()>0)
			{
				Thread.sleep(1000);
			}
			
			Advisor advisor = new Advisor();
			advisor.setScalingFactor(scalingFactor);
			advisor.start();
			
			Section section = new Section(100);
			section.setScalingFactor(scalingFactor);
			section.start();
			
			while(section.isAlive() || advisor.isAlive())
			{	
				Thread.sleep(1000);
			}

			long endTime = System.nanoTime();
			long duration = (endTime - startTime)/1000000000;  //divide by 1000000 to get milliseconds.
			
			System.out.println("\nTotal Time for Generation = " + duration +" sec");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

}
