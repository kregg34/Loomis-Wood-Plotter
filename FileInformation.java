package loomisWood;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileInformation implements Serializable
{
	private static final long serialVersionUID = -9035919392539359550L;
	
	private final File FILE;
	
	private static ArrayList<Double> xValues = new ArrayList<Double>();
	private static ArrayList<Double> yValues = new ArrayList<Double>();

	private char[] charArray;
	
	public FileInformation(File file) 
	{
		FILE = file;
		charArray = getFileInformation();
		
		ParseFile temp = new ParseFile("Temp", -1, -1);
		temp.setCharArray(charArray);
		
		int indexOfFirstIntensityVal = findNextIndexPosOf(0, '\n');
		int indexOfMiddleOfWN = findNextIndexPosOf(indexOfFirstIntensityVal / 2, ' ');
		int indexOfMiddleOfIntensity = findNextIndexPosOf( ((charArray.length - indexOfFirstIntensityVal) / 2) + indexOfFirstIntensityVal, '\n');
		
		ParseFile t1 = new ParseFile("ThreadA", 0, indexOfMiddleOfWN + 1);
		t1.start();
		
		ParseFile t2 = new ParseFile("ThreadC", indexOfFirstIntensityVal, indexOfMiddleOfIntensity + 1);
		t2.start();
		
		ParseFile t3 = new ParseFile("ThreadB", indexOfMiddleOfWN, indexOfFirstIntensityVal + 1);
		t3.start();
		
		ParseFile t4 = new ParseFile("ThreadD", indexOfMiddleOfIntensity, charArray.length);
		t4.start();
		
		//main thread waits until other threads are done
		try 
		{
			t1.join();
			t2.join();
			t3.join();
			t4.join();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		xValues = ParseFile.getXPoints();
		yValues = ParseFile.getYPoints();
	}
	
	public double getLowestXValue() 
	{
		return xValues.get(0);
	}
	
	public double getHighestXValue()
	{
		return xValues.get(xValues.size() - 1);
	}
	
	public static ArrayList<Double> getXValues()
	{
		return xValues;
	}
	
	public static ArrayList<Double> getYValues()
	{
		return yValues;
	}
	
	//loop until the special char is found and return the index
	private int findNextIndexPosOf(int startIndex, char charToFind)
	{
		int index = startIndex;
		
		//used to skip the first few random characters
		int firstValidIndex = ParseFile.findFirstValidIndex();
		
		if(index < firstValidIndex)
		{
			index = firstValidIndex;
		}
		
		while(charArray[index] != charToFind)
		{
			index++;
		}
		
		return index;
	}

	//gets the contents of the file and returns it as an array of chars
	private char[] getFileInformation()
	{
		FileReader reader = null;
    	
		try 
		{
			reader = new FileReader(FILE);
			byte[] bytes;
        	
	    	bytes = Files.readAllBytes(Paths.get(FILE.getAbsolutePath()));
	    	String text = new String(bytes, "ASCII");
	    	char[] chars = text.toCharArray();
	    	
			reader.close();
			
			return chars;
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	//Binary search for a close value, returns -1 if nothing was found
	public static int convertValueToClosestIndex(double valueToFind, double tolerance) 
	{
		final double LOG_BASE2  = Math.log(2);
		int minIndex            = 0;
		int maxIndex            = xValues.size() - 1;
		
		//Binary search
		for(int i = 0; i <= (Math.log(xValues.size()) / LOG_BASE2) ; i++)
		{
			int currentIndex = (minIndex + maxIndex) / 2;
			
			//if true, found a value within the tolerance range
			if(Math.abs(xValues.get(currentIndex) - valueToFind) <= tolerance)
			{
				return currentIndex;
			}
			
			//update the search range
			if(xValues.get(currentIndex) < valueToFind) 
			{
				minIndex = currentIndex + 1; 
			}else
			{
				maxIndex = currentIndex - 1;
			}
		}
		
		return -1;
	}
}
