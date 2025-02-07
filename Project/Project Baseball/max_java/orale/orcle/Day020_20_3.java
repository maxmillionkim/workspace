package orcle;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import util.DBConnectionMgr;
import util.DeptVO;

public class Day020_20_3 {

	Connection	con = null;
	CallableStatement	cstmt = null;
	OracleCallableStatement ocstmt = null;

	@SuppressWarnings({ "static-access", "unchecked" })
	public DeptVO[] getMy_proc3() {
		DeptVO[] dvos = null;
		DeptVO dvo = null;
		DBConnectionMgr dbMgr = DBConnectionMgr.getInstance();   
		try {
			//예외가 발생할 가능성이 있는 코드 
			con = dbMgr.getConnection();
			cstmt = con.prepareCall("{call MY_PROC(?)}");
			cstmt.registerOutParameter(1,OracleTypes.CURSOR);
			cstmt.execute();
			ocstmt = (OracleCallableStatement)cstmt;
			ResultSet rs = ocstmt.getCursor(1);
			@SuppressWarnings({ "rawtypes", "unused" })
			ArrayList a1 = new ArrayList();	//싱글스레드 안전한 구조
			@SuppressWarnings("rawtypes")
			Vector v = new Vector(); 		// 멀티스레드 안전한  구조
			while(rs.next()) {
				dvo = new DeptVO();
				dvo.setDeptno(rs.getInt("deptno"));
				dvo.setDname(rs.getString("dname"));
				dvo.setLoc(rs.getString("loc"));
				//백터 클래스 추가한 이유 = 테이블에 로우수를 알 수 없음으로
				//백터 클래스에 add할 때마다 원소의 갯수 카운트
				v.add(dvo);//순서대로 들어감 
				System.out.println(dvo.getDeptno()+","+dvo.getDname()+","+dvo.getLoc());
				dvo = null;
			}
			//객체 배열을 생성할 때 사이즈 메소드 사용
			dvos = new DeptVO[v.size()];
			//파라미터로 비어있는 객체배열을 넣어주면 벡터에 담긴 값들이 복제됨
			v.copyInto(dvos);

		}catch (Exception e) {
			System.out.println("[[Exception]]"+e.toString());

		}finally {
			//사용한 자원 반납할것 - 명시적으로 
			//생성된 역순으로 반납처리할것 
			try {
				if(cstmt !=null) {
					cstmt.close();

				}
				if(ocstmt!=null) {
					ocstmt.close();
				}
			}catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return dvos;
	}
	
	public static void main(String[] args) {
		Day020_20_3 emp = new Day020_20_3();
	
		DeptVO[] dvos = emp.getMy_proc3();
		System.out.println("================================");
		// 일반 for문
		for(int j = 0; j<dvos.length; j++) {
			DeptVO dvo1 = dvos[j];
			System.out.println(dvo1.getDeptno()
								+","+dvo1.getDname()
								+","+dvo1.getLoc());
		}
		System.out.println("================================");
		//개선된 for문	- 전체 모든 값을 확인 출력 할 때만 사용
		for(DeptVO dvo2:dvos) {
			System.out.println(dvo2.getDeptno()
								+","+dvo2.getDname()
								+","+dvo2.getLoc());
		}
		
	}
}