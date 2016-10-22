package com.fit.database;

import java.util.InputMismatchException;
import java.util.Scanner;

public class DataGenerator 
{
	public static void main(String[] args) 
	{
		try
		{
			
			System.out.println("Please enter SF (Scaaling Factor) > 0 :");
			
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
			dept.init();
			dept.start();
			
			while(dept.isAlive())
			{
				Thread.sleep(100);
			}
			
			ThreadGroup group = new ThreadGroup("2");
			
			Student student =  new Student(group);
			student.init();
			student.setMinCount(250);
			student.setScalingFactor(scalingFactor);
			student.start();
			
			Classroom classroom =  new Classroom(group);
			classroom.init();
			classroom.start();
			
			Course course =  new Course(group);
			course.init();
			course.setScalingFactor(scalingFactor);
			course.setMinCount(20);
			course.start();
			
			Instructor instructor = new Instructor(group);
			instructor.init();
			instructor.setScalingFactor(scalingFactor);
			instructor.start();
			
			while(group.activeCount()>0)
			{
				Thread.sleep(1000);
			}
			
			
			Advisor advisor = new Advisor();
			advisor.init();
			advisor.setScalingFactor(scalingFactor);
			advisor.setMinCount(250);
			advisor.start();
			
			Prerequisite pre = new Prerequisite();
			pre.setMinCount(100);
			pre.init();
			pre.setScalingFactor(scalingFactor);
			pre.start();
			
			Section section = new Section(100);
			section.init();
			section.setScalingFactor(scalingFactor);
			section.start();
			
			while(section.isAlive() || advisor.isAlive())
			{	
				Thread.sleep(1000);
			}
			
			Teaches teaches = new Teaches();
			teaches.setMinCount(100);
			teaches.init();
			teaches.setScalingFactor(scalingFactor);
			teaches.start();
			
			Takes takes = new Takes();
			takes.init();
			takes.setMinCount(250);
			takes.setScalingFactor(scalingFactor);
			takes.start();
			
			while(teaches.isAlive() || takes.isAlive())
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
