package it.polimi.tiw.frontend.controllers.contentmanagement;

import it.polimi.tiw.backend.beans.Document;
import it.polimi.tiw.backend.beans.User;
import it.polimi.tiw.backend.dao.DocumentDAO;
import it.polimi.tiw.backend.utilities.Validators;
import it.polimi.tiw.backend.utilities.exceptions.FailedInputParsingException;
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
 * This servlet manages the visualization of the details of a document inside the DMS.
 */
@WebServlet(name = "ViewDocumentDetailsServlet", value = "/document")
public class ViewDocumentDetailsServlet extends HttpServlet {
    private Connection servletConnection;
    private TemplateEngine templateEngine;

    /**
     * Default constructor, called by the servlet container.
     */
    @SuppressWarnings("unused")
    public ViewDocumentDetailsServlet() {
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // Retrieve and parse the parameters from the request
            int documentID = Validators.parseInt(request.getParameter("documentID"));
            // Get the ownerID from the session and store it in a variable
            int ownerID = ((User) request.getSession().getAttribute("user")).getUserID();

            // Retrieve the document from the database
            DocumentDAO documentDAO = new DocumentDAO(servletConnection);
            Document document = documentDAO.getDocumentByID(documentID, ownerID);
            // Ensure that the document exists and is owned by the user
            if (document == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "The document you are asking for does not exist or is not owned by the logged user." +
                                " Are you trying to hijack the request?");
                return;
            }

            // Create the WebContext object that will be passed to the Thymeleaf template
            WebContext webContext = getWebContextFromServlet(this, request, response);

            // Set the variables in the context
            webContext.setVariable("document", document);

            // Process the template
            templateEngine.process("ViewDocumentDetailsTemplate", webContext, response.getWriter());
        } catch (FailedInputParsingException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Malformed request. " +
                    "Are you trying to hijack the request?");
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to retrieve the document details due to a critical error in the database.");
        }
    }
}
