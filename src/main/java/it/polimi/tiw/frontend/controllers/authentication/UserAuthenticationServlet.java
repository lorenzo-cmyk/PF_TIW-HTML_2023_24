package it.polimi.tiw.frontend.controllers.authentication;

import it.polimi.tiw.backend.beans.User;
import it.polimi.tiw.backend.beans.exceptions.InvalidArgumentException;
import it.polimi.tiw.backend.dao.UserDAO;
import it.polimi.tiw.backend.dao.exceptions.LoginException;
import it.polimi.tiw.backend.utilities.Validators;
import it.polimi.tiw.backend.utilities.exceptions.FailedInputParsingException;
import it.polimi.tiw.backend.utilities.exceptions.UnknownErrorCodeException;
import it.polimi.tiw.frontend.filters.MessageTypesEnumeration;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static it.polimi.tiw.backend.utilities.DatabaseConnectionBuilder.closeConnection;
import static it.polimi.tiw.backend.utilities.DatabaseConnectionBuilder.getConnectionFromServlet;
import static it.polimi.tiw.backend.utilities.ThymeleafObjectsBuilder.getTemplateEngineFromServlet;
import static it.polimi.tiw.backend.utilities.ThymeleafObjectsBuilder.getWebContextFromServlet;

/**
 * This servlet is used to handle the authentication of a user.
 */
@WebServlet(name = "UserAuthenticationServlet", value = "/login")
public class UserAuthenticationServlet extends HttpServlet {
    private Connection servletConnection;
    private TemplateEngine templateEngine;

    /**
     * Default constructor, called by the servlet container.
     */
    @SuppressWarnings("unused")
    public UserAuthenticationServlet() {
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // First, we get the errorCode parameter from the request (0 if it is not present)
            int errorCode = Validators.parseInt(request.getParameter("errorCode") == null ?
                    "0" : request.getParameter("errorCode"));

            // Then, we create a new WebContext object and we process the template
            WebContext context = getWebContextFromServlet(this, request, response);

            if (errorCode == 0) {
                context.setVariable("message", "Please enter your credentials to log in.");
                context.setVariable("messageContext", MessageTypesEnumeration.DEFAULT.getValue());
            } else {
                context.setVariable("message", Validators.retrieveErrorMessageFromErrorCode(errorCode));
                context.setVariable("messageContext", MessageTypesEnumeration.ERROR.getValue());
            }

            templateEngine.process("UserAuthenticationTemplate", context, response.getWriter());
        } catch (FailedInputParsingException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Malformed request. " +
                    "Are you trying to hijack the request?");
        } catch (UnknownErrorCodeException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown error code provided. " +
                    "Are you trying to hijack the request?");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // First, we get the username and password parameters from the request
            String username = Validators.parseString(request.getParameter("username"));
            String password = Validators.parseString(request.getParameter("password"));

            // Then, we use the credentials to create a new User object
            User user = new User(username, password);

            // Now, we check if the user is registered in the database
            UserDAO userDAO = new UserDAO(servletConnection);
            User authenticatedUser = userDAO.authenticateUser(user);

            // If the user is registered, we put the user object in the session, and we redirect to the home page
            request.getSession().setAttribute("user", authenticatedUser);
            // Redirect the user to the homepage
            response.sendRedirect(getServletContext().getContextPath() +
                    "/home");
        } catch (FailedInputParsingException | InvalidArgumentException | LoginException e) {
            // Now we redirect the user to the registration page with the errorCode
            response.sendRedirect(getServletContext().getContextPath() +
                    "/login?errorCode=" + e.getErrorCode());
        } catch (SQLException e) {
            // If a SQLException is thrown, we send an error directly to the client
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to authenticate the user due to a critical error in the database.");
        }
    }
}
