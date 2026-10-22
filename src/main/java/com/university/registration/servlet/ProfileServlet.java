package com.university.registration.servlet;

import com.university.registration.dao.UserDAO;
import com.university.registration.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User sessionUser = (User) request.getSession().getAttribute("user");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String newPassword = request.getParameter("newPassword");

        userDAO.updateProfile(sessionUser.getId(), name, email, newPassword);

        // Refresh the session copy so the navbar etc. reflect the change immediately
        User updated = userDAO.getById(sessionUser.getId());
        request.getSession().setAttribute("user", updated);

        request.setAttribute("success", "Profile updated successfully.");
        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }
}
