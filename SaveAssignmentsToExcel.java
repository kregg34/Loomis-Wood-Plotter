package loomisWood;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/* reads in assignments from an excel peak list file and 
 * adds them into SubBandContainers list of assignments
*/

public class SaveAssignmentsToExcel
{
	private File excelFile;
	private JFileChooser fileChooser;
	private XSSFWorkbook workBook;
	private Sheet sheet;
	private FileInputStream fileInputStream;
	
	
	public SaveAssignmentsToExcel()
	{
		initComponents();
		setComponentSettings();
		promptForFile();
		
		if(excelFile != null) 
		{
			try 
			{
				fileInputStream = new FileInputStream(excelFile);
				workBook = new XSSFWorkbook(fileInputStream);
			} catch (EncryptedDocumentException | IOException e)
			{
				JOptionPane.showMessageDialog(null, "Issue reading the excel file. Make sure the file is closed.");
			}
			
			workBook.setMissingCellPolicy(MissingCellPolicy.CREATE_NULL_AS_BLANK);
			
			if(workBook.getNumberOfSheets() == 0)
			{
				JOptionPane.showMessageDialog(null, "There are no sheets contained in the file!");
				return;
			}else 
			{
				//Assumes the 1st sheet is the right one
				sheet = workBook.getSheetAt(0);
				goThroughAssignments();
			}
			
			createOutputFile();
		}
	}
	
	
	private void createOutputFile() 
	{
	    try 
	    {
	    	fileInputStream.close();
	    	
            FileOutputStream outFile = new FileOutputStream(excelFile);
            workBook.write(outFile);
            outFile.close();
	    }
	    catch (IOException e) 
	    {
			JOptionPane.showMessageDialog(null, "Error, could not create output file...");
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
	
	
	private void goThroughAssignments() 
	{
		int numOfRow = sheet.getPhysicalNumberOfRows();
		int firstRow = getFirstNumericRow();
		int lastRow = numOfRow;
		
		if(firstRow != -1) 
		{
			ArrayList<SubBand> subBandArray = SubBandContainer.getSubBandArray();
			
			for(SubBand band: subBandArray)
			{
				for(Branch branch: band.getBranches()) 
				{
					for(AssignedPoint point: branch.getBranchData())
					{
						double assignedPointWaveNumber = point.getWavenumber();
						int rowIndex = binarySearch(firstRow - 1, lastRow - 1, assignedPointWaveNumber); //-1 to convert to an index
						
						if(rowIndex != -1) 
						{
							String pointInfo = branch.getType() + "(" + point.getJValue() + ") " + band.getUpperK()
												+ "-" + band.getLowerK() + " vt=" + band.getUpperVt() 
												+ "-" + band.getLowerVt() + " Note: " + band.getNote();
							
							addAssignmentToRow(pointInfo, rowIndex);
						}else 
						{
							continue;
						}

					}
				}
			}
			
			JOptionPane.showMessageDialog(null, "Successfully added the assignments to the Excel sheet");
		}else
		{
			JOptionPane.showMessageDialog(null, "Could not find a numeric cell type within the first 100 cells. Exiting...");
			return;
		}
	}
	

	private void addAssignmentToRow(String pointInfo, int rowIndex)
	{
		Row row = sheet.getRow(rowIndex);
		final int STARTING_COL_NUM = 2;
		
		Cell cell;
		cell = getNextEmptyCell(row, STARTING_COL_NUM);
		
		if(cell == null) 
		{
			JOptionPane.showMessageDialog(null, "errorrrr");
			return;
		}
		
		cell.setCellValue(pointInfo);
	}

	
	//Returns the next blank cell in a row to avoid overwriting pre-existing data
	private Cell getNextEmptyCell(Row row, int columnNum)
	{
		Cell cell = null;
		
		for(; columnNum < 10; columnNum++)
		{
			cell = row.getCell(columnNum);
			
			if(cell.getCellTypeEnum() == CellType.BLANK)
			{
				cell.setCellType(CellType.STRING);
				return cell;
			}else
			{
				if(cell.getCellTypeEnum() == CellType.STRING)
				{
					if(cell.getStringCellValue().equals("")) 
					{
						return cell;
					}
				}
			}
		}
		
		return cell;
	}
	
	//returns the row of the found row, otherwise returns -1 if nothing found
	private int binarySearch(int firstRow, int lastRow, double assignedPointWaveNumber) 
	{
		final int DATA_SIZE = lastRow - firstRow + 1;
		final int NUM_ITERATIONS = (int) (Math.log(DATA_SIZE) / Math.log(2)) + 1; //Log base 2 of the data size
		final double TOLERANCE = 0.0001;
		final int WAVE_NUM_INDEX = 0;
		int middleRow = DATA_SIZE/2 + firstRow; //-1 to convert to an index
		
		//Start the binary search in the middle of the data
		Row currentRow = sheet.getRow(middleRow);
		Cell currentCell;
		
		//Loop through the binary search
		for(int i = 0; i < NUM_ITERATIONS; i++) 
		{
			currentCell = currentRow.getCell(WAVE_NUM_INDEX);
			
			if(currentCell == null)
			{
				JOptionPane.showMessageDialog(null, "Trouble reading column one");
				return -1;
			}
			
			double cellValue = currentCell.getNumericCellValue();
			
			//Found a match
			if(Math.abs(cellValue - assignedPointWaveNumber) <= TOLERANCE) 
			{
				return middleRow;
			}
			
			//Not a match, repeat binary search with a new point
			if(cellValue > assignedPointWaveNumber) 
			{
				lastRow = middleRow - 1;
			}else
			{
				firstRow = middleRow + 1;
			}
			
			middleRow = (lastRow + firstRow) / 2;
			currentRow = sheet.getRow(middleRow);
		}
		
		return -1;
	}
	
	
	private int getFirstNumericRow()
	{
		for(int i = 0; i < 100; i++)
		{
			Row currentRow = sheet.getRow(i);
			Cell currentCell = currentRow.getCell(0);
			
			if(currentCell.getCellTypeEnum() == CellType.NUMERIC) 
			{
				return i+1;
			}
			
		}
		
		return -1;
	}
}
