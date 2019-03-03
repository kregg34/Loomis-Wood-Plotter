/*Credit for Class: https://www.javaworld.com/article/2077579/learn-java/java-tip-77--enable-copy-and-paste-functionality-between-swing-s-jtables-and-excel.html
 *Code has been modified so it doesn't skip blank lines
 */

package loomisWood;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
/**
 * ExcelAdapter enables Copy-Paste Clipboard functionality on JTables.
 * The clipboard data format used by the adapter is compatible with
 * the clipboard format used by Excel. This provides for clipboard
 * interoperability between enabled JTables and Excel.
 */
public class ExcelAdapter implements ActionListener
{
	private Clipboard system;
	private StringSelection stsel;
	private JTable jTable1;
		
	/**
	 * The Excel Adapter is constructed with a
	 * JTable on which it enables Copy-Paste and acts
	 * as a Clipboard listener.
	 */
	public ExcelAdapter(JTable myJTable)
	{
		jTable1 = myJTable;
		KeyStroke copy = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK, false);
		// Identifying the copy KeyStroke user can modify this
		// to copy on some other Key combination.
		KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK, false);
		// Identifying the Paste KeyStroke user can modify this
		//to copy on some other Key combination.
		jTable1.registerKeyboardAction(this, "Copy", copy, JComponent.WHEN_FOCUSED);
		jTable1.registerKeyboardAction(this, "Paste", paste, JComponent.WHEN_FOCUSED);
		system = Toolkit.getDefaultToolkit().getSystemClipboard();
	}
	
	/*
	 * Public Accessor methods for the Table on which this adapter acts.
	 */
	public JTable getJTable() 
	{
		return jTable1;
	}
	
	public void setJTable(JTable jTable1) 
	{
		this.jTable1 = jTable1;
	}
	
	/*
	 * This method is activated on the Keystrokes we are listening to
	 * in this implementation. Here it listens for Copy and Paste ActionCommands.
	 * Selections comprising non-adjacent cells result in invalid selection and
	 * then copy action cannot be performed.
	 * Paste is done by aligning the upper left corner of the selection with the
	 * 1st element in the current selection of the JTable.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(jTable1.getRowCount() <= 0)
		{
			return;
		}
		
		if (e.getActionCommand().compareTo("Copy") == 0)
		{
			StringBuffer sbf = new StringBuffer();
			// Check to ensure we have selected only a contiguous block of
			// cells
			int numcols = jTable1.getSelectedColumnCount();
			int numrows = jTable1.getSelectedRowCount();
			int[] rowsSelected = jTable1.getSelectedRows();
			int[] colsSelected = jTable1.getSelectedColumns();
			
			if(colsSelected.length == 0 || rowsSelected.length == 0) 
			{
				return;
			}
			
			if (!((numrows-1 == rowsSelected[rowsSelected.length-1]-rowsSelected[0] &&
					numrows == rowsSelected.length) &&
					(numcols-1 == colsSelected[colsSelected.length-1]-colsSelected[0] &&
					numcols == colsSelected.length)))
			{
				JOptionPane.showMessageDialog(null, "Invalid Copy Selection",
						"Invalid Copy Selection", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			for (int i=0;i<numrows;i++)
			{
				for (int j=0;j<numcols;j++)
				{
					sbf.append(jTable1.getValueAt(rowsSelected[i],colsSelected[j]));
					if (j<numcols-1) sbf.append("\t");
				}
				sbf.append("\n");
			}
			
			stsel  = new StringSelection(sbf.toString());
			system = Toolkit.getDefaultToolkit().getSystemClipboard();
			system.setContents(stsel,stsel);
		}
		
		if (e.getActionCommand().compareTo("Paste") == 0)
		{
			if((jTable1.getSelectedRows()).length == 0 ||
					(jTable1.getSelectedColumns()).length == 0) 
			{
				return;
			}
			
			int startRow = (jTable1.getSelectedRows())[0];
			int startCol = (jTable1.getSelectedColumns())[0];
			
			try
			{
				String pasteString = (String)(system.getContents(this).getTransferData(DataFlavor.stringFlavor));
				String rowString = null, elementString = null;
				int row = 0, col = 0;
				
				Scanner scanRows = new Scanner(pasteString);
				Scanner scanCols = null;
				scanRows.useDelimiter("\n");
				
				//loop through the rows
				while(scanRows.hasNext())
				{
					col = 0; //reset column index for every new row
					rowString = scanRows.next();
					
					scanCols = new Scanner(rowString);
					scanCols.useDelimiter("\t");
					
					//loop through the elements in a row
					while(scanCols.hasNext()) 
					{
						elementString = scanCols.next();
						
						if (startRow + row < jTable1.getRowCount() && startCol + col < jTable1.getColumnCount())
						{
							jTable1.setValueAt(elementString, startRow + row, startCol + col);
							col++;
						}
					}
					
					row++;
				}

				scanRows.close();
				scanCols.close();
			}
			catch(IOException ioe)
			{
				System.out.println("ExcelAdapter: IOException");
			}
			catch(UnsupportedFlavorException ufe)
			{
				System.out.println("ExcelAdapter: UnsupportedFlavorException");
			}
		}
	}
}