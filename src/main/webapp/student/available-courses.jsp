<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register for Courses - Course Registration</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="/WEB-INF/navbar.jsp"/>

<div class="container">
    <h1>Register for New Courses</h1>

    <c:if test="${not empty results}">
        <div class="card">
            <h2>Registration Results</h2>

            <c:if test="${allFailed}">
                <div class="alert alert-error">None of the selected courses could be registered. See the reasons below.</div>
            </c:if>
            <c:if test="${anySucceeded and not allFailed}">
                <div class="alert alert-success">Some courses were registered. See the details below for any that failed.</div>
            </c:if>
            <table>
                <thead>
                    <tr>
                        <th>Course</th>
                        <th>Status</th>
                        <th>Details</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="r" items="${results}">
                        <tr>
                            <td>${r.courseCode} — ${r.courseTitle}</td>
                            <td>
                                <span class="badge ${r.success ? 'badge-success' : 'badge-fail'}">
                                    ${r.success ? 'Registered' : 'Failed'}
                                </span>
                            </td>
                            <td>${r.message}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>

    <c:if test="${not empty submitError}">
        <div class="alert alert-error">${submitError}</div>
    </c:if>

    <div class="card">
        <h2>Available Courses</h2>

        <c:choose>
            <c:when test="${empty courses}">
                <div class="alert alert-error">
                    No courses are currently available for you to register for. This may be because
                    you're already registered for everything currently open, or because open courses
                    are full. Check back later, or contact your administrator if you believe this is an error.
                </div>
            </c:when>
            <c:otherwise>
                <form action="${pageContext.request.contextPath}/register-courses" method="post">
                    <table>
                        <thead>
                            <tr>
                                <th></th>
                                <th>Code</th>
                                <th>Title</th>
                                <th>Credits</th>
                                <th>Instructor</th>
                                <th>Seats Left</th>
                                <th>Prerequisites</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="course" items="${courses}">
                                <tr>
                                    <td><input type="checkbox" name="courseIds" value="${course.id}"></td>
                                    <td>${course.courseCode}</td>
                                    <td>${course.title}<br>
                                        <span style="color:#7b8794; font-size:12px;">${course.description}</span>
                                    </td>
                                    <td>${course.creditHours}</td>
                                    <td>${course.instructor}</td>
                                    <td>${course.availableSeats}</td>
                                    <td>${empty course.prerequisites ? '—' : course.prerequisites}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <button type="submit" style="margin-top:16px; width:fit-content;">Submit</button>
                </form>
            </c:otherwise>
        </c:choose>
    </div>
</div>

</body>
</html>
