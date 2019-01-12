//http://esus.com/embedding-a-jtextarea-in-a-jtable-cell/

package loomisWood;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

class TextAreaRenderer extends JScrollPane implements TableCellRenderer
{
	private static final long serialVersionUID = 60734555541521365L;
	JTextArea textArea;
  
	public TextAreaRenderer() 
	{
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setFont(new Font("Arial", Font.PLAIN, 20));
		getViewport().add(textArea);
	}
  
	public Component getTableCellRendererComponent(JTable table, Object value,
                                  boolean isSelected, boolean hasFocus,
                                  int row, int column)
	{
		if (isSelected) 
		{
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
			textArea.setForeground(table.getSelectionForeground());
			textArea.setBackground(table.getSelectionBackground());
		} else 
		{
			setForeground(table.getForeground());
			setBackground(table.getBackground());
			textArea.setForeground(table.getForeground());
			textArea.setBackground(table.getBackground());
		}
  
		textArea.setText((String) value);
		textArea.setCaretPosition(0);
		return this;
	}
}
