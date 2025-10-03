package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class WriteExcel {
	
	static String writeFilePath = "output_data/Writedata.xlsx";
	
	public static void writeExcelValue(String sheetName, String columnName, int rowIndex, String firstRecordAWB) throws IOException, InterruptedException {
		FileInputStream inputStream = new FileInputStream(new File(writeFilePath));
		/*
		 * try { inputStream = new FileInputStream(new File(writeFilePath)); } catch
		 * (FileNotFoundException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
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
				Cell cell = sheet.getRow(rowIndex).getCell(i);
				cell.setCellValue(firstRecordAWB);
				inputStream.close();
			}
		}
		
		FileOutputStream fos = new FileOutputStream(new File(writeFilePath));
		wb.write(fos);
		Thread.sleep(3000);
		fos.close();
	}

}
