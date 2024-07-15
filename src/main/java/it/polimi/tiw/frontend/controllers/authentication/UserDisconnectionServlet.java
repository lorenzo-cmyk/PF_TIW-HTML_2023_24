package it.polimi.tiw.frontend.controllers.authentication;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * This servlet is used to handle the disconnection of a user.
 */
@WebServlet(name = "UserDisconnectionServlet", value = "/logout")
public class UserDisconnectionServlet extends HttpServlet {
    /**
     * Default constructor, called by the servlet container.
     */
    @SuppressWarnings("unused")
    public UserDisconnectionServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Invalidate the session and redirect the user to the login page
        request.getSession().invalidate();
        response.sendRedirect("login");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}
