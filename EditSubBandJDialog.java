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

public class EditSubBandJDialog extends CustomJDialog
{
	private static final long serialVersionUID = 5586526285258832499L;
	
	private JLabel lowerKLabel, upperKLabel, lowerVtLabel, upperVtLabel, notesLabel;
	private JTextField lowerKField, upperKField, lowerVtField, upperVtField, notesField;
	private JButton confirmButton;
	private GradientPanel panel;
	
	private int indexOfSubBand;

	public EditSubBandJDialog(int widthIn, int heightIn, String titleIn, Image iconIn, int indexOfSubBand)
	{
		super(widthIn, heightIn, titleIn, iconIn);
		
		this.indexOfSubBand = indexOfSubBand;
		
		initComponents();
		setComponentSettings();
		addComponents();
		
		setVisible(true);
	}

	private void initComponents()
	{
		lowerKLabel  = new JLabel("Lower K");
		upperKLabel  = new JLabel("Upper K");
		lowerVtLabel = new JLabel("Lower Vt");
		upperVtLabel = new JLabel("Upper Vt");
		notesLabel   = new JLabel("Notes (Optional)");
		
		lowerKField  = new JTextField();
		upperKField  = new JTextField();
		lowerVtField = new JTextField();
		upperVtField = new JTextField();
		notesField   = new JTextField();
		
		confirmButton = new JButton("Confirm");
		
		panel = new GradientPanel(BACKGROUND_COLOR_1, BACKGROUND_COLOR_2);
	}

	private void setComponentSettings()
	{
		Dimension dim = new Dimension(100,30);
		Dimension dim2 = new Dimension(150,30);
		Font font = new Font("Arial", Font.PLAIN, 20);
		
		lowerKLabel.setPreferredSize(dim2);
		upperKLabel.setPreferredSize(dim2);
		lowerVtLabel.setPreferredSize(dim2);
		upperVtLabel.setPreferredSize(dim2);
		notesLabel.setPreferredSize(dim2);
		
		lowerKLabel.setFont(font);
		upperKLabel.setFont(font);
		lowerVtLabel.setFont(font);
		upperVtLabel.setFont(font);
		notesLabel.setFont(font);
		
		lowerKField.setPreferredSize(dim);
		upperKField.setPreferredSize(dim);
		lowerVtField.setPreferredSize(dim);
		upperVtField.setPreferredSize(dim);
		notesField.setPreferredSize(dim);
		
		confirmButton.setPreferredSize(new Dimension(250,30));
		confirmButton.addActionListener(new EventHandling());
		confirmButton.setFocusable(false);
		confirmButton.setFont(new Font("Arial", Font.PLAIN, 22));
		
		panel.setLayout(new GridBagLayout());
	}

	private void addComponents()
	{
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5,5,5,5);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(lowerKLabel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		panel.add(lowerKField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(upperKLabel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		panel.add(upperKField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(lowerVtLabel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		panel.add(lowerVtField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		panel.add(upperVtLabel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		panel.add(upperVtField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		panel.add(notesLabel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 4;
		panel.add(notesField, gbc);
		
		gbc.gridwidth = 2;
		gbc.insets = new Insets(25,5,5,5);
		gbc.gridx = 0;
		gbc.gridy = 5;
		panel.add(confirmButton, gbc);
		
		add(panel);
	}
	
	private class EventHandling implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == confirmButton) 
			{
				int choice = JOptionPane.showConfirmDialog(null, "Save changes?",
						"Edit Subband Information", JOptionPane.YES_NO_OPTION);
				
				if(choice == 0) 
				{
					int lowerK, upperK, lowerVt, upperVt;
					String note;
					
					try
					{
						lowerK  = Integer.parseInt(lowerKField.getText());
						upperK  = Integer.parseInt(upperKField.getText());
						lowerVt = Integer.parseInt(lowerVtField.getText());
						upperVt = Integer.parseInt(upperVtField.getText());
						note    = notesField.getText();
					}catch(NumberFormatException nfe) 
					{
						JOptionPane.showMessageDialog(null, "Trouble reading one of the fields");
						return;
					}
					
					SubBandContainer.getItemAt(indexOfSubBand).setLowerK(lowerK);
					SubBandContainer.getItemAt(indexOfSubBand).setUpperK(upperK);
					SubBandContainer.getItemAt(indexOfSubBand).setUpperVt(upperVt);
					SubBandContainer.getItemAt(indexOfSubBand).setLowerVt(lowerVt);
					SubBandContainer.getItemAt(indexOfSubBand).setNote(note);
					
					JOptionPane.showMessageDialog(null, "Changes Saved");
					
					dispose();
				}else
				{
					return;
				}
				
			}
		}
	}
}
