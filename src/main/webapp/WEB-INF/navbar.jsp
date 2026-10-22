<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="navbar">
    <div class="brand">Course Registration System</div>
    <div class="links">
        <span class="badge-role">${sessionScope.user.role}</span>
        <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
        <c:if test="${sessionScope.user.role == 'ADMIN'}">
            <a href="${pageContext.request.contextPath}/admin/courses">Manage Courses</a>
        </c:if>
        <c:if test="${sessionScope.user.role == 'STUDENT'}">
            <a href="${pageContext.request.contextPath}/register-courses">Register for Courses</a>
            <a href="${pageContext.request.contextPath}/my-courses">My Courses</a>
        </c:if>
        <a href="${pageContext.request.contextPath}/profile">Profile</a>
        <a href="${pageContext.request.contextPath}/logout">Log Out</a>
    </div>
</div>
