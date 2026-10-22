<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sign Up - Course Registration</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="auth-container">
    <h1>Create Student Account</h1>

    <div class="card">
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/signup" method="post">
            <label>Full Name
                <input type="text" name="name" value="${name}" required autofocus>
            </label>
            <label>Email
                <input type="email" name="email" value="${email}" required>
            </label>
            <label>Student ID
                <input type="text" name="studentId" value="${studentId}" required>
            </label>
            <label>Password
                <input type="password" name="password" required minlength="6">
            </label>
            <label>Confirm Password
                <input type="password" name="confirmPassword" required minlength="6">
            </label>
            <button type="submit">Sign Up</button>
        </form>

        <div class="auth-link">
            Already have an account? <a href="${pageContext.request.contextPath}/login">Log in</a>
        </div>
    </div>
</div>

</body>
</html>
