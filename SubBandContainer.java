package loomisWood;

//controls branches, sub-bands and adds them to arrays. "Static class"
//Only one instance of the "subBand" field at any given time!

import java.io.Serializable;

import java.util.ArrayList;

public class SubBandContainer implements Serializable
{
	private static final long serialVersionUID = -8396382390868982823L;
	
	private static ArrayList<SubBand> subBandArray = new ArrayList<SubBand>();
	private static SubBand subBand = new SubBand();
	private static double tolerance;
	
	public static void setSubBandInfo(int lowerK, int upperK, int lowerVt, int upperVt, String note, String symmetryType) 
	{
		subBand.setLowerK(lowerK);
		subBand.setUpperK(upperK);
		subBand.setLowerVt(lowerVt);
		subBand.setUpperVt(upperVt);
		subBand.setNote(note);
		subBand.setSymmetry(symmetryType);
	}
	
	public static void addSubBand() 
	{
		subBandArray.add(subBand);
	}
	
	public static void addBranch(Branch branch) 
	{
		subBand.addBranch(branch);
	}
	
	public static void addBranchToExistingSubBand(int indexOfSubBand, Branch branchToAdd)
	{
		subBandArray.get(indexOfSubBand).addBranch(branchToAdd);
	}
	
	public static void deleteSubBandAt(int indexOfSubBand)
	{
		subBandArray.remove(indexOfSubBand);
	}
	
	public static void deleteBranchAt(int indexOfBranch, int indexOfSubBand)
	{
		subBandArray.get(indexOfSubBand).getBranches().remove(indexOfBranch);
	}
	
	public static void swapBranchAt(int indexOfSubBand, int indexOfBranch, Branch branch)
	{
		subBandArray.get(indexOfSubBand).getBranches().set(indexOfBranch, branch);
	}
	
	public static void setSubBandArray(ArrayList<SubBand> subBandArrayIn) 
	{
		subBandArray = subBandArrayIn;
	}
	
	public static void setTolerance(double toleranceVal) 
	{
		tolerance = toleranceVal;
	}
	
	public static double getTolerance() 
	{
		return tolerance;
	}
	
	public static int getNumBranches() 
	{
		return subBand.getNumBranches();
	}
	
	public static ArrayList<SubBand> getSubBandArray() 
	{
		return subBandArray;
	}
	
	public static void getNewSubBandInstance()
	{
		subBand = new SubBand();
	}
	
	public static void clearAssignments() 
	{
		subBandArray.clear();
		getNewSubBandInstance();
	}
	
	public static SubBand getItemAt(int index)
	{
		if(index < subBandArray.size())
		{
			return subBandArray.get(index);
		}else 
		{
			return null;
		}
	}
	
	public static ArrayList<String> getAssignmentInfo(double valToCheck)
	{
		ArrayList<String> assignmentInfo = new ArrayList<String>();
		
		for(SubBand band: subBandArray)
		{
			for(Branch branch: band.getBranches()) 
			{
				for(AssignedPoint point: branch.getBranchData())
				{
					if(Math.abs(point.getWavenumber() - valToCheck) <= tolerance)
					{
						assignmentInfo.add(branch.getType().replace("<", "&lt;") + "<br>J value: " + point.getJValue());
						assignmentInfo.add(band.toStringHTML());
						return assignmentInfo;
					}
				}
			}
		}
		
		return assignmentInfo;
	}
	
	public static String getAssignmentInfoStr(double valToCheck)
	{
		String assignmentInfo = "";
		
		for(SubBand band: subBandArray)
		{
			for(Branch branch: band.getBranches()) 
			{
				for(AssignedPoint point: branch.getBranchData())
				{
					if(Math.abs(point.getWavenumber() - valToCheck) <= tolerance)
					{
						assignmentInfo += branch.getType() + "(" + point.getJValue()
						+ ") " + band.getUpperK() + "-" + band.getLowerK() + band.getSymmetry() 
						+ " vt=" + band.getUpperVt() + "-" + band.getLowerVt();
					}
				}
			}
		}
		
		return assignmentInfo;
	}
	
	public static boolean checkIfValueIsAssigned(double valToCheck)
	{
		for(SubBand band: subBandArray)
		{
			for(Branch branch: band.getBranches()) 
			{
				for(AssignedPoint tuple: branch.getBranchData())
				{
					if(Math.abs(tuple.getWavenumber() - valToCheck) <= tolerance)
					{
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	
	public static ArrayList<AssignedPoint> getAssignedPoints(double xAxisLow, double xAxisHigh)
	{
		ArrayList<AssignedPoint> points = new ArrayList<AssignedPoint>();
		
		for(SubBand band: subBandArray)
		{
			for(Branch branch: band.getBranches()) 
			{
				for(AssignedPoint point: branch.getBranchData())
				{
					double waveNum = point.getWavenumber();
					if(waveNum >= xAxisLow && waveNum <= xAxisHigh)
					{
						points.add(point);
					}
				}
			}
		}
		
		return points;
	}
	
	//Used for testing
	public static void printAllData()
	{
		System.out.println("\nPrinting data in SubBandContainer:");
		
		for(SubBand band: subBandArray)
		{
			System.out.println("\n-----------NEW ASSIGNMENT--------------\n");
			for(Branch branch: band.getBranches()) 
			{
				System.out.println("NEW BRANCH... (" + branch.getType() + ")");
				for(AssignedPoint tuple: branch.getBranchData())
				{
					System.out.println(tuple.getWavenumber() + " Intensity: " + tuple.getIntensity());
				}
				System.out.println();
			}
		}
	}
}
