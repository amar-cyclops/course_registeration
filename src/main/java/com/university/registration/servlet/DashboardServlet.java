package com.university.registration.servlet;

import com.university.registration.dao.CourseDAO;
import com.university.registration.dao.RegistrationDAO;
import com.university.registration.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final CourseDAO courseDAO = new CourseDAO();
    private final RegistrationDAO registrationDAO = new RegistrationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");

        if (user.isAdmin()) {
            request.setAttribute("totalCourses", courseDAO.getAllCourses().size());
        } else {
            request.setAttribute("registeredCount", registrationDAO.getRegisteredCoursesForStudent(user.getId()).size());
            request.setAttribute("availableCount", courseDAO.getAvailableCoursesForStudent(user.getId()).size());
        }

        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }
}
