<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- JSTL Tag를 사용하기 위한 필수 설정 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>GameboardDetail</title>
<link href="${pageContext.request.contextPath}/bootstrap-5.1.3-dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
<link href="${pageContext.request.contextPath}/fontawesome-free-6.2.0-web/css/font-awesome.min.css" rel="stylesheet">
<script language=javascript>
function checkId(){
	var boardUserId = '${requestScope.resultContent.userId}';
	var currentUserId = '<%= (String)session.getAttribute("userId") %>';
	// session의 id와 현재 id 비교
	if(boardUserId !== currentUserId){
		alert("권한이 없습니다.");
		return false;
	} else {
		return true;
	}
}
function sendUpdate(){
	if(checkId()){
		document.requestForm.command.value ="updateForm";
		document.requestForm.submit();
	}
}	
function sendDelete(){
	if(checkId()){
		confirm("삭제하시겠습니까?");
		document.requestForm.command.value ="delete";
		document.requestForm.submit();
	}
}	
</script>

<style>
.list-wrap {
	display: flex;
    justify-content: flex-start;
    margin-top: 1em;
    border-bottom: 1px solid #b6b6b6;
}

.list-wrap > * {
	margin-right: 29px;
    margin-left: 40px;
}

.in-list {
	display: flex;
    justify-content: space-around;
    margin-top: 1em;
    border-bottom: 1px solid #ccc;
}

</style>


</head>
<body>


	<!-- Navigation-->
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top" id="mainNav">
            <div class="container px-4">
                <a class="navbar-brand" href="game">Team Happy</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>
                <div class="collapse navbar-collapse" id="navbarResponsive">
                	<ul class="navbar-nav ms-auto">
					<c:choose>
						<c:when test="${sessionScope.userId != null}">
							<li class="nav-item" style="color: white;">환영합니다. ${sessionScope.userId} 님
							&nbsp;</li><li class="nav-item" style="color: white; font-size: 15px;
							cursor: pointer;" onclick="location.href='Logout.jsp';">[ Logout ]</li>
						</c:when>

						<c:otherwise>
							<li class="nav-item"><a class="nav-link" href="NewLogin.jsp">LogIn</a></li>
							<li class="nav-item"><a class="nav-link" href="NewSignUp.jsp">SignUp</a></li>
						</c:otherwise>
					</c:choose>

				</ul>
			</div>
            </div>
        </nav>
	
	
	<div style="margin: 5rem auto 0; border: 1px solid black; width:700px; height: 500px; overflow: auto;">

		<br><h2 class="text-center">게시글 보기</h2>
		<p>&nbsp;</p>
		<div class="table table-responsive">
			<table class="table">
				<tr>
					<th class="success">글번호</th>
					<td>${requestScope.resultContent.boardNum}</td>
					<th class="success">조회수</th>
					<td>${requestScope.resultContent.readNum}</td>
				</tr>


				<tr>
					<th class="success">작성자</th>
					<td>${requestScope.resultContent.userId}</td>
					<th class="success">작성일</th>
					<td>${requestScope.resultContent.boardCreatedDate}</td>
				</tr>

				<tr>
					<th class="success">제목</th>
					<td colspan="3">${requestScope.resultContent.boardTitle}</td>
				</tr>

				<tr>
					<th class="success">글 내용</th>
					<td colspan="3">${requestScope.resultContent.boardContent}</td>
				</tr>
			</table>
		</div>
		<form action="comment" method="post">
			<input type="hidden" name="command" value="commentSave">
			<input type="hidden" name = "boardNum" value="${requestScope.resultContent.boardNum}"> 
			<input type="text" name="commentWrite" placeholder="댓글을 입력해주세요."> 
			<input type="submit" value="저장">
		</form>
	
		<%-- 덧글창 입니다. --%>
	
			<div class="list-wrap">
			      <p>No</p>
			      <p>작성자</p>
			      <p>내용</p>
			      <p>작성일자</p>
			      <p> </p>
			</div>
			<c:forEach items="${requestScope.comList}" var="data" varStatus="loop">
			  <div class="in-list">
			      <p scope="row">${loop.count}</p>
			      <p>${data.userId}</p>
			      <p>${data.commentBoard}</p>
			      <p>${data.commentCreatedDate}</p>
			      <form action="comment" method="post" name="comfun" >
			      	<input type="hidden" name="command" value="commentDelete">
			      	<input type="hidden" name="commentNum" value="${data.commentNum}"> 
			      	<input type="hidden" name="boardNum" value="${requestScope.resultContent.boardNum}">
			      	<input type="submit" value="삭제" onclick="idCheck2('${data.userId}')"  >
			      </form>
			       </div> 
			</c:forEach>
			
		
			<%-- 덧글창 입니다. --%>
	</div>	
	
	
	

	<div style="margin: 0 auto; border: 1px solid black; text-align: right; width:700px;">
		<form name="requestForm" method=post action="board">
			<input type=hidden name=num value="${requestScope.resultContent.boardNum}">
			<input type=hidden name="command" value="">
			<input type=hidden name="game_num" value="${requestScope.resultContent.gameNum}">
			<input type=button class="btn btn-sm btn-primary" id="btnUpdate" value="수정하기" onClick="sendUpdate()">
			<input type=button class="btn btn-sm btn-primary" id="btnList" value="삭제하기" onClick="sendDelete()">
		</form>

		<button type="button" class="btn btn-sm btn-primary" id="btnList" onclick="location.href='javascript:window.history.back();'">목록</button>
	</div>
	
	

	<!-- Footer-->
	<footer class="py-5 bg-dark" style="position: absolute; bottom: 0; width: 100%;">
		<div class="container px-4">
			<p class="m-0 text-center text-white">Team Happy _ 주현 , 재선 , 정현</p>
		</div>
	</footer>
	
	
	<script type="text/javascript">
	function idCheck2(test){
		var commentUser = test;
		var currentUserId = '<%= (String)session.getAttribute("userId") %>';
		// session의 id와 현재 id 비교
		if(commentUser !== currentUserId){
			alert("권한이 없습니다.");
			return false;
		} else {
			alert("덧글이 삭제 되었습니다.");
			return true;
		}
	}
	</script>
	
	
	
	
</body>
</html>