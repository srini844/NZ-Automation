package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcel {
	public static XSSFWorkbook workbook;
	public static XSSFSheet sheet;
	static String ReadFilePath = "input_data/ReadQuery.xlsx";
	public static  String readExcelValue(String sheetName, String columnName, int rowIndex) throws IOException {
		System.out.println("!!!!!!!! Inside Excel !!!!!!!!!!!!");
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(ReadFilePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Workbook wb = null;
		
		try {
			wb = WorkbookFactory.create(inputStream);
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Sheet sheet = wb.getSheet(sheetName);
		Row row = sheet.getRow(0);
		int cellNum = row.getPhysicalNumberOfCells();
		
		for (int i = 0; i < cellNum; i++) {
			
			if ((row.getCell(i).toString()).equals(columnName)) {
				
				System.out.println(sheet.getRow(rowIndex).getCell(i).toString());
				return sheet.getRow(rowIndex).getCell(i).toString();
			}
		}
		
		
		inputStream.close();
		return "";
	}
	
	
	public static int getRowCount(String sheetName) throws IOException {
		FileInputStream inputStream = null;
				inputStream = new FileInputStream(new File(ReadFilePath));
		workbook = new XSSFWorkbook(inputStream);
		sheet = workbook.getSheet(sheetName);
		int rowcount = sheet.getPhysicalNumberOfRows();
		//System.out.println("Total number of rows:"+rowcount);
		inputStream.close();
		return rowcount;
	}

}
