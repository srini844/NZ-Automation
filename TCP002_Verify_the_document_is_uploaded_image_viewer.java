package com.framework.selenium.testcase.ManifestCheck;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.framework.pages.LoginPage;
import com.framework.pages.ManifestCheckPage;
import com.framework.testng.api.base.ProjectSpecificMethods;
import com.framework.utils.DataLibrary;

public class TCP002_Verify_the_document_is_uploaded_image_viewer extends ProjectSpecificMethods{
	static int row=1;

	@BeforeTest
	public void setTest() {
		testcaseName = "MFC_image_panel_upload";
		testDescription = "Enable CI flag for the shipment thru image panel";
		category = "REGRESSION";
		author = "Mphasis";
		excelFileName = "data";
		sheetName="MFC_ImagePanel";
	}


	@Test(dataProvider = "getdata")//,dependsOnMethods = {"com.framework.selenium.testcase.MFC_floating_panel_upload.mfc_FloatingPanel"})
	public void mfc_Image_Upload_Panel(String tcNo,String name,String country,String role,String awb,String clrsch)
			throws Throwable {

		ProjectSpecificMethods.methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		System.out.println("!*** Execution Method Name : "+methodName.toUpperCase());

		int sheetRows = getRowCount(this.sheetName);	
		if(row<=sheetRows) {

		}
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
		.retrieve_AWBNo()
		.writeIntoExcelSheet("MFC_ImagePanel", "AWB_NBR", row)
		.clickFilterIcon("AWB Number")
		.setFilterValueFromSheet("MFC_ImagePanel", "AWB_NBR", row)	
		.close_FilterIcon()
		.assignTo()
		.search_user()
		.selectUser_Submit()
		.click_UserList()
		.clickFilterIcon("AWB Number")
		.setFilterValueFromSheet("MFC_ImagePanel", "AWB_NBR", row)
		.close_FilterIcon()
		.click_Upload_FloatingPanel("true")
		.select_Location_documentType("NZ","PERMIT")
		.uploadFile_FloatingPanel()
		.click_UserList()
		.unassignAllShipemnt_UserList();
		row++;



	}

}

