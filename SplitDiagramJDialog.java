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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class SplitDiagramJDialog extends CustomJDialog
{
	private static final long serialVersionUID = -7885010046675935093L;

	private JComboBox<Integer> comboBox;
	private JLabel label;
	private GradientPanel panel;
	private JButton confirmButton;
	
	public SplitDiagramJDialog(int widthIn, int heightIn, String titleIn, Image iconIn)
	{
		super(widthIn, heightIn, titleIn, iconIn);
		
		initComponents();
		setComponentSettings();
		addComponents();
		
		setVisible(true);
	}

	private void initComponents()
	{
		comboBox = new JComboBox<Integer>();
		label = new JLabel("<html> Split the diagram into<br>how many parts? </html>");
		panel = new GradientPanel(new Color(185, 188, 191), new Color(90, 92, 94));
		confirmButton = new JButton("Confirm");
	}

	private void setComponentSettings()
	{
		label.setFont(new Font("Arial", Font.BOLD, 20));
		label.setPreferredSize(new Dimension(250,60));
		
		panel.setLayout(new GridBagLayout());
		
		comboBox.setPreferredSize(new Dimension(200,30));
		comboBox.setFont(new Font("Times new Roman", Font.PLAIN, 22));
		((JLabel)comboBox.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		
		comboBox.addItem(1);
		comboBox.addItem(2);
		comboBox.addItem(3);
		comboBox.addItem(4);
		
		confirmButton.setFont(new Font("Arial", Font.PLAIN, 22));
		confirmButton.addActionListener(new EventHandling());
		confirmButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		confirmButton.setFocusable(false);
		confirmButton.setBorder(BorderFactory.createRaisedBevelBorder());
		confirmButton.setBackground(new Color(34, 62, 74));
		confirmButton.setForeground(new Color(253, 253, 253));
		confirmButton.setContentAreaFilled(false);
		confirmButton.setOpaque(true);
	}

	private void addComponents()
	{
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5,5,5,5);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(label, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(comboBox, gbc);
		
		gbc.insets = new Insets(50,5,5,5);
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
				int numberOfParts = (int) comboBox.getSelectedItem();
				
				GUIForLoomisWood.setNumberOfParts(numberOfParts);
				
				if(numberOfParts == 1) 
				{
					JOptionPane.showMessageDialog(null, "The diagram is no longer broken into pieces");
				}else 
				{
					JOptionPane.showMessageDialog(null, "The diagram is now split into " + numberOfParts + " parts");
				}
				
				dispose();
			}
		}
	}
}
