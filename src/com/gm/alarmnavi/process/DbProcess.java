package com.gm.alarmnavi.process;

import java.sql.*;
import java.util.*;

import com.gm.alarmnavi.serializable.*;

public class DbProcess {

	List<String> list;
	Connection conn;
	Statement stmt;
	ResultSet rs = null;

	public void connectDB() {
		// TODO Auto-generated method stub

		try {
			Class.forName("com.mysql.jdbc.Driver");// 드라이버 로딩: DriverManager에 등록
		} catch (ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
		}

		try {
			String jdbcUrl = "jdbc:mysql://localhost/";
			String userId = "root";// 사용자계정
			String userPass = "Starter";// 사용자 패스워드

			conn = DriverManager.getConnection(jdbcUrl, userId, userPass);// Connection
																			// 객체를
																			// 얻어냄

			stmt = conn.createStatement();// Statement 객체를 얻어냄
			stmt.execute("use navi");

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
	}

	public List<String> getAllAppid() {

		list = new ArrayList<String>();

		try {
			rs = stmt.executeQuery("Select * from targetlocation");
			while (rs.next()) {
				String appid = rs.getString("appid");

				list.add(appid);

			}

			System.out.println("success for trace");// 성공시 화면에 표시됨

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	public List<String> traceLocation(FireData fireData) {
		List<double[]> routePointList = fireData.getRoutePointList();
		list = new ArrayList<String>();

		try {
			rs = stmt.executeQuery("Select * from targetlocation");
			while (rs.next()) {
				String appid = rs.getString("appid");
				Double xcoordinate = rs.getDouble("xcoordinate");
				Double ycoordinate = rs.getDouble("ycoordinate");
				int xCoo = (int) (xcoordinate * 1000);
				int yCoo = (int) (ycoordinate * 1000);

				Iterator itr = fireData.getRoutePointList().iterator();
				for (int i = 0; i < 10; i++) {
					double[] k = (double[]) itr.next();
					int fireDataXint = (int) (k[0] * 1000);
					int fireDataYint = (int) (k[1] * 1000);
					System.out.println(fireDataXint + " ," + fireDataYint
							+ " : " + xCoo + " " + yCoo);
					if (fireDataXint == xCoo) {
						if (fireDataYint == yCoo) {
							list.add(appid);
							System.out.println("발견!");
						}
					}
				}

			}

			System.out.println("success for trace");// 성공시 화면에 표시됨

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

}
