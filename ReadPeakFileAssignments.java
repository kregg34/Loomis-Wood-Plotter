package loomisWood;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/* reads in assignments from an excel peak list file and 
 * adds them into SubBandContainers list of assignments
*/

public class ReadPeakFileAssignments
{
	private File excelFile;
	private JFileChooser fileChooser;
	private Workbook workBook;
	private Sheet sheet;
	
	public ReadPeakFileAssignments()
	{
		initComponents();
		setComponentSettings();
		promptForFile();
		
		if(excelFile != null) 
		{
			try 
			{
				workBook = WorkbookFactory.create(excelFile);
			} catch (EncryptedDocumentException | InvalidFormatException | IOException e)
			{
				JOptionPane.showMessageDialog(null, "Issue reading the excel file. Make sure the file is closed.");
			}
			
			workBook.setMissingCellPolicy(MissingCellPolicy.CREATE_NULL_AS_BLANK);
			
			sheet = workBook.getSheetAt(0);
			
			goThroughFile();
		}
	}
	
	private void initComponents() 
	{
		fileChooser = new JFileChooser();
	}
	
	private void setComponentSettings()
	{
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel files", "xlsx");
		fileChooser.setPreferredSize(new Dimension(1000, 618));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setFileFilter(filter);
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home"), "Desktop"));
	}
	
	private void promptForFile()
	{
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
		{
			excelFile = fileChooser.getSelectedFile();
		}
	}
	
	private void goThroughFile() 
	{
		int numOfRow = sheet.getPhysicalNumberOfRows();
		
		for(int i = 0; i < numOfRow; i++) 
		{
			parseLine(sheet.getRow(i));
		}
	}
	
	private void parseLine(Row row) 
	{
		Cell cell = row.getCell(2);
		String cellContent;
		
		if(cell.getCellTypeEnum() == CellType.STRING)
		{
			cellContent = cell.getStringCellValue();
			
			if(!cellContent.equals("")) 
			{
				double waveNumberValue = getWaveNumberValue(row);
				parseCellContents(cellContent);
			}
		}
	}
	
	private void parseCellContents(String cellContent) 
	{
		//ignore water lines
		if(cellContent.equals("H2O")) 
		{
			return;
		}
		
		
	}
	
	//returns -1 if there is an issue reading the cell
	private double getWaveNumberValue(Row row)
	{
		Cell cell = row.getCell(0);
		double waveNumberValue;

		if(cell.getCellTypeEnum() != CellType.NUMERIC) 
		{
			//This is an issue since the wave number is a double value
			waveNumberValue = -1;
		}else
		{
			waveNumberValue = cell.getNumericCellValue();
		}
		
		return waveNumberValue;
	}
}
