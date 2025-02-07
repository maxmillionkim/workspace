package com.util;
/*
 * 오라클은 동시 접속자들을 세션으로 관리
 * 일정 개수의 세션을 넘어가면 서버 접속 불가, 연결불가
 * 자바단에서 사용한 자원 반드시 반납할 것 : 자바튜닝 권고사항
 * 물리적으로 떨어져 있는 오라클 서버에 접속 시 - Connection
 * 연결통로를 통해 DML구문을 가져가고 요청
 * PreparedStatmentm CallableStatement, /??
 * select > 트랜잭션처리대상 아님 : 테이블 변화 없음
 * pstm.excuteQuery():ResultSet -> Cursor조작 rs.next(), rs.isFirst(),  rs.isLast(). rs.absolute(3);
 * insert update, delete -> 트랜잭션 처리 대상 : 물리적 변화
 */

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBConnectionMgr  {
	public static final String _DRIVER = "oracle.jdbc.driver.OracleDriver";
	public static final String _URL    = "jdbc:oracle:thin:@192.168.0.28:1521:orcl11";
	public static final String _USER   = "scott";
	public static final String _PW 	   = "tiger";

	static DBConnectionMgr dbMgr = null;
		// 다른 클래스에서 직접 인스턴스화 하지 않음 > 결합도가 낮아짐
	public static DBConnectionMgr getInstance() {
		if(dbMgr == null) {
			dbMgr = new DBConnectionMgr();
		}
		return dbMgr;
	}

	public static Connection getConnection() {
		Connection con = null;

		try {
			Class.forName(_DRIVER);
			con = DriverManager.getConnection(_URL,_USER,_PW);
		} catch (ClassNotFoundException ce) {
			System.out.println("Not found Driver Class");
		} catch (Exception e) {
			System.out.println("Oracle Server Connection faile");
		}

			//드라이버 클래스 필요
		return con;
	}
	//사용자원 반납 : 오라클 세션과 관련 : 반드시 할 것
    public void freeConnection(Connection con, PreparedStatement pstmt) {
       try {
          if(pstmt!=null) {
             pstmt.close();
          }
          if(con!=null) {
             con.close();
          }
       } catch (Exception e) {
          // TODO: handle exception
       }
    }
       
    public void freeConnection(Connection con, PreparedStatement pstmt, ResultSet rs) {
       try {
          if(rs!=null) {
             rs.close();
          }
          if(pstmt!=null) {
             pstmt.close();
          }
          if(con!=null) {
             con.close();
          }
       } catch (Exception e) {
          // TODO: handle exception
       }
    }
    public void freeConnection(Connection con, CallableStatement cstmt) {
       try {
          
          if(cstmt!=null) {
             cstmt.close();
          }
          if(con!=null) {
             con.close();
          }
       } catch (Exception e) {
          // TODO: handle exception
       }
    }
    //사용한 자원 반납 순서는 생성된 역순으로 반납할 것.(rs -cstmt, pstmt -con)
    public void freeConnection(Connection con, CallableStatement cstmt, ResultSet rs) {
       try {
          if(rs!=null) {
             rs.close();
          }
          if(cstmt!=null) {
             cstmt.close();
          }
          if(con!=null) {
             con.close();
          }
       } catch (Exception e) {
          // TODO: handle exception
       }
    }
}