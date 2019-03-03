package loomisWood;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

public class LoomisDisplay
{
	//Contains all of the X and Y values for the spectrum
	private static ArrayList<Double> X_VALUES, Y_VALUES;
	
	private static double lowIntensityValue, highIntensityValue;
	private static boolean showAssignments = true;
	private static XYLineAndShapeRenderer assignmentRenderer, basicRenderer;
	private static final int BASIC_DATASET = 1, ASSIGNMENT_DATASET = 0;
	private static XYDataset xyDataSetBasic, xyDataSetAssigned;
	
	
	public LoomisDisplay() 
	{
		X_VALUES = FileInformation.getXValues();
		Y_VALUES = FileInformation.getYValues();
		
		//Sets the initial values
		lowIntensityValue  = -0.05;
		highIntensityValue = 0.5;
	}
	
	
	public static double getLowIntensityValue() 
	{
		return lowIntensityValue;
	}
	
	
	public static double getHighIntensityValue() 
	{
		return highIntensityValue;
	}
	
	
	public static int getLastIndex()
	{
		return X_VALUES.size() - 1;
	}
	
	
	public static void setLowIntensityValue(double val) 
	{
		lowIntensityValue = val;
	}
	
	
	public static void setHighIntensityValue(double val) 
	{
		highIntensityValue = val;
	}
	
	
	public static void setWavenumberRange(JFreeChart chart, double xAxisLow, double xAxisHigh) 
	{
		ValueAxis domainAxis = chart.getXYPlot().getDomainAxis();
		double xAxisOffset = (xAxisHigh - xAxisLow) / 200.0;
		domainAxis.setRange(xAxisLow - xAxisOffset, xAxisHigh + xAxisOffset);
	}
	
	
	public static void setIntensityRange(JFreeChart chart) 
	{
		ValueAxis rangeAxis = chart.getXYPlot().getRangeAxis();
		rangeAxis.setRange(lowIntensityValue, highIntensityValue);
	}
	
	
	public static double getXAxisLowerBound(JFreeChart chart) throws IndexOutOfBoundsException
	{
		return chart.getXYPlot().getDataset().getXValue(0, 0);
	}
	
	
	public static boolean getToggleState() 
	{
		return showAssignments;
	}
	
	
	public static void flipToggleFlag() 
	{
		showAssignments = !showAssignments;
	}
	
	
	public static ChartPanel getEmptyPlot()
	{
		XYSeries loomisChart = new XYSeries("Empty");
		XYDataset xyDataset = new XYSeriesCollection(loomisChart);
		JFreeChart chart = ChartFactory.createXYLineChart("", "", "", xyDataset, PlotOrientation.VERTICAL, false, true, false);
		ChartPanel chartPanel = new ChartPanel(chart);
		
		setupPlot(chart);
		
		return chartPanel;
	}
	
	
	private static void setupPlot(JFreeChart chart) 
	{
		XYPlot xyPlot = chart.getXYPlot();
		xyPlot.getRenderer().setSeriesPaint(BASIC_DATASET, Color.BLACK);
		xyPlot.getRangeAxis().setVisible(false);
		xyPlot.setDomainGridlinesVisible(false);
		xyPlot.setRangeGridlinesVisible(false);
		xyPlot.setBackgroundPaint(Color.WHITE);
		
		ValueAxis domainAxis = chart.getXYPlot().getDomainAxis();
		domainAxis.setTickLabelFont(new Font("Arial", Font.PLAIN, 11));
		domainAxis.setTickLabelPaint(Color.BLACK);
		
		setUpBasicRenderer();
		turnOnBasicRendererToolTip();
		xyPlot.setDatasetRenderingOrder(DatasetRenderingOrder.REVERSE);
		xyPlot.setRenderer(BASIC_DATASET, basicRenderer);
	}
	
	
	public static void setUpBasicRenderer() 
	{
		basicRenderer = new XYLineAndShapeRenderer(true, false) {
			
			private static final long serialVersionUID = 8691398375695769740L;
			
			@Override
	        public Paint getItemPaint(int row, int col)
			{
				return Color.BLACK;
			}
		};
	}
	
	
	private static void turnOffBasicRendererToolTip() 
	{
		basicRenderer.setBaseToolTipGenerator(null);
	}
	
	
	private static void turnOnBasicRendererToolTip() 
	{
		StandardXYToolTipGenerator tooltipGenerator = new StandardXYToolTipGenerator()
		{
			private static final long serialVersionUID = -230085220909109223L;

			@Override
		    public String generateToolTip(XYDataset dataset, int series, int item)
		    {
		        return "<html>Wavenumber: " + dataset.getXValue(series, item) 
		        + " cm^-1<br>Intensity: " + dataset.getYValue(series, item);
		    }
		};
	    
		basicRenderer.setBaseToolTipGenerator(tooltipGenerator);
	}
	
	
	private static void setUpAssignmentRenderer(JFreeChart chart) 
	{		
		final int BRANCH_TYPE = 0;
		final int BAND_INFO = 1;
		
		if(chart.getXYPlot().getDatasetCount() == 0)
		{
			return;
		}
		
		assignmentRenderer = new XYLineAndShapeRenderer(false, true)
		{
			private static final long serialVersionUID = -6537452689811616115L;
			
			@Override
	        public Shape getItemShape(int row, int col) 
	        {
				return ShapeUtilities.createDownTriangle(4);
	        }
			
			@Override
	        public Paint getItemPaint(int series, int item)
			{
				double xVal = chart.getXYPlot().getDataset().getXValue(series, item);
				ArrayList<String> assignedType = SubBandContainer.getAssignmentInfo(xVal);
				
				String branchInfo = assignedType.get(BRANCH_TYPE);
				
				if(branchInfo.charAt(0) == 'Q')
				{
					return Color.BLUE;
				}else if(branchInfo.charAt(0) == 'R')
				{
					return Color.RED;
				}else 
				{
					return Color.MAGENTA;
				}
	        }
	    };
	    
		StandardXYToolTipGenerator tooltipGenerator = new StandardXYToolTipGenerator()
		{
			private static final long serialVersionUID = -230085220909109223L;

			@Override
		    public String generateToolTip(XYDataset dataset, int series, int item)
		    {
				double xVal = chart.getXYPlot().getDataset(ASSIGNMENT_DATASET).getXValue(series, item);
				ArrayList<String> assignedType = SubBandContainer.getAssignmentInfo(xVal);
				String branchInfo = "";
				String bandInfo = "";
				
				if(assignedType.size() != 0)
				{
					branchInfo = assignedType.get(BRANCH_TYPE);
					bandInfo = assignedType.get(BAND_INFO);
				}
				
		        return "<html>Wavenumber: "
	            + dataset.getXValue(series, item) + " cm^-1<br>Intensity: "
	            + dataset.getYValue(series, item) + "<br>Branch Type: " + branchInfo
	            + "<br>" + bandInfo + "</html>";
		    }
		};
	    
	    assignmentRenderer.setBaseToolTipGenerator(tooltipGenerator);
	}
	
	
	public static void updatePlot(JFreeChart chart, int startIndex, int endIndex, double width)
	{
		XYSeries xySeries = new XYSeries("(Wavenumber, Intensity)");
		//Range of x axis values shown on the display (e.g. shows from 100 to 101 cm^-1)
		double xAxisLow, xAxisHigh;
		//Range of indexes of the X_VALUES array that will be added to the plot
		int loopStart, loopEnd;
		
		//Checks if the range of the display is out of the X_VALUES range 
		if(startIndex != -1 && endIndex != -1) 
		{
			//Nothing is out of limits -> typical case
			xAxisLow  = X_VALUES.get(startIndex);
			xAxisHigh = X_VALUES.get(endIndex);
			loopStart = startIndex;
			loopEnd   = endIndex;
		}else 
		{
			if(startIndex == -1 && endIndex != -1)
			{
				//Out of the limits to the left only
				xAxisLow  = X_VALUES.get(endIndex) - width;
				xAxisHigh = X_VALUES.get(endIndex);
				loopStart = 0;
				loopEnd   = endIndex;
			}else 
			{
				if(startIndex != -1 && endIndex == -1)
				{
					//Out of the limits to the right only
					xAxisLow  = X_VALUES.get(startIndex);
					xAxisHigh = X_VALUES.get(startIndex) + width;
					loopStart = startIndex;
					loopEnd   = getLastIndex();
				}else 
				{
					//Out of limits for both the right and left
					xAxisLow  = 0;
					xAxisHigh = 1;
					loopStart = 0;
					loopEnd   = 0;
				}
			}
		}
		
		//Adds the X and Y values to the display over the appropriate range
		for(int i = loopStart; i <= loopEnd; i++)
		{
			xySeries.add(X_VALUES.get(i), Y_VALUES.get(i));
		}
		
		//Adds the x-y values to the display as the first data set
		xyDataSetBasic = new XYSeriesCollection(xySeries);
		
		//Sets the ranges for the axes
		setWavenumberRange(chart, xAxisLow, xAxisHigh);
		setIntensityRange(chart);
		
		//Adds the assigned points to the display as the second data set
		addAssignmentsToSecondSeries(chart, xAxisLow, xAxisHigh);
		
		displayData(chart);
	}
	
	
	private static void addAssignmentsToSecondSeries(JFreeChart chart, double xAxisLow, double xAxisHigh)
	{
		XYSeries xyAssignedPoints = new XYSeries("Assigned Points");
		ArrayList<AssignedPoint> assignedPointsInRange = SubBandContainer.getAssignedPoints(xAxisLow, xAxisHigh);
		
		for(int i = 0; i < assignedPointsInRange.size(); i++)
		{
			double xVal = assignedPointsInRange.get(i).getWavenumber();
			double yVal = assignedPointsInRange.get(i).getIntensity();
			xyAssignedPoints.add(xVal, yVal);
		}
		
		setUpAssignmentRenderer(chart);
		xyDataSetAssigned = new XYSeriesCollection(xyAssignedPoints);
	}
	
	
	public static void displayData(JFreeChart chart) 
	{		
		if(showAssignments) 
		{
			turnOffBasicRendererToolTip();

			chart.getXYPlot().setDataset(ASSIGNMENT_DATASET, xyDataSetAssigned);
			chart.getXYPlot().setDataset(BASIC_DATASET, xyDataSetBasic);
			
			XYLineAndShapeRenderer [] rendererArray = {assignmentRenderer, basicRenderer};
			chart.getXYPlot().setRenderers(rendererArray);
		}else
		{
			turnOnBasicRendererToolTip();
			
			chart.getXYPlot().setDataset(ASSIGNMENT_DATASET, null);
			chart.getXYPlot().setDataset(BASIC_DATASET, xyDataSetBasic);
			
			XYLineAndShapeRenderer [] rendererArray = {assignmentRenderer, basicRenderer};
			chart.getXYPlot().setRenderers(rendererArray);
		}
	}
}
