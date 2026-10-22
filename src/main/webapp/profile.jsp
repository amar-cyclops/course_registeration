<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Profile - Course Registration</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="/WEB-INF/navbar.jsp"/>

<div class="container">
    <h1>My Profile</h1>

    <div class="card" style="max-width: 480px;">
        <c:if test="${not empty success}">
            <div class="alert alert-success">${success}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/profile" method="post">
            <label>Full Name
                <input type="text" name="name" value="${sessionScope.user.name}" required>
            </label>
            <label>Email
                <input type="email" name="email" value="${sessionScope.user.email}" required>
            </label>
            <c:if test="${sessionScope.user.role == 'STUDENT'}">
                <label>Student ID
                    <input type="text" value="${sessionScope.user.studentId}" disabled>
                </label>
            </c:if>
            <label>New Password <span style="font-weight:400;">(leave blank to keep current password)</span>
                <input type="password" name="newPassword" minlength="6">
            </label>
            <button type="submit">Save Changes</button>
        </form>
    </div>
</div>

</body>
</html>
