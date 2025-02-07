package project_BaseballGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Part1_1_BaseballGameView extends JFrame {
	//선언부
	
	// 게임 결과창 추가	
	JPanel jp_center = new JPanel();
	
	// 게임 결과창 결과 출력물
	JTextArea jta_display = new JTextArea();
	
	public JTextArea getContent() {
		return jta_display;
	}
	
	//스크롤바 - 
	JScrollPane jsp_display = 
			new JScrollPane(jta_display
					,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
					,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	//jp_east속지에 버튼 4개 추가 - new GridLayout(4,1);
	JPanel jp_east = new JPanel();
	JTextField jtf_user = new JTextField();
	// 버튼 생성
	JButton jbtn_new = new JButton("New Game");
	JButton jbtn_dap = new JButton("Give up?");
	JButton jbtn_clear = new JButton("Delete All");
	JButton jbtn_exit = new JButton("Exist");


	//화면 처리부
	public void initDisplay() {
		
		//센터 서쪽 배경색 설정
		jp_center.setBackground(new Color(224,203,234));
		jp_east.setBackground(new Color(255,255,101));
		// 창 크기 설정
		int width = 400;		//지역변수 = 메소드 안에 선언
		int height = 500;		//지역변수 > 화면크기는 고정이므로
		// 타이틀 설정
		String title = "야구숫자게임 ver2.1";
		//속지 레이아웃을 BorderLayout으로 변경
		jp_center.setLayout(new BorderLayout());
		//jp_east 속지 레이아웃을 GridLayout으로 변경
		jp_east.setLayout(new  GridLayout(4,1));
		//JFrame 중앙에 hp_center, 동쪽에 jp_east
		jp_center.add("Center",jsp_display);
		this.add("Center",jp_center);
		//버튼 추가
		jp_east.add(jbtn_new);
		jp_east.add(jbtn_dap);
		jp_east.add(jbtn_clear);
		jp_east.add(jbtn_exit);
		jta_display.setBackground(new Color(255,255,200));
		//채팅창 추가
		jp_center.add("South",jtf_user);
		this.add("East",jp_east);
		this.setTitle(title);
		this.setSize(width, height);
		this.setVisible(true);

		//버튼의 배경색과 글자색 변경
		jbtn_new.setBackground(new Color(106,87,115));
		jbtn_new.setForeground(new Color(255,255,255));
		jbtn_dap.setBackground(new Color(106,32,20));
		jbtn_dap.setForeground(new Color(255,255,255));
		jbtn_clear.setBackground(new Color(247,169,0));
		jbtn_clear.setForeground(new Color(255,255,255));
		jbtn_exit.setBackground(new Color(103,90,208));
		jbtn_exit.setForeground(new Color(255,255,255));
		
		// 게임 출력창
//		jp_center.setLayout(new BoxLayout(jp_center, BoxLayout.Y_AXIS));
		jta_display = new JTextArea("Let's play new Game! \n Click the New Game button \n", 30, 30);
		jta_display.setEnabled(false);
		jp_center.add(jta_display);
		
	}
	
	public static void main(String[] args) {
		Part1_1_BaseballGameView view = new Part1_1_BaseballGameView();
		Part1_2_BaseballGameLogic logic = new Part1_2_BaseballGameLogic(view);
		Part1_3_BaseballGameEvent event = new Part1_3_BaseballGameEvent(view, logic);
		
		view.initDisplay();
		event.GameEvent();
		logic.ranCom();
		
	}
}
