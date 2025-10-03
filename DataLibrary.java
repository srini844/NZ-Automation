package com.framework.utils;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFName;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataLibrary {
	public static Object[][] readExcelData(String excelfileName, String sheetName) {

		XSSFWorkbook wbook;
		Object[][] value = null;
		try {
			Path path = FileSystems.getDefault().getPath("").toAbsolutePath();			
			//wbook = new XSSFWorkbook(path+"/TestData/" + excelfileName + ".xlsx");			
			wbook = new XSSFWorkbook("./TestData/" + excelfileName + ".xlsx");
			
			XSSFSheet sheet = wbook.getSheet(sheetName);
			//System.out.println("Total number of sheets in \""+excelfileName+"\" :"+wbook.getNumberOfSheets());

			int rowCount = sheet.getLastRowNum();
			System.out.println("Total row count:"+rowCount);
			int colCount = sheet.getRow(0).getLastCellNum();
			value = new Object[rowCount][colCount];
			for (int i = 1; i <= rowCount; i++) {				
					for (int j = 0; j < colCount; j++) {
						System.out.println(sheet.getRow(i).getCell(j).getStringCellValue());
						value[i - 1][j] = sheet.getRow(i).getCell(j).getStringCellValue();						
					}
				}
			
			wbook.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return value;
	}

}
