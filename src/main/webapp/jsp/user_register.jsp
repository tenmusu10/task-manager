<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>user_register.jsp</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<a href="${pageContext.request.contextPath}/jsp/login.jsp">戻る</a>

<center><h1>新規登録</h1></center><br/>

<c:if test="${not empty errorMsg}">
    <center>
        <p style="color: red;"><c:out value="${errorMsg}"/></p>
    </center>
</c:if>

<form action="${pageContext.request.contextPath}/UserRegisterServlet" method="post">
<center>
<table border="1">
<tr>
    <td>ユーザー名</td>
    <td><input type="text" size="50" name="user_name" required></td>
</tr>
<tr>
    <td>ID</td>
    <td><input type="text" size="50" name="login_id" required></td>
</tr>
<tr>
    <td>PASS</td>
    <td><input type="password" size="50" name="password" required></td>
</tr>
</table>

    <input type="submit" value="新規登録"/>

</center>
</form><br/>

</body>
</html>