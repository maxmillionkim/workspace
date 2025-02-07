package p02_2_DVD_ADRRESS;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class T01_AddressBook extends JFrame  {
			
		 T02_SubBook subBook = null;
		 static T01_AddressBook aBook = null;
		 JPanel jp_north = new JPanel();
		 JButton jbtn_ins = new JButton("입력");
		 JButton jbtn_upd = new JButton("수정");
		 JButton jbtn_del = new JButton("삭제");
		 JButton jbtn_det = new JButton("상세조회");
		 //헤더 정보 담을 객체 추가
		 String cols[] = {"아이디","이름","주소","HP"};
		 String data[][] = new String[0][4];
		 //데이터를 담을 수 있는 클래스가 필요
		 //data set
		 DefaultTableModel dtm_address = new DefaultTableModel(data,cols);
		 //화면,그리드 만 제공 데이터 없음 
		 JTable jt_address = new JTable(dtm_address);
		 JScrollPane jsp_address = new JScrollPane(jt_address);
		 JTableHeader jth_address = jt_address.getTableHeader();
		 
 public void initDisplay() {
	 	  //내안에 있을 때는 this 외부에 있을 떄는 인스턴스 변수
	 jbtn_ins.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				insertActionPerformed(e);
			}     //인터페이스라서 new를 못하는 앤데 그 안에 직접 메소드를 구현해 주면 문법적으로 허락해준대
	      });
	      jbtn_upd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				updateActionPerformed(e);
			}
		
		});
	      jbtn_del.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteActionPerformed(e);
				
			}
		});
	      
	      jbtn_det.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				detailActionPerformed(e);
				
			}
		});
/////////////////////////////////////////////////////
	 jp_north.setLayout(new FlowLayout());

	 jp_north.add(jbtn_ins);
	 jp_north.add(jbtn_upd);
	 jp_north.add(jbtn_del);
	 jp_north.add(jbtn_det);
	 this.add("North",jp_north);
	 this.add("Center",jsp_address);
	 this.setSize(700, 500);
	 this.setVisible(true);
	 jth_address.setFont(new Font("맑은고딕",Font.BOLD,18));
	 jth_address.setBackground(new Color(22,22,100));
	 jth_address.setForeground(Color.white);
	 jth_address.setReorderingAllowed(false);
	 jth_address.setResizingAllowed(false);
	 jt_address.setGridColor(Color.blue);
	 jt_address.getColumnModel().getColumn(0).setPreferredWidth(80);
	 jt_address.getColumnModel().getColumn(1).setPreferredWidth(100);
	 jt_address.getColumnModel().getColumn(2).setPreferredWidth(390);
	 jt_address.getColumnModel().getColumn(3).setPreferredWidth(130);
	 jt_address.repaint();
	 refreshData();
	 
	 //싱글 선택 가능하도록 하기
     jt_address.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
     this.addWindowListener(new WindowListener() {
    	 @Override
			public void windowActivated(WindowEvent e) {
			}
			@Override
			public void windowClosed(WindowEvent e) {
			}
			@Override
			public void windowClosing(WindowEvent e) {
				//System.out.println("11");
				//가상머신과 어플사이에 연결고리 끊김
				//finally 예외
				System.exit(0);
			}
			@Override
			public void windowDeactivated(WindowEvent e) {
			}
			@Override
			public void windowDeiconified(WindowEvent e) {
			}
			@Override
			public void windowIconified(WindowEvent e) {
			}
			@Override
			public void windowOpened(WindowEvent e) {
			}    	 
     });
 }
 
 
 //새로고침 처리 메소드 구현
	public void refreshData() {
		System.out.println("새로고침 처리");
		// 기존 테이블에 존재하는 데이터 삭제
		while(dtm_address.getRowCount()>0) {
			dtm_address.removeRow(0);
		}
		T06_AddressBookCtrl aCtrl = new T06_AddressBookCtrl();
		List<T03_AddressVO> list = aCtrl.send("select");
		if((list==null)||(list.size()==0)) {
			JOptionPane.showMessageDialog(this,"데이터가 없습니다");
		}else {
			for(int i=0;i<list.size();i++) {
				T03_AddressVO raVO = list.get(i);
				//Vector를 생성한 이유는 DB에서 꺼낸 값을 행단위로 dtm_address디폴트테이블에
				//추가할 수 있는 addRow(Vector or Object[])라는 메소드에 파라미터로 넣기 위해서
				Vector rowData= new Vector();
				rowData.add(0,raVO.getId());
				rowData.add(1,raVO.getName());
				rowData.add(2,raVO.getAddress());
				rowData.add(3,raVO.getHp());
				dtm_address.addRow(rowData);
			}
		}
	}

	 protected void insertActionPerformed(ActionEvent e) {
		    String label = e.getActionCommand();//버튼의 라벨을 읽어올 수 있음.
		 	subBook = null;
			subBook = new T02_SubBook();
			subBook.set(null, label,aBook,true);
			}
	 //수정
	 protected void updateActionPerformed(ActionEvent e) {
		 String label = e.getActionCommand();
		   subBook = null;
		   subBook = new T02_SubBook();
			subBook.set(new T03_AddressVO(), label,aBook,false);
		
	 }
	 //삭제
	 protected void deleteActionPerformed(ActionEvent e) {
		 //index : 사용자가 선택한 로우값 담기
		 int index = jt_address.getSelectedRow();
	      if(index<0) {
	         JOptionPane.showMessageDialog(this, "삭제할 데이터를 선택하세요.");
	         return;
	      } else {
	         try {
	            //paVO에 담을 정보는 command = "delete" 와 id -> u_id
	        	 T03_AddressVO paVO = new T03_AddressVO();
	        	 T03_AddressVO raVO = null;
	            String u_id = null;
	            //여러건을 다 수집하여 삭제하려면 -> 멀티로우 처리
	            for(int i=0; i<dtm_address.getRowCount();i++) {
	               if(jt_address.isRowSelected(i)) {
	                  u_id = (String)dtm_address.getValueAt(i, 0);
	                  T06_AddressBookCtrl aCtrl = new T06_AddressBookCtrl();
	                  paVO.setCommand("delete");
	                  paVO.setId(u_id);
	                  raVO = aCtrl.send(paVO);
	               }
	            }
	            if(raVO.getStatus()==1) { //삭제 성공
	               //데이터 새로고침
	               refreshData();
	            } else {
	               JOptionPane.showMessageDialog(this, "삭제 실패");
	               return;// deleteActionPerformed 탈출
	            }
	         } catch (Exception e2) {
	            e2.printStackTrace();
	         }
	      }
	      
	   }
	 //상세조회
	 protected void detailActionPerformed(ActionEvent e) {
		 String label = e.getActionCommand();
		 //이벤트 어디다 걸지? JTable(폼, 이벤트). DefaultTableModel(값을 저장, 값을 입력)
		 int index = jt_address.getSelectedRow();
		 //로그를 출력할 때 - 주의사항
		 //main을 가진 클래스는 sysout
		 //main 클래스가 없을 때는 JOptionPane.showMessageDialog(this, "데이터가 없습니다.");
		 //System.out.println("index : "+index);
		 if(index<0) {
			 JOptionPane.showMessageDialog(this, "조회할 데이터를 한 건만 선택하세요.","Error",JOptionPane.ERROR_MESSAGE);
			 return;
		 }
		 else {
			 try {
				 //선택한 후에 상세조회 화면이 열리면 기존에 선택한 로우는 clear 처리
				 jt_address.clearSelection();
				 T03_AddressVO paVO = new T03_AddressVO();
				String u_id = (String)dtm_address.getValueAt(index, 0);
				paVO.setId(u_id);
				paVO.setCommand("detail");
				T06_AddressBookCtrl aCtrl = new T06_AddressBookCtrl();
				T03_AddressVO raVO = aCtrl.send(paVO);
				subBook = null;
				subBook = new T02_SubBook();
				//문제제기- 어!! 화면그리는 메소드가 사라졌네?
				subBook.set(raVO, label,aBook,false);
			} catch (Exception e2) {
				
			} 
				
			 
		 }
	 }
	
//인스턴스화처리 ->메모리상주
	public static void main(String[] args) {
		if(aBook==null) {
			aBook = new T01_AddressBook();
		}
		aBook.initDisplay();
	}


}
 
/*********************************************
 * 1)
 * A a = NEW a();
 * a = null;
 * a = new A();
 * 이벤트 처리할 때 마다 추가
 * 선언부에서 선언만 하고 - 전변으로 선언
 * 생성은 이벤트 처리시 마다 생성
 * 
 * 객체를 인스턴스화 할 때 주의사항
 * 무조건 전역변수 위치에서 인스턴스화 하는 것이 좋은 건 아니다.
 * 
 * A a = new A();
 * 
 * A a = null;
 * a = new A();
 * 
 * 그 객체가 필요할 때 주입되는 것이 효율적이다.
 * 객체가 협업이 필요할 때 같은 메소드를 반복해서 호출하는 것은 피해야 한다. -stackoverflow 일어날 가능성 높음.
 * ->해결방법
 * 메소드 중심의 코딩을 한다.
 * -> * 반복되는 코드를 줄일 수 있다.
 * 
 * 입력 
 * 입력은 새로 입력받아야 함
 * insert를 해야 되는 경우임.
 * 
 * 수정 
 * 수정은 기존에 가지고 있는 정보를 변경하는 것
 * 수정의 경우 기본 정보를 보여주는 경우가 대부분이므로 select를 먼저하고
 * 화면을 보여주어야하지 않을까?
 * update를 해야 되는 경우임
 * 
 * 상세조회
 * 상세조회는
 * 기존에 가입된 사람의 정보를 조회하는 것
 * select를 해야 되는 경우임 
 * 
 * 세가지 메뉴를 하나의 화면에서 처리할 때 - 문제발생
 * 
 * 등록하는 화면 추가
 *
 * 
 * 
 * 2)
 * set 메소드 추가
 * set(AddressBook aBook, String title)
 * main() - AddressBook 인스턴스화처리 - 메모리상주
 * 버튼클릭(입력,수정,상세조회) -이벤트 감지
 * actionPerformed호출- 자식창 호출(set 호출)
 * SubBook의 initDisplay()가 호출이 안됨.
 * 생성자에서 호출했다.
 * subBook.initDisplay();
 * subBook.setTitle(label);
 * 
 * 문제제기
 * 실제로 생성된 객체는 하나이지만 메소드콜이 일어날 때 마다
 * (initDisplay) 객체가 여러개 복제되는
 * 일이 일어남.
 *********************************************/

/******************************************
 * 생성자 호출 - 순서 - 처리 
 * 
 * 해결 - 메소드
 * set(aBook,title,boolean,   )
 * 
 * 
*/////////////////////////////////////////////////