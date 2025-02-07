package part7_2_networrk2;

import java.awt.Color;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


public class TalkClientThread extends Thread {
	//TalkClientThread에서 TalkClient 원본을 참조하기 위해서 선언
	//생성자에서 초기화
	TalkClientVer2 tc2 = null;
	public TalkClientThread(TalkClientVer2 tc2) {
		this.tc2 = tc2;
	}
	public SimpleAttributeSet makeAttribute(String fontColor) {
		SimpleAttributeSet sas = new SimpleAttributeSet();
		sas.addAttribute(StyleConstants.ColorConstants.Foreground
				, new Color(Integer.parseInt(fontColor)));
		return sas;
	}
	public SimpleAttributeSet makeAttribute() {
		SimpleAttributeSet sas = new SimpleAttributeSet();
		return sas;
	}
	public void run() {
		String msg = null;
		boolean isStop = false;
		while(!isStop) {
			try {
				msg = (String)tc2.ois.readObject();
				//JOptionPane.showMessageDialog(tc2, "msg:"+msg);
				StringTokenizer st = null;
				int protocol = 0;
				if(msg!=null) {
					st = new StringTokenizer(msg,Protocol.seperator);
					protocol = Integer.parseInt(st.nextToken());
				}
				switch(protocol) {
				
					case Protocol.WAIT:{
						String nickName = st.nextToken();
						String state = st.nextToken();
						Vector<String> v_nick = new Vector<>();
						v_nick.add(nickName);
						v_nick.add(state);
						tc2.wr.dtm_wait.addRow(v_nick);
						//테이블 스크롤바를 자동위치 변경
						tc2.wr.jsp_wait.getVerticalScrollBar()
										.addAdjustmentListener
										(new AdjustmentListener() {
											@Override
											public void adjustmentValueChanged(AdjustmentEvent e) {
												JScrollBar jsb = (JScrollBar)e.getSource();
												jsb.setValue(jsb.getMaximum());
											}
										});
						
					}break;	
					
					case Protocol.ROOM_CREATE:{
						String roomTitle = st.nextToken();
						String currentNum = st.nextToken();
						Vector<String> v_room = new Vector<>();
						v_room.add(roomTitle);
						v_room.add(currentNum);
						tc2.wr.dtm_room.addRow(v_room);
						//테이블 스크롤바를 자동위치 변경
						tc2.wr.jsp_room.getVerticalScrollBar()
										.addAdjustmentListener
										(new AdjustmentListener() {
											@Override
											public void adjustmentValueChanged(AdjustmentEvent e) {
												JScrollBar jsb = (JScrollBar)e.getSource();
												jsb.setValue(jsb.getMaximum());
											}
										});
					}break;
					
					case Protocol.ROOM_LIST:{
				    	String roomTitle = st.nextToken();
				    	String currentNum = st.nextToken();
				    	Vector<String> v_room = new Vector<>();
				    	v_room.add(roomTitle);
				    	v_room.add(currentNum);
				    	tc2.wr.dtm_room.addRow(v_room);
				    	tc2.wr.jsp_room.getVerticalScrollBar()
				    	.addAdjustmentListener(
				    			new AdjustmentListener() {
				    				@Override
				    				public void adjustmentValueChanged(AdjustmentEvent e) {
				    					JScrollBar jsb = (JScrollBar)e.getSource();
				    					jsb.setValue(jsb.getMaximum());
				    				}
				    			});				    	
				    }break;
				    
					case Protocol.ROOM_IN:{
					/* 정보 가져오기 */
					String roomTitle = st.nextToken();
					String current = st.nextToken();
					String nickName = st.nextToken();
					
					//단톡 인원 정보 갱신 처리 : 단톡(방제)를 비교 하여 갱신
					//JOptionPane.showMessageDialog(tc2, "인원수 갱신");
					for(int i=0;i<tc2.wr.jtb_room.getRowCount();i++) {
						if(roomTitle.equals(tc2.wr.dtm_room.getValueAt(i, 0))) {
							//current = 현재인원 / i = 로우값  / 1 = 두번째 컬럼 : 인원수
							tc2.wr.dtm_room.setValueAt(current, i, 1);
							break;
						}
					}
					
					//대기실 위치 갱신 : 닉네임 비교하여 갱신
					//JOptionPane.showMessageDialog(tc2, "대기실 위치 갱신 중");
					for(int i=0;i<tc2.wr.jtb_wait.getRowCount();i++) {
						if(nickName.equals(tc2.wr.dtm_wait.getValueAt(i, 0))) {
							//
							tc2.wr.dtm_wait.setValueAt(roomTitle, i, 1);
						}
					}
					
					//MessageRoom추가 : 단톡명을 누른 사람만 화면 이동 처리 = MessageRoom
					if(tc2.nickName.equals(nickName)) {
						tc2.tp.setSelectedIndex(1);
						for(int x=0; x<tc2.wr.jtb_wait.getRowCount(); x++) {
							if(roomTitle.equals(tc2.wr.dtm_wait.getValueAt(x, 1))) {
								String imsi[] = {(String)tc2.wr.dtm_wait.getValueAt(x, 0)};
								tc2.mr.dtm_name.addRow(imsi);
							}
						}
					}
					
					
				}break;
					case Protocol.ROOM_INLIST:{
						String roomTitle = st.nextToken();
						String current = st.nextToken();
						String nickName = st.nextToken();
						Vector<String> v_room = new Vector<>();
				    	v_room.add(nickName);
				    	tc2.mr.dtm_name.addRow(v_room);
					}break;
				case Protocol.MESSAGE:{
					String nickName = st.nextToken();
					String message = st.nextToken();
					String imgChoice = "";
					while(st.hasMoreTokens()) {
						imgChoice = st.nextToken();
					}
					MutableAttributeSet attr1 = 
							new SimpleAttributeSet();
					if(!imgChoice.equals("default")) {//이모티콘 메시지 일때
						int i=0;
						//이모티콘 배열 객체에서 같은 이미지를 찾아서 출력해야 함.
						for(i=0;i<tc2.mr.emo.imgFiles.length;i++) {
							if(tc2.mr.emo.imgFiles[i].equals(imgChoice)) {
								StyleConstants.setIcon
								(attr1, new ImageIcon(tc2.mr.emo.imgPath+tc2.mr.emo.imgFiles[i]));
								try {
									tc2.mr.sd_display.insertString(tc2.mr.sd_display.getLength()
											, "\n"
											, attr1);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
					else if(imgChoice.equals("default")) {//이모티콘 메시지가 아닐때
						try {
							tc2.mr.sd_display.insertString(tc2.mr.sd_display.getLength()
									, "["+nickName+"]"+message+"\n"
									, null);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					//tc2.jta_display.append("["+nickName+"]"+message+"\n");
					tc2.mr.jtp_display.setCaretPosition(tc2.mr.sd_display.getLength());
				}break;
				
				case Protocol.WHISHER:{
				     String fromName = st.nextToken();
				     String toName = st.nextToken();
				     String message = st.nextToken();
				     try {
				      tc2.mr.sd_display.insertString(tc2.mr.sd_display.getLength()
				             ,fromName+"님이"+toName+"님에게"+message+"\n"
				             ,null);
				     } catch (Exception e) {
				      e.printStackTrace();
				     }
				    }break; 
				    
				case Protocol.CHANGE:{
					String nickName = st.nextToken();
					String afterName = st.nextToken();
					String noticemsg  = st.nextToken();
					//대화내용 잘라서 받기
					for(int i=0;i<tc2.mr.dtm_name.getRowCount();i++) {
						//테이블 대화명 변경
						String currentName = (String)tc2.mr.dtm_name.getValueAt(i, 0);
						if(currentName.equals(nickName)) {
							tc2.mr.dtm_name.setValueAt(afterName, i, 0);
							break;
						}
						//변경된 대화명 메시지 출력
						try {
							//첫번째는 이모티콘 때문에 위치 얻는 파라미터
							//두번째는 출력할 메시지를 나타냄
							//세번째는 스타일 적용 = 글꼴 글자크기 글자색 등
							tc2.mr.sd_display.insertString(tc2.mr.sd_display.getLength()
									, noticemsg +"\n"
									, null);
						} catch (Exception e) {
							e.printStackTrace();
						}
						tc2.mr.jtp_display.setCaretPosition(tc2.mr.sd_display.getLength());
						//채팅창 타이틀 변경
						if(nickName.equals(tc2.nickName)) {
							tc2.setTitle(afterName+"님의 대화창");
							//전역변수 활용능력 + 초기화 문제
							tc2.nickName = afterName;		
						}
					}					
				}break;
				}/////////end of switch
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
