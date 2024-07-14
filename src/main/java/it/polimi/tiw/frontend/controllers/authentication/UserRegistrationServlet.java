package it.polimi.tiw.frontend.controllers.authentication;

import it.polimi.tiw.backend.beans.User;
import it.polimi.tiw.backend.dao.UserDAO;
import it.polimi.tiw.backend.exceptions.InvalidArgumentException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

import static it.polimi.tiw.backend.utilities.DatabaseConnectionBuilder.closeConnection;
import static it.polimi.tiw.backend.utilities.DatabaseConnectionBuilder.getConnectionFromServlet;
import static it.polimi.tiw.backend.utilities.PasswordHasher.hashPassword;

@WebServlet(name = "UserRegistration", value = "/User-Registration")
public class UserRegistrationServlet extends HttpServlet {
    private Connection servletConnection;

    /**
     * Default constructor, called by the servlet container.
     */
    @SuppressWarnings("unused")
    public UserRegistrationServlet() {
        super();
    }

    public void init() {
        servletConnection = getConnectionFromServlet(this);
    }

    public void destroy() {
        closeConnection(servletConnection);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String path = "/WEB-INF/User-Registration.html";
        // Just send the page for now
        response.setContentType("text/html");
        request.getRequestDispatcher(path).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get and parse the parameters from the request
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");


        System.out.println("Here1");
        // Check if the parameters are valid
        User newUser;
        try {
            newUser = new User(username, hashPassword(password), email);
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Here2");
        // Register the user
        UserDAO userDAO = new UserDAO(servletConnection);
        try {
            userDAO.registerUser(newUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
