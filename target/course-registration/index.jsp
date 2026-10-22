<%
    if (session.getAttribute("user") != null) {
        response.sendRedirect("dashboard");
    } else {
        response.sendRedirect("login");
    }
%>
