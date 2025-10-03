package com.framework.selenium.testcase.ManifestCheck;
import java.io.IOException;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.framework.pages.LoginPage;
import com.framework.pages.ManifestCheckPage;
import com.framework.selenium.api.base.SeleniumBase;
import com.framework.testng.api.base.ProjectSpecificMethods;
import com.framework.utils.DataLibrary;



public class MFC_floating_panel_upload extends ProjectSpecificMethods{
	static int row=1;

	@BeforeTest
	public void setTest() {
		testcaseName = "MFC_floating_panel_upload";
		testDescription = "Enable CI flag for the shipment thru floating panel";
		category = "REGRESSION";
		author = "Mphasis";
		excelFileName = "data";
		sheetName="MFC_FloatingPanel";
	}

	@Test(dataProvider = "getdata")


	public void mfc_Floating_Panel(String tcNo,String tcName,String country,String role,String awb,String clrsch)
			throws Throwable {

		ProjectSpecificMethods.methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		System.out.println("Execution Method Name : "+methodName.toUpperCase());

		int sheetRows = getRowCount(this.sheetName);	
		if(row<=sheetRows) {

			new LoginPage()
			.userAuthentication() 
			.OktaAuthentication()
			.select_Country_Role(country, role);		

			new ManifestCheckPage()
			.select_competency("MANIFEST_CHECK")
			.disable_Headercolumns()
			.enable_HeaderColumns()
			.click_TeamLeaderList()
			.select_Team("Phantom")
			.filter_TableColumns("CI Flag", " Blank Cells",row)
			.retrieve_AWBNo()
			.writeIntoExcelSheet("MFC_FloatingPanel", "AWB_NBR", row)
			.clickFilterIcon("AWB Number")
			.setFilterValueFromSheet("MFC_FloatingPanel", "AWB_NBR", row)	
			.close_FilterIcon()
			.assignTo()
			.search_user()
			.selectUser_Submit()
			.click_UserList()
			.clickFilterIcon("AWB Number")
			.setFilterValueFromSheet("MFC_FloatingPanel", "AWB_NBR", row)
			.close_FilterIcon()
			.click_Upload_FloatingPanel("true")
			.select_Location_documentType("NZ","CI")
			.uploadFile_FloatingPanel()
			.click_UserList()
			.Validate_CI_Flag("Y");
			row++;


		}
	}

}



