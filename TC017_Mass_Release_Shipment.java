package com.framework.selenium.testcase.ManifestCheck;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.framework.pages.LoginPage;
import com.framework.pages.ManifestCheckPage;
import com.framework.testng.api.base.ProjectSpecificMethods;

public class TC017_Mass_Release_Shipment extends ProjectSpecificMethods{
	static int row=1; 
	ManifestCheckPage mf;
	
	@BeforeTest
	public void setTest() {
		testcaseName = "MFC_Mass_Release_Shipment";
		testDescription = "Shipment mass release";
		category = "REGRESSION";
		author = "Mphasis";
		excelFileName = "data";
		sheetName="Mass_Release";
	}


	@Test(dataProvider = "getdata")
	public void mfc_Mass_Release_Shipment(String tcNo,String name,String country,String role,String par_awb,String awb,String clrsch,String clrstat,String ship_ManiStat,String sanitygrp)
			throws Throwable {		

		ProjectSpecificMethods.methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		System.out.println("!*** Execution Started for Method : "+methodName.toUpperCase());

		int sheetRows = getRowCount(this.sheetName);	
		if(row<=sheetRows-1) {
			
			new LoginPage()		
			.userAuthentication() 
			.OktaAuthentication()		
			.select_Country_Role(country, role);		

			new ManifestCheckPage()
			.select_competency("ENTRY_BUILD")
			.disable_Headercolumns()
			.enable_HeaderColumns()
			.click_TeamList()
			.select_Team("Post Clearance")
			.clickFilterIcon("Clearance Status")
			.setFilterValueFromSheet("Mass_Release", "CLR_STAT", row)
			.close_FilterIcon()
			.clickFilterIcon("Clearance Scheme")
			.setFilterValueFromSheet("Mass_Release", "CLR_SCH", row)
			.close_FilterIcon()
			.clickFilterIcon("Shipment Manifest Status")
			.setFilterValueFromSheet("Mass_Release", "SHIP_MANI_STAT", row)
			.close_FilterIcon()
			.retrieve_AWBNo()
			.writeIntoExcelSheet("Mass_Release", "PARENT_AWB_NBR", row)
			.select_competency("MANIFEST_CHECK")
			.filter_TableColumns("Sanity Group", " Contains ",row)
			.setFilterValueFromSheet("Mass_Release", "SANITY_GRP", row)
			.close_FilterIcon()
			.retrieve_AWBNo()
			.writeIntoExcelSheet("Mass_Release", "AWB_NBR", row)
			.assignToMe()
			.click_UserList()		
			.clickFilterIcon("AWB Number")
			.setFilterValueFromSheet("Mass_Release", "AWB_NBR", row)	
			.close_FilterIcon()	
			.btn_massRelease()
			.txt_parentCons("Mass_Release", "PARENT_AWB_NBR", row)
			.btn_save();
			Thread.sleep(3000);
			new ManifestCheckPage()
			.click_UserList()
			.click_global_search_icon()
			.enter_shipment_global_search(readExcelValue("Mass_Release", "AWB_NBR", row))
			.click_shipment_search()			
			.retrieve_glblSrch_application_value("Clearance Status");
			mf=new ManifestCheckPage();
			if(mf.retrieve_glblSrch_application_value("Clearance Status").equalsIgnoreCase("RELEASED"))		
				reportStep("Baby Cons Shipment released successfully", "Pass");
			else
				reportStep("Baby Cons Shipment release is unsuccessful", "Fail");
			new ManifestCheckPage()
			.retrieve_glblSrch_application_value("Competency");
			if(mf.retrieve_glblSrch_application_value("Competency").equalsIgnoreCase("Entry Build"))		
				reportStep("Baby Cons - Shipment successfully  moved to Entry build competency", "Pass");
			else
				reportStep("Baby Cons - Shipment is NOT moved to Entry build competency", "Fail");
		}

			row++;
			;

		}

	}






