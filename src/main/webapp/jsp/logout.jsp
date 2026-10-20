<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>logout.jsp</title>

	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">

</head>
<body>
<main class="login-container">
	<section class="login-card">
	<h1>ログアウトしました</h1>
	<form action="${pageContext.request.contextPath}/jsp/login.jsp" method="get" autocomplete="off">
		<div class="form-group"></div>
		<button type="submit" class="login-button">ログイン画面へ</button>
	</form>
	</section>
</main>
</body>
</html>