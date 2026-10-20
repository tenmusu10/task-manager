<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>${not empty project ? "プロジェクト編集" : "新規プロジェクト作成"}</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>


<body>
<header class="main-header">
<div class="user-info">
ログイン中のユーザー：
<strong><c:out value="${not empty sessionScope.user_name ? sessionScope.user_name : (not empty sessionScope.loginid ? sessionScope.loginid : 'ゲスト')}"/></strong><span class="current-datetime"><c:out value="${currentDateTime}"/></span></div>

<!-- ボタン（1行で完結するタイプ） -->
<button type="button" class="logout-button" onclick="openLogoutPopup()">
    ログアウト
</button>

<!-- ★修正：ログアウト確認ポップアップを task_board.jsp と同じ形に統一 -->
<div id="logout-popup" class="popup-overlay" style="display: none;">
    <div class="popup-card">
        <h2>ログアウト確認</h2>
        <p class="popup-message">本当にログアウトしますか？</p>
        <form action="${pageContext.request.contextPath}/LogoutController" method="post">
            <div class="popup-action-buttons">
                <button type="submit" class="popup-yes-button">はい</button>
                <button type="button" class="popup-cancel-button" onclick="closeLogoutPopup()">いいえ</button>
            </div>
        </form>
    </div>
</div>
</header>




    <main class="list-container">
	<a href="${pageContext.request.contextPath}/ProjectListServlet" class="back-link">← プロジェクト一覧に戻る</a>
        <h1>${not empty project ? "プロジェクト編集" : "新規プロジェクト作成"}</h1>

        <%--新規作成ならCreateへ、編集ならUpdateへデータを送る --%>
        <form action="${pageContext.request.contextPath}/${not empty project ? 'ProjectUpdateServlet' : 'ProjectCreateServlet'}" method="post">
            
            <%-- 編集モードの時だけプロジェクトIDを持たせる --%>
            <c:if test="${not empty project}">
                <input type="hidden" name="project_id" value="${project.projectId}">
            </c:if>

            <table class="project-table">
                <tr>
                    <th>プロジェクト名</th>
                    <td><input type="text" name="project_name" value="<c:out value='${project.projectName}'/>" required></td>
                </tr>
                <tr>
                    <th>締め切り</th>
                    <td><input type="date" name="deadline" value="${project.deadline}"></td>
                </tr>
            </table>
            <br>
            <div style="text-align: center;">
                <button type="submit" class="create-button">${not empty project ? "更新" : "作成"}</button>
            </div>
        </form>
    </main>
 
<script>
// ログアウトポップアップ用（task_board.jsp と同じ）
function openLogoutPopup() {
    document.getElementById('logout-popup').style.display = 'flex';
}

function closeLogoutPopup() {
    document.getElementById('logout-popup').style.display = 'none';
}
</script>
</body>
</html>