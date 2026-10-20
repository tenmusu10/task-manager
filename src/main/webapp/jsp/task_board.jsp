<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>タスク一覧｜進捗管理表</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<script src="${pageContext.request.contextPath}/js/clock.js" defer></script>
</head>

<body>
	<header class="main-header">
    <div class="user-info">
        ログイン中のユーザー：<strong><c:out value="${not empty sessionScope.user_name ? sessionScope.user_name : (not empty sessionScope.loginid ? sessionScope.loginid : 'ゲスト')}"/></strong><span id="current-datetime" class="current-datetime"></span>
    </div>
    <button type="button" class="logout-button" onclick="openLogoutPopup()">ログアウト</button>
	</header>
	
	<main class="list-container">
    <div style="margin-bottom: 15px;">
        <a href="${pageContext.request.contextPath}/ProjectListServlet" class="back-link">← プロジェクト一覧に戻る</a>
    </div>

    <h1>
        <c:choose>
            <c:when test="${not empty project}"><c:out value="${project.projectName}"/></c:when>
            <c:otherwise>プロジェクト（ID: <c:out value="${projectId}"/>）</c:otherwise>
        </c:choose>
        - タスク一覧
    </h1>
    
    <div class="action-area">
        <a href="${pageContext.request.contextPath}/TaskCreateServlet?project_id=${projectId}" class="create-button">新規タスク作成</a>
    </div>
	
    <table class="project-table">
    <thead>
    <tr>
    <th>タスク名</th>
    <th>期限</th>
    <th>担当者</th>
    <th>進捗</th>
    <th>操作</th>
    </tr>
    </thead>
    <tbody>
        <c:choose>
            <c:when test="${not empty taskList}">
                <c:forEach var="task" items="${taskList}">
                    <tr>
                        <td class="project-name"><c:out value="${task.taskName}"/></td>
                        <td><c:out value="${task.deadline}"/></td>
                        <td><c:out value="${task.assigneeName}"/></td>
                        <td><c:out value="${task.status}"/></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/TaskUpdateServlet?task_id=${task.taskId}&project_id=${projectId}" class="edit-link">編集</a> / 
                            <a href="#" class="delete-link" data-id="${task.taskId}" data-name="<c:out value='${task.taskName}'/>" onclick="openDeletePopup(this.dataset.id, this.dataset.name); return false;">削除</a>
                        </td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="5" style="text-align: center;">登録されているタスクはありません。</td>
                </tr>
            </c:otherwise>
        </c:choose>
    </tbody>
    </table>
	</main>
	
	<!-- タスク削除確認ポップアップ -->
	<div id="delete-popup" class="popup-overlay" style="display: none;">
        <div class="popup-card">
            <h2>タスク削除確認</h2>
            <p class="popup-message" id="delete-message">タスクを削除しますか？</p>
            <form action="${pageContext.request.contextPath}/TaskDeleteServlet" method="post">
                <input type="hidden" name="task_id" id="delete-task-id" value="">
                <input type="hidden" name="project_id" value="${projectId}">
                <div class="popup-action-buttons">
                    <!-- ★修正：削除確認も「はい（赤い太字）」「いいえ（黒字）」の3分割スタイルに統一 -->
                    <button type="submit" class="popup-yes-button">はい</button>
                    <button type="button" class="popup-cancel-button" onclick="closeDeletePopup()">いいえ</button>
                </div>
            </form>
        </div>
	</div>

	<!-- 削除完了ポップアップ -->
	<c:if test="${not empty deleteTaskSuccess}">
	    <%-- ★修正：表示したらフラグを消す（消さないと毎回ポップアップが出る） --%>
	    <c:remove var="deleteTaskSuccess" scope="session"/>
        <div id="success-popup" class="popup-overlay success-active">
            <div class="popup-card">
                <h2>完了</h2>
                <p class="popup-message" style="border-bottom: none; margin-bottom: 0; padding-bottom: 0;">タスクの削除を完了しました</p>
                <div class="popup-action-buttons">
                    <a href="${pageContext.request.contextPath}/TaskBoardServlet?project_id=${projectId}" class="create-button" style="text-decoration: none; margin: 15px 0;">OK</a>
                </div>
            </div>
        </div>
	</c:if>
	
	<!-- ログアウト確認ポップアップ -->
	<div id="logout-popup" class="popup-overlay" style="display: none;">
	    <div class="popup-card">
	        <h2>ログアウト確認</h2>
	        <p class="popup-message">本当にログアウトしますか？</p>
	        <form action="${pageContext.request.contextPath}/LogoutController" method="post">
	            <div class="popup-action-buttons">
	                <!-- ★修正：古いインライン記述を廃止し、新しい共通CSSクラスへ完全統一 -->
	                <button type="submit" class="popup-yes-button">はい</button>
	                <button type="button" class="popup-cancel-button" onclick="closeLogoutPopup()">いいえ</button>
	            </div>
	        </form>
	    </div>
	</div>

	<!-- ポップアップ制御用JavaScript -->
	<script>
	// 削除ポップアップ用
	function openDeletePopup(taskId, taskName) {
	    document.getElementById('delete-task-id').value = taskId;
	    document.getElementById('delete-message').innerText = taskName + ' を削除しますか？';
	    document.getElementById('delete-popup').style.display = 'flex'; /* flexにすることでCSSの中央揃えを稼働 */
	}

	function closeDeletePopup() {
	    document.getElementById('delete-popup').style.display = 'none';
	}

	// ログアウトポップアップ用
	function openLogoutPopup() {
	    document.getElementById('logout-popup').style.display = 'flex'; /* flexにすることでCSSの中央揃えを稼働 */
	}

	function closeLogoutPopup() {
	    document.getElementById('logout-popup').style.display = 'none';
	}
	</script>
</body>
</html>
