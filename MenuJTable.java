package loomisWood;

import javax.swing.JTable;

public class MenuJTable extends JTable
{
	private static final long serialVersionUID = 4488620853856172180L;

	public MenuJTable() 
	{
		addJTableRenderer();
	}
	
	private void addJTableRenderer()
	{
		setDefaultRenderer(Object.class, new TextAreaRenderer());
	}

	@Override
	public boolean isCellEditable(int row, int column) 
	{
		return false;
	}
}
