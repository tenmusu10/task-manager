<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${not empty task ? "タスク編集" : "新規タスク作成"}</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>

<body>
    <!-- ヘッダーエリア -->
    <header class="main-header">
        <div class="user-info">
            ログイン中のユーザー：
            <strong>
                <c:out value="${not empty sessionScope.user_name ? sessionScope.user_name : (not empty sessionScope.loginid ? sessionScope.loginid : 'ゲスト')}"/>
            </strong>
        </div>
        <button type="button" class="logout-button" onclick="openLogoutPopup()">ログアウト</button>
    </header>

    <!-- メインコンテンツコンテナ -->
    <main class="list-container">
        
        <!-- 戻るリンクエリア -->
        <div class="navigation-area">
            <a href="${pageContext.request.contextPath}/ProjectListServlet" class="back-link">← タスク一覧に戻る</a>
        </div>
        
        <!-- ページタイトル -->
        <h1>${not empty task ? "タスク編集" : "新規タスク作成"}｜進捗管理表</h1>
        
        <!-- フォームカードコンテナ -->
        <div class="form-card">
            <form action="${pageContext.request.contextPath}/${not empty task ? 'TaskUpdateServlet' : 'TaskCreateServlet'}" method="get/post" accept-charset="UTF-8">

                <%-- 隠しフィールド --%>
                <input type="hidden" name="project_id" value="${projectId}">
                <c:if test="${not empty task}">
                    <input type="hidden" name="task_id" value="${task.taskId}">
                </c:if>

                <!-- 入力テーブル -->
                <table class="task-table">
                    <tbody>
                        <tr>
                            <td>プロジェクト名</td>
                            <td>
                                <span class="project-name-display">
                                    <c:choose>
                                        <c:when test="${not empty project}">${project.projectName}</c:when>
                                        <c:otherwise>プロジェクトID: ${projectId}</c:otherwise>
                                    </c:choose>
                                </span>
                            </td>
                        </tr>

                        <tr>
                            <td>タスク名</td>
                            <td><input type="text" name="task_name" value="${task.taskName}" required placeholder="タスク名を入力してください"></td>
                        </tr>

                        <tr>
                            <td>締め切り</td>
                            <td><input type="date" name="deadline" value="${task.deadline}"></td>
                        </tr>

                        <tr>
                            <td>進捗</td>
                            <td class="radio-group">
                                <label><input type="radio" name="status" value="未着手" ${empty task or task.status == '未着手' ? 'checked' : ''}> 未着手</label>
                                <label><input type="radio" name="status" value="進行中" ${not empty task and task.status == '進行中' ? 'checked' : ''}> 進行中</label>
                                <label><input type="radio" name="status" value="完了" ${not empty task and task.status == '完了' ? 'checked' : ''}> 完了</label>
                            </td>
                        </tr>

                        <tr>
                            <td>担当者</td>
                            <td>
                                <select name="assignee_id" required>
                                    <option value="">-- 担当者を選択してください --</option>
                                    <c:forEach var="user" items="${userList}">
                                        <option value="${user.userId}" ${not empty task and task.assigneeId == user.userId ? 'selected' : ''}>
                                            ${user.userName}
                                        </option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                    </tbody>
                </table>

                <!-- 送信ボタンエリア -->
                <div class="form-action">
                    <input type="submit" class="submit-button" value="${not empty task ? 'タスク更新' : 'タスク作成'}"/>
                </div>
            </form>
        </div>
    </main>

    <!-- ログアウト確認ポップアップ（スタイルをCSSで綺麗に制御するためのクラスを追加） -->
    <div id="logout-popup" class="custom-popup" style="display: none;">
        <h2>ログアウト確認</h2>
        <p>本当にログアウトしますか？</p>
        <form action="${pageContext.request.contextPath}/jsp.logout.jsp" method="post">
            <div class="popup-buttons">
                <button type="submit" class="popup-btn-submit">ログアウト</button>
                <button type="button" class="popup-btn-cancel" onclick="document.getElementById('logout-popup').style.display='none'">戻る</button>
            </div>
        </form>
    </div>

    <!-- JavaScript関数の定義（未定義エラー防止） -->
    <script>
        function openLogoutPopup() {
            document.getElementById('logout-popup').style.display = 'block';
        }
    </script>
</body>
</html>
