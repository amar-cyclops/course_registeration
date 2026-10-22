<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manage Courses - Course Registration</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="/WEB-INF/navbar.jsp"/>

<div class="container">
    <h1>Manage Courses</h1>

    <div class="card">
        <h2>Add New Course</h2>
        <form action="${pageContext.request.contextPath}/admin/courses" method="post">
            <div class="form-row">
                <label>Course Code
                    <input type="text" name="courseCode" placeholder="e.g. CS101" required>
                </label>
                <label>Credit Hours
                    <input type="number" name="creditHours" min="1" max="6" required>
                </label>
                <label>Total Seats
                    <input type="number" name="totalSeats" min="0" required>
                </label>
            </div>
            <label>Title
                <input type="text" name="title" required>
            </label>
            <label>Description
                <textarea name="description" rows="2"></textarea>
            </label>
            <div class="form-row">
                <label>Instructor
                    <input type="text" name="instructor">
                </label>
                <label>Prerequisites <span style="font-weight:400;">(comma-separated course codes, optional)</span>
                    <input type="text" name="prerequisites" placeholder="e.g. CS101, MATH150">
                </label>
                <label>Status
                    <select name="status">
                        <option value="Active">Active</option>
                        <option value="Inactive">Inactive</option>
                    </select>
                </label>
            </div>
            <button type="submit">Add Course</button>
        </form>
    </div>

    <div class="card">
        <h2>All Courses</h2>

        <c:choose>
            <c:when test="${empty courses}">
                <p class="empty">No courses yet. Add one above.</p>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                        <tr>
                            <th>Code</th>
                            <th>Title</th>
                            <th>Credits</th>
                            <th>Instructor</th>
                            <th>Seats</th>
                            <th>Prerequisites</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="course" items="${courses}">
                            <tr>
                                <td>${course.courseCode}</td>
                                <td>${course.title}</td>
                                <td>${course.creditHours}</td>
                                <td>${course.instructor}</td>
                                <td>${course.availableSeats} / ${course.totalSeats}</td>
                                <td>${empty course.prerequisites ? '—' : course.prerequisites}</td>
                                <td>
                                    <span class="badge ${course.status == 'Active' ? 'badge-active' : 'badge-inactive'}">
                                        ${course.status}
                                    </span>
                                </td>
                                <td class="actions">
                                    <a href="${pageContext.request.contextPath}/admin/courses?action=edit&id=${course.id}">Edit</a>
                                    <a href="${pageContext.request.contextPath}/admin/courses?action=delete&id=${course.id}"
                                       onclick="return confirm('Delete this course? This cannot be undone.');"
                                       style="color:#e5484d;">Delete</a>
                                </td>
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
