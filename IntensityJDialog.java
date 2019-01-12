package loomisWood;

import java.awt.Color;
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

public class IntensityJDialog extends CustomJDialog
{	
	private static final long serialVersionUID = -7606781133319703348L;
	
	private final Dimension DIM;
	
	private GradientPanel panel;
	private JTextField startField, endField;
	private JLabel fromLabel, toLabel;
	private JButton button;
	
	private double lowIntensity, highIntensity;

	public IntensityJDialog(int widthIn, int heightIn, String titleIn, Image iconIn)
	{
		super(widthIn, heightIn, titleIn, iconIn);
		
		DIM = new Dimension(200,30);
		
		initComponents();
		setComponentSettings();
		addComponents();
		
		setVisible(true);
	}
	
	
	public void initComponents() 
	{
        panel = new GradientPanel(new Color(185, 188, 191), new Color(90, 92, 94));
        startField = new JTextField(LoomisDisplay.getLowIntensityValue() + "");
        endField = new JTextField(LoomisDisplay.getHighIntensityValue() + "");
        fromLabel = new JLabel("Lower Bound");
        toLabel = new JLabel("Upper Bound");
        button = new JButton("Confirm");
	}
	
	private void setComponentSettings()
	{
		Font fontBold = new Font("Arial", Font.BOLD, 18);
		Font fontPlain = new Font("Arial", Font.PLAIN, 18);
		
        startField.setPreferredSize(DIM);
        startField.setFont(fontPlain);
        
        endField.setPreferredSize(DIM);
        endField.setFont(fontPlain);
        
        fromLabel.setPreferredSize(DIM);
        fromLabel.setFont(fontPlain);
        
        toLabel.setPreferredSize(DIM);
        toLabel.setFont(fontPlain);
        
        button.setFont(fontBold);
        button.setPreferredSize(new Dimension(100,30));
        button.addActionListener(new EventHandling());
        
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
		panel.add(fromLabel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		panel.add(startField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(toLabel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		panel.add(endField, gbc);
		
		gbc.insets = new Insets(50,50,5,50);
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(button, gbc);
		
		add(panel);
	}
	
	private class EventHandling implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == button)
			{
				if(startField.getText().equals("") && endField.getText().equals("")) 
				{
					setVisible(false);
					dispose();
					return;
				}
				
				try 
				{
					lowIntensity  = Double.parseDouble(startField.getText());
					highIntensity = Double.parseDouble(endField.getText());
				}catch(NumberFormatException nfe) 
				{
					JOptionPane.showMessageDialog(null, "Could not parse values");
					return;
				}
				
				if(lowIntensity >= highIntensity) 
				{
					JOptionPane.showMessageDialog(null, "The first value must be smaller");
					return;
				}
				
				GUIForLoomisWood.changeIntensityRange(lowIntensity, highIntensity);
				
				dispose();
			}
		}
	}
	
	public double getLowIntensityValue()
	{
		return lowIntensity;
	}
	
	public double getHighIntensityValue()
	{
		return highIntensity;
	}
}
