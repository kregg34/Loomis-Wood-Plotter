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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class AddAssignmentJDialog extends CustomJDialog
{
	private static final long serialVersionUID = 4647967858439861195L;
	private final Image ICON;
	
	private static MenuJTable table;
	
	private JButton addBranchButton, addAssignButton;
	private JScrollPane scrollPane;
	private JTextField lowerKField, upperKField, lowerVtField, upperVtField, noteField;
	private JLabel lowerKLabel, upperKLabel, lowerVtLabel, upperVtLabel, noteLabel, symmetryLabel;
	private GradientPanel panel;
	private JComboBox<String> symmetryBox;

	public AddAssignmentJDialog(int widthIn, int heightIn, String titleIn, Image iconIn)
	{
		super(widthIn, heightIn, titleIn, iconIn);

		initComponents();
		setComponentSettings();
		addComponents();
		
		SubBandContainer.getNewSubBandInstance();
		ICON = iconIn;
		
		setVisible(true);
	}
	
	private void initComponents()
	{
		table = new MenuJTable();
		table.setRowHeight(35);
		scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		addBranchButton = new JButton("Add a Branch");
		addAssignButton = new JButton("Add Assignment");
		
		lowerKField  = new JTextField();
		upperKField  = new JTextField();
		lowerVtField = new JTextField();
		upperVtField = new JTextField();
		noteField    = new JTextField();
		
		lowerKLabel  = new JLabel("Lower K");
		upperKLabel  = new JLabel("Upper K");
		lowerVtLabel = new JLabel("Lower Vt");
		upperVtLabel = new JLabel("Upper Vt");
		noteLabel    = new JLabel("Notes (Optional)");
		symmetryLabel = new JLabel("Symmetry Type");
		
		symmetryBox = new JComboBox<String>();
		
		panel = new GradientPanel(BACKGROUND_COLOR_1, BACKGROUND_COLOR_2);
	}
	
	private void setComponentSettings()
	{
		panel.setLayout(new GridBagLayout());
		
		String[] header = {"Added Branches"};
		Object[][] data = {};
		DefaultTableModel model = new DefaultTableModel(data, header);
		
		table.setModel(model);
		table.getTableHeader().setFont(new Font("SansSerif", Font.PLAIN, 18));
		
		scrollPane.setPreferredSize(new Dimension(300,400));
		
		Dimension labelDim = new Dimension(100,30);
		lowerKLabel.setPreferredSize(labelDim);
		upperKLabel.setPreferredSize(labelDim);
		lowerVtLabel.setPreferredSize(labelDim);
		upperVtLabel.setPreferredSize(labelDim);
		
		Font labelFont = new Font("Arial", Font.PLAIN, 22);
		lowerKLabel.setFont(labelFont);
		upperKLabel.setFont(labelFont);
		lowerVtLabel.setFont(labelFont);
		upperVtLabel.setFont(labelFont);
		noteLabel.setFont(labelFont);
		symmetryLabel.setFont(labelFont);
		
		Dimension fieldDim = new Dimension(100,30);
		lowerKField.setPreferredSize(fieldDim);
		upperKField.setPreferredSize(fieldDim);
		lowerVtField.setPreferredSize(fieldDim);
		upperVtField.setPreferredSize(fieldDim);
		noteField.setPreferredSize(fieldDim);
		noteField.setBackground(Color.LIGHT_GRAY);
		
		Font fieldFont = new Font("Arial", Font.PLAIN, 20);
		lowerKField.setFont(fieldFont);
		upperKField.setFont(fieldFont);
		lowerVtField.setFont(fieldFont);
		upperVtField.setFont(fieldFont);
		noteField.setFont(fieldFont);
		
		addAssignButton.addActionListener(new EventHandling());
		addAssignButton.setPreferredSize(new Dimension(100,30));
		addAssignButton.setFocusable(false);
		addAssignButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		addAssignButton.setFont(fieldFont);
		
		addBranchButton.addActionListener(new EventHandling());
		addBranchButton.setPreferredSize(new Dimension(100,30));
		addBranchButton.setFocusable(false);
		addBranchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		addBranchButton.setFont(fieldFont);
		
		symmetryBox.setPreferredSize(new Dimension(100,30));
		symmetryBox.setFocusable(false);
		((JLabel)symmetryBox.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		symmetryBox.setFont(new Font("Arial", Font.PLAIN, 18));
		symmetryBox.addItem("");
		symmetryBox.addItem("E1");
		symmetryBox.addItem("E2");
		symmetryBox.addItem("A");
	}
	
	private void addComponents()
	{
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.insets = new Insets(5,15,5,15);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(addBranchButton, gbc);
		
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(scrollPane, gbc);
		
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(symmetryLabel, gbc);
		
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 2;
		panel.add(symmetryBox, gbc);
		
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 3;
		panel.add(lowerKLabel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		panel.add(lowerKField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		panel.add(upperKLabel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 4;
		panel.add(upperKField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 5;
		panel.add(lowerVtLabel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 5;
		panel.add(lowerVtField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 6;
		panel.add(upperVtLabel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 6;
		panel.add(upperVtField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 7;
		panel.add(noteLabel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 7;
		panel.add(noteField, gbc);
		
		gbc.insets = new Insets(20,15,5,15);
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 8;
		panel.add(addAssignButton, gbc);
		
		add(panel);
	}
	
	public static void updateTable(String branchType) 
	{
		((DefaultTableModel) table.getModel()).addRow(new Object[]{branchType});
	}
	
	private class EventHandling implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == addBranchButton) 
			{
				new AddBranchJDialog(450, 660, "Add a New Branch", ICON);
			}
			
			if(e.getSource() == addAssignButton) 
			{
				if(symmetryBox.getSelectedItem().equals("")) 
				{
					JOptionPane.showMessageDialog(null, "You must first select a symmetry type");
					return;
				}
				
				int lowerK, upperK, lowerVt, upperVt;
				String note, symmetryType;
				
				try
				{
					lowerK  = Integer.parseInt(lowerKField.getText());
					upperK  = Integer.parseInt(upperKField.getText());
					lowerVt = Integer.parseInt(lowerVtField.getText());
					upperVt = Integer.parseInt(upperVtField.getText());
					note    = noteField.getText();
					symmetryType = (String) symmetryBox.getSelectedItem();
				}catch(NumberFormatException nfe) 
				{
					JOptionPane.showMessageDialog(null, "Trouble reading one of the fields");
					return;
				}
				
				if(SubBandContainer.getNumBranches() == 0) 
				{
					JOptionPane.showMessageDialog(null, "No branches added yet!");
					return;
				}
				
				//check if assignment already exists! :)
				
				SubBandContainer.setSubBandInfo(lowerK, upperK, lowerVt, upperVt, note, symmetryType);
				SubBandContainer.addSubBand();
				SubBandContainer.getNewSubBandInstance();
				
				for (int i = table.getRowCount() - 1; i >= 0; i--) 
				{
				    ((DefaultTableModel) table.getModel()).removeRow(i);
				}
				
				JOptionPane.showMessageDialog(null, "Assignment Successfully Added");
				dispose();
			}
		}
	}
}
