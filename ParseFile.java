package loomisWood;

import java.util.ArrayList;

public class ParseFile extends Thread
{
	private static ArrayList<Double> listOfXPointsLow = new ArrayList<Double>();
	private static ArrayList<Double> listOfXPointsHigh = new ArrayList<Double>();
	private static ArrayList<Double> listOfYPointsLow = new ArrayList<Double>();
	private static ArrayList<Double> listOfYPointsHigh = new ArrayList<Double>();
	private static char[] chars;
	private int startIndex, endIndex;
	
	//'start' and 'end' define which part of the chars array will be parsed by this thread instance
	public ParseFile(String name, int startIndex, int endIndex) 
	{
		super(name);
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		clearArrays();
	}
	
	private void clearArrays()
	{
		listOfXPointsLow.clear();
		listOfXPointsHigh.clear();
		listOfYPointsLow.clear();
		listOfYPointsHigh.clear();
	}

	public void setCharArray(char[] charArr) 
	{
		chars = charArr;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run()
	{
    	String currentNumber = "";
		boolean isWaveNum;
    	double valueAsDouble;
    	ArrayList<Double> tempList = new ArrayList<Double>();
    	
    	if(chars[startIndex] == '\n') 
    	{
    		isWaveNum = false;
    	}else 
    	{
    		isWaveNum = true;
    	}
    	
  	    for(int i = startIndex; i < endIndex; i++)
        {
        	//Is a new number if true
        	if((chars[i] == ' ' || chars[i] == '\n') && !currentNumber.equals(""))
        	{
    			try 
    			{
    				valueAsDouble = Double.parseDouble(currentNumber);
    			}catch(NumberFormatException nfe)
    			{
    				currentNumber = "";
    				continue;
    			}
        		
    			tempList.add(valueAsDouble);
        		currentNumber = "";
        		continue;
        	}
        	
        	currentNumber += chars[i];
        }
  	    
		if(isWaveNum) 
		{
			if(getName().equals("ThreadA")) 
			{
    			listOfXPointsLow = (ArrayList<Double>) tempList.clone();
			}else 
			{
    			listOfXPointsHigh = (ArrayList<Double>) tempList.clone();
			}

		}else
		{
			if(getName().equals("ThreadC")) 
			{
				listOfYPointsLow = (ArrayList<Double>) tempList.clone();
			}else 
			{
				listOfYPointsHigh = (ArrayList<Double>) tempList.clone();
			}
		}
	}
	
	public static int findFirstValidIndex()
	{
		int firstValidIndex = 0;
    	String currentNumber = "";
    	
  	    for(int i = 0; i < chars.length; i++)
        {
        	//If true, then it is a new number
        	if((chars[i] == ' ' || chars[i] == '\n') && !currentNumber.equals(""))
        	{
    			try 
    			{
    				Double.parseDouble(currentNumber);
    			}catch(NumberFormatException nfe)
    			{
    				currentNumber = "";
    				continue;
    			}
        		
    			firstValidIndex = i;
        		break;
        	}
        	
        	currentNumber += chars[i];
        }
		
		return firstValidIndex;
	}
	
	public static ArrayList<Double> getXPoints()
	{
		listOfXPointsLow.addAll(listOfXPointsHigh);
		return listOfXPointsLow;
	}
	
	public static ArrayList<Double> getYPoints()
	{
		listOfYPointsLow.addAll(listOfYPointsHigh);
		return listOfYPointsLow;
	}

}
