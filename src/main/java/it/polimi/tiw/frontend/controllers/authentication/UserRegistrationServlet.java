package it.polimi.tiw.frontend.controllers.authentication;

import it.polimi.tiw.backend.beans.User;
import it.polimi.tiw.backend.dao.UserDAO;
import it.polimi.tiw.backend.exceptions.InvalidArgumentException;
import it.polimi.tiw.backend.exceptions.RegistrationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static it.polimi.tiw.backend.utilities.DatabaseConnectionBuilder.closeConnection;
import static it.polimi.tiw.backend.utilities.DatabaseConnectionBuilder.getConnectionFromServlet;
import static it.polimi.tiw.backend.utilities.PasswordHasher.hashPassword;
import static it.polimi.tiw.backend.utilities.TemplateEngineBuilder.getTemplateEngineFromServlet;

@WebServlet(name = "UserRegistration", value = "/UserRegistration")
public class UserRegistrationServlet extends HttpServlet {
    private Connection servletConnection;
    private TemplateEngine templateEngine;

    /**
     * Default constructor, called by the servlet container.
     */
    @SuppressWarnings("unused")
    public UserRegistrationServlet() {
        super();
    }

    /**
     * This method is called by the servlet container when the servlet is initialized.
     * It initializes the servletConnection and the templateEngine objects.
     */
    public void init() {
        servletConnection = getConnectionFromServlet(this);
        templateEngine = getTemplateEngineFromServlet(this);
    }

    /**
     * This method is called by the servlet container when the servlet is destroyed.
     * It closes the connection to the database.
     */
    public void destroy() {
        closeConnection(servletConnection);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        final JakartaServletWebApplication myApplication = JakartaServletWebApplication.buildApplication(getServletContext());
        final IWebExchange exchange = myApplication.buildExchange(request, response);
        final WebContext context = new WebContext(exchange, exchange.getLocale());
        templateEngine.process("UserRegistration", context, response.getWriter());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get and parse the parameters from the request
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String passwordRepeat = request.getParameter("confirmPassword");
        String email = request.getParameter("email");

        final JakartaServletWebApplication myApplication = JakartaServletWebApplication.buildApplication(getServletContext());
        final IWebExchange exchange = myApplication.buildExchange(request, response);
        final WebContext context = new WebContext(exchange, exchange.getLocale());

        // Check if the password matches the repeated password
        if (!password.equals(passwordRepeat)) {
            context.setVariable("message", "Passwords do not match!");
            templateEngine.process("UserRegistration", context, response.getWriter());
            return;
        }

        try {
            User newUser = new User(username, hashPassword(password), email);
            UserDAO userDAO = new UserDAO(servletConnection);
            userDAO.registerUser(newUser);
        } catch (InvalidArgumentException | RegistrationException | SQLException e) {
            context.setVariable("message", e.getMessage());
            templateEngine.process("UserRegistration", context, response.getWriter());
            return;
        }

        context.setVariable("message", "User successfully registered!");
        templateEngine.process("UserRegistration", context, response.getWriter());
    }

}
