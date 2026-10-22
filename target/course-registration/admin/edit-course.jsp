<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Course - Course Registration</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="/WEB-INF/navbar.jsp"/>

<div class="container">
    <h1>Edit Course</h1>

    <div class="card">
        <form action="${pageContext.request.contextPath}/admin/courses" method="post">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="id" value="${course.id}">

            <div class="form-row">
                <label>Course Code
                    <input type="text" name="courseCode" value="${course.courseCode}" required>
                </label>
                <label>Credit Hours
                    <input type="number" name="creditHours" value="${course.creditHours}" min="1" max="6" required>
                </label>
                <label>Total Seats
                    <input type="number" name="totalSeats" value="${course.totalSeats}" min="0" required>
                </label>
            </div>
            <label>Title
                <input type="text" name="title" value="${course.title}" required>
            </label>
            <label>Description
                <textarea name="description" rows="2">${course.description}</textarea>
            </label>
            <div class="form-row">
                <label>Instructor
                    <input type="text" name="instructor" value="${course.instructor}">
                </label>
                <label>Prerequisites <span style="font-weight:400;">(comma-separated course codes)</span>
                    <input type="text" name="prerequisites" value="${course.prerequisites}">
                </label>
                <label>Status
                    <select name="status">
                        <option value="Active" ${course.status == 'Active' ? 'selected' : ''}>Active</option>
                        <option value="Inactive" ${course.status == 'Inactive' ? 'selected' : ''}>Inactive</option>
                    </select>
                </label>
            </div>

            <div style="display:flex; gap:12px;">
                <button type="submit">Save Changes</button>
                <a href="${pageContext.request.contextPath}/admin/courses" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>

</body>
</html>
