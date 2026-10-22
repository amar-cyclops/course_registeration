package com.university.registration.servlet;

import com.university.registration.dao.CourseDAO;
import com.university.registration.dao.RegistrationDAO;
import com.university.registration.model.Course;
import com.university.registration.model.RegistrationResult;
import com.university.registration.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements user story US-0014 "Register for course":
 * 1. Student sees available courses.
 * 2. Student selects one or more and submits.
 * 3. Each selected course is checked (seats + prerequisites) and registered or rejected.
 * 4. A confirmation report is shown listing per-course success/failure with reasons.
 */
@WebServlet("/register-courses")
public class RegisterCourseServlet extends HttpServlet {

    private final CourseDAO courseDAO = new CourseDAO();
    private final RegistrationDAO registrationDAO = new RegistrationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        if (user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        showAvailableCourses(request, response, user);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        if (user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        String[] selectedIds = request.getParameterValues("courseIds");

        if (selectedIds == null || selectedIds.length == 0) {
            request.setAttribute("submitError", "Please select at least one course before submitting.");
            showAvailableCourses(request, response, user);
            return;
        }

        List<RegistrationResult> results = new ArrayList<>();

        for (String idStr : selectedIds) {
            int courseId = Integer.parseInt(idStr);
            Course course = courseDAO.getById(courseId);

            if (course == null) {
                continue;
            }

            results.add(attemptRegistration(user, course));
        }

        boolean anySucceeded = results.stream().anyMatch(RegistrationResult::isSuccess);
        boolean allFailed = results.stream().noneMatch(RegistrationResult::isSuccess);

        request.setAttribute("results", results);
        request.setAttribute("anySucceeded", anySucceeded);
        request.setAttribute("allFailed", allFailed);

        // Refresh the available list for anything still shown below the results
        request.setAttribute("courses", courseDAO.getAvailableCoursesForStudent(user.getId()));

        request.getRequestDispatcher("student/available-courses.jsp").forward(request, response);
    }

    private RegistrationResult attemptRegistration(User student, Course course) {
        // Prerequisite check
        if (course.getPrerequisites() != null && !course.getPrerequisites().trim().isEmpty()) {
            String[] prereqCodes = course.getPrerequisites().split(",");
            for (String rawCode : prereqCodes) {
                String prereqCode = rawCode.trim();
                if (!prereqCode.isEmpty() &&
                        !registrationDAO.hasActiveRegistrationForCourseCode(student.getId(), prereqCode)) {
                    return new RegistrationResult(course.getCourseCode(), course.getTitle(), false,
                            "Missing prerequisite: " + prereqCode);
                }
            }
        }

        // Seat check + atomic claim
        boolean claimed = courseDAO.tryClaimSeat(course.getId());
        if (!claimed) {
            return new RegistrationResult(course.getCourseCode(), course.getTitle(), false,
                    "No seats available.");
        }

        registrationDAO.insert(student.getId(), course.getId(), "REGISTERED", null);
        return new RegistrationResult(course.getCourseCode(), course.getTitle(), true,
                "Registered successfully.");
    }

    private void showAvailableCourses(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {

        List<Course> courses = courseDAO.getAvailableCoursesForStudent(user.getId());
        request.setAttribute("courses", courses);
        request.getRequestDispatcher("student/available-courses.jsp").forward(request, response);
    }
}
