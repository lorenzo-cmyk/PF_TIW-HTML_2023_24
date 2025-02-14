package it.polimi.tiw.frontend.controllers.authentication;

import it.polimi.tiw.backend.beans.User;
import it.polimi.tiw.backend.beans.exceptions.InvalidArgumentException;
import it.polimi.tiw.backend.dao.UserDAO;
import it.polimi.tiw.backend.dao.exceptions.RegistrationException;
import it.polimi.tiw.backend.utilities.Validators;
import it.polimi.tiw.backend.utilities.exceptions.FailedInputParsingException;
import it.polimi.tiw.backend.utilities.exceptions.PasswordMismatchException;
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
 * This servlet is used to handle the registration of a new user.
 */
@WebServlet(name = "UserRegistrationServlet", value = "/register")
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

    @SuppressWarnings("ConstantValue")
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // First, we get the success parameter from the request
            boolean success = Validators.parseBoolean(request.getParameter("success") == null ?
                    "false" : request.getParameter("success"));
            // Then, we get the errorCode parameter from the request (0 if it is not present)
            int errorCode = Validators.parseInt(request.getParameter("errorCode") == null ?
                    "0" : request.getParameter("errorCode"));

            // Now, we create a new WebContext object and we process the template
            WebContext context = getWebContextFromServlet(this, request, response);

            if (!success && errorCode == 0) {
                context.setVariable("message", "Please fill in the form to register a new user.");
                context.setVariable("messageContext", MessageTypesEnumeration.DEFAULT.getValue());
            } else if (!success && errorCode != 0) {
                context.setVariable("message", Validators.retrieveErrorMessageFromErrorCode(errorCode));
                context.setVariable("messageContext", MessageTypesEnumeration.ERROR.getValue());
            } else if (success && errorCode == 0) {
                context.setVariable("message", "The user has been successfully registered!" +
                        " You can now log in with your credentials.");
                context.setVariable("messageContext", MessageTypesEnumeration.SUCCESS.getValue());
            } else if (success && errorCode != 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid combination of parameters." +
                        " Are you trying to hijack the request?");
                return;
            }

            templateEngine.process("UserRegistrationTemplate", context, response.getWriter());
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
            // First, we get the parameters from the request
            String username = Validators.parseString(request.getParameter("username"));
            String password = Validators.parseString(request.getParameter("password"));
            String passwordRepeat = Validators.parseString(request.getParameter("confirmPassword"));
            String email = Validators.parseString(request.getParameter("email"));

            // Then, we validate the password and the password confirmation
            // (PasswordMismatchException is thrown if they do not match)
            Validators.validatePassword(password, passwordRepeat);

            // Now, we can try to create a User object
            // (InvalidArgumentException is thrown if the arguments are not valid)
            User newUser = new User(username, password, email);

            // Then, we can try to register the user into the database
            // (RegistrationException is thrown if the registration fails)
            // (SQLException is thrown if an error occurs communicating with the database)
            UserDAO userDAO = new UserDAO(servletConnection);
            userDAO.registerUser(newUser);

            // If everything went well, we redirect the user to the registration page with a success message
            response.sendRedirect(getServletContext().getContextPath() +
                    "/register?success=true");
        } catch (PasswordMismatchException | InvalidArgumentException |
                 RegistrationException | FailedInputParsingException e) {
            // Now we redirect the user to the registration page with the errorCode
            response.sendRedirect(getServletContext().getContextPath() +
                    "/register?errorCode=" + e.getErrorCode());
        } catch (SQLException e) {
            // If a SQLException is thrown, we send an error directly to the client
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to register user due to a critical error in the database.");
        }
    }
}