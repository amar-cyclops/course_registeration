package com.university.registration.servlet;

import com.university.registration.dao.CourseDAO;
import com.university.registration.model.Course;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/courses")
public class CourseServlet extends HttpServlet {

    private final CourseDAO courseDAO = new CourseDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        if ("edit".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            request.setAttribute("course", courseDAO.getById(id));
            request.getRequestDispatcher("admin/edit-course.jsp").forward(request, response);
        } else if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            courseDAO.deleteCourse(id);
            response.sendRedirect(request.getContextPath() + "/admin/courses");
        } else {
            listCourses(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        String courseCode = request.getParameter("courseCode");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        int creditHours = parseIntOrZero(request.getParameter("creditHours"));
        String instructor = request.getParameter("instructor");
        int totalSeats = parseIntOrZero(request.getParameter("totalSeats"));
        String prerequisites = request.getParameter("prerequisites");
        String status = request.getParameter("status");

        if (prerequisites != null) {
            prerequisites = prerequisites.trim();
            if (prerequisites.isEmpty()) {
                prerequisites = null;
            }
        }

        if ("update".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Course existing = courseDAO.getById(id);

            // Preserve how many seats are currently taken when total seats changes
            int seatsTaken = existing.getTotalSeats() - existing.getAvailableSeats();
            int newAvailableSeats = Math.max(totalSeats - seatsTaken, 0);

            Course course = new Course(id, courseCode, title, description, creditHours,
                    instructor, totalSeats, newAvailableSeats, prerequisites, status);
            courseDAO.updateCourse(course);
        } else {
            Course course = new Course(0, courseCode, title, description, creditHours,
                    instructor, totalSeats, totalSeats, prerequisites, status);
            courseDAO.addCourse(course);
        }

        response.sendRedirect(request.getContextPath() + "/admin/courses");
    }

    private void listCourses(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("courses", courseDAO.getAllCourses());
        RequestDispatcher dispatcher = request.getRequestDispatcher("admin/courses.jsp");
        dispatcher.forward(request, response);
    }

    private int parseIntOrZero(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }
}
