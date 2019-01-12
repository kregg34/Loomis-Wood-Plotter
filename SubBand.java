package loomisWood;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class SubBand implements Serializable
{
	private static final long serialVersionUID = 4426371225681488472L;
	
	private ArrayList<Branch> branchArray = new ArrayList<Branch>();;
	private int upperK, lowerK, upperVt, lowerVt;
	private String note;
	
	public SubBand(int lowerK, int upperK, int lowerVt, int upperVt, String note)
	{
		this.lowerK   = lowerK;
		this.upperK   = upperK;
		this.lowerVt  = lowerVt;
		this.upperVt  = upperVt;
		this.note     = note;
	}
	
	public SubBand()
	{
		this.lowerK   = -100;
		this.upperK   = -100;
		this.lowerVt  = -100;
		this.upperVt  = -100;
		this.note     = "";
	}
	
	public ArrayList<Branch> getBranches()
	{
		return branchArray;
	}
	
	public int getNumBranches() 
	{
		return branchArray.size();
	}
	
	public Branch getBranch(String type)
	{
		for(Branch branch : branchArray) 
		{
			if(branch.getType().equals(type)) 
			{
				return branch;
			}
		}
		
		return null;
	}
	
	public Branch getBranchAt(int index)
	{
		if(index < branchArray.size())
		{
			return branchArray.get(index);
		}else
		{
			return null;
		}
	}
	
	public Branch getNewestBranch()
	{
		if(branchArray.size() != 0)
		{
			return branchArray.get(branchArray.size() - 1);
		}else 
		{
			return null;
		}
	}
	
	public void addBranch(Branch branch) 
	{
		branchArray.add(branch);
	}

	public String toStringHTML() 
	{
		String arrow = "-";
		try 
		{
			arrow = new String(((char) 8592 + "").getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
		return "K values: " + upperK + arrow + lowerK + "<br>Torsional values: "
			+ upperVt + arrow + lowerVt + "<br>Additional Note: " + note;
	}
	
	public String toString() 
	{
		String arrow = "-";
		try
		{
			arrow = new String(((char) 8592 + "").getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
		return "K values: " + upperK + arrow + lowerK + "\nTorsional values: "
			+ upperVt + arrow + lowerVt + "\nAdditional Note: " + note;
	}
	
	public String getNote() 
	{
		return note;
	}
	
	public void setNote(String note) 
	{
		this.note = note;
	}
	
	public int getUpperK()
	{
		return upperK;
	}

	public void setUpperK(int upperK)
	{
		this.upperK = upperK;
	}

	public int getLowerK()
	{
		return lowerK;
	}

	public void setLowerK(int lowerK)
	{
		this.lowerK = lowerK;
	}

	public int getUpperVt()
	{
		return upperVt;
	}

	public void setUpperVt(int upperVt)
	{
		this.upperVt = upperVt;
	}

	public int getLowerVt()
	{
		return lowerVt;
	}

	public void setLowerVt(int lowerVt)
	{
		this.lowerVt = lowerVt;
	}
}
