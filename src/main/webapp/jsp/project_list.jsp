<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>プロジェクト一覧｜進捗管理表</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>

<!-- ページ上部固定表示 -->
<body>
<header class="main-header">
<div class="user-info">
ログイン中のユーザー：
<strong><c:out value="${not empty sessionScope.user_name ? sessionScope.user_name : (not empty sessionScope.loginid ? sessionScope.loginid : 'ゲスト')}"/></strong></div>
<button type="button" class="logout-button" onclick="document.getElementById('logout-popup').style.display='block'">
ログアウト</button>
</header>





	<main class="list-container">
	<h1>プロジェクト一覧</h1>
	<div class="action-area">
	<a href="${pageContext.request.contextPath}/jsp/project_form.jsp" class="create-button">新規プロジェクト作成</a>
	</div>
	<table class="project-table">
	<thead>
	<tr>
	<th>プロジェクト名</th>
	<th>締め切り</th>
	<th>進捗</th>
	<th>タスク管理</th>
	<th>操作</th>
	</tr>
	</thead>
	
	<tbody>
	<c:choose>
	<c:when test="${not empty projectlist}">
	<c:forEach var="project" items="${projectlist}">
	<tr>
	<!-- 1列目：プロジェクト名 -->
	<td class="project-name"><c:out value="${project.projectName}"/></td>
	
	<!-- 2列目：締め切り -->
	<td><c:out value="${project.deadline}"/></td>
	
	<!-- 3列目：進捗（棒グラフ ＆ 件数表示） -->
	<td class="progress-cell">
	    <div class="progress-bar-container">
	        <div class="progress-bar-fill" style="width: ${project.progressPercent}%;"></div>
	    </div>
	    <div class="progress-text">
	        <span class="percent-num"><c:out value="${project.progressPercent}"/>%</span>
	        <span class="task-count">(<c:out value="${project.doneTasks}"/> / <c:out value="${project.totalTasks}"/> 完了)</span>
	    </div>
	</td>
	
	<!-- 4列目：タスク管理 -->
	<td><a href="${pageContext.request.contextPath}/TaskBoardServlet?project_id=${project.projectId}" class="task-link">タスク一覧</a></td>
	
	<!-- 5列目：操作 -->
	<td>
	    <a href="${pageContext.request.contextPath}/ProjectEditServlet?project_id=${project.projectId}" class="edit-link">編集</a> / 
	    <a href="#" class="delete-link" data-id="${project.projectId}" data-name="<c:out value='${project.projectName}'/>" onclick="openDeletePopup(this.dataset.id, this.dataset.name); return false;">削除</a>
	</td>
	</tr>
	</c:forEach>
	
	</c:when>
	<c:otherwise>
	<tr>
	<td colspan="5" style="text-align: center;">プロジェクトが登録されていません。</td>
	</tr>
	</c:otherwise>
	</c:choose>
	</tbody>
	</table>
	</main>
	
	<!-- プロジェクト削除確認ポップアップ -->
	<div id="delete-popup" class="popup-overlay" style="display: none;">
	<div class="popup-card">
	<h2>プロジェクト削除確認</h2>
	<p class="popup-message" id="delete-message">プロジェクトを削除しますか?</p>
	<form action="${pageContext.request.contextPath}/ProjectDeleteServlet" method="post">
	<input type="hidden" name="project_id" id="delete-project-id" value="">
	<div class="popup-action-buttons">
	    <!-- ★修正：削除確認のボタン構造・文字も「はい」「いいえ」の共通CSSクラスに統一 -->
	    <button type="submit" class="popup-yes-button">はい</button>
	    <button type="button" class="popup-cancel-button" onclick="closeDeletePopup()">いいえ</button>
	</div>
	</form>
	</div>
	</div>

	<!-- 削除成功完了ポップアップ -->
	<c:if test="${not empty deleteSuccess}">
	<%-- ★修正：表示したらフラグを消す（消さないと毎回ポップアップが出る） --%>
	<c:remove var="deleteSuccess" scope="session"/>
	<div id="success-popup" class="popup-overlay success-active">
	<div class="popup-card">
	<h2>完了</h2>
	<p class="popup-message">削除しました</p>
	<div class="popup-action-buttons">
	<a href="${pageContext.request.contextPath}/ProjectListServlet" class="create-button" style="text-decoration: none;">OK</a>
	</div>
	</div>
	</div>
	</c:if>

	<!-- ポップアップ-->
<div id="logout-popup" class="custom-popup" style="display: none;">
    <h2>ログアウト確認</h2>
    <p>本当にログアウトしますか？</p>
    <form action="${pageContext.request.contextPath}/LogoutController" method="post">
        <div class="popup-buttons">
            <button type="submit" class="popup-btn-submit">ログアウト</button>
            <button type="button" class="popup-btn-cancel" onclick="document.getElementById('logout-popup').style.display='none'">戻る</button>
        </div>
    </form>
</div>

	<!-- スクリプト制御部 -->
	<script>
	function openDeletePopup(projectId, projectName) {
	document.getElementById('delete-project-id').value = projectId;
	document.getElementById('delete-message').innerText = projectName + ' を削除しますか？';
	document.getElementById('delete-popup').style.display = 'flex';
	}

	function closeDeletePopup() {
	document.getElementById('delete-popup').style.display = 'none';
	}

	function openLogoutPopup() {
	    document.getElementById('logout-popup').style.display = 'flex';
	}

	function closeLogoutPopup() {
	    document.getElementById('logout-popup').style.display = 'none';
	}
	</script>
</body>
</html>
