package com.fit.database;

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;


public class DataGenerator 
{
	static float scalingFactor;
	private static boolean loadData = false;
	static Scanner input;
	
	public static void main(String[] args) 
	{
		try
		{
			input = new Scanner(System.in);
			getInput();
			init();
			generateData(scalingFactor);
		}
		catch (Exception e) 
		{
			System.out.println("DataGenerator ==> main() ->" + e.getStackTrace());
		}
	}



	private static void getInput() 
	{
		initScalingFactor();
		initDatabaseManager();
	}



	private static void initDatabaseManager() 
	{
		System.out.print("\nWould you like to simultaneously load data in Database (y/n) : ");
		String answer = input.next();

		if(answer.toLowerCase().equals("y"))
		{
			loadData=true;
			if(DatabaseManager.init())
			{
				System.out.println("Connection Established..!!");
				createDatabaseAndTables();
			}
			else
			{
				System.out.println("Connection cannot be established. Check connection details in DatabaseManager");	
				loadData=false;
			}
		}
	}



	private static void initScalingFactor()
	{
		System.out.print("Please enter SF (Scaling Factor) > 0 :");
		scalingFactor = input.nextFloat();
		try
		{
			if(scalingFactor<=0)
			{
				initScalingFactor();
			}
		}
		catch (InputMismatchException e) 
		{
			System.out.println("Please enter only numbers :) \n");
			initScalingFactor();
		}
	}

	
	
	private static void init() 
	{
		String path = System.getProperty("user.dir");
		try
		{
			FileUtils.cleanDirectory(new File(path+"/resources/tables/"));
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}



	private static void generateData(float scalingFactor) 
	{
		try 
		{
			printDataDetails();
			
			long startTime = System.nanoTime();
			startGeneration();
			long endTime = System.nanoTime();
			long duration = (endTime - startTime)/1000000000;  //divide by 1000000 to get milliseconds.
			System.out.println("\nTotal Time = " + duration +" sec");
			
			cleanUp();
			printDataDetails();
			
			loadDataInDatabase(scalingFactor);
			
		} 
		catch (Exception e) 
		{
			System.out.println("DataGenerator => generateData -> " + e );
		}
	}



	private static void loadDataInDatabase(float scalingFactor) 
	{
		if(!loadData)
		{
			System.out.print("Would you like to load data in Database (y/n) : ");
			String answer = input.next();
			if(answer.toLowerCase().equals("y"))
			{
				loadData=true;
				createDatabaseAndTables();
				generateData(scalingFactor);
			}
		}
	}

	private static void printDataDetails() 
	{
		File file = new File("resources/tables");
		System.out.println("\nTotal size of data generated in files=" + Utils.getInstance().getFolderSizeInMB(file) + " MB");
		if(loadData)
			System.out.println("Total size of data in database=" + Utils.getInstance().getDatabaseSizeInMB("UNIVERSITY") + " MB\n");
	}
	
	private static void cleanUp() 
	{
		try
		{
			File studentIds = new File("resources/tables/studentIds.txt");
			studentIds.delete();
			
			File courseIds = new File("resources/tables/courseIds.txt");
			courseIds.delete();
		}
		catch (Exception e) 
		{
			System.out.println("DataGenerator => cleanUp -> " + e );
		}
	}
	
	private static void startGeneration()
	{
		try
		{
			TimeSlot timeslot= new TimeSlot();
			timeslot.start();
			timeslot.waitForComplete(1000);
			new Thread(() -> startTimeSlotLoading(timeslot)).start();
			
			Department dept = new Department();
			dept.start();
			dept.join(1000);
			
		
			new Thread(() -> startDepartmentLoading(dept)).start();
			Thread thread = new Thread(() -> generateSecondGroup());
			thread.start();
			thread.join();
			while(thread.isAlive())
			{
				Thread.sleep(1000);
			}
		}
		catch (Exception e) 
		{
			System.out.println("DataGenerator ==> startGeneration() -> " + e);
		}
	}

	private static void startTimeSlotLoading(TimeSlot timeslot)
	{
		new DataLoader(timeslot).start();
	}



	private static void startDepartmentLoading(Table thread) 
	{
		new DataLoader(thread).start();
	}

	private static void generateSecondGroup() 
	{
		try
		{
			ThreadGroup secondGenerateGroup = new ThreadGroup("secondGenerateGroup");
			ThreadGroup secondDataLoadGroup = new ThreadGroup("secondDataLoadGroup");
			
			Student student =  new Student(secondGenerateGroup,250,scalingFactor);
			student.start();
			DataLoader studentDataLoader= new DataLoader(secondDataLoadGroup,student);
			studentDataLoader.start();
			
			
			Classroom classroom =  new Classroom(secondGenerateGroup);
			classroom.start();
			DataLoader classroomDataLoader = new DataLoader(secondDataLoadGroup,classroom);
			classroomDataLoader.start();
			
			
			Course course =  new Course(secondGenerateGroup,20,scalingFactor);
			course.start();
			DataLoader courseDataLoader = new DataLoader(secondDataLoadGroup,course);
			courseDataLoader.start();
			
			
			Instructor instructor = new Instructor(secondGenerateGroup,100,scalingFactor);
			instructor.start();
			DataLoader instructorDataLoader = new DataLoader(secondDataLoadGroup,instructor);
			instructorDataLoader.start();
			
			ThreadGroup independentGenerateGroup = new ThreadGroup("independentGenerateGroup");
			
			new Thread(independentGenerateGroup,() -> generateSection(student,studentDataLoader,classroom)).start();
			new Thread(independentGenerateGroup,() -> generateAdvisor(student, instructor,studentDataLoader,instructorDataLoader)).start();
			new Thread(independentGenerateGroup,() -> generatePrerequisite(course,courseDataLoader)).start();
		
			
			while(secondGenerateGroup.activeCount()>0 || secondDataLoadGroup.activeCount()>0 || independentGenerateGroup.activeCount()>0)
			{
					Thread.sleep(1000);
			}
		}
		catch (Exception e) 
		{
			
		}
	}

	private static void generateSection(Student student, DataLoader studentDataLoader,Classroom classroom) 
	{
		try
		{
			ThreadGroup nextBatch = new ThreadGroup("nextBatch");
			student.waitForComplete(1000);
			classroom.waitForComplete(1000);
			Section section = new Section(100,scalingFactor);
			section.start(); 
			section.waitForComplete(1000);
			
			DataLoader sectionDataLoader = new DataLoader(section);
			
			new Thread(nextBatch,() -> generateTakes(section,sectionDataLoader,studentDataLoader)).start();
			
			
			sectionDataLoader.start();
			sectionDataLoader.waitForComplete();

			//new Thread(nextBatch,() -> generateTeaches(sectionDataLoader)).start();
			
			while(nextBatch.activeCount()>0)
			{
				Thread.sleep(1000);
			}
		}
		catch (Exception e) 
		{
			System.out.println("DataGenerator => generateSection -> " + e );
		}
	}
	
	/*private static void generateTeaches(Thread sectionDataLoader) 
	{
		try
		{
			Teaches teaches= new Teaches(1000, scalingFactor);
			teaches.start();
			teaches.waitForComplete(1000);
			DataLoader teachesDataLoader = new DataLoader(teaches);
			teachesDataLoader.start();
			teachesDataLoader.waitForComplete();
		}
		catch (Exception e) 
		{
			System.out.println("DataGenerator => generateTeaches -> " + e );
		}
	}*/

	private static void generateTakes(Section section,DataLoader sectionDataLoader,DataLoader studentDataLoader)
	{
		try
		{
			section.waitForComplete(1000);
			Takes takes = new Takes(250,scalingFactor);
			takes.start();
			takes.waitForComplete(1000);
			studentDataLoader.waitForComplete();
			sectionDataLoader.waitForComplete();
			DataLoader takesDataLoader = new DataLoader(takes);
			takesDataLoader.start();
			takesDataLoader.waitForComplete();
		}
		catch (Exception e) 
		{
			System.out.println("DataGenerator => generateTakes -> " + e );
		}
	}
	
	private static void generatePrerequisite(Course course, DataLoader courseDataLoader)
	{
		try
		{
			course.waitForComplete(1000);
			Prerequisite pre = new Prerequisite(100,scalingFactor);
			pre.start(); 
			courseDataLoader.waitForComplete();
			DataLoader prerequisiteDataLoader = new DataLoader(pre);
			prerequisiteDataLoader.start();
			prerequisiteDataLoader.waitForComplete();
		}
		catch (Exception e) 
		{
			
		}
	}
	
	private static void generateAdvisor(Student student, Instructor instructor, DataLoader studentDataLoader, DataLoader instructorDataLoader) 
	{
		try
		{
			student.waitForComplete(1000);
			instructor.waitForComplete(1000);
			Advisor advisor = new Advisor(250,scalingFactor);
			advisor.start();
			advisor.waitForComplete(1000);
			studentDataLoader.waitForComplete();
			instructorDataLoader.waitForComplete();
			DataLoader advisorDataLoader = new DataLoader(advisor);
			advisorDataLoader.start();
			advisorDataLoader.waitForComplete();
		}
		catch (Exception e) 
		{
			System.out.println("DataGenerator => generateAdvisor -> " + e );
		}	
	}
	
	private static void createDatabaseAndTables()
	{
		try
		{
			Process p = Runtime.getRuntime().exec("./resources/createDatabase.sh");
			p.waitFor();
		}
		catch (Exception e) 
		{
			System.out.println(e);
		}
	}



	public static boolean isLoadData() 
	{
		return loadData;
	}
	
}
