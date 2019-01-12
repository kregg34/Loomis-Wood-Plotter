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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/*
 * This class matches the row position of the selected item to the array index position of the item.
 */

public class ViewAssignmentsJDialog extends CustomJDialog
{
	private final Image ICON;
	
	private static final long serialVersionUID = -7355682772558526099L;
	
	private static MenuJTable table;
	
	private JScrollPane scrollPane;
	private GradientPanel panel;
	private JButton backButton;
	private JMenuItem deleteItem, editItem, addItem;
	
	private int indexOfSelectedSubBand, indexOfSelectedBranch;
	
	private boolean isOnMainPage = true;

	public ViewAssignmentsJDialog(int widthIn, int heightIn, String titleIn, Image iconIn)
	{
		super(widthIn, heightIn, titleIn, iconIn);
		
		ICON = iconIn;
		
		initComponents();
		setComponentSettings();
		addComponents();
		addAssignments();
		
		setVisible(true);
	}

	private void clearTable()
	{
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);
	}
	
	private void addAssignments()
	{
		ArrayList<SubBand> list = SubBandContainer.getSubBandArray();
		
		for(SubBand band: list) 
		{
			((DefaultTableModel) table.getModel()).addRow(new Object[]{band.toString()});
		}
	}
	
	private void addSubBandContents() 
	{
		ArrayList<Branch> branchesArray = SubBandContainer.getItemAt(indexOfSelectedSubBand).getBranches();
		
		for(Branch branch : branchesArray) 
		{
			((DefaultTableModel) table.getModel()).addRow(new Object[]{branch.getType()});
		}
	}

	private void initComponents()
	{
		table = new MenuJTable();
		scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		panel = new GradientPanel(new Color(185, 188, 191), new Color(90, 92, 94));
		
		backButton = new JButton("Back");
		
		deleteItem = new JMenuItem("Delete");
		editItem = new JMenuItem("Edit");
		addItem = new JMenuItem("Add New Branch");
	}

	private void setComponentSettings()
	{
		panel.setLayout(new GridBagLayout());
		
		String[] header = {"Assignments"};
		Object[][] data = {};
		DefaultTableModel model = new DefaultTableModel(data, header);
		
		table.setModel(model);
		table.getTableHeader().setFont(new Font("SansSerif", Font.PLAIN, 18));
		table.setRowHeight(80);
		
		table.addMouseListener(new MouseAdapter() 
		{
	        public void mouseClicked (MouseEvent e)
	        {
	        	//https://stackoverflow.com/questions/3558293/java-swing-jtable-right-click-menu-how-do-i-get-it-to-select-aka-highlight-t
	        	int r = table.rowAtPoint(e.getPoint());
	            
	            if (r >= 0 && r < table.getRowCount()) 
	            {
	                table.setRowSelectionInterval(r, r);
	            } else 
	            {
	                table.clearSelection();
	            }

	            int rowindex = table.getSelectedRow();
	            
	            if (rowindex < 0) 
	            {
	                return;
	            }
	            
	            JTable table = (JTable) e.getSource();
	            
	            if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) 
	            {
	            	respondToClick(table.getSelectedRow());
	            	return;
	            }
	            
	            if(e.getButton() == MouseEvent.BUTTON3) 
	            {
	            	if(isOnMainPage)
	            	{
		                JPopupMenu popUp = new JPopupMenu();
		                popUp.add(addItem);
		                popUp.add(editItem);
		                popUp.add(deleteItem);
		                popUp.show(e.getComponent(), e.getX(), e.getY());
		            	return;
	            	}else
	            	{
		                JPopupMenu popup = new JPopupMenu();
		                popup.add(deleteItem);
		                popup.show(e.getComponent(), e.getX(), e.getY());
		            	return;
	            	}
	            }
	        }
	    });
		
		scrollPane.setPreferredSize(new Dimension(300,500));
		
		backButton.setPreferredSize(new Dimension(300,30));
		backButton.setFocusable(false);
		backButton.addActionListener(new EventHandling());
		backButton.setFont(new Font("Arial", Font.PLAIN, 20));
		backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		deleteItem.addActionListener(new EventHandling());
		deleteItem.setFont(new Font("Times new Roman", Font.PLAIN, 20));
		deleteItem.setForeground(Color.RED);
		
		editItem.addActionListener(new EventHandling());
		editItem.setFont(new Font("Times new Roman", Font.PLAIN, 20));
		editItem.setForeground(Color.BLACK);
		
		addItem.addActionListener(new EventHandling());
		addItem.setFont(new Font("Times new Roman", Font.PLAIN, 20));
		addItem.setForeground(Color.BLACK);
	}
	
	//This is called when an item is doubled clicked; (row equals the array index!)
	private void respondToClick(int row)
	{
		if(isOnMainPage) 
		{
			isOnMainPage = false;
			indexOfSelectedSubBand = row;
			clearTable();
			addSubBandContents();
		}else
		{
			indexOfSelectedBranch = row;
			new AddBranchJDialog(450, 660, "Edit Branch", ICON, indexOfSelectedSubBand, indexOfSelectedBranch);
			clearTable();
			addSubBandContents();
		}
	}

	private void addComponents()
	{
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.5;
		gbc.weighty = 0.99;
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(scrollPane, gbc);
		
		gbc.insets = new Insets(2,2,2,2);
		gbc.weighty = 0.01;
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(backButton, gbc);
		
		add(panel);
	} 
	
	private class EventHandling implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == backButton && !isOnMainPage) 
			{
				clearTable();
				addAssignments();
				isOnMainPage = true;
			}
			
			if(e.getSource() == deleteItem) 
			{
				if(isOnMainPage) 
				{
					 int input = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this sub-band?",
							 		"Confirm Delete", JOptionPane.YES_NO_OPTION);
					 
					 switch(input) 
					 {
					 	case 0: 
					 		int rowindex = table.getSelectedRow();
							SubBandContainer.deleteSubBandAt(rowindex);
							clearTable();
							addAssignments();
							return;
					 	default: 
					 		return;
					 }
				}else
				{
					 int input = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this branch?",
							 		"Confirm Delete", JOptionPane.YES_NO_OPTION);
					 
					 switch(input) 
					 {
						 case 0: 
						 	 int rowindex = table.getSelectedRow();
							 SubBandContainer.deleteBranchAt(rowindex, indexOfSelectedSubBand);
							 clearTable();
							 addSubBandContents();
							 return;
						 default: 
							 return;
					 }
				}
			}
			
			if(e.getSource() == editItem) 
			{
				int rowindex = table.getSelectedRow();
				
				if(rowindex < 0) 
				{
					return;
				}else 
				{
					new EditSubBandJDialog(400, 300, "Edit Subband", ICON, rowindex);
					clearTable();
					addAssignments();
				}
			}
			
			if(e.getSource() == addItem) 
			{
				new AddBranchJDialog(450, 660, "Add a New Branch", ICON, indexOfSelectedSubBand);
			}
		}
	}
}
