package com.fit.database;

public class Teaches extends Table 
{	
	public Teaches(int minCount,float scalingFactor)  
	{
		super();
		setMinCount(minCount);
		setScalingFactor(scalingFactor);
	}

	static int maxValue=0;
	protected synchronized void incrementMaxValue()
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
		//Logic clubbed with Section
	}

	
}
