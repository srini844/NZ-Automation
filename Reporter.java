package com.framework.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.openqa.selenium.remote.UnreachableBrowserException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.MediaEntityModelProvider;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.framework.selenium.api.base.DriverInstance;
import com.framework.testng.api.base.ProjectSpecificMethods;

public abstract class Reporter extends DriverInstance {
	File folder;
	private static ExtentReports extent;
	private static final ThreadLocal<ExtentTest> parentTest = new ThreadLocal<ExtentTest>();
	private static final ThreadLocal<ExtentTest> test = new ThreadLocal<ExtentTest>();
	private static final ThreadLocal<String> testName = new ThreadLocal<String>();
	private static final HashMap<String, Object> jsonString = new HashMap<String, Object>();
	private String fileName = "NZ_Automation_Test_Result.html";
	private String pattern = "dd-MMM-yyyy HH-mm";

	public String testcaseName, testDescription, author, category, dataFileName, dataFileType, excelFileName,sheetName;
	public static String folderName = "";
	
	@BeforeSuite(alwaysRun = true)	
	public synchronized void startReport() {
			String date = new SimpleDateFormat(pattern).format(new Date());
			//folderName = "reports/" + date;
			folderName = date;

			folder = new File("./reports/" + folderName);
			if (!folder.exists()) {
				folder.mkdir();
			}
		

		//ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("./" + folderName + "/" + fileName);
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(folder + "/" + fileName);
		htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
		htmlReporter.config().setChartVisibilityOnOpen(!true);
		htmlReporter.config().setTheme(Theme.STANDARD);
		htmlReporter.config().setDocumentTitle("GEMINI_Automation_Result");
		htmlReporter.config().setEncoding("utf-8");
		htmlReporter.config().setReportName("GEMINI_Execution_Report");
		htmlReporter.setAppendExisting(true);
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
	}
	

	@BeforeClass(alwaysRun = true)
	public synchronized void startTestCase() {
			ExtentTest parent = extent.createTest(testcaseName, testDescription);
			parent.assignCategory(category);
			parent.assignAuthor(author);
			parentTest.set(parent);
			testName.set(testcaseName);
		}

	

	public synchronized void setNode() {
		ExtentTest child = parentTest.get().createNode(getTestName());
		test.set(child);
	}

	public abstract long takeSnap();

	public void reportStep(String desc, String status, boolean bSnap) {
		synchronized (test) {

			MediaEntityModelProvider img = null ;
			if (bSnap && !(status.equalsIgnoreCase("INFO") || status.equalsIgnoreCase("skipped")
					|| status.equalsIgnoreCase("pass"))) {
				long snapNumber = 100000L;
				snapNumber = takeSnap();

				try {
					img = MediaEntityBuilder
							.createScreenCaptureFromPath(folder+"/images/" + snapNumber + ".png").build();
							//.createScreenCaptureFromPath(folder+"/" + snapNumber + ".png").build();
				} catch (IOException e) {

				}

			}

			if (status.equalsIgnoreCase("pass")) {
				test.get().pass(desc, img);
			} else if (status.equalsIgnoreCase("fail")) {
				test.get().fail(desc, img);
				throw new RuntimeException("See execution report for failure details");
			} else if (status.equalsIgnoreCase("warning")) {
				test.get().warning(desc, img);

			} else if (status.equalsIgnoreCase("skipped")) {
				test.get().skip("The test is skipped due to dependency failure");

			}else if(status.equalsIgnoreCase("INFO")) {
				
				test.get().info(desc);
			}
		}
	}
	
	
	public void reportStep(String desc , String status) {
		reportStep(desc, status,true);	
	}
	
	@AfterSuite(alwaysRun = true)
	public synchronized void endResult() {
		try {
			if(getDriver()!=null) {
				getDriver().quit();
			}
		}catch(UnreachableBrowserException e) {
			e.getMessage();
		}
		extent.flush();
	}
	
	
	public void setValue(String key, String value) {
		jsonString.put(key, value);		
	}
	
	public void setValue(String key, int value) {
		jsonString.put(key, value);		
	}
	
	public Object getValue(String key) {
		return jsonString.get(key);
	}
	
	public String getTestName() {
		return testName.get();
	}
	
	
	public Status getTestStatus() {
		
		return parentTest.get().getModel().getStatus();
	}
	
	
	//TO log API steps

		public static void reportRequest(String desc, String status) {
			
			MediaEntityModelProvider img = null;
			if(status.equalsIgnoreCase("PASS")) {
				test.get().pass(desc, img);		
			}else if(status.equalsIgnoreCase("FAIL")) {
				test.get().fail(desc, img);
				throw new RuntimeException();
			}else if(status.equalsIgnoreCase("WARNING")) {
				test.get().info(desc, img);		
			}else {
				test.get().warning(desc);
			}	
	
		}
		
	

}
