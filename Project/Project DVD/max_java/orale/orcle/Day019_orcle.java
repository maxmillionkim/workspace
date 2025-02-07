package orcle;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Scanner;

import oracle.jdbc.OracleCallableStatement;
import util.DBConnectionMgr;

public class Day019_orcle {

	Connection	con = null;
	CallableStatement	cstmt = null;
	OracleCallableStatement ocstmt = null;

	@SuppressWarnings({ "unused", "resource" })
	public void getProc_deptnoUpate() {
			//프로시저의 out속성값 받아서 저장할 변수
		String msg = null;
			//외부 클랠스의 객체를 주입 받기 위해서 작성
			//직접 인스턴스화 하지 않음
		DBConnectionMgr dbMgr = DBConnectionMgr.getInstance();
		try {
				//물리적으로 떨어져 있는 오라클 서버에 연결 통로 확보
			con = DBConnectionMgr.getConnection();
				//프로시저 호출
			cstmt = con.prepareCall("{call proc_deptnoUpdate(?,?)}");
				//부서번호 받아오기
			System.out.println("부서번호 입력하세요");
			Scanner scan = new Scanner(System.in);
			int u_deptno = scan.nextInt();

			cstmt.setInt(1, u_deptno);
			cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);
			int result = cstmt.executeUpdate();
			System.out.println(cstmt.getString(2));

			// commit > 결과확인 후 정상작동시 될 수 있도록
			//			if(result == 1) {
			//				con.commit();		//저장
			//				con.rollback();		//롤백
			//			}

		} catch (Exception e) {
			//절대로 print 안에 넣지 말것
			//stack메모리 영역에 쌓여있는 에러메시지 모두 출력
			e.printStackTrace();

		}finally {
			//사용한 자원 반납할 것! 명시적으로
			//생성된 역순으로 반납
			try {
				if(cstmt != null) {
					cstmt.close();
				}
				if(con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	@SuppressWarnings({ "resource", "unused" })
	public String getProc_empnoUpdate() {
		String msg = null;
		DBConnectionMgr dbMgr = DBConnectionMgr.getInstance();
		try {
			con = DBConnectionMgr.getConnection();
			cstmt = con.prepareCall("{call proc_empnoUpdate(?,?)}");
			System.out.println("사원번호 입력하세요");
			Scanner scan = new Scanner(System.in);
				// 사용자가 입력한 사원번호 받아오기
			int u_empno = scan.nextInt();
			cstmt.setInt(1, u_empno);
			cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);
				//오라클 서버에게 처리요청
			int result = cstmt.executeUpdate();
			cstmt.executeUpdate();
			msg = cstmt.getString(2);
			System.out.println(msg);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} 
		return msg;
	}

	public static void main(String[] args) {
		Day019_orcle emp = new Day019_orcle();
		emp.getProc_deptnoUpate();
		emp.getProc_empnoUpdate();


	}
}
