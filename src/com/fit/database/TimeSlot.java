package com.fit.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimeSlot extends Table
{
	public static boolean generationCompleted = false;
	
	public TimeSlot() 
	{
		super();
	}
	static int maxValue=0;
	
	protected void incrementMaxValue()
	{
		maxValue++;
	}
	protected static int getMaxValue()
	{
		return maxValue;
	}
	
	@Override
	public void generateData() 
	{
		if(generationCompleted)
			return;
		
		String days;
		int startTime;
		
		for (int i = 1; i <= 17; i++) 
		{
			incrementMaxValue();
			days = getRandomDays();
			startTime=Utils.getInstance().getRandomNumber(8, 18);
			//Sample Row : 1,MWF,10,11
			addRow(i,days,startTime,startTime+1);
		}
		writeToFile();
		generationCompleted=true;
	}
	private String getRandomDays() 
	{
		String charDays[] = new String[]{"M","T","W","R","F"};
		List<Integer> selectedIndexs=new ArrayList<Integer>();
		
		String days="";
		int randomNum=0;
		
		int maxDays = Utils.getInstance().getRandomNumber(1,5);
		for (int i = 0; i < maxDays;) 
		{
			randomNum = Utils.getInstance().getRandomNumber(4);
			if(!selectedIndexs.contains(randomNum))
			{
				selectedIndexs.add(randomNum);
				i++;
			}
		}
		Collections.sort(selectedIndexs);
		for (int i = 0; i < selectedIndexs.size(); i++) 
		{
			days+=charDays[selectedIndexs.get(i)];
		}
		return days;
	}

}
