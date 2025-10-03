package com.framework.selenium.testcase.ManifestCheck;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.framework.pages.LoginPage;
import com.framework.pages.ManifestCheckPage;
import com.framework.testng.api.base.ProjectSpecificMethods;

public class MFC_modify_clearance_scheme extends ProjectSpecificMethods{
	static int row=1; 

	@BeforeTest
	public void setTest() {
		testcaseName = "MFC_modify_clearance_scheme";
		testDescription = "Modify clearance scheme in MF page";
		category = "REGRESSION";
		author = "Mphasis";
		excelFileName = "data";
		sheetName="MFC_ModifyClrScheme";
	}


	@Test(dataProvider = "getdata")
	public void mfc_Modify_Clearance_Scheme(String tcNo,String name,String country,String role,String awb,String clrsch)
			throws Throwable {		

		ProjectSpecificMethods.methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		System.out.println("<==> Execution Started for Method : "+methodName.toUpperCase());

		int sheetRows = getRowCount(this.sheetName);	
		if(row<=sheetRows-1) {
			
			new LoginPage()		
			.userAuthentication() 
			.OktaAuthentication()		
			.select_Country_Role(country, role);		

			new ManifestCheckPage()
			.select_competency("MANIFEST_CHECK")
			.disable_Headercolumns()
			.enable_HeaderColumns()
			.click_TeamList()
			.select_Team("Phantom")
			.clickFilterIcon("Clearance Scheme")
			.setFilterValueFromSheet("MFC_ModifyClrScheme", "CLR_SCH", row)
			.close_FilterIcon()
			.retrieve_AWBNo()
			.writeIntoExcelSheet("MFC_ModifyClrScheme", "AWB_NBR", row)		
			.assignToMe()
			.click_UserList()		
			.clickFilterIcon("AWB Number")
			.setFilterValueFromSheet("MFC_ModifyClrScheme", "AWB_NBR", row)	
			.close_FilterIcon()			
			.click_btn_modify_Clearance_Scheme()
			.set_clearance_scheme(readExcelValue("MFC_ModifyClrScheme", "CLR_SCH", row))
			.click_global_search_icon()
			.enter_shipment_global_search(readExcelValue("MFC_ModifyClrScheme", "AWB_NBR", row))
			.click_shipment_search()
			.retrieve_glblSrch_application_value("Clearance Scheme");
			
			
			if(!readExcelValue("MFC_ModifyClrScheme", "CLR_SCH", row).equals(ManifestCheckPage.tValue))		
				reportStep("Clearance Scheme Modified Successfully", "Pass");
			
			row++;
			;

		}

	}

}




