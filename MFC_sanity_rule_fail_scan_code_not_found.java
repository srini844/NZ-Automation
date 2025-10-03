package com.framework.selenium.testcase.ManifestCheck;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.framework.pages.LoginPage;
import com.framework.pages.ManifestCheckPage;
import com.framework.testng.api.base.ProjectSpecificMethods;

public class MFC_sanity_rule_fail_scan_code_not_found extends ProjectSpecificMethods{
	
	ManifestCheckPage mf;
	static int row=1; 

	@BeforeTest
	public void setTest() {
		testcaseName = "MFC_When scan code not found, sanity rule will fail";
		testDescription = "No Scan code, Sanity rule fail the shipment";
		category = "REGRESSION";
		author = "Mphasis";
		excelFileName = "data";
		sheetName="MFC_ScanNotFound";
	}


	@Test(dataProvider = "getdata")
	public void mfc_Sanity_Fail_No_Scan_Found(String Tc_No,String Tc_Name,String country,String	role,
			String awb_nbr,String sanity_grp,String clrSch,String failureReason)
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
			.clickFilterIcon("Sanity Group")
			.setFilterValueFromSheet("MFC_ScanNotFound", "SANITY_GRP", row)
			.close_FilterIcon()			
			.retrieve_AWBNo()
			.writeIntoExcelSheet("MFC_ScanNotFound", "AWB_NBR", row)		
			.assignToMe()
			.click_UserList()		
			.clickFilterIcon("AWB Number")
			.setFilterValueFromSheet("MFC_ScanNotFound", "AWB_NBR", row)	
			.close_FilterIcon()
			.Scan_Failure_Reason(readExcelValue("MFC_ScanNotFound", "SANITY_GRP", row),failureReason);
			row++;
			;

		}

	}

}




