package loomisWood;

import java.io.Serializable;

public class Tuple implements Serializable
{
	private static final long serialVersionUID = -4076906133459995983L;
	
	private Double waveNumber;
    private int jVal;

    public Tuple(int jVal, Double waveNumber)
    {
    	this.waveNumber = waveNumber;
    	this.jVal = jVal;
    }
    
    public void setWavenumberValue(double waveNumber)
    {
    	this.waveNumber = waveNumber;
    }
    
    public void setJValue(int jVal) 
    {
    	this.jVal = jVal;
    }
    
    public Double getWavenumber()  
    { 
    	return waveNumber;  
    }
    
    public int getJValue()
    { 
    	return jVal;
    }
}
