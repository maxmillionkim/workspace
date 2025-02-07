package p02_2_DVD_SAL;

import java.util.ArrayList;
import java.util.List;

import address.AddressVO;
import util.DBConnectionMgr;

public class SalDao implements SalBookInterface {

	java.sql.Connection 		con   = null;
	java.sql.PreparedStatement 	pstmt = null;
	java.sql.ResultSet          rs    = null;
	util.DBConnectionMgr    dbMgr = null;

	@Override	//전체조회
	public SalVO SalAll(SalVO psVO) {
		return null;
	}

	@Override	//멤버별조회
	public SalVO SalMem(SalVO psVO) {
		List<SalVO> list = new ArrayList<SalVO>();
		System.out.println("DAO getAddressDetail호출 성공");
		dbMgr = DBConnectionMgr.getInstance();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT StartDay, DVDTITLE, ReturnDay, LATE, TOTALSAL, ID");
		sql.append("  	FROM DVDSAL");
		sql.append(" 	WHERE id=?");
		SalVO rsVO = null;
		try {
			con = dbMgr.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, psVO.getStartDay());
			rs = pstmt.executeQuery();
			for(;rs.next();) {
				rsVO = new SalVO();
				rsVO.setStartDay(rs.getString("StartDay"));
				rsVO.setMovieTitle(rs.getString("DVDTITLE"));
				rsVO.setReturnDay(rs.getString("ReturnDay"));
				rsVO.setLateFee(rs.getString("LATE"));
				rsVO.setTotalSal(rs.getString("TotalSal"));
				rsVO.setId(rs.getString("Id"));
				list.add(rsVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbMgr.freeConnection(con, pstmt, rs);
		}
		return rsVO;
	}

	@Override	//월별조회
	public SalVO SalsMonths(SalVO psVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override	//선택조회
	public SalVO SalsSelect(SalVO psVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override //전체조회
	public List<SalVO> getSelect() {
		List<SalVO> list = new ArrayList<SalVO>();
		dbMgr = DBConnectionMgr.getInstance();
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT STARTDAY, DVDTITLE, RETURNDAY, LATE, TOTALSAL, ID");
		sql.append("  	FROM DVDSAL");

		try {
			con = dbMgr.getConnection();
			System.out.println(-1);
			pstmt = con.prepareStatement(sql.toString());
			System.out.println(0);
			rs = pstmt.executeQuery(); 
			System.out.println(1);
			SalVO rsVO = null;
			System.out.println(2);
			for(;rs.next();) {
				rsVO = new SalVO();
				rsVO.setStartDay(rs.getString("StartDay"));
				rsVO.setMovieTitle(rs.getString("DVDTITLE"));
				rsVO.setReturnDay(rs.getString("ReturnDay"));
				rsVO.setLateFee(rs.getString("LATE"));
				rsVO.setTotalSal(rs.getString("TotalSal"));
				rsVO.setId(rs.getString("Id"));
				list.add(rsVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(" getSelect 오류! "+ e.toString());
		} finally {
			dbMgr.freeConnection(con, pstmt, rs);
		}
		return list;
	}

}
