package database;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.List;
import org.testng.annotations.Test;


public class DbConnection {
	 String url = "jdbc:oracle:thin:@udba0133.ute.apac.fedex.com:1521:GEMINIS1";
	 String user = "GEMINI_SCHEMA";
	 String pwd = "EQHnUdIVhqGmfvEe5OKvVAfdn";
	 String splitString[];
	 Connection con;
	 Statement stmt;
	 ResultSet rs;
	 ResultSetMetaData metaData;
	 int colCnt;
	static int Row_No=1;
	static int i = 2;
	
public static void main(String[] args) throws SQLException, IOException, InterruptedException {
		DbConnection db = new DbConnection();
		db.OracleSIT_DB_Connection();	
		ReadExcel.getRowCount("DB_Query");
		try {
			for ( i = 2; i <= ReadExcel.getRowCount("DB_Query"); i++) {				
				db.executeQuery();
				db.processDbRecords();				
				Row_No++;
			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}

		finally {
			try {
				db.closeConnection();
			} catch (SQLException e) {
				e.getStackTrace();
			}
		}
		System.err.println("No query available to execute");
	}

	public  void OracleSIT_DB_Connection() throws SQLException, IOException {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(url, user, pwd);
		} catch (ClassNotFoundException | SQLException e) {
			e.getStackTrace();
		}
		System.out.println("DB CONNECTION ESTABLISHED SUCCESSFULLY!!");		
	}

	public  void executeQuery() throws SQLException, IOException, InterruptedException {
		stmt = con.createStatement();
		System.err.println("\n********************<<<<DB Query :"+(i-1)+" >>>> *****************");
		splitString = ReadExcel.readExcelValue("DB_Query", "Query_List", Row_No).split(":");
		//rs = stmt.executeQuery(ReadExcel.readExcelValue("DB_Query", "Query_List", Row_No));
		rs = stmt.executeQuery(splitString[2]);
		Thread.sleep(3000);
		metaData = rs.getMetaData();
		colCnt = metaData.getColumnCount();
		System.out.println("Total Column Size:" + colCnt);

	}

	public  void processDbRecords() throws SQLException, IOException, InterruptedException {
		try {
			try {
				for (int i = 1; i < colCnt; i++) {
					String columnName = metaData.getColumnName(i);
					//System.out.println("Column Name is :[" + i + "]=" + columnName);
					if (columnName.equalsIgnoreCase("AWB_NBR")) {
						while (rs.next()) {
							System.out.println(rs.getString(i));
							WriteExcel.writeExcelValue("Result", "TC_No", Row_No, splitString[0]);
							WriteExcel.writeExcelValue("Result", "Module_Name", Row_No, splitString[1]);
							WriteExcel.writeExcelValue("Result", "AWB_No", Row_No, rs.getString(i));
							break;
						}

					}

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	public  void closeConnection() throws SQLException {
		con.close();
		System.err.println("DB connection is closed");
	}

}
