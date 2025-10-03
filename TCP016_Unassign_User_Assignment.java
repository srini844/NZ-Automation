package com.framework.selenium.testcase.ManifestCheck;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.framework.pages.LoginPage;
import com.framework.pages.ManifestCheckPage;
import com.framework.testng.api.base.ProjectSpecificMethods;

public class TCP016_Unassign_User_Assignment extends ProjectSpecificMethods{
	
	/* Following test cases are covered
	 
	 	 
	 */
	
	ManifestCheckPage mf;
	static int row=1; 

	@BeforeTest
	public void setTest() {
		testcaseName = "UserAssignment_Unassign_Multiple Shipment_User List";
		testDescription = "Unassign_Multiple_shipment_User_List";
		category = "REGRESSION";
		author = "Mphasis";
		excelFileName = "data";
		sheetName="Common";
	}


	@Test(dataProvider = "getdata")
	public void unassign_userAssignment_userlist(String Tc_No,String Tc_Name,String country,String	role)
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
			.select_competency("MANIFEST_CHECK")
			.click_UserList()
			.unassignAllShipemnt_UserList();
		
			row++;
			

		}

	}

}




