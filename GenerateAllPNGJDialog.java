package loomisWood;

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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class GenerateAllPNGJDialog extends CustomJDialog
{
	private static final long serialVersionUID = -6525677324321774587L;
	
	private JButton confirmButton, selectButton;
	private JTextField fromField, toField, widthField;
	private JLabel rangeLabel, fromLabel, toLabel, widthLabel, selectedLabel;
	private GradientPanel panel;
	private JFileChooser fileChooser;
	private File saveDir;

	public GenerateAllPNGJDialog(int widthIn, int heightIn, String titleIn, Image iconIn)
	{
		super(widthIn, heightIn, titleIn, iconIn);
		
		initComponents();
		setComponentSettings();
		addComponents();
		
		setVisible(true);
	}

	private void initComponents()
	{
		confirmButton = new JButton("Confirm");
		selectButton  = new JButton("Select a Folder");
		
		fromField     = new JTextField();
		toField       = new JTextField();
		widthField    = new JTextField();
		
		rangeLabel    = new JLabel("Select the Wavenumber Range:");
		fromLabel     = new JLabel("From");
		toLabel       = new JLabel("To");
		widthLabel    = new JLabel("Row Width");
		selectedLabel = new JLabel("Folder Selected: None");
		
		panel         = new GradientPanel(new Color(185, 188, 191), new Color(90, 92, 94));
		fileChooser   = new JFileChooser();
	}

	private void setComponentSettings()
	{
		confirmButton.setPreferredSize(new Dimension(100,30));
		selectButton.setPreferredSize(new Dimension(200,30));
		fromField.setPreferredSize(new Dimension(40,30));
		toField.setPreferredSize(new Dimension(40,30));
		widthField.setPreferredSize(new Dimension(40,30));
		rangeLabel.setPreferredSize(new Dimension(400,30));
		fromLabel.setPreferredSize(new Dimension(60,30));
		toLabel.setPreferredSize(new Dimension(30,30));
		widthLabel.setPreferredSize(new Dimension(120,30));
		selectedLabel.setPreferredSize(new Dimension(300,30));
		
		confirmButton.addActionListener(new EventHandling());
		selectButton.addActionListener(new EventHandling());
		
		panel.setLayout(new GridBagLayout());
		
		confirmButton.setFont(new Font("Arial", Font.PLAIN, 22));
		selectButton.setFont(new Font("Arial", Font.PLAIN, 22));
		rangeLabel.setFont(new Font("Arial", Font.BOLD, 18));
		fromLabel.setFont(new Font("Arial", Font.PLAIN, 22));
		toLabel.setFont(new Font("Arial", Font.PLAIN, 22));
		widthLabel.setFont(new Font("Arial", Font.PLAIN, 22));
		selectedLabel.setFont(new Font("Arial", Font.BOLD, 20));
		widthField.setFont(new Font("Arial", Font.PLAIN, 22));
		toField.setFont(new Font("Arial", Font.PLAIN, 22));
		fromField.setFont(new Font("Arial", Font.PLAIN, 22));
		
		confirmButton.setFocusable(false);
		selectButton.setFocusable(false);
		
		confirmButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		selectButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		confirmButton.setBorder(BorderFactory.createRaisedBevelBorder());
		confirmButton.setBackground(new Color(34, 62, 74));
		confirmButton.setForeground(new Color(253, 253, 253));
		confirmButton.setContentAreaFilled(false);
		confirmButton.setOpaque(true);
	}

	private void addComponents()
	{
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,15,5,15);
		
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(rangeLabel, gbc);
		
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(fromLabel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		panel.add(fromField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(toLabel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		panel.add(toField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		panel.add(widthLabel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		panel.add(widthField, gbc);
		
		gbc.insets = new Insets(40,15,5,15);
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 4;
		panel.add(selectButton, gbc);
		
		gbc.insets = new Insets(5,15,5,15);
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 5;
		panel.add(selectedLabel, gbc);
		
		gbc.insets = new Insets(40,15,5,15);
		gbc.gridx = 0;
		gbc.gridy = 6;
		panel.add(confirmButton, gbc);
		
		add(panel);
	}
	
	private class EventHandling implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == confirmButton) 
			{
				if(saveDir == null) 
				{
					JOptionPane.showMessageDialog(null, "No Folder Selected");
					return;
				}
				
				if(toField.getText().equals("") || fromField.getText().equals("")
						|| widthField.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "Cannot leave fields empty");
					return;
				}
				
				double lowRange, highRange, widthValue;
				
				try 
				{
					lowRange   = Double.parseDouble(fromField.getText());
					highRange  = Double.parseDouble(toField.getText());
					widthValue = Double.parseDouble(widthField.getText());
				}catch(NumberFormatException nfe)
				{
					JOptionPane.showMessageDialog(null, "Issue reading one of the fields");
					return;
				}
				
				if(lowRange >= highRange || widthValue <=0)
				{
					JOptionPane.showMessageDialog(null, "The second value must be greater than the first.\n"
							+ "Also the row width must be greater than 0.");
					return;
				}
				
				String msg = "Warning - This can create a large number of\nimages. Ensure that the selected"
						+ " folder is the\ncorrect one.\n\nContinue?";
				int result = JOptionPane.showConfirmDialog(null, msg, "Confirm?", JOptionPane.YES_NO_OPTION);
				
				if(result == 0)
				{
					generateImages(lowRange, highRange, widthValue);
				}else
				{
					saveDir = null;
					return;
				}
				
				saveDir = null;
				dispose();
			}
			
			if(e.getSource() == selectButton) 
			{
				fileChooser.setPreferredSize(new Dimension(800, 550));
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home"), "Desktop"));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				if (fileChooser.showOpenDialog(GenerateAllPNGJDialog.this) == JFileChooser.APPROVE_OPTION) 
				{
					saveDir = fileChooser.getSelectedFile();
					
					selectedLabel.setText("Folder Selected: " + saveDir.getName());
				}else
				{
					return;
				}
			}
		}
	}
	
	//Generate the images using the input values
	private void generateImages(double lowRange, double highRange, double widthValue)
	{
		//check if the lowRange and highRange values are within the limits of the spectra
		FileInformation fileInfo = GUIForLoomisWood.getFileInfoObject();
		
		double lowest = fileInfo.getLowestXValue();
		double highest = fileInfo.getHighestXValue();
		
		double currentLow = lowRange;
		double currentHigh = lowRange + widthValue;
		
		if(lowest > currentLow) 
		{
			String msg = "The lower value is lower than the lowest value in the spectra.\n\nContinue?";
			int result = JOptionPane.showConfirmDialog(null, msg, "Confirm?", JOptionPane.YES_NO_OPTION);
			
			if(result == 1)
			{
				return;
			}
		}
		
		GUIForLoomisWood.setWidthValue(widthValue);
		GUIForLoomisWood.setPictureMode();
		int numOfParts = GUIForLoomisWood.getNumberOfParts();
		
		while(currentHigh <= highRange && currentHigh < highest) 
		{
			GUIForLoomisWood.setStartingValue(currentLow);
			GUIForLoomisWood.setEndValue(currentHigh);
			
			GUIForLoomisWood.updateCharts();
			GUIForLoomisWood.updateChartsIntoParts();
			
			for(int i = 0; i < numOfParts; i++)
			{
				GUIForLoomisWood.generateImage(saveDir.getAbsolutePath());
				GUIForLoomisWood.goRight();
			}
			
			currentLow = 5 * widthValue + currentLow;
			currentHigh = currentLow + widthValue;
		}

		GUIForLoomisWood.returnFromPictureMode();
	}
}
