package loomisWood;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.CellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class AddBranchJDialog extends CustomJDialog
{
	private static final long serialVersionUID = 8242707205315467042L;
	
	private GradientPanel panel;
	private JComboBox<String> branchType;
	private ExcelJTable table;
	private JLabel label;
	private JScrollPane scrollPane; 
	private JButton confirmButton;
	
	private boolean isInEditMode, isAddingToExistingSubBand;
	private int selectedSubBandIndex, selectedBranchIndex;
	
	//Used when adding a new branch to a new sub-band
	public AddBranchJDialog(int widthIn, int heightIn, String titleIn, Image iconIn)
	{
		super(widthIn, heightIn, titleIn, iconIn);
		
		basicSetup();
		
		isInEditMode = false;
		isAddingToExistingSubBand = false;
		setVisible(true);
	}
	
	//Used when editing a branch
	public AddBranchJDialog(int widthIn, int heightIn, String titleIn, Image iconIn,
			int selectedSubBandIndex, int selectedBranchIndex)
	{
		super(widthIn, heightIn, titleIn, iconIn);
		
		basicSetup();
		
		isInEditMode = true;
		isAddingToExistingSubBand = false;
		
		this.selectedSubBandIndex = selectedSubBandIndex;
		this.selectedBranchIndex  = selectedBranchIndex;
		
		addSelectedBranchToDisplay();
		setBranchTypeToExisting();
		
		setVisible(true);
	}
	
	//Used when adding a new branch to an existing sub-band
	public AddBranchJDialog(int widthIn, int heightIn, String titleIn, Image iconIn,
			int selectedSubBandIndex)
	{
		super(widthIn, heightIn, titleIn, iconIn);
		
		basicSetup();
		
		isInEditMode = false;
		isAddingToExistingSubBand = true;
		
		this.selectedSubBandIndex = selectedSubBandIndex;
		
		setVisible(true);
	}
	
	private void setBranchTypeToExisting() 
	{
		SubBand selectedSubBand = SubBandContainer.getItemAt(selectedSubBandIndex);
		Branch selectedBranch   = selectedSubBand.getBranchAt(selectedBranchIndex);
		branchType.setSelectedItem(selectedBranch.getType());
	}
	
	private void addSelectedBranchToDisplay()
	{
		SubBand selectedSubBand = SubBandContainer.getItemAt(selectedSubBandIndex);
		Branch selectedBranch   = selectedSubBand.getBranchAt(selectedBranchIndex);
		
		ArrayList<Tuple> branchArray = selectedBranch.getBranchData();
		
		for(Tuple tuple : branchArray) 
		{
			int jValue = tuple.getJValue();
			double waveNumber = tuple.getWavenumber();
			
			((DefaultTableModel) table.getModel()).setValueAt(waveNumber + "", jValue, 1);
		}
	}

	private void basicSetup() 
	{
		initComponents();
		setComponentSettings();
		addComponents();
	}

	private void initComponents()
	{
		panel = new GradientPanel(BACKGROUND_COLOR_1, BACKGROUND_COLOR_2);
		branchType = new JComboBox<String>();
		table = new ExcelJTable();
		label = new JLabel("Branch Type");
		scrollPane = new JScrollPane(table);
		confirmButton = new JButton("Confirm");
	}
	
	private void setComponentSettings()
	{
		panel.setLayout(new GridBagLayout());
		
		label.setPreferredSize(new Dimension(150,30));
		label.setFont(new Font("Arial", Font.PLAIN, 18));
		label.setHorizontalAlignment(JLabel.RIGHT);
		
		branchType.setPreferredSize(new Dimension(100,30));
		branchType.setFocusable(false);
		((JLabel)branchType.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		
		branchType.setFont(new Font("Arial", Font.PLAIN, 22));
		branchType.addItem("Q");
		branchType.addItem("P");
		branchType.addItem("R");
		branchType.addItem("Q+");
		branchType.addItem("Q-");
		branchType.addItem("Q+<-");
		branchType.addItem("Q-<+");
		branchType.addItem("P+");
		branchType.addItem("P-");
		branchType.addItem("P+<-");
		branchType.addItem("P-<+");
		branchType.addItem("R+");
		branchType.addItem("R-");
		branchType.addItem("R+<-");
		branchType.addItem("R-<+");
		
		confirmButton.setPreferredSize(new Dimension(300,30));
		confirmButton.setFont(new Font("Arial", Font.PLAIN, 22));
		confirmButton.setFocusable(false);
		confirmButton.addActionListener(new EventHandling());
		
		String[] columnNames = {"J", "Wave-number", "Intensity"};
		Object[][] data = new Object[100][3];
		
		for(int j = 0; j < 100; j++) 
		{
			data[j][0] = j;
		}

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        
        table.setModel(model);
        table.getColumn(columnNames[1]).setPreferredWidth(230);
        table.getColumn(columnNames[2]).setPreferredWidth(230);
        
        scrollPane.setPreferredSize(new Dimension(400, 510));
	}
	
	private void addComponents()
	{
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.insets = new Insets(5,25,5,0);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(label, gbc);
		
		gbc.insets = new Insets(5,20,5,105);
		gbc.gridx = 1;
		gbc.gridy = 0;
		panel.add(branchType, gbc);
		
		gbc.gridwidth = 2;
		gbc.insets = new Insets(5,5,5,5);
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(scrollPane, gbc);
		
		gbc.insets = new Insets(20,15,5,15);
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
				ArrayList<Tuple> branchArr = new ArrayList<Tuple>();
				String transitionType = (String) branchType.getSelectedItem();
				
				CellEditor cellEditor = table.getCellEditor();
				
				if(cellEditor != null) 
				{
					table.getCellEditor().stopCellEditing();
				}

				for(int row = 0; row < table.getRowCount(); row++) 
				{
					if(table.getValueAt(row, 1) != null && table.getValueAt(row, 2) != null)
					{
						//if the wave-number or intensity field is blank, skip this row
						if(table.getValueAt(row, 1).equals("") || table.getValueAt(row, 2).equals("")) 
						{
							continue;
						}
						
						int jVal;
						double waveNum;
						
						try
						{
							jVal = Integer.parseInt(table.getValueAt(row, 0) + "");
							waveNum = Double.parseDouble(table.getValueAt(row, 1) + "");
						}catch(NumberFormatException nfe) 
						{
							JOptionPane.showMessageDialog(null, "Trouble reading one of the cells");
							return;
						}

						branchArr.add(new Tuple(jVal, waveNum));
					
					}
				}
				
				if(branchArr.size() == 0) 
				{
					JOptionPane.showMessageDialog(null, "No wavenumber values could be read in");
					return;
				}
				
				Branch newBranch = new Branch(transitionType, branchArr);
				
				//logic for deciding how to add the new Branch to the SubBandContainer
				if(isAddingToExistingSubBand) 
				{
					JOptionPane.showMessageDialog(null, "The branch was successfully added");
					SubBandContainer.addBranchToExistingSubBand(selectedSubBandIndex, newBranch);
				}else
				{
					if(isInEditMode) 
					{
						JOptionPane.showMessageDialog(null, "Edit Successful");
						SubBandContainer.swapBranchAt(selectedSubBandIndex, selectedBranchIndex, newBranch);
					}else 
					{
						SubBandContainer.addBranch(newBranch);
						AddAssignmentJDialog.updateTable(transitionType);
					}
				}
			}
			
			dispose();
		}
	}
}
