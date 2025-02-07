<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String c_name=null;
	Cookie cookies[] = request.getCookies();
	for(int i=0;i<cookies.length;i++){
		out.println("CookieInfo: "+cookies[i].getValue());
		if(cookies[i].getName().equals("c_name")){
			c_name=cookies[i].getValue();
		}
	}
	if(c_name==null){
		out.print("c_name==null");
		/* response.sendRedirect("../D03_1_p47_Ch2"); */ 
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Layout test</title>
<%@ include file="../../../Stylesheet/JEasyUICommon2.jsp" %>
<!-- 이벤트 처리 -->
	<script type="text/javascript">
		//멀티초이스 구현
		function menuChoice() {}
		
		var db_name ="김유신";
		
		//로그인 요청시 구현
		function loginAction(){
			//front-end 언어는 오라클 서버와 연동 할 수 없음 = 상수로 처리
			var db_id ="test";
			var db_pw ="test";
			
			var u_id = $("#tb_id").val();
			var u_pw = $("#tb_pw").val();
			
			if(db_id==u_id||mem_id==u_id){//아이디 확인
				if(db_pw==u_pw||mem_id==u_pw){//비밀번호 확인
					//로그인 성공
					//로그인창 숨기기 
					$("#login1").hide();
					$("#login2").hide();
					//로그아웃창 보여주기
					$("#logout").show();
					//메시지 추가
					$("#msg").html("<b> welcome "+db_name);
				}
				else{//비밀번호 틀림
					alert("Check your pw");
					$("#tb_pw").textbox('setValue','');
				}
			}else{//아이디 없음
				alert("Check your id");
			}
		}
		
		//로그아웃 요청시 구현
		function logoutAction(){
			//로그인창 보여주기
			$("#login1").show();
			$("#login2").show();
			//로그아웃창 감추기
			$("#logout").hide();
			//DB에 남은 잔존 자료 초기화
			$("#tb_id").textbox('setValue','');
			$("#tb_pw").textbox('setValue','');
		}
		//H.P 전화번호로 검색
		function HPSearch(){
			alert("검색 기능 성공")
		}
		//추가버튼 클릭시 다이알로그 팝업
		function addForm(){
			$('#dlg_add').dialog('open');
		}
		//저장 버튼 이벤트 : 사용자 입력정보를 Tomcat서버로 전송
		// form 태그  
		// method 속성 : get(default:인터셉트 가능성)
		// action 속성 : 
		function add(){
			// action 값을 sumit 서버로 get방식으로 전송
			$("#f_add").submit();
		}	
		//취소 버튼 이벤트 
		function canclce(){
			
		}
		
		//수정버튼 클릭시 팝업창 열기 => 데이터 불러와서 조회
		function updateForm(){
			var row = $("#dg_member").datagrid("getSelected")
			var c_id =row.id;
			//alert("weolcome"+c_id);
			//톰캣 서버로 아이디와 같은 상세정보 조회 하면 화면에 출력
			location.href="./memberUpdateForm.jsp?id="+c_id;
		}
	
		//databox에 대한 날짜 포맷 재정의 : 날짜 넣기
		$.fn.datebox.defaults.formatter = function(date){
			var y = date.getFullYear();
			var m = date.getMonth()+1;
			var d = date.getDate();
			return y+'/'+(m<10?('0'+m):m)+'/'+(d<10?('0'+d):d);
		}
		//databox에 날짜 포맷을 적용
		$.fn.datebox.defaults.parser = function(s){
			var t = Date.parse(s);
			if (!isNaN(t)){
				return new Date(t);
			} else {
				return new Date();
			}
		}
	</script>
<!-- 아이디 위치 정해주기 -->
	<style>
		#login{
			border: 1px solid red;
			width:  219px;
			height: 201px;
		}
	</style>
</head>
<body>
	<!-- 페이지 숨김 처리 및 클릭시 보여지기 -->
	<script type="text/javascript">
		$(document).ready(function() {
			//페이지 모두 숨기기
			$("#Staff").hide();
			$("#Member").hide();
			$("#DVD").hide();
			$("#Sal").hide();
			//처음에 로그인 창만, 로그아웃창 숨기기
			$("#logout").hide();
			//로그인 성공 시, 로그인창 숨기기
			
			//클릭하는 메뉴 페이지 보여주기 : easyUI - tree 객체 - onClick 이벤트 사용
			//따로 함수 선언하지 않고 즉시 처리
			//파라미터로 넘어오는 node는 사용자가 선택한 엘리먼트 정보 가짐
			$('#tree_menu').tree({
				//트리메뉴 선택했을때 이벤트 처리
				//alert(node.text); // = alert node text property when clicked
				//node.text하면 선택한 노드문자열 가져옴.
				onClick : function(node) {
					if("Staff INF" == node.text){
						//show메소드 호출하면 화면에 출력되고
						//hide메소드 호출하면 화면에 숨김 처리됨.
						$("#Staff").show();
						$("#Member").hide();
						$("#DVD").hide();
						$("#Sal").hide();
					}
					else if("Member INF" == node.text){
						$("#Staff").hide();
						$("#Member").show();
						$("#DVD").hide();
						$("#Sal").hide();
					}
					else if("DVD INF" == node.text){
						$("#Staff").hide();
						$("#Member").hide();
						$("#DVD").show();
						$("#Sal").hide();
					}
					else if("Sal INF" == node.text){
						$("#Staff").hide();
						$("#Member").hide();
						$("#DVD").hide();
						$("#Sal").show();
					}
					
				}
				
			}); /* 트리구조 종료 */
			/*사원 관리 데이터 그리드 추가*/
/* 			$("#dg_staff").datagrid({
				url:'../Json/dept.json'
				,singleSelect:'true'
				// 툴바 추가
				,toolbar:'#toolbar_staff_search'
				,columns:[[
					 {field:'dept', 	title:'dept', 	 width:100}
					,{field:'dname', 	title:'dname', 	 width:100}
					
				]]
			}); */

			/* 회원 관리에 필요한 데이터 그리드 추가 설정 */
/* 			$("#dg_member").datagrid({
				url:'../Json/Jsonmember.json'
				,singleSelect:'true'
				// 툴바 추가
				,toolbar:'#toolbar_search'
				,columns:[[
					 {field:'id', 		title:'ID', 	 width:100}
					,{field:'name', 	title:'name', 	 width:100}
					,{field:'address', 	title:'address', width:100}
					,{field:'HP', 		title:'HP', 	 width:100}
					
				]]
			}); */

	}); /* end of ready dom 구성  */
	</script>

	<!-- 전체 레이아웃 설정 -->
	<div id="cc" class="easyui-layout"
		style="width: 1300px; height: 750px;">

		<!-- 서쪽 레이아웃 설정 -->
		<div data-options="region:'west',split:true"
			 title="Company example" style="width: 219px;">
			 
		<!-- 여백 설정 -->
		<div style="margin:20px 0;"></div>
		
			<!-- login창 easyUI에서 가져와 만들기 -->
			<div id="login1" class="easyui-panel" title="Login to system"
				style="width: 100%; max-width: 400px; padding: 5px 5px;">
				<div style="margin-bottom: 10px">
					<input id="tb_id" class="easyui-textbox"
						style="width: 200px; height: 40px;"
						data-options="prompt:'Username',iconCls:'icon-man',iconWidth:38">
				</div>
				<div style="margin-bottom: 20px">
					<input id="tb_pw" class="easyui-textbox" type="password"
						   style="width: 200px; height: 40px;"
						data-options="prompt:'Password',iconCls:'icon-lock',iconWidth:38">
				</div>
				<div style="margin-bottom: 20px">
					<input type="checkbox" checked="checked"> <span>Remember me</span>
				</div>
				<div>
					<a href="javascript:loginAction()"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-ok'"
						style="display: inline; padding: 5px 0px; width: 40%;"> <span
						style="font-size: 10px;">Log-in</span>
					</a>
					<a href="#" class="easyui-linkbutton"
						data-options="iconCls:'icon-ok'"
						style="display: inline; padding: 5px 0px; width: 40%;"> <span
						style="font-size: 10px;">Sign in</span>
					</a>
				</div>
			</div>
			<!-- 로그인 추가 끝 -->
						
			<!-- LogOut창 구현하기 시작 -->
			<div id="logout" align="center">
				<span id="msg"></span>
				<div style= "margin:5px 0;"></div>
				<a href="javascript:logoutAction()" class="easyui-linkbutton"
					data-options="iconCls:'icon-ok'"
					style="padding: 5px 0px; width: 50%; "> <span
					style="font-size: 14px;">Log-Out</span>
				</a> <a href="#" class="easyui-linkbutton"
					data-options="iconCls:'icon-ok'"
					style="padding: 5px 0px; width: 50%; "> <span
					style="font-size: 14px;">Edit Info</span>
				</a>
			</div>
			<!-- LogOut창 구현하기 종료-->
			
			<!-- 트리 구조 시작 -->
			<!-- 트리 외부라인선 추가 -->
			<div class="easyui-panel" style="padding: 5px">
				<ul id="tree_menu" class="easyui-tree">
				
					<li data-options="state:'closed'"><span>Staff INF</span>
						<ul>
							<li>Select</li>
							<li>Join</li>
							<li>Delete</li>
						</ul></li>
				
					<li data-options="state:'closed'"><span>Member INF</span>
						<ul>
							<li>Select</li>
							<li>Join</li>
							<li>Delete</li>
						</ul></li>

					<li data-options="state:'closed'"><span>DVD INF</span>
						<ul>
							<li>Select</li>
							<li>Join</li>
							<li>Delete</li>
							<li>check</li>
						</ul></li>

					<li data-options="state:'closed'"><span>Sal INF</span>
						<ul>
							<li>Today</li>
							<li>Months</li>
							<li>Select</li>
						</ul></li>
				</ul>
			</div>
		</div>
		<!-- 트리 구조 종료 -->
		<!-- 센터 레이아웃 설정 시작 -->
		<div data-options="region:'center',title:'DVD관리 시스템'">
			<!-- 여백설정 -->
			<div style="margin: 10px;"></div>
						<!--------- Staff -------------------> 
			<div id="Staff">
				<!-- 사원관리 화면 시작 -->
				 <table id="dg_staff" 
						width="650px" height="450px"
						class="easyui-datagrid">
				
				</table>
				<!-- 사원관리 화면 툴바 시작-->
				<div id="toolbar_staff_search">
					<table border="1" width="650">
						<!-- 조건 검색 추가 -->
						<tr>
							<!-- 이름검색창 -->
							<td align="left">
							<label>&nbsp;Name&nbsp;</label>
								<input class="easyui-textbox"
										data-options="prompt:'Enter Name...', buttonText:'SEARCH'" 
										style="width:250px;">
							</td>
							<!-- 생년월일 검색 -->
							<td align="left">
							<label>&nbsp;B-day&nbsp;</label>
								<input class="easyui-datebox"
										data-options="prompt:'Enter B-day...'" 
										style="width:250px;">
							</td>
						</tr>
						<tr>
							<!-- HP 전화번호 검색 -->
							<td>
							<label>&nbsp;&nbsp;H.P.&nbsp;&nbsp;</label>
								<input class="easyui-maskedbox" mask="999-9999-9999" 
										data-options="prompt:'Enter H.P...',searcher:HPSearch" 
										style="width:250px;">
							</td>
						</tr>
					</table>
						<!-- 조건검색 화면 종료  -->	
				</div>
			</div>
				<!-- 사원관리 화면 종료 -->
			<!--------- Staff -------------------> 
			<!--------- Member -------------------> 
			<div id="Member">
				<!-- 회원관리 화면 시작 -->
				 <table id="dg_member" 
						width="650px" height="450px"
						class="easyui-datagrid">
				
				</table>
				<!-- 회원관리 화면 툴바 시작-->
				<div id="toolbar_search">
					<table border="1" width="650">
						<!-- 조건 검색 추가 -->
						<tr>
							<!-- 이름검색창 -->
							<td align="left">
							<label>&nbsp;Name&nbsp;</label>
								<input class="easyui-textbox"
										data-options="prompt:'Enter Name...', buttonText:'SEARCH'" 
										style="width:250px;">
							</td>
							<!-- 생년월일 검색 -->
							<td align="left">
							<label>&nbsp;B-day&nbsp;</label>
								<input class="easyui-datebox"
										data-options="prompt:'Enter B-day...'" 
										style="width:250px;">
							</td>
						</tr>
						<tr>
							<!-- HP 전화번호 검색 1 -->
							<td>
							<label>&nbsp;&nbsp;H.P.&nbsp;&nbsp;</label>
								<input class="easyui-searchbox" 
										data-options="prompt:'Enter H.P...',searcher:HPSearch" 
										style="width:250px;">
							</td>
							<!-- HP 전화번호 검색 2 -->
							<td>
							<label>&nbsp;&nbsp;H.P.&nbsp;&nbsp;</label>
								<input class="easyui-maskedbox" mask="999-9999-9999" 
										data-options="prompt:'Enter H.P...',searcher:HPSearch" 
										style="width:250px;">
							</td>
						</tr>
						<!-- 조건검색 화면 종료  -->	
						<!-- 툴바 버튼 추가 -->
						<tr>
							<td align="right">
								<a href="#" class="easyui-linkbutton" 
									iconCls="icon-search" plain="true">조회</a>
								<a href="javascript:addForm()" class="easyui-linkbutton" 
									iconCls="icon-add" plain="true">입력</a>
								<a href="javascript:updateForm()" class="easyui-linkbutton" 
									iconCls="icon-edit" plain="true">수정</a>
								<a href="#" class="easyui-linkbutton" 
									iconCls="icon-remove" plain="true">삭제</a>
							</td>
						</tr>
					</table>
				<!-- 회원관리 화면 툴바 종료 -->
				</div>
				
<!-------  --> <!-- 회원정보 등록 화면 시작 -->
				<div id="dlg_add" class="easyui-dialog" title="회원등록"
						style="width:600px; height:350px; padding:10px"
						data-options="toolbar:'#toolbar_add', closed:true,
									cache:  false, modal: true">
					<form id="f_add" method="get" action="addtest.jsp">
						<table>
							<tr>
								<td>
								<input class="easyui-textbox" 
									label="ID"
									data-options="prompt:'Enter New ID...'" 
									style="width:300px;">
								</td>
							</tr>
							<tr>
								<td>
								<input class="easyui-textbox" 
									label="Name"
									data-options="prompt:'Enter New Name:...'" 
									style="width:300px;">
								</td>
							</tr>
							<tr>
								<td>
								<input class="easyui-textbox" 
									label="Address"
									data-options="prompt:'Enter New Address...'" 
									style="width:300px;">
								</td>
							</tr>
							<tr>
								<td>
								<input class="easyui-textbox" 
									label="H.P."
									data-options="prompt:'Enter New H.P....'" 
									style="width:300px;">
								</td>
							</tr>
							<tr>
								<td>
								<input  class="easyui-textbox" 
										label="Gender"
										data-options="prompt:'Enter Gender...'" 
										style="width:300px;">
								</td>
							</tr>
							<tr>
								<td>
								<input  class="easyui-textbox" 
										label="Birthday"
										data-options="prompt:'Enter your Birthday...'" 
										style="width:300px;">
								</td>
								 <td 	class="easyui-datebox" 
								 		label="Birthday" 
								 		data-options="prompt:'Choose your Birthday...'" 
								 		style="width:250px;">
								 </td>
							</tr>
							<tr>
								<td>
								<input class="easyui-textbox" 
									label="Reference"
									data-options="prompt:'Enter Reference...'" 
									style="width:300px;">
								</td>
							</tr>
							<tr>
								<td>
								<input class="easyui-textbox" 
									label="Address"
									data-options="prompt:'Enter New Address...'" 
									style="width:300px;">
								</td>
							</tr>
						</table>
					</form>
				</div>
				<div id="toolbar_add">
            			<a href="javascript:add()" class="easyui-linkbutton" 
							iconCls="icon-ok" plain="true">OK</a>
            			<a href="javascript:cancel()" class="easyui-linkbutton" 
							iconCls="icon-cancel" plain="true">Cancel</a>
				</div>
<!-------  --> <!-- 회원정보 등록 화면 종료 -->

<!-------  --> <!-- 회원정보 수정 화면 시작 -->

<!-------  --> <!-- 회원정보 수정 화면 종료 -->

<!-------  --> <!-- 회원정보 삭제 화면 시작 -->

<!-------  --> <!-- 회원정보 삭제 화면 종료 -->
			</div>
				<!-- 회원관리 화면 종료 -->
			<!--------- Member -------------------> 
			<!-- DVD -->
			<div id="DVD">DVD</div>
			<!-- Sal -->
			<div id="Sal">Sal</div>
		<!-- 센터 레이아웃 설정 종료 -->
		</div>

	</div>

</body>
</html>