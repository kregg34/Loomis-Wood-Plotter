/*
 * @author: Craig Beaman
 *   Email: Craig.Beaman@unb.ca
 */

package loomisWood;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

public class GUIForLoomisWood
{
	private static double startValue, widthValue, endValue;
	private static int numberOfParts = 1, currentPart = 1;
	
	private static JFrame frame;
	private static LoomisDisplay loomisObject;
	private static FileInformation fileInfo;
	private static GradientPanel loomisPanel;
	private static JPanel subLoomisPanel;
	private static GridBagConstraints gbc;
	private static JLabel infoHeader;
	
	//The i'th element holds the starting value for the i'th chart panel (used when splitting the diagram)
	private static ArrayList<Double> chartStartValues;
	private static ArrayList<ChartPanel> chartPanelArray;
	
	private final Image ICON;
	private static final double TOLERANCE = 0.00005;
	
	private JMenuBar menuBar;
	private JMenu fileMenu, assignmentMenu, displayMenu, helpMenu;   
	private JMenuItem loadSpectra, loadExcelAssignments, saveExcelAssignments, saveAssignments, 
						loadAssignments, generateDiagram, generatePNG, generateAllPNG,
						toggle, addNew, setIntensity, editAssignments, helpItem, splitDiagram; 
	
	private JFileChooser fileChooser;
	private File file;
	private JButton leftButton, rightButton;
	
	public GUIForLoomisWood() 
	{
		ICON = new ImageIcon(getClass().getResource("atomFinal.png")).getImage();
		
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Automatic Loomis-Wood Diagrams with Assignments");
		frame.setResizable(true);
		frame.setIconImage(ICON);
		
		initComponents();
		setComponentSettings();
		addComponents();
		addEmptyPlots();
		
		frame.setJMenuBar(menuBar); 
		frame.setSize(1500, 1000);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		SubBandContainer.setTolerance(TOLERANCE);
	}
	
	private void initComponents()
	{	
		menuBar = new JMenuBar();
		
		fileMenu = new JMenu("File");
		assignmentMenu = new JMenu("Assignments");
		displayMenu = new JMenu("Display");
		helpMenu = new JMenu("Help");

		loadSpectra = new JMenuItem("Load Spectrum File");
		loadExcelAssignments = new JMenuItem("Load Assignments from Excel Peak List");
		saveExcelAssignments = new JMenuItem("Save Assignments to an Excel Peak List");
		saveAssignments = new JMenuItem("Save Assignments...");
		loadAssignments = new JMenuItem("Load Assignments...");
		generateDiagram = new JMenuItem("Create Loomis-Wood Diagram");
		toggle = new JMenuItem("Toggle Assignment Overlay");
		addNew = new JMenuItem("Add New Assignment");
		splitDiagram = new JMenuItem("Split the Diagram into parts");
		setIntensity = new JMenuItem("Set Intensity Range");
		editAssignments = new JMenuItem("Edit Assignments");
		helpItem = new JMenuItem("Features and General Information");
		generatePNG = new JMenuItem("Create a single Loomis-Wood image");
		generateAllPNG = new JMenuItem("Create many Loomis-Wood images");

		gbc = new GridBagConstraints();
		
		fileChooser = new JFileChooser();
		
		chartPanelArray = new ArrayList<ChartPanel>();
		chartStartValues = new ArrayList<Double>();
		
		loomisPanel = new GradientPanel(new Color(83,120,149), new Color(9,32,63));
		subLoomisPanel = new JPanel(new BorderLayout());

		leftButton = new JButton();
		rightButton = new JButton();
		
		infoHeader = new JLabel();
	}
	
	private void setComponentSettings() 
	{
		loomisPanel.setLayout(new GridBagLayout());
		
		subLoomisPanel.setPreferredSize(new Dimension(1000,60));
		subLoomisPanel.setBackground(Color.DARK_GRAY);
		setUpLoomisSubPanel();
		
		rightButton.setPreferredSize(new Dimension(32,32));
		rightButton.setFocusable(false);
		rightButton.setBorder(BorderFactory.createRaisedBevelBorder());
		rightButton.addActionListener(new EventHandling());
		rightButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		leftButton.setPreferredSize(new Dimension(32,32));
		leftButton.setFocusable(false);
		leftButton.setBorder(BorderFactory.createRaisedBevelBorder());
		leftButton.addActionListener(new EventHandling());
		leftButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		rightButton.setIcon(new ImageIcon(getClass().getResource("arrow32Right.png")));
		leftButton.setIcon(new ImageIcon(getClass().getResource("arrow32Left.png")));
		
		Font font = new Font("Arial", Font.PLAIN, 18);
		
		fileMenu.setPreferredSize(new Dimension(45,25));
		fileMenu.setFont(font);
		fileMenu.add(loadSpectra);
		fileMenu.add(saveAssignments);
		fileMenu.add(loadAssignments);
		fileMenu.add(saveExcelAssignments);
		fileMenu.add(loadExcelAssignments);

		assignmentMenu.setPreferredSize(new Dimension(115,25));
		assignmentMenu.setFont(font);
		assignmentMenu.add(addNew);
		assignmentMenu.add(editAssignments);

		displayMenu.setPreferredSize(new Dimension(70,25));
		displayMenu.setFont(font);
		displayMenu.add(generateDiagram);
		displayMenu.add(setIntensity);
		displayMenu.add(toggle);
		displayMenu.add(splitDiagram);
		displayMenu.add(generatePNG);
		displayMenu.add(generateAllPNG);
		
		helpMenu.setPreferredSize(new Dimension(45,25));
		helpMenu.setFont(font);
		helpMenu.add(helpItem);
		
		loadSpectra.addActionListener(new EventHandling());
		loadExcelAssignments.addActionListener(new EventHandling());
		saveExcelAssignments.addActionListener(new EventHandling());
		saveAssignments.addActionListener(new EventHandling());
		loadAssignments.addActionListener(new EventHandling());
		editAssignments.addActionListener(new EventHandling());
		helpItem.addActionListener(new EventHandling());
		generateDiagram.addActionListener(new EventHandling());
		generatePNG.addActionListener(new EventHandling());
		splitDiagram.addActionListener(new EventHandling());
		generateAllPNG.addActionListener(new EventHandling());
		toggle.addActionListener(new EventHandling());
		addNew.addActionListener(new EventHandling());
		setIntensity.addActionListener(new EventHandling());
		
		menuBar.add(fileMenu);
		menuBar.add(assignmentMenu);
		menuBar.add(displayMenu);
		menuBar.add(helpMenu);
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "dat", "txt");
		fileChooser.setFileFilter(filter);
		fileChooser.setPreferredSize(new Dimension(1000, 618));
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home"), "Desktop"));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		infoHeader.setFont(new Font("Times new Roman", Font.BOLD, 30));
		infoHeader.setForeground(Color.WHITE);
		infoHeader.setPreferredSize(new Dimension(500,40));
	}
	
	private void addComponents() 
	{
		gbc.weightx = 0.01;
		gbc.weighty = 0.01;
		gbc.fill = GridBagConstraints.BOTH;
		
		setLocationOfComponenet(loomisPanel, subLoomisPanel, 0, 0);
		
		frame.add(loomisPanel);
	}
	
	private void setUpLoomisSubPanel() 
	{
		JPanel subRightpanel = new JPanel();
		subRightpanel.setBackground(Color.DARK_GRAY);
		subRightpanel.add(leftButton);
		subRightpanel.add(rightButton);
		subLoomisPanel.add(subRightpanel, BorderLayout.LINE_END);
	}
	
	private static void setLocationOfComponenet(JPanel panel, JComponent component, int x, int y)
	{
		gbc.gridx = x;
		gbc.gridy = y;
		panel.add(component, gbc);
	}
	
	public static void setStartingValue(double startVal) 
	{
		startValue = startVal;
	}
	
	public static void setEndValue(double endVal) 
	{
		endValue = endVal;
	}
	
	public static void setWidthValue(double widthVal) 
	{
		widthValue = widthVal;
	}
	
	public static int getNumberOfParts() 
	{
		return numberOfParts;
	}
	
	public static FileInformation getFileInfoObject() 
	{
		return fileInfo;
	}
	
	public static void setCurrentPart(int part) 
	{
		currentPart = part;
	}
	
	//Sets and controls how the number of parts affects the display
	public static void setNumberOfParts(int numberOfPartsIn)
	{		
		numberOfParts = numberOfPartsIn;
		currentPart = 1;
		
		if(numberOfParts != 1) 
		{
			addInformationHeader();
			updateChartsIntoParts();
		}else
		{
			subLoomisPanel.remove(infoHeader);
			updateChartsIntoParts();
		}
		
		frame.repaint();
		frame.revalidate();
	}
	
	//displays the number of parts and the current part (eg. Diagram broken into 2 parts. On part 1 of 2.)
	private static void addInformationHeader()
	{
		infoHeader.setText("Currently showing part " + currentPart + " of " + numberOfParts);
		
		subLoomisPanel.add(infoHeader, BorderLayout.LINE_START);
	}
	
	private static void updateInformationHeader()
	{
		infoHeader.setText("Currently showing part " + currentPart + " of " + numberOfParts);
		
		frame.repaint();
		frame.revalidate();
	}
	
	//Renders the charts into parts
	public static void updateChartsIntoParts()
	{
		//base case: Nothing is displayed
		if(chartStartValues.size() == 0)
		{
			return;
		}
		
		//update each chart
		for(int i = 0; i < chartPanelArray.size(); i++) 
		{
			JFreeChart chart = chartPanelArray.get(i).getChart();
			
			double lowerBoundToBeShown, upperBoundToBeShown;
			
			double chunkSize = widthValue * (1.0 / numberOfParts);
			
			lowerBoundToBeShown = chartStartValues.get(i) + ((currentPart - 1) * chunkSize);
			upperBoundToBeShown = chartStartValues.get(i) + (currentPart * chunkSize);
			
			int lowIndex = loomisObject.convertValueToClosestIndex(lowerBoundToBeShown, TOLERANCE);
			int highIndex = loomisObject.convertValueToClosestIndex(upperBoundToBeShown, TOLERANCE);
			
			loomisObject.updatePlot(chart, lowIndex, highIndex, chunkSize);
		}
	}
	
	//used for a multiple image call
	public static void generateImage(String path) 
	{
		long time = System.currentTimeMillis();
		
		try 
		{
			if(numberOfParts == 1)
			{
				ScreenImage.writeImage(ScreenImage.createImage(loomisPanel), path + "\\Loomis - " + time + ".png");
			}else
			{
				ScreenImage.writeImage(ScreenImage.createImage(loomisPanel), path + "\\Loomis - " + time + 
						" - Part " + currentPart + " of " + numberOfParts + ".png");
			}
		} catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null, "IO issue - Ensure the folder is not open");
		}
	}
	
	//used for a single image call 
	private void generateSingleImage()
	{
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG", "png");
		fileChooser.resetChoosableFileFilters();
		fileChooser.setFileFilter(filter);
		fileChooser.setDialogTitle("Save As");
		
		File outputFile;
		
		if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) 
		{
			outputFile = fileChooser.getSelectedFile();
			String filePath = outputFile.getPath();
			
			if(!filePath.toLowerCase().endsWith(".png"))
			{
				if(filePath.indexOf('.') == -1) 
				{
					outputFile = new File(filePath + ".png");
				}else 
				{
					outputFile = new File(filePath.substring(0, filePath.indexOf('.')) + ".png");
				}
			}
		}else 
		{
			return;
		}
		
		setPictureMode();
		
		try 
		{
			ScreenImage.writeImage(ScreenImage.createImage(loomisPanel), outputFile.getAbsolutePath());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		returnFromPictureMode();
	}
	
	//removes the header bar to be able to take good screenshots
	public static void setPictureMode() 
	{
		loomisPanel.removeAll();
		gbc.weightx = 0.99;
		gbc.weighty = 0.99;
		gbc.fill = GridBagConstraints.BOTH;
		
		for(int i = 0; i < 5; i++) 
		{
			gbc.gridy = i;
			loomisPanel.add(chartPanelArray.get(i), gbc);
		}
		
		frame.repaint();
		frame.revalidate();
	}
	
	//adds the header bar back in after picture taking is done
	public static void returnFromPictureMode()
	{
		loomisPanel.removeAll();
		gbc.fill = GridBagConstraints.BOTH;
		
		gbc.weightx = 0.01;
		gbc.weighty = 0.01;
		setLocationOfComponenet(loomisPanel, subLoomisPanel, 0, 0);
		
		gbc.weightx = 0.99;
		gbc.weighty = 0.99;
		for(int i = 0; i < 5; i++) 
		{
			gbc.gridy = i + 1;
			loomisPanel.add(chartPanelArray.get(i), gbc);
		}
		
		frame.repaint();
		frame.revalidate();
	}
	
	public static void updateCharts()
	{
		chartStartValues.clear();
		
		for(int i = 0; i < chartPanelArray.size(); i++) 
		{
			chartStartValues.add(startValue);
			
			int startIndex = loomisObject.convertValueToClosestIndex(startValue, TOLERANCE);
			int endIndex   = loomisObject.convertValueToClosestIndex(endValue  , TOLERANCE);

			loomisObject.updatePlot(chartPanelArray.get(i).getChart(), startIndex, endIndex, widthValue);
			
			startValue = endValue;
			endValue   = startValue + widthValue;
		}
	}
	
	private void addEmptyPlots()
	{
		gbc.insets = new Insets(0,0,0,0);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.98;
		gbc.weighty = 0.98;
		
		for(int i = 0; i < 5; i++)
		{
			ChartPanel chartPanel = LoomisDisplay.getEmptyPlot();
			chartPanel.setPreferredSize(new Dimension(100, 300));
			chartPanel.setMouseWheelEnabled(true);
			chartPanel.setDismissDelay(100000000);
			chartPanel.getChart().setBackgroundPaint(new Color(95, 144, 168));
			chartPanelArray.add(chartPanel);
			
			gbc.gridy = i + 1;
			loomisPanel.add(chartPanelArray.get(i), gbc);
		}
	}
	
	public static void changeIntensityRange(double lowIntensity, double highIntensity) 
	{
		LoomisDisplay.setLowIntensityValue(lowIntensity);
		LoomisDisplay.setHighIntensityValue(highIntensity);
		
		for(ChartPanel panel : chartPanelArray)
		{
			LoomisDisplay.setIntensityRange(panel.getChart());
		}
	}
	
	private class EventHandling implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == helpItem) 
			{
				new HelpJDialog(1000, 600, "Information", ICON);
				return;
			}
			
			if(e.getSource() == loadSpectra) 
			{
				loadSpectraIn();
			}
			
			if(fileInfo == null)
			{
				JOptionPane.showMessageDialog(null, "Need to Load a Spectrum File");
				return;
			}
			
			if(e.getSource() == saveExcelAssignments) 
			{
				JOptionPane.showMessageDialog(null, "Not implemented yet :(");
			}
			
			if(e.getSource() == loadExcelAssignments) 
			{
				//new ReadPeakFileAssignments();
				JOptionPane.showMessageDialog(null, "Not implemented yet :(");
			}
			
			if(e.getSource() == saveAssignments) 
			{
				saveAssignments();
			}
			
			if(e.getSource() == loadAssignments) 
			{
				loadAssignments();
			}
			
			if(e.getSource() == editAssignments) 
			{
				new ViewAssignmentsJDialog(450, 660, "Assignments", ICON);
			}
			
			if(e.getSource() == generateDiagram) 
			{
				new CreateDiagramJDialog(400,250, "Create Plots", ICON);
			}
			
			if(e.getSource() == splitDiagram) 
			{
				new SplitDiagramJDialog(300, 300, "Split up the Diagram", ICON);
			}
			
			if(e.getSource() == generatePNG) 
			{
				generateSingleImage();
			}
			
			if(e.getSource() == generateAllPNG) 
			{
				new GenerateAllPNGJDialog(400, 400, "Generate Loomis-Wood Diagram Images", ICON);
			}
			
			if(e.getSource() == toggle) 
			{
				toggleOverlayState();
			}
			
			if(e.getSource() == addNew) 
			{
				new AddAssignmentJDialog(400, 800, "Add a New Assignment", ICON);
			}
			
			if(e.getSource() == setIntensity) 
			{
				new IntensityJDialog(300,230, "Select Range", ICON);
			}
			
			if(e.getSource() == rightButton && loomisObject != null) 
			{
				goRight();
			}
			
			if(e.getSource() == leftButton && loomisObject != null)
			{
				goLeft();
			}
		}
	}
	
	private void loadSpectraIn() 
	{
		if(fileInfo != null)
		{
			JOptionPane.showMessageDialog(null, "Already have a spectra loaded");
			return;
		}
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "dat", "txt");
		fileChooser.resetChoosableFileFilters();
		fileChooser.setDialogTitle("Open a data file");
		fileChooser.setFileFilter(filter);
		
		if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) 
		{
			file = fileChooser.getSelectedFile();
			
			//Get the file information and report the time taken
			long start = System.currentTimeMillis();
			fileInfo = new FileInformation(file);
			long stop = System.currentTimeMillis();
			JOptionPane.showMessageDialog(null, "Finished loading\nLoad time: " + (stop-start)/1000.0 + " seconds");
			
			loomisObject = new LoomisDisplay(fileInfo);
		}else 
		{
			return;
		}
	}
	
	private void saveAssignments() 
	{
		fileChooser.resetChoosableFileFilters();
		fileChooser.setDialogTitle("Save As");
		
		File outputFile;
		
		if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) 
		{
			outputFile = fileChooser.getSelectedFile();
			String filePath = outputFile.getPath();
			
			if(!filePath.toLowerCase().endsWith(".ser"))
			{
				outputFile = new File(filePath.substring(0, filePath.indexOf('.')) + ".ser");
			}
			
	        try 
	        {
	        	FileOutputStream fos = new FileOutputStream(outputFile);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				
				oos.writeObject(SubBandContainer.getSubBandArray());
				oos.close();
	        } catch (Exception ex) 
	        {
	            ex.printStackTrace();
	        }
		}else
		{
			return;
		}
		
		JOptionPane.showMessageDialog(null, "Save Successful");
	}
	
	@SuppressWarnings("unchecked")
	private void loadAssignments() 
	{
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Java Object File (.ser)", "ser");
		fileChooser.resetChoosableFileFilters();
		fileChooser.setFileFilter(filter);
		fileChooser.setDialogTitle("Load Assignments");
		
		if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) 
		{
			File inputFile = fileChooser.getSelectedFile();
			ArrayList<SubBand> subBandArray;
			
			try 
			{
		        FileInputStream fis = new FileInputStream(inputFile);
		        ObjectInputStream ois = new ObjectInputStream(fis);
		        
		        subBandArray = (ArrayList<SubBand>) ois.readObject();
				SubBandContainer.setSubBandArray(subBandArray);
				
				ois.close();
			}catch(Exception ex) 
			{
				ex.printStackTrace();
			}
		}else 
		{
			return;
		}
		
		JOptionPane.showMessageDialog(null, "Assignments are now loaded");
	}

	private void toggleOverlayState() 
	{
		for(ChartPanel panel : chartPanelArray)
		{
			LoomisDisplay.toggleRenderer(panel.getChart());
		}
		
		if(LoomisDisplay.getToggleState()) 
		{
			JOptionPane.showMessageDialog(null, "Assignments now hidden from display");
		}else 
		{
			JOptionPane.showMessageDialog(null, "Assignments now visible in the display");
		}
		
		LoomisDisplay.flipToggleFlag();
	}
	
	//called by either the right button or the GenerateAllPNGJDialog class
	public static void goRight() 
	{
		if(numberOfParts == currentPart) 
		{
			currentPart = 1;
			updateCharts();
		}else 
		{
			currentPart++;
		}
		
		updateChartsIntoParts();
		updateInformationHeader();
	}
	
	//called by either the left button or the GenerateAllPNGJDialog class
	public static void goLeft() 
	{
		if(currentPart == 1) 
		{
			endValue = startValue - 9 * widthValue;
			startValue = startValue - 10 * widthValue;
			currentPart = numberOfParts;
			updateCharts();
		}else 
		{
			currentPart--;
		}
		
		updateChartsIntoParts();
		updateInformationHeader();
	}
	
	public static void main(String [] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				try 
				{
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					UIManager.put("TabbedPane.selected", new Color(0, 149, 249));
					UIManager.put("ToolTip.background", Color.BLACK);
					UIManager.put("ToolTip.foreground", Color.WHITE);
					UIManager.put("ToolTip.font", new Font(Font.MONOSPACED, Font.PLAIN, 22));
				} catch (Exception ignored) { JOptionPane.showMessageDialog(null, "UIManager exception..."); }

				new GUIForLoomisWood();
			}
		});
	}
}