package it.polimi.tiw.frontend.controllers.contentmanagement;

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

import static it.polimi.tiw.backend.utilities.DatabaseConnectionBuilder.closeConnection;
import static it.polimi.tiw.backend.utilities.DatabaseConnectionBuilder.getConnectionFromServlet;
import static it.polimi.tiw.backend.utilities.ThymeleafObjectsBuilder.getTemplateEngineFromServlet;
import static it.polimi.tiw.backend.utilities.ThymeleafObjectsBuilder.getWebContextFromServlet;

/**
 * This servlet manages the creation of new content inside the DMS.
 */
@WebServlet(name = "ContentManagementServlet", urlPatterns = "/create")
public class ContentManagementServlet extends HttpServlet {
    private Connection servletConnection;
    private TemplateEngine templateEngine;

    /**
     * Default constructor, called by the servlet container.
     */
    @SuppressWarnings("unused")
    public ContentManagementServlet() {
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
            // Retrieve and parse the parameters from the request
            int errorCode = Validators.parseInt(request.getParameter("errorCode") == null ?
                    "0" : request.getParameter("errorCode"));

            // Create the WebContext object that will be passed to the Thymeleaf template
            WebContext webContext = getWebContextFromServlet(this, request, response);

            String message;
            if (errorCode == 0) {
                message = "Here you can create a new folder, a new subfolder or a new document.";
                webContext.setVariable("messageContext", MessageTypesEnumeration.DEFAULT.getValue());
            } else {
                message = Validators.retrieveErrorMessageFromErrorCode(errorCode);
                webContext.setVariable("messageContext", MessageTypesEnumeration.ERROR.getValue());
            }

            // Set the errorCode parameter in the context
            webContext.setVariable("message", message);

            // Process the template
            templateEngine.process("ContentManagementTemplate", webContext, response.getWriter());
        } catch (FailedInputParsingException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Malformed request. " +
                    "Are you trying to hijack the request?");
        } catch (UnknownErrorCodeException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown error code provided. " +
                    "Are you trying to hijack the request?");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}
