package p02_2_DVD_ADRRESS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.DBConnectionMgr;

public class T04_AddressBookDao implements T05_AddressBookInterface {
	java.sql.Connection          con     = null;
	java.sql.PreparedStatement  	pstmt 	= null;
	java.sql.ResultSet         	rs      = null;
	java.sql.CallableStatement 	cstmt 	 = null;         // 얘네 java.sql 기억! (다른 곳에 같은 이름있음)
	util.DBConnectionMgr    		dbMgr 	= null;

	@SuppressWarnings("static-access")
	@Override
	public T03_AddressVO getAddressDetail(T03_AddressVO paVO) {
		System.out.println("DAO addressDetail호출 성공");
		dbMgr = DBConnectionMgr.getInstance();
		StringBuilder sql = new StringBuilder();
		sql.append("select ID, NAME, ADDRESS, HP, GENDER,  ");
		sql.append("  BIRTHDAY, COMMENTS, REGDATE          ");
		sql.append("    FROM MKADDRTB            		    ");
		sql.append("    WHERE id=?          		    ");

		T03_AddressVO raVO = null;
		try {
			con = dbMgr.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1,paVO.getId());
			rs = pstmt.executeQuery();
			//rs.previous() 쓰지 않는 이유는 오라클 커서가 항상 (디폴트) top에 있어서. list
			//VO는 한행만 담을 수 있는 장애를 가지고 있다.
			//System.out.println(rs.next());// 커서 다음으로 이동
			if(rs.next()) {
				raVO = new T03_AddressVO();
				raVO.setId(rs.getString("id"));
				raVO.setName(rs.getString("name"));
				raVO.setAddress(rs.getString("address"));
				raVO.setGender(rs.getString("gender"));
				raVO.setHp(rs.getString("hp"));
				raVO.setBirthday(rs.getString("birthday"));
				raVO.setRegdate(rs.getString("regdate"));
				raVO.setComments(rs.getString("comments"));

			}
		}
		catch(Exception e) {
			//예외가 발생할 경우 stack 영역에 메시지를 쌓아두는데 이 정보를 출력하는
			//메소드임. 꼭 기억해 둘 것. 클래스명과 라인번호 정보까지도 얻을 수 있음.
			//주의:print()안에 사용하지 말것.
			//Exception에 대한 history정보까지 출력해줌
			e.printStackTrace();
		}
		finally {//사용한 자원 반납하기. con,pstmt,rs
			dbMgr.freeConnection(con, pstmt, rs);
		}
		return raVO;
	}

	@SuppressWarnings("static-access")
	@Override
	public T03_AddressVO AddressInsert(T03_AddressVO paVO) {
		System.out.println("AddressInsert 호출 성공");
		T03_AddressVO raVO = new T03_AddressVO();
		dbMgr= util.DBConnectionMgr.getInstance();
		StringBuilder sql = new StringBuilder();//스트링쓰지말고 이거써서 문자열 붙여주세여.
		int status =0;
		try {
			sql.append("INSERT INTO mkaddrtb(id, name,  address, hp, gender,birthday  ");
			sql.append(" ,comments, regdate)   ");
			sql.append("VALUES(?,?,?,?,?,?,?,TO_CHAR(sysdate, 'yyyy-mm-dd'))   ");
			con = dbMgr.getConnection();
			pstmt = con.prepareStatement(sql.toString());

			int i=0;
			/*
			 * java.sql.SQLException: 인덱스에서 누락된 IN 또는 OUT 매개변수:: 1
			 * 원인: PreaparedStatement 사용시 인덱스 값 치환 누락
			 * 해결방법: ? 물음표 자리에 대응되는 값을 설정할 것.
			 */
			pstmt.setString(++i, paVO.getId());
			pstmt.setString(++i, paVO.getName());
			pstmt.setString(++i, paVO.getAddress());
			pstmt.setString(++i, paVO.getHp());
			pstmt.setString(++i, paVO.getGender());
			pstmt.setString(++i, paVO.getBirthday());
			pstmt.setString(++i, paVO.getComments());

			//입력된 후에 오라클 서버로부터 응답 받은 값 - int
			status=pstmt.executeUpdate();
			//Dao계층에서 처리된 결과를 리턴타입인 raVO(AddressVO)에 담자
			System.out.println(status);
			raVO.setStatus(status); //T03_AddressVO status변수에 1 저장
		} catch (SQLException se) { //ORA-XXXX
			System.out.println(se.toString());
			System.out.println(sql.toString());
		} catch (Exception e) { //ORA-XXXX
			System.out.println(e.toString());
		} finally {
			dbMgr.freeConnection(con, pstmt);
		}
		return raVO;
	}


	@Override
	public T03_AddressVO AddressUpdate(T03_AddressVO paVO) {
		System.out.println("DAO addressUpdate호출 성공");
		return null;
	}
	/*******************************************************
	 * @param AddressVO - command = delete, id = 사용자가 선택한 아이디
	 * @return AddressVO - status = 1 (삭제성공 ) 0 (삭제실패)
	 ********************************************************/
	@SuppressWarnings("static-access")
	@Override
	public T03_AddressVO AddressDelete(T03_AddressVO paVO) {
		  System.out.println("DAO addressDelete 호출성공");
	      //DML구문 작성시 절대 String 사용 불가
	      //String은 + 연산자로 붙혔을 때마다 객체가 새로 생성되서 메모리 누수
	      //문자열을 여러 행 붙일 때는 반드시 StringBuilder - 싱글스레드 안전 - 속도빠름
	      //혹은 StringBuffer를 사용할 것 - 멀티스레드 안전 - 속도느림 - why? 동기화 처리
	      //이해코드 com.basic.StringTest.java
	      StringBuilder sql = new StringBuilder();
	      T03_AddressVO raVO = new T03_AddressVO();
	      sql.append(" DELETE FROM mkaddrtb WHERE id=? ");
	      dbMgr = DBConnectionMgr.getInstance();
	      int status = 0;
	      try {
	         con = dbMgr.getConnection();
	         pstmt = con.prepareStatement(sql.toString());
	         pstmt.setString(1, paVO.getId());
	         status = pstmt.executeUpdate();
	         raVO.setStatus(status);
	      } catch (Exception se) {
	         se.printStackTrace();
	      } finally {
	         dbMgr.freeConnection(con, pstmt);
	      }
	      return raVO;
	   }


	@SuppressWarnings("unused")
	private DBConnectionMgr DBConnection() {
		// TODO Auto-generated method stub
		return null;
	}
/*
	@Override
	public List<T03_AddressVO> getAddress() {
		//조회한 결과 n건을 담기위한 객체 생성
		//테이블 정보는 계속 변함 : 배열은 변하지 않음
		List<T03_AddressVO> list = new ArrayList<T03_AddressVO>();
		dbMgr = DBConnectionMgr.getInstance();
		StringBuilder sql = new StringBuilder();
		sql.append("select id, name, address, gender, hp");
		sql.append("	  ,birthday ,comments, REGDATE  ");
		sql.append("	from mkaddrtb                   ");
		try {
			con = dbMgr.getConnection();
			// 접속해서 select전달 > 리턴값 받아오기 
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			//re.previous() 쓰지 않는 이유 : 오라클 커서가 항상 디폴트 top에 있음
			//VO는 한행만 담을 수 있는 장애 보유 > list와 결합 > 다양한 값 가질 수 있음
			
			T03_AddressVO raVO = null;
			while(rs.next()) {
				raVO = new T03_AddressVO();
				raVO.setId(rs.getString("id"));
				raVO.setName(rs.getString("Name"));
				raVO.setAddress(rs.getString("Address"));
				raVO.setGender(rs.getString("Gender"));
				raVO.setBirthday(rs.getString("Birthday"));
				raVO.setHp(rs.getString("Hp"));
				raVO.setRegdate(rs.getString("Regdate"));
				raVO.setComments(rs.getString("Comments"));
				list.add(raVO);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}finally {	//사용한 자원 반납하기 .con pstmt, rs
			dbMgr.freeConnection(con, pstmt, rs);
		}
		return list;
	}*/
	
	@SuppressWarnings("static-access")
	@Override
	public List<Map<String, Object>> getAddressMap() {
		//조회한 결과 n건을 담기위한 객체 생성
		//테이블 정보는 계속 변함 : 배열은 변하지 않음
		List<Map<String,Object>> list = new ArrayList<>();
		dbMgr = DBConnectionMgr.getInstance();
		StringBuilder sql = new StringBuilder();
		sql.append("select id, name, address, gender, hp");
		sql.append("	  ,birthday ,comments, REGDATE  ");
		sql.append("	from mkaddrtb                   ");
		try {
			con = dbMgr.getConnection();
			// 접속해서 select전달 > 리턴값 받아오기 
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			//re.previous() 쓰지 않는 이유 : 오라클 커서가 항상 디폴트 top에 있음
			//VO는 한행만 담을 수 있는 장애 보유 > list와 결합 > 다양한 값 가질 수 있음
			
			Map<String,Object> rMap = null;
			while(rs.next()) {
				rMap = new HashMap<>();
				rMap.put("id",rs.getString("id"));
				rMap.put("Name",rs.getString("Name"));
				rMap.put("Address",rs.getString("Address"));
				rMap.put("Gender",rs.getString("Gender"));
				rMap.put("Birthday",rs.getString("Birthday"));
				rMap.put("Hp",rs.getString("Hp"));
				rMap.put("Regdate",rs.getString("Regdate"));
				rMap.put("Comments",rs.getString("Comments"));
				//커서 이동시, 주소번지가 매번 바뀌므로, 주서번지가 바뀌기 전에 그 주소번지 list에 저장
				list.add(rMap);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}finally {	//사용한 자원 반납하기 .con pstmt, rs
			dbMgr.freeConnection(con, pstmt, rs);
		}
		return list;
	}

	@Override
	public List<T03_AddressVO> getAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	//	public static void main(String args[]) {
	//		new T04_AddressBookDao().AddressInsert(null);
	//	}
}
