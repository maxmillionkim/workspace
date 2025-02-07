package orcle;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import util.DBConnectionMgr;
import util.DeptVO;

public class Day020_1 {

	Connection	con = null;
	CallableStatement	cstmt = null;
	OracleCallableStatement ocstmt = null;

	public DeptVO getMy_proc() {
		DeptVO dvo = null;
		DBConnectionMgr dbMgr = DBConnectionMgr.getInstance();

		try {
			//예외가 발생할 가능성 있는 코드
			con = dbMgr.getConnection();
			cstmt = con.prepareCall("{call MY_PROC(?)}");
			cstmt.registerOutParameter(1, OracleTypes.CURSOR);
			cstmt.execute();
			ocstmt = (OracleCallableStatement)cstmt;
			ResultSet rs = ocstmt.getCursor(1);

			while(rs.next()) {
				dvo = new DeptVO();
				dvo.setDeptno(rs.getInt("deptno"));
				dvo.setDname(rs.getString("dname"));
				dvo.setLoc(rs.getString("loc"));
				System.out.println
				(dvo.getDeptno()+"/"+dvo.getDname()+"/"+dvo.getLoc());
				dvo = null; 
			}

		} catch (Exception e) {
			System.out.println("[Exception]"+e.toString());
		} finally {
			try {
				if(cstmt != null) {
					cstmt.close();
				}
				if(ocstmt != null) {
					ocstmt.close();
				}
				if(con != null) {
					con.close();
				}				
			} catch (Exception e2) {

			}
		}
		return dvo;
	}

	
	public static void main(String[] args) {
		Day020_1 emp = new Day020_1();
				DeptVO dvo = emp.getMy_proc();
				System.out.println(dvo.getDeptno());
	
	}
}

