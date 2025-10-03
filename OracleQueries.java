package database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OracleQueries {

	public  List<String> queriesList;

	public List<String> queries() throws IOException {
		queriesList = new ArrayList();
		
		queriesList.add(ReadExcel.readExcelValue("DB_Query", "Query_List", 2));
		
		queriesList.add("SELECT AWB_NBR, JSON_VALUE(SHPMT_DATA_JSON,'$.docAvlblStat') docAvlblStat FROM ib_shipment WHERE clrnc_cntry_cd = 'SG' \r\n"
				+ "and JSON_VALUE(SHPMT_DATA_JSON,'$.docAvlblStat') = 'Y' AND ROWNUM < 11");
		
		queriesList.add("SELECT d.*\r\n"
				+ "       FROM ib_shipment ibs,\r\n"
				+ "            json_table(ibs.shpmt_data_json, '$'\r\n"
				+ "              COLUMNS (\r\n"
				+ "                awb_nbr VARCHAR2(20)       PATH '$.awbNbr',\r\n"
				+ "                dutyBillAccNbr VARCHAR2(30)       PATH '$.dutyBillAccNbr',\r\n"
				+ "                NESTED PATH '$.declnReqs[*]'\r\n"
				+ "                  COLUMNS (\r\n"
				+ "                            code VARCHAR2(30) PATH '$.code',\r\n"
				+ "                            desc1 VARCHAR2(30) PATH '$.desc',\r\n"
				+ "                            status  VARCHAR2(20) PATH '$.status'))) d\r\n"
				+ "where ibs.CLRNC_CNTRY_CD = 'SG'\r\n"
				+ "and ibs.awb_nbr = d.awb_nbr\r\n"
				+ "AND code = 'CAGED_MODE_REQUIRED' AND STATUS = 'Failed' \r\n"
				+ "AND ROWNUM < 11");
		
		queriesList.add("SELECT * FROM IB_SHPMT_INFO WHERE clrnc_cntry_cd = 'SG' AND CMPTN_NM = 'Customer Matching'");
		
		
		return queriesList;

	}

	public int getTotalQuerySize() {
		return queriesList.size(); 
	}

	public static void main(String[] args) throws IOException {
		OracleQueries oq = new OracleQueries();
		oq.queries();
		oq.getTotalQuerySize();
		for (int j = 0; j < oq.queriesList.size(); j++) {
			System.out.println(oq.queriesList.get(j));
		}

	}

}
