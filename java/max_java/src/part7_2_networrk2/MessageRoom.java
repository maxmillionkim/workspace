package part7_2_networrk2;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import part7_2_networrk2.Protocol;

public class MessageRoom extends JPanel implements ActionListener {
	//선언부
		TalkClientVer2 	tc2 				= null;
		JPanel 			jp_first 			= new JPanel();
		JPanel 			jp_second 			= new JPanel();
		JPanel 			jp_second_south 	= new JPanel();
		//메시지 전송할 때 - 이벤트처리 필요함
		JTextField  	jtf_msg 			= new JTextField();
		/*
		 * JTextPane에 스타일을 적용하기 위해서는 스타일을 지원하는 클래스를 추가로 매핑해야함.
		 * 왜냐하면 문자도 그리는 개념으로 이해해야 하므로 글꼴을 변경하거나 글크기를 변경하는
		 * 부분도 반영하려면 필요함
		 */
		StyledDocument sd_display = 
				new DefaultStyledDocument(new StyleContext());
		JTextPane 		jtp_display 		= new JTextPane(sd_display);
		//메세지 내역 출력  - 비활성함. -이벤트처리 필요없음
		JScrollPane 	jsp_display			= new JScrollPane(jtp_display
															,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
															,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		String 			cols[] 				= {"닉네임"};
		String 			data[][] 			= new String[0][1];
		//실제 정보를 담을 객체
		DefaultTableModel dtm_name		= new DefaultTableModel(data,cols);
		//화면을 처리해줄 객체
		JTable 	          jtb_name  	= new JTable(dtm_name);
		JScrollPane       jsp_name  	= new JScrollPane(jtb_name);
		JButton           jbtn_whisper 	= new JButton("1:1대화");
		JButton           jbtn_change 	= new JButton("대화명변경");
		JButton           jbtn_icon 	= new JButton("이모티콘");
		JButton           jbtn_exit 	= new JButton("종료");	
		emoticonMessage   emo 			= new emoticonMessage(this);
		//생성자
		public MessageRoom() {
			
		}
		public MessageRoom(TalkClientVer2 tc2) {
			this.tc2 = tc2;
			initDisplay();
		}
		//화면처리부
		public void initDisplay() {
			jbtn_icon.addActionListener(this);
			jbtn_whisper.addActionListener(this);
			jbtn_change.addActionListener(this);
			this.setLayout(new GridLayout(1,2));
			jtf_msg.addActionListener(this);
			jp_first.setLayout(new BorderLayout());
			jp_first.add("Center",jsp_display);
			jp_first.add("South", jtf_msg);
			jp_second.setLayout(new BorderLayout());
			jp_second.add("Center",jsp_name);
			jp_second_south.setLayout(new GridLayout(2,2));
			jp_second_south.add(jbtn_whisper);
			jp_second_south.add(jbtn_change);
			jp_second_south.add(jbtn_icon);
			jp_second_south.add(jbtn_exit);
			jp_second.add("South", jp_second_south);
			this.add("Center",jp_first);
			this.add("East",jp_second);
			//this.setSize(500, 600);
			//this.setVisible(true);		
		}
		public void message_process(String msg, String imgChoice) {//200|닉네임|메세지
			//단톡명 가져오기
			String roomTitle=null;
			for(int i=0;i<tc2.wr.jtb_wait.getRowCount();i++) {
				//대화명을 비교하므로 getValueAt의 두 번째 파라미터는 0으로 하고
				if(tc2.nickName.equals(tc2.wr.dtm_wait.getValueAt(i, 0))) {//현재 내 대화명과 dtm_wait에서 가져온대화명이 같니?
					//위에서 같은 대화명을 찾았으므로 i는 위 if문의 i값과 동일한 로우를 가리키고
					//두번째 파라미터는 단톡명이므로 0이아니라 1을 준다.
					roomTitle = (String)tc2.wr.dtm_wait.getValueAt(i, 1);
					break;
				}
			}
			//이모티콘 메시지를 전송
			if(imgChoice!=null) {//이모티콘을 보낼거니?
				msg = "이모티콘";
				try {
					tc2.oos.writeObject(Protocol.MESSAGE//200
							+ Protocol.seperator+roomTitle//단톡명
							+ Protocol.seperator+tc2.nickName//닉네임
							+ Protocol.seperator+msg//이모티콘
							+ Protocol.seperator+imgChoice//선택한 이모티콘 정보 넘김
							);				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//텍스트 메시지를 전송
			else{
				try {
					tc2.oos.writeObject(Protocol.MESSAGE//200
							+ Protocol.seperator+roomTitle//단톡명
							+ Protocol.seperator+tc2.nickName//닉네임
							+ Protocol.seperator+msg//주말에 뭐해?
							+ Protocol.seperator+"default"//주말에 뭐해?
							);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			jtf_msg.setText("");
		}	
		//단위테스트 때문에 추가함 - TalkClientVer2에 붙일것임.
		public static void main(String[] args) {
			MessageRoom mr = new MessageRoom();
			mr.initDisplay();
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			Object obj = e.getSource();
			String msg = jtf_msg.getText();
			if(obj==jtf_msg) {
				//다자간 대화하기
				message_process(msg,null);				
			}else if(obj==jbtn_icon) {
				emo.setVisible(true);
			}
		}
}
