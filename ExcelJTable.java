package loomisWood;

import java.awt.Font;

import javax.swing.JTable;

public class ExcelJTable extends JTable
{
	private static final long serialVersionUID = 4223532037774266459L;
	
	public ExcelJTable() 
	{
		new ExcelAdapter(this);
		
		setRowHeight(30);
		setCellSelectionEnabled(true);
		setFont(new Font("Arial", Font.PLAIN, 22));
	}
}
