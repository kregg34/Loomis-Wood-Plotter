package loomisWood;

import java.io.Serializable;

public class AssignedPoint implements Serializable
{
	private static final long serialVersionUID = -4076906133459995983L;
	
	private double waveNumber;
    private double intensity;
    private String intensityStr;
    private int jVal;

    
    public AssignedPoint(int jVal, double waveNumber, String intensity)
    {
    	this.waveNumber = waveNumber;
    	this.jVal = jVal;
    	intensityStr = intensity;
    	
    	convertIntensityToNumeric(intensity);
    }
    
    
    private void convertIntensityToNumeric(String intensity)
	{
		int index = FileInformation.convertValueToClosestIndex(waveNumber, SubBandContainer.getTolerance());
		
		//-1 indicates that the wave-number wasn't found in the spectrum values given
		if(index != -1)
		{
			this.intensity = FileInformation.getYValues().get(index);
		}else 
		{
			try
			{
				this.intensity = Double.parseDouble(intensity);
			}catch(NumberFormatException nfe) 
			{
				this.intensity = 0;
			}
		}
	}
    
    
    public void setIntensityValue(String intensity)
    {
    	convertIntensityToNumeric(intensity);
    }
    
    public void setIntensityValueStr(String intensity)
    {
    	intensityStr = intensity;
    }

    
	public void setWavenumberValue(double waveNumber)
    {
    	this.waveNumber = waveNumber;
    }
    
	
    public void setJValue(int jVal) 
    {
    	this.jVal = jVal;
    }
    
    
    public double getIntensity()  
    { 
    	return intensity;  
    }
    
    
    public String getIntensityStr()  
    { 
    	return intensityStr;  
    }
    
    
    public double getWavenumber()  
    { 
    	return waveNumber;  
    }
    
    
    public int getJValue()
    { 
    	return jVal;
    }
}
