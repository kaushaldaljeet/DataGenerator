package com.fit.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.FileUtils;


public class DataGenerator 
{
	static float scalingFactor;
	private static boolean loadData = false;
	private static boolean databaseException = false;
	static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

	public static void main(String[] args) 
	{
		try
		{
			init();
			getInput();
			generateData(scalingFactor);
		}
		catch (Exception e) 
		{
			System.out.println("DataGenerator ==> main() ->");
			e.printStackTrace();
		}
	}



	private static void getInput() throws Exception
	{
		initScalingFactor();
		initDatabaseManager();
	}



	private static void initDatabaseManager() throws IOException 
	{
		System.out.print("\nWould you like to simultaneously load data in Database (y/n) : ");
		String answer = input.readLine();

		if(answer.toLowerCase().equals("y"))
		{
			loadData=true;
			createDatabaseAndTables();
			if(DatabaseManager.init())
			{
				System.out.println("Connection Established..!!");
			}
			else
			{
				System.out.println("Connection cannot be established. Check connection details in DatabaseManager");
				databaseException=true;
				loadData=false;
			}
		}
	}



	private static void initScalingFactor() throws Exception
	{
		try
		{
			System.out.print("Please enter SF (Scaling Factor) > 0 :");
			scalingFactor = Float.parseFloat(input.readLine());
			if(scalingFactor<=0)
			{
				initScalingFactor();
			}
		}
		catch (Exception e) 
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
		catch (Exception e) 
		{
			//ne.printStackTrace();
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



	private static void loadDataInDatabase(float scalingFactor) throws Exception
	{
		if(!databaseException && !loadData)
		{
			System.out.print("Would you like to load data in Database (y/n) : ");
			String answer = input.readLine();
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
		if(!file.exists())
		{
			file.mkdirs();
		}
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
			waitForComplete(timeslot);
			new DataLoader(timeslot).start();
			
			Department dept = new Department();
			dept.start();
			waitForComplete(dept);
			new DataLoader(dept).start();
			
			generateSecondGroup();
		}
		catch (Exception e) 
		{
			System.out.println("DataGenerator ==> startGeneration() -> " + e);
		}
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
			waitForComplete(student);
			waitForComplete(classroom);
			Section section = new Section(100,scalingFactor);
			section.start(); 
			waitForComplete(section);
			
			DataLoader sectionDataLoader = new DataLoader(section);
			
			new Thread(nextBatch,() -> generateTakes(section,sectionDataLoader,studentDataLoader)).start();
			
			
			sectionDataLoader.start();
			waitForComplete(sectionDataLoader);

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
			waitForComplete(section);
			Takes takes = new Takes(250,scalingFactor);
			takes.start();
			waitForComplete(takes);
			waitForComplete(studentDataLoader);
			waitForComplete(sectionDataLoader);
			DataLoader takesDataLoader = new DataLoader(takes);
			takesDataLoader.start();
			waitForComplete(takesDataLoader);
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
			waitForComplete(course);
			Prerequisite pre = new Prerequisite(100,scalingFactor);
			pre.start(); 
			waitForComplete(courseDataLoader);
			DataLoader prerequisiteDataLoader = new DataLoader(pre);
			prerequisiteDataLoader.start();
			waitForComplete(prerequisiteDataLoader);
		}
		catch (Exception e) 
		{
			
		}
	}
	
	private static void generateAdvisor(Student student, Instructor instructor, DataLoader studentDataLoader, DataLoader instructorDataLoader) 
	{
		try
		{
			waitForComplete(student);
			waitForComplete(instructor);
			Advisor advisor = new Advisor(250,scalingFactor);
			advisor.start();
			waitForComplete(advisor);
			waitForComplete(studentDataLoader);
			waitForComplete(instructorDataLoader);
			DataLoader advisorDataLoader = new DataLoader(advisor);
			advisorDataLoader.start();
			waitForComplete(advisorDataLoader);
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
			String operatingSystem = System.getProperty("os.name").toLowerCase();
			if(operatingSystem.contains("windows"))
			{
				Process p = Runtime.getRuntime().exec("cmd /c resources\\createDatabase.bat");
				p.waitFor();				
			}
			else
			{
				Process p = Runtime.getRuntime().exec("./resources/createDatabase.sh");
				p.waitFor();
				System.out.println("Please load University.sql to MySQL from resources folder and  Press Enter");
				input.readLine();
			}
		}
		catch (Exception e) 
		{
			System.out.println(e);
		}
	}


	private static void waitForComplete(Thread thread) throws Exception
	{
		while(thread.isAlive())
		{
			Thread.sleep(1000);
		}
	}
	

	public static boolean isLoadData() 
	{
		return loadData;
	}
	
}
