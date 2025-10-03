package com.framework.testng.api.base;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import com.framework.selenium.api.base.SeleniumBase;
import com.framework.utils.DataLibrary;

public class ProjectSpecificMethods extends SeleniumBase {

	public static String methodName;
	
	@DataProvider(name = "getdata")
	public Object[][] fetchData() throws IOException {
		return DataLibrary.readExcelData(excelFileName, sheetName);
	}

	
	@BeforeMethod(alwaysRun = true)
	
	
	public void init() throws IOException {
		
		
			if (getValueFromConfig("environment").equalsIgnoreCase("release")) { 
				startApp(getValueFromConfig("url_release"), getValueFromConfig("executionMode"), false);
			}
			else if (getValueFromConfig("environment").equalsIgnoreCase("dev")) {
				startApp(getValueFromConfig("url_dev"), getValueFromConfig("executionMode"), false);
			}
			else {
				startApp(getValueFromConfig("url_uat"), getValueFromConfig("executionMode"), false);
			}

		}
	

	@AfterMethod
	public void terminate() {
		quit();
		
	}

}
