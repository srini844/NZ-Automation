package com.framework.selenium.api.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;


public class DriverInstance {
	String excelFilePath = "TestData/data.xlsx";
	
	private static final ThreadLocal<RemoteWebDriver> remoteWebDriver = new ThreadLocal<RemoteWebDriver>();
	private static final ThreadLocal<WebDriverWait> wait = new ThreadLocal<WebDriverWait>();
	
	public void setWait() {		
		wait.set(new WebDriverWait(getDriver(), Duration.ofSeconds(25)));
	}
	
	public WebDriverWait getWait() {
		return wait.get();
		
	}
	
	
	public void setDriver(String browser, String executionMode) {
		
		switch(browser) {
		case "chrome" :
			if(executionMode.equalsIgnoreCase("grid")) {				
				ChromeOptions options = new ChromeOptions();
				DesiredCapabilities dc = new DesiredCapabilities();
				dc.setPlatform(Platform.LINUX);
				dc.setBrowserName("chrome");
				dc.setVersion("117.0");
				dc.setCapability(ChromeOptions.CAPABILITY, options);
				try {
					//RemoteWebDriver driver = new RemoteWebDriver(new URL("http://4.240.84.204:4445/wd/hub"), dc);
					remoteWebDriver.set(new RemoteWebDriver(new URL("http://4.240.84.204:4445/wd/hub"), dc));
					//System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
				} catch (MalformedURLException e) {
					System.out.println("Unable to initiate grid execution");
					e.printStackTrace();					
				}
			}else {	
				try {				
					//System.setProperty("webdriver.chrome.silentOutput", "true");
					//WebDriverManager.chromedriver().setup();
					System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
					ChromeOptions options = new ChromeOptions();
					options.addArguments("--start-maximized");					
					options.addArguments("--disable-notifications");
					options.addArguments("--remote-allow-origins=*");
					//options.addArguments("--incognito");
					options.addArguments("--force-device-scale-factor=0.90");
					System.out.println("Chrome Browser started to launch......");
					remoteWebDriver.set(new ChromeDriver(options));
					
					
				} catch (Exception e) {
					System.out.println("Unable to launch Chrome browser."
							+ "Refer report for error.");
					e.printStackTrace();
				}				
			}
			break;
			
		case "edge":
			
			System.setProperty("webdriver.edge.driver", "./drivers/msedgedriver.exe");
			remoteWebDriver.set(new EdgeDriver());
			EdgeOptions options = new EdgeOptions();
			options.addArguments("--start-maximized");
			System.out.println("Edge browser started to launch......");
			break;			
			
		case "firefox":
			System.setProperty("webdriver.gecko.driver", "./drivers/firefoxDriver.exe");
			remoteWebDriver.set(new FirefoxDriver());
			System.out.println("Firefox browser started to launch......");
			break;
		}
		
	}
	
	public RemoteWebDriver getDriver() {		
		return remoteWebDriver.get();
	}
	
	public String readExcelValue(String sheetName, String columnName, int rowIndex) throws IOException {
		System.out.println("*** Reading From Excel ***");
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(excelFilePath));
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
	
	
	public void writeExcelValue(String sheetName, String columnName, int rowIndex, String firstRecordAWB) throws IOException, InterruptedException {
		System.out.println("*** Writing into Excel ***");
		FileOutputStream fos;
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(excelFilePath));
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
				Cell cell = sheet.getRow(rowIndex).getCell(i);
				cell.setCellValue(firstRecordAWB);					
				inputStream.close();
			}
		}
		fos = new FileOutputStream(new File(excelFilePath));
		wb.write(fos);
		Thread.sleep(3000);
		fos.close();
	}
	



	
	
}
