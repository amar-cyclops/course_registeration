<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard - Course Registration</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="/WEB-INF/navbar.jsp"/>

<div class="container">
    <h1>Welcome, ${sessionScope.user.name}</h1>

    <c:if test="${not empty param.error}">
        <div class="alert alert-error">${param.error}</div>
    </c:if>

    <c:choose>
        <c:when test="${sessionScope.user.role == 'ADMIN'}">
            <div class="stats">
                <div class="stat-box">
                    <div class="value">${totalCourses}</div>
                    <div class="label">Total Courses</div>
                </div>
            </div>

            <div class="quick-links">
                <a class="card-link" href="${pageContext.request.contextPath}/admin/courses">
                    Manage Courses
                    <span class="desc">Add, edit, or remove courses</span>
                </a>
                <a class="card-link" href="${pageContext.request.contextPath}/profile">
                    My Profile
                    <span class="desc">Update your account details</span>
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="stats">
                <div class="stat-box">
                    <div class="value">${registeredCount}</div>
                    <div class="label">Courses Registered</div>
                </div>
                <div class="stat-box">
                    <div class="value">${availableCount}</div>
                    <div class="label">Courses Available to Register</div>
                </div>
            </div>

            <div class="quick-links">
                <a class="card-link" href="${pageContext.request.contextPath}/register-courses">
                    Register for New Courses
                    <span class="desc">Browse and sign up for open courses</span>
                </a>
                <a class="card-link" href="${pageContext.request.contextPath}/my-courses">
                    My Courses
                    <span class="desc">View courses you're registered for</span>
                </a>
                <a class="card-link" href="${pageContext.request.contextPath}/profile">
                    My Profile
                    <span class="desc">Update your account details</span>
                </a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

</body>
</html>
