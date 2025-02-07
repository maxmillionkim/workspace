package p02_2_DVD_SAL;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import p02_1_DVD_DVD.DVDManager;

public class MainBook extends JFrame implements ActionListener  {

	static MainBook mBook = null;
	// 패널 추가
	JPanel jp_north = new JPanel();
	JPanel jp_north_2  = new JPanel(); 
	JPanel jp_north_1  = new JPanel(); 
	JPanel jp_center = new JPanel();
	// 버튼 추가
	JButton jbtn_all = new JButton("전체 조회");
	JButton jbtn_mem = new JButton("회원별 조회");
	JButton jbtn_thism = new JButton("이번달 조회");
	JButton jbtn_per = new JButton("기간별 조회");
	//헤더 정보 담을 객체 추가
	String cols[] = {"대출일","DVD이름","반납일","연체여부","매출"};
	String data[][] = new String[0][5];
	//데이터를 담을 수 있는 클래스가 필요
	//data set
	DefaultTableModel dtm_sal = new DefaultTableModel(data,cols);
	//화면,그리드 만 제공 데이터 없음 
	JTable jt_sal = new JTable(dtm_sal);
	JScrollPane jsp_sal = new JScrollPane(jt_sal);
	JTableHeader jth_sal = jt_sal.getTableHeader();

	////////////////////// 검색기 추가 시작 /////////////////////
	String searchLable[] = {"회원 ID", "DVD Title"};
	JComboBox jcb_search = new JComboBox(searchLable);
	JTextField jtf_keyword = new JTextField("검색할 키워드 입력",50);
	////////////////////// 검색기 추가   끝 /////////////////////
	
	// 갱신 처리 시 컨테이너가 필요
	Container cont = this.getContentPane();

	public void initDisplay() {

		//////////////////  화면 구성 시작  /////////////////////////
		jp_north.setLayout(new GridLayout(3,1));
		jp_north_1.setLayout(new FlowLayout(FlowLayout.LEFT));
		jp_north_1.add(jbtn_all);
		jp_north_1.add(jbtn_mem);
		jp_north_1.add(jbtn_thism);
		jp_north_1.add(jbtn_per);
		jp_north_2.setLayout(new FlowLayout(FlowLayout.LEFT));
		jp_north_2.add(jcb_search);
		jp_north_2.add(jtf_keyword);
		
		//jp_center.add(jsp_sal);
		jp_center.setSize(700, 500);
		
		cont.add("North",jp_north);
		cont.add("Center",jp_center);
		this.setSize(700, 500);
		this.setVisible(true);
		jp_north.add(jp_north_2);
		jp_north.add(jp_north_1);
		
		jth_sal.setFont(new Font("맑은고딕",Font.BOLD,18));
		jth_sal.setBackground(new Color(22,22,100));
		jth_sal.setForeground(Color.white);
		jth_sal.setReorderingAllowed(false);
		jth_sal.setResizingAllowed(false);
		jt_sal.setGridColor(Color.blue);
		jt_sal.getColumnModel().getColumn(0).setPreferredWidth(100);
		jt_sal.getColumnModel().getColumn(1).setPreferredWidth(100);
		jt_sal.getColumnModel().getColumn(2).setPreferredWidth(100);
		jt_sal.getColumnModel().getColumn(3).setPreferredWidth(50);
		jt_sal.getColumnModel().getColumn(4).setPreferredWidth(80);
		jt_sal.repaint();
		refreshData();
		///////////  이벤트 감지  ////////////////////////////
		jbtn_all.addActionListener(this);
		jbtn_mem.addActionListener(this);
		jbtn_thism.addActionListener(this);
		jbtn_per.addActionListener(this);
	}


	//새로고침 처리 메소드 구현
	public void refreshData() {
		System.out.println("새로고침 처리");
		// 기존 테이블에 존재하는 데이터 삭제
		while(dtm_sal.getRowCount()>0) {
			dtm_sal.removeRow(0);
		}
		SalBookCtrl aCtrl = new SalBookCtrl();
		List<SalVO> list = aCtrl.send("all");
		if((list==null)||(list.size()==0)) {
			JOptionPane.showMessageDialog(this,"데이터가 없습니다");
		}else {
			for(int i=0;i<list.size();i++) {
				SalVO raVO = list.get(i);
				Vector rowData= new Vector();
				rowData.add(0,raVO.getStartDay());
				rowData.add(1,raVO.getMovieTitle());
				rowData.add(2,raVO.getReturnDay());
				rowData.add(3,raVO.getLateFee());
				rowData.add(4,raVO.getTotalSal());
				dtm_sal.addRow(rowData);
			}
		}
	}



	//인스턴스화처리 ->메모리상주
	public static void main(String[] args) {
		if(mBook==null) {
			mBook = new MainBook();
		}
		mBook.initDisplay();
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		jp_center.remove(jsp_sal);
		cont.remove(jp_center);
		
		if(obj==jbtn_thism) { // 이번달 조회 ThisMLogic
//			jp_center.remove(jsp_sal);
//			cont.remove(jp_center);
			jp_center = new MainBook_Thism();
			cont.add("Center",jp_center);
			cont.revalidate();
		}
		else if(obj==jbtn_per) { // 기간별 조회 PeriodLogic
//			jp_center.remove(jsp_sal);
//			cont.remove(jp_center);
			jp_center = new MainBook_per();
			cont.add("Center",jp_center);
			cont.revalidate();
		}
		else if(obj==jbtn_mem) { // 회원별 조회 MemberLogic
//			jp_center.remove(jsp_sal);
//			cont.remove(jp_center);
			jp_center.add(jsp_sal);
			cont.add("Center",jp_center);
			cont.revalidate();
		}
		else { // 전체조회
//			jp_center.remove(jsp_sal);
//			cont.remove(jp_center);
			jp_center.add(jsp_sal);
			cont.add("Center",jp_center);
			cont.revalidate();
		}
	}


}
