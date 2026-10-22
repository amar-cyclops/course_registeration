package com.university.registration.servlet;

import com.university.registration.dao.UserDAO;
import com.university.registration.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("signup.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String studentId = request.getParameter("studentId");

        String error = validate(name, email, password, confirmPassword, studentId);

        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("studentId", studentId);
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }

        userDAO.createStudent(name, email, password, studentId);

        User user = userDAO.authenticate(email, password);
        HttpSession session = request.getSession(true);
        session.setAttribute("user", user);

        response.sendRedirect(request.getContextPath() + "/dashboard");
    }

    private String validate(String name, String email, String password, String confirmPassword, String studentId) {
        if (isBlank(name) || isBlank(email) || isBlank(password) || isBlank(studentId)) {
            return "All fields are required.";
        }
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match.";
        }
        if (password.length() < 6) {
            return "Password must be at least 6 characters.";
        }
        if (userDAO.emailExists(email)) {
            return "An account with this email already exists.";
        }
        if (userDAO.studentIdExists(studentId)) {
            return "An account with this student ID already exists.";
        }
        return null;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
