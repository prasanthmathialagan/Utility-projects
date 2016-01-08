package com.novelty.xml;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BLANK;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 	Excel utilities
 * @author prasanthm
 */
public class ExcelUtils
{
	private final static int  ROW_LIMIT = 65535;
	/**
	 * 		This method fetches all the values from spreadsheet with the given fileName,
	 * sheetNumber and column indices.
	 * 
	 * @param fileName
	 * @param columnIndices
	 * @param sheetNumber
	 * @return 2D array of values
	 * @throws Exception
	 */
	public static Object[][] fetchValues(String fileName,int[] columnIndices,int sheetNumber) throws Exception
	{
		return fetchValues(fileName, sheetNumber, new int[]{}, columnIndices);
	}
	
	/**
	 * 		This method fetches all the values from spreadsheet with the given fileName
	 * and sheetNumber.
	 * 
	 * @param fileName
	 * @param sheetNumber
	 * @return 2D array of values
	 * @throws Exception
	 */
	public static Object[][] fetchValues(String fileName,int sheetNumber) throws Exception
	{
		return fetchValues(fileName, sheetNumber, new int[]{});
	}
	
	/**
	 * 		This method fetches all the values from spreadsheet with the given fileName,
	 * sheetNumber and excluded row indices.
	 * 
	 * @param fileName
	 * @param sheetNumber
	 * @param excludedRows
	 * @return 2D array of values
	 * @throws Exception
	 */
	public static Object[][] fetchValues(String fileName,int sheetNumber,int[] excludedRows) throws Exception
	{
		return fetchValues(fileName, sheetNumber, excludedRows, null);
	}
	
	/**
	 * 		This method fetches all the values from spreadsheet with the given fileName,
	 * sheetNumber,excluded row indices and column indices.
	 * 
	 * @param fileName
	 * @param sheetNumber
	 * @param excludedRows
	 * @param columnIndices
	 * @return 2D array of values
	 * @throws Exception
	 */
	public static Object[][] fetchValues(String fileName,int sheetNumber,int[] excludedRows,int[] columnIndices) throws Exception
	{
		Workbook wb = getWorkBook(fileName);
		if (wb == null)
			throw new Exception("File extension is not supported yet...");
		Sheet sheet = wb.getSheetAt(sheetNumber);
		
		if(columnIndices == null)
			columnIndices = getColumnIndices(sheet);
		
		List<Object[]> listOfArr = new ArrayList<Object[]>();
		Iterator<Row> rows = sheet.rowIterator();
		while (rows.hasNext()) 
		{
			Row currentRow = rows.next();
			if (isExcludedRow(currentRow, excludedRows)) 
				continue;
				
			Object[] objArr = getRowValues(columnIndices, currentRow);
			listOfArr.add(objArr);
		}
		return listOfArr.toArray(new Object[0][0]);	
	}
	
	private static int[] getColumnIndices(Sheet s)
	{
		int lastIndex = 0;
		Iterator<Row> rows = s.rowIterator();
		while (rows.hasNext()) 
		{
			Row currentRow = rows.next();
			int lastCellIndex = currentRow.getLastCellNum();
			if(lastCellIndex > lastIndex)
				lastIndex = lastCellIndex;
		}
		
		int[] columnIndices = new int[lastIndex];
		for (int i = 0; i < lastIndex; i++)
		{
			columnIndices[i]=i;
		}
		
		return columnIndices;
	}
	
	// For the given row, get the values.
	private static Object[] getRowValues(int[] columnIndices, Row currentRow)
	{
		Object[] objArr = new Object[columnIndices.length];
		for (int i = 0; i < columnIndices.length; i++) 
		{
			int columnIndex = columnIndices[i];
			Cell currentCell = currentRow.getCell(columnIndex,Row.CREATE_NULL_AS_BLANK);
			objArr[i] = getCellValue(currentCell);
		}
		return objArr;
	}
	
	/*
	* For getting appropriate workbook object for the given file.
	* This method is used while reading from spreadsheet.
	*/
	private static Workbook getWorkBook(String fileName) throws IOException
	{
		FileInputStream fis = new FileInputStream(fileName);
		try
		{
			if (fileName.endsWith("xls")) 
				return new HSSFWorkbook(fis);
			else if(fileName.endsWith("xlsx"))
				return new XSSFWorkbook(fis);
		} 
		catch (Exception e){}
		finally
		{
			fis.close();
		}
		return null;
	}
	
	/*
	 * For creating appropriate workbook object for the given file.
	 * This method is used while creating new spreadsheet.
	 */
	private static Workbook createWorkBook(String fileName)
	{
		if (fileName.endsWith("xls")) 
			return new HSSFWorkbook();
		else if(fileName.endsWith("xlsx"))
			return new XSSFWorkbook();
		return null;
	}
	
	// For writing workbook
	private static void writeWorkBook(Workbook wb,String fileName) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(fileName,true);
		wb.write(fos);
	}
	
	// For getting appropriate object for the given cell
	private static Object getCellValue(Cell cell)
	{
		int cellType = cell.getCellType();
		switch (cellType)
		{
			case CELL_TYPE_NUMERIC:
				return (long)cell.getNumericCellValue();				
			case CELL_TYPE_STRING:
				return cell.getStringCellValue();
			case CELL_TYPE_BOOLEAN:
				return cell.getBooleanCellValue();
			case CELL_TYPE_BLANK:
				return "";
			default:
				return "";
		}
	}
	
	// check for excluded row
	private static boolean isExcludedRow(Row row ,int[] excludedRows)
	{
		for (int index : excludedRows) 
		{
			if (row.getRowNum() == index) 
				return true;
		}
		return false;
	}
	
	/**
	 * 		This method is used for writing values into the spreadsheet.
	 * 
	 * @param values
	 * @param headers
	 * @param fileName
	 * @param sheetName
	 * @throws IOException
	 */
	public static void writeData(Object[][] values,String[] headers,String fileName,String sheetName) throws IOException
	{
		Workbook wb = createWorkBook(fileName);
		int length = values.length;
		int counter = 0;
		int fileNameIndex=1;
		String oldFileName = fileName;
		while(length > ROW_LIMIT)
		{
			Object[][] newValues = Arrays.copyOfRange(values, counter, counter+ROW_LIMIT);
			writeData(newValues, headers, wb,fileName, sheetName);
			length = length - ROW_LIMIT;
			counter = counter + ROW_LIMIT;
			fileName = getFileName(oldFileName,fileNameIndex);
			fileNameIndex++;
			wb = createWorkBook(fileName);
		}
		if(length >= 0)
		{
			Object[][] newValues = Arrays.copyOfRange(values, counter, counter+length);
			writeData(newValues, headers, wb,fileName, sheetName);
		}
	}

	private static String getFileName(String fileName,int fileNameIndex)
	{
		int index = fileName.lastIndexOf(File.separator);
		if(index == -1)
			return fileName;
		else
		{
			String modifiedFileName = fileName.substring(index+1);
			modifiedFileName = fileName.substring(0, index) + File.separator + fileNameIndex + "_"
					+ modifiedFileName;
			return modifiedFileName;
		}
	}
	
	/**
	 * 		This method is used for writing values into the spreadsheet for the given workbook.
	 * This method can be used for adding additional sheets to the existing workbook.
	 * 
	 * @param values
	 * @param headers
	 * @param wb
	 * @param fileName
	 * @param sheetName
	 * @throws IOException
	 */
	public static void writeData(Object[][] values,String[] headers,Workbook wb,String fileName,String sheetName) throws IOException
	{
		Sheet sheet = wb.createSheet(sheetName);
		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++)
		{
			Cell cell = headerRow.createCell(i);
			String header = headers[i];
			cell.setCellValue(header);
		}
		
		int rowNum = 1;
		for (Object[] objects : values) 
		{
			Row row = sheet.createRow(rowNum);
			for (int i = 0; i < objects.length; i++) 
			{
				Cell cell = row.createCell(i);
				Object obj = objects[i];
				setCellValue(obj, cell);
			}
			rowNum++;
		}
		writeWorkBook(wb, fileName);
	}
	
	// Setting cell value based on Object type
	private static void setCellValue(Object o,Cell c)
	{
		if(o == null)
		{
			c.setCellValue("");
			return ;
		}
		if (o instanceof String) 
			c.setCellValue((String)o);
		else if (o instanceof Number)
			c.setCellValue(((Number)o).longValue());
		else if (o instanceof Boolean)
			c.setCellValue(((Boolean)o).booleanValue());
		else 
			c.setCellValue(o.toString());
	}
}
