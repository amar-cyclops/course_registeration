<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Log In - Course Registration</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="auth-container">
    <h1>Course Registration System</h1>

    <div class="card">
        <h2>Log In</h2>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="alert alert-error">${param.error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <label>Email
                <input type="email" name="email" required autofocus>
            </label>
            <label>Password
                <input type="password" name="password" required>
            </label>
            <button type="submit">Log In</button>
        </form>

        <div class="auth-link">
            Don't have an account? <a href="${pageContext.request.contextPath}/signup">Sign up</a>
        </div>

        <div class="auth-link" style="margin-top:10px; color:#7b8794; font-size:12px;">
            Admin demo login: admin@university.edu / admin123
        </div>
    </div>
</div>

</body>
</html>
