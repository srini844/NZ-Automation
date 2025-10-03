package com.framework.selenium.testcase.ManifestCheck;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.framework.pages.LoginPage;
import com.framework.pages.ManifestCheckPage;
import com.framework.testng.api.base.ProjectSpecificMethods;

public class MFC_edit_shipment_description extends ProjectSpecificMethods{
	
	ManifestCheckPage mf;
	static int row=1; 

	@BeforeTest
	public void setTest() {
		testcaseName = "MFC_modify_clearance_scheme";
		testDescription = "Modify clearance scheme in MF page";
		category = "REGRESSION";
		author = "Mphasis";
		excelFileName = "data";
		sheetName="MFC_EditShipment_Override";
	}


	@Test(dataProvider = "getdata")
	public void mfc_Edit_Shipment_Description(String TC_NO,String TC_NAME,String COUNTRY,String	ROLE,String override,String AWB_NBR,String SANITY_GRP,String clrsch,String Weight,
			String Value,String	PhNo,String	Shipper_Street1,String	Shipper_Street2,String	Shipper_City,
			String Shipper_PostalCode,String Consignee_Street1,String Consignee_Street2,String Consignee_City,String Consignee_Postal_Code,String Shipper_Name,
			String Consignee_Name,String Shipper_Company_Name,String Consignee_Company_Name,String Shipper_PhoneNo,String Consignee_PhoneNo,String Shipment_Desc
)
			throws Throwable {		

		ProjectSpecificMethods.methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		System.out.println("<==> Execution Started for Method : "+methodName.toUpperCase());

		int sheetRows = getRowCount(this.sheetName);	
		if(row<=sheetRows-1) {
			
			new LoginPage()		
			.userAuthentication() 
			.OktaAuthentication()		
			.select_Country_Role(COUNTRY, ROLE);		

			new ManifestCheckPage()
			.select_competency("MANIFEST_CHECK")
			.disable_Headercolumns()
			.enable_HeaderColumns()
			.click_TeamList()
			.select_Team("Phantom")
			.filter_TableColumns("CI Flag", " Blank Cells",row)
			.clickFilterIcon("Clearance Scheme")
			.setFilterValueFromSheet("MFC_EditShipment", "CLR_SCH", row)
			.close_FilterIcon()
			.retrieve_AWBNo()
			.writeIntoExcelSheet("MFC_EditShipment", "AWB_NBR", row)		
			.assignToMe()
			.click_UserList()		
			.clickFilterIcon("AWB Number")
			.setFilterValueFromSheet("MFC_EditShipment", "AWB_NBR", row)	
			.close_FilterIcon()
			.click_EditIcon()
			.enter_Shipment_weight(Weight)
			.enter_Shipment_value(Value)
			.enter_Shipment_phoneNo(PhNo)
			.enter_Shipment_ShipperStreet1(Shipper_Street1)
			.enter_Shipment_ShipperStreet2(Shipper_Street2)
			.enter_Shipment_City(Shipper_City)
			.enter_Shipment_ShipperPC(Shipper_PostalCode)
			.enter_Shipment_ConsigneeStreet1(Consignee_Street1)
			.enter_Shipment_ConsigneeStreet2(Consignee_Street2)			
			.enter_Shipment_ConsigneeCity(Consignee_City)
			.enter_Shipment_ConsigneePostalCode(Consignee_Postal_Code)
			.enter_Shipment_ShipperName(Shipper_Name)
			.enter_Shipment_ConsigneeName(Consignee_Name)
			.enter_Shipment_ShipperCompanyName(Shipper_Company_Name)
			.enter_Shipment_ConsigneeCompanyName(Consignee_Company_Name)
			.enter_Shipment_ShipperPhNo(Shipper_PhoneNo)
			.enter_Shipment_ConsigneePhNo(Consignee_PhoneNo)
			.enter_Shipment_ShipmentDesc(Shipment_Desc)
			.btn_override()
			.click_global_search_icon()
			.enter_shipment_global_search(readExcelValue("MFC_EditShipment", "AWB_NBR", row))
			.click_shipment_search();
			//.retrieve_glblSrch_application_value("Competency");	
			
			 mf= new ManifestCheckPage();
			if(!mf.retrieve_glblSrch_application_value("Competency").equals("Manifest Check"))		
				reportStep("Shipment moved away from competency", "Pass");
			else
				reportStep("Shipment stays in same competency, Overridden is unsuccessful.", "Fail");
			row++;
			;

		}

	}

}




