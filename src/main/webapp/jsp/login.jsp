<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>ログイン｜進捗管理表</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>

<body>
	<main class="login-container">
	<section class="login-card">
	<h1>進捗管理表</h1>
	<p class="subtitle">ログインしてプロジェクトを管理しましょう</p>
    
	<c:if test="${not empty errorMessage}">
	<p class="error-message"><c:out value="${errorMessage}"/></p>
	</c:if>
	
		<form action="${pageContext.request.contextPath}/LoginServlet" method="post" autocomplete="off">
		<div class="form-group">
		<label for="login_id">ID</label>
		<input type="text" id="login_id" name="login_id" placeholder="IDを入力してください" autocomplete="one-time-code" required>
		</div>
		<div class="form-group">
		<label for="password">パスワード</label>
		<input type="password" id="password" name="password" placeholder="パスワードを入力してください" autocomplete="new-password" required>
		</div>
		<button type="submit" class="login-button">ログイン</button>
		</form>
		
		<div class="member-link">
		<a href="${pageContext.request.contextPath}/LoginToRegisterController">新しくメンバーを追加する</a>
		</div>
		
</section>
</main>
</body>
</html>