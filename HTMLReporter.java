package com.framework.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.MediaEntityModelProvider;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public abstract class HTMLReporter {

	public static ExtentHtmlReporter html;
	public static ExtentReports extent;
	public static ExtentTest svcTest;
	public ExtentTest testSuite, test;
	public String authors,category,testCaseName, testDescription,nodes, dataFileName,dataFileType;
	private String pattern = "dd-MMM-yyyy HH-mm";
	public static String folderName = "";
	private String fileName = "API_TestExecution_Report.html";

	
	public synchronized void startReport() {
		String date = new SimpleDateFormat(pattern).format(new Date());
		folderName = "./reports/" + date;

		File folder = new File(folderName);
		if (!folder.exists()) {
			folder.mkdir();
		}

		
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("./src/main/resources/config.properties")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		html = new ExtentHtmlReporter(folder + "/" + fileName);
		html.setAppendExisting(false);// include all result
		html.loadXMLConfig("./src/main/resources/extent-config.xml");
		extent = new ExtentReports();
		extent.attachReporter(html);		
	}

	// Every @Test
	public ExtentTest startTestCase(String testCaseName, String testDescription) {
		testSuite = extent.createTest(testCaseName, testDescription);
		testSuite.assignAuthor(authors);
		testSuite.assignCategory(category);
		return testSuite;
	}

	// every test data
	public ExtentTest startTestModule(String nodes) {
		test = testSuite.createNode(nodes);
		return test;
	}
// write the report
	public void endResult() {
		extent.flush();
	}

	public abstract long takeSnap();

	
	
	//To log UI steps
	public void reportStep(String desc,String status, boolean bSnap) {

		MediaEntityModelProvider img = null;

		if(bSnap && !status.equalsIgnoreCase("INFO")){

			long snapNumber = 100000L;
			snapNumber = takeSnap();
			try {
				img = MediaEntityBuilder.createScreenCaptureFromPath
						("./reports/images/"+snapNumber+".png").build();
			} catch (IOException e) {

			}
		}		

		if(status.equalsIgnoreCase("PASS")) {
			test.pass(desc, img);		
		}else if(status.equalsIgnoreCase("FAIL")) {
			test.fail(desc, img);
			throw new RuntimeException();
		}else if(status.equalsIgnoreCase("WARNING")) {
			test.warning(desc, img);		
		}else {
			test.info(desc);
		}
	}

	public void reportStep(String desc, String status) {
		reportStep(desc, status, true);
	}

	//To log Rest Steps
	public static void reportRequest(String desc, String status) {
		
		MediaEntityModelProvider img = null;
		if(status.equalsIgnoreCase("PASS")) {
			svcTest.pass(desc, img);		
		}else if(status.equalsIgnoreCase("FAIL")) {
			svcTest.fail(desc, img);
			throw new RuntimeException();
		}else if(status.equalsIgnoreCase("WARNING")) {
			svcTest.warning(desc, img);		
		}else {
			svcTest.info(desc);
		}	
	}


}
