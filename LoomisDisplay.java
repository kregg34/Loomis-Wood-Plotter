package loomisWood;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

public class LoomisDisplay
{
	private final ArrayList<Double> X_VALUES, Y_VALUES;
	
	private static double lowIntensityValue, highIntensityValue;
	private static boolean rendererState = false;
	private static XYItemRenderer basicRenderer;
	
	public LoomisDisplay(FileInformation fi) 
	{
		X_VALUES = fi.getXValues();
		Y_VALUES = fi.getYValues();
		
		//Set the initial values
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
	
	public int getLastIndex()
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
	
	//Binary search for a close value, returns -1 if nothing was found
	public int convertValueToClosestIndex(double valueToFind, double tolerance) 
	{
		final double LOG_BASE2  = Math.log(2);
		int minIndex            = 0;
		int maxIndex            = X_VALUES.size() - 1;
		
		//binary search
		for(int i = 0; i <= (Math.log(X_VALUES.size()) / LOG_BASE2) ; i++)
		{
			int currentIndex = (minIndex + maxIndex) / 2;
			
			//if true, found a value within the tolerance range
			if(Math.abs(X_VALUES.get(currentIndex) - valueToFind) <= tolerance)
			{
				return currentIndex;
			}
			
			//update the search range
			if(X_VALUES.get(currentIndex) < valueToFind) 
			{
				minIndex = currentIndex + 1; 
			}else
			{
				maxIndex = currentIndex - 1;
			}
		}
		
		return -1;
	}
	
	public void updatePlot(JFreeChart chart, int startIndex, int endIndex, double width)
	{
		XYSeries loomisChart = new XYSeries("(Wavenumber, Intensity)");
		double xAxisLow, xAxisHigh;
		int loopStart, loopEnd;
		
		if(startIndex == -1 && endIndex == -1) 
		{
			xAxisLow  = 0;
			xAxisHigh = 1;
		}else 
		{
			if(startIndex == -1 && endIndex != -1)
			{
				xAxisLow  = X_VALUES.get(endIndex) - width;
				xAxisHigh = X_VALUES.get(endIndex);
				loopStart = 0;
				loopEnd   = endIndex;
			}else 
			{
				if(startIndex != -1 && endIndex == -1)
				{
					xAxisLow  = X_VALUES.get(startIndex);
					xAxisHigh = X_VALUES.get(startIndex) + width;
					loopStart = startIndex;
					loopEnd   = getLastIndex();
				}else 
				{
					xAxisLow  = X_VALUES.get(startIndex);
					xAxisHigh = X_VALUES.get(endIndex);
					loopStart = startIndex;
					loopEnd   = endIndex;
				}
			}
			
			for(int i = loopStart; i <= loopEnd; i++)
			{
				loomisChart.add(X_VALUES.get(i), Y_VALUES.get(i));
			}
		}
		
		XYDataset xyDataset = new XYSeriesCollection(loomisChart);
		chart.getXYPlot().setDataset(0, xyDataset);
		
		setWavenumberRange(chart, xAxisLow, xAxisHigh);
		setIntensityRange(chart);
	}
	
	public static void setIntensityRange(JFreeChart chart) 
	{
		ValueAxis rangeAxis = chart.getXYPlot().getRangeAxis();
		rangeAxis.setRange(lowIntensityValue, highIntensityValue);
	}
	
	public static void setWavenumberRange(JFreeChart chart, double xAxisLow, double xAxisHigh) 
	{
		ValueAxis domainAxis = chart.getXYPlot().getDomainAxis();
		double xAxisOffset = (xAxisHigh - xAxisLow) / 200.0;
		domainAxis.setRange(xAxisLow - xAxisOffset, xAxisHigh + xAxisOffset);
	}
	
	public static double getXAxisLowerBound(JFreeChart chart) throws IndexOutOfBoundsException
	{
		System.out.println(chart.getXYPlot().getDataset().getXValue(0, 0));
		return chart.getXYPlot().getDataset().getXValue(0, 0);
	}
	
	public static void clearPlot(JFreeChart chart)
	{
		XYSeries loomisChart = new XYSeries("Test"); 
		XYDataset xyDataset = new XYSeriesCollection(loomisChart);
		chart.getXYPlot().setDataset(0, xyDataset);
	}
	
	public static ChartPanel getEmptyPlot()
	{
		XYSeries loomisChart = new XYSeries("Empty");
		XYDataset xyDataset = new XYSeriesCollection(loomisChart);
		JFreeChart chart = ChartFactory.createXYLineChart("", "", "", xyDataset, PlotOrientation.VERTICAL, false, true, false);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(100, 100));
		setupPlotRenderer(chart);
		
		return chartPanel;
	}
	
	public static void setupPlotRenderer(JFreeChart chart) 
	{
		XYPlot xyPlot = chart.getXYPlot();
		XYItemRenderer renderer = xyPlot.getRenderer();
		xyPlot.getRenderer().setSeriesPaint(0, Color.BLACK);
		xyPlot.getRangeAxis().setVisible(false);
		xyPlot.setDomainGridlinesVisible(false);
		xyPlot.setRangeGridlinesVisible(false);
		xyPlot.setBackgroundPaint(Color.WHITE);
		
		basicRenderer = renderer;
		basicPlotRenderer(chart);
	}
	
	public static void flipToggleFlag() 
	{
		rendererState = !rendererState;
	}
	
	public static void toggleRenderer(JFreeChart chart) 
	{
		if(rendererState) 
		{
			basicPlotRenderer(chart);
		}else
		{
			extraPlotRenderer(chart);
		}
	}
	
	public static void basicPlotRenderer(JFreeChart chart) 
	{
		XYPlot xyPlot = chart.getXYPlot();
		xyPlot.setRenderer(basicRenderer);
	}
	
	public static void extraPlotRenderer(JFreeChart chart) 
	{
		final int BRANCH_TYPE = 0;
		final int BAND_INFO = 1;
		
		chart.getXYPlot().setRenderer(new XYLineAndShapeRenderer(true, true)
		{
			private static final long serialVersionUID = -6537452689811616115L;
				
			@Override
	        public Shape getItemShape(int row, int col) 
	        {
				double xVal = chart.getXYPlot().getDataset().getXValue(row, col);
				boolean isAssigned = SubBandContainer.checkIfValueIsAssigned(xVal);

				if(isAssigned)
				{
					return ShapeUtilities.createDownTriangle(4);
							
				}else 
				{
					return ShapeUtilities.createDownTriangle(0);
				}
	        }
			
			@Override
	        public Paint getItemPaint(int row, int col)
			{
				double xVal = chart.getXYPlot().getDataset().getXValue(row, col);
				ArrayList<String> assignedType = SubBandContainer.getAssignmentInfo(xVal);

				if(assignedType.size() == 0)
				{
					return Color.BLACK;
							
				}else 
				{
					String branchInfo = assignedType.get(BRANCH_TYPE);
					
					if(branchInfo.contains("Q"))
					{
						return Color.BLUE;
					}else if(branchInfo.contains("R"))
					{
						return Color.RED;
					}else
					{
						return Color.MAGENTA;
					}
				}
	        }
	    });
		
		
		StandardXYToolTipGenerator tooltipGenerator = new StandardXYToolTipGenerator()
		{
			private static final long serialVersionUID = -230085220909109223L;

			@Override
		    public String generateToolTip(XYDataset dataset, int series, int item)
		    {
				double xVal = chart.getXYPlot().getDataset().getXValue(series, item);
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
		
		chart.getXYPlot().getRenderer().setBaseToolTipGenerator(tooltipGenerator);
	}
	
	public static boolean getToggleState() 
	{
		return rendererState;
	}
}
