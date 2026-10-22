<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Courses - Course Registration</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="/WEB-INF/navbar.jsp"/>

<div class="container">
    <h1>My Courses</h1>

    <div class="card">
        <c:choose>
            <c:when test="${empty registrations}">
                <p class="empty">You aren't registered for any courses yet.</p>
                <a href="${pageContext.request.contextPath}/register-courses" class="btn" style="width: fit-content;">
                    Register for Courses
                </a>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                        <tr>
                            <th>Code</th>
                            <th>Title</th>
                            <th>Credits</th>
                            <th>Instructor</th>
                            <th>Registered On</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="reg" items="${registrations}">
                            <tr>
                                <td>${reg.courseCode}</td>
                                <td>${reg.courseTitle}</td>
                                <td>${reg.creditHours}</td>
                                <td>${reg.instructor}</td>
                                <td>${reg.registeredAt}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</div>

</body>
</html>
