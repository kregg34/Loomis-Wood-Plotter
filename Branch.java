package loomisWood;

import java.io.Serializable;
import java.util.ArrayList;

public class Branch implements Serializable
{
	private static final long serialVersionUID = -7371108724513933096L;
	
	private String type;     			//eg. Q, P, R, Q+<- etc...
	private ArrayList<AssignedPoint> branch;
	
	public Branch(String type, ArrayList<AssignedPoint> branch)
	{
		this.type   = type;
		this.branch = branch;
	}
	
	public ArrayList<AssignedPoint> getBranchData()
	{
		return branch;
	}
	
	public String getType()
	{
		return type;
	}
}
