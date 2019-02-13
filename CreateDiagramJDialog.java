package loomisWood;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class CreateDiagramJDialog extends CustomJDialog
{
	private static final long serialVersionUID = -6774448267055822069L;
	
	private JTextField startField, widthField;
	private JButton confirmButton;
	private JLabel startLabel, widthLabel;
	private GradientPanel panel;

	public CreateDiagramJDialog(int widthIn, int heightIn, String titleIn, Image iconIn)
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
		startField = new JTextField();
		widthField = new JTextField();
		startLabel = new JLabel("Starting Value");
		widthLabel = new JLabel("Row Width");
		panel = new GradientPanel(BACKGROUND_COLOR_1, BACKGROUND_COLOR_2);
	}
	
	private void setComponentSettings()
	{
		confirmButton.setPreferredSize(new Dimension(200,30));
		confirmButton.addActionListener(new EventHandling());
		confirmButton.setFont(new Font("Arial", Font.PLAIN, 20));
		
		startField.setPreferredSize(new Dimension(100,30));
		widthField.setPreferredSize(new Dimension(100,30));
		startLabel.setPreferredSize(new Dimension(100,30));
		widthLabel.setPreferredSize(new Dimension(100,30));
		
		Font font = new Font("Arial", Font.PLAIN, 18);
		startField.setFont(font);
		widthField.setFont(font);
		
		startLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		widthLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		
		panel.setLayout(new GridBagLayout());
	}

	private void addComponents()
	{
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.insets = new Insets(5,15,5,15);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(startLabel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		panel.add(startField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(widthLabel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		panel.add(widthField, gbc);
		
		gbc.insets = new Insets(50,50,5,50);
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(confirmButton, gbc);
		
		add(panel);
	}

	private class EventHandling implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == confirmButton) 
			{
				String startValueStr = startField.getText();
				String widthValueStr = widthField.getText();
				double startValue, widthValue, endValue;
				
				try
				{
					startValue = Double.parseDouble(startValueStr);
					widthValue = Double.parseDouble(widthValueStr);
					endValue   = startValue + widthValue;
				}catch(NumberFormatException nfe) 
				{
					JOptionPane.showMessageDialog(null, "Invalid start or width values");
					return;
				}
				
				if(widthValue <= 0) 
				{
					JOptionPane.showMessageDialog(null, "The width value must be greater than zero");
					return;
				}
				
				GUIForLoomisWood.setStartingValue(startValue);
				GUIForLoomisWood.setWidthValue(widthValue);
				GUIForLoomisWood.setEndValue(endValue);
				GUIForLoomisWood.updateCharts();
				GUIForLoomisWood.updateChartsIntoParts();
				
				dispose();
			}
		}
	}
}
