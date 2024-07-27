package it.polimi.tiw.frontend.controllers.contentmanagement.helpers;

import it.polimi.tiw.backend.beans.Document;
import it.polimi.tiw.backend.beans.User;
import it.polimi.tiw.backend.beans.exceptions.InvalidArgumentException;
import it.polimi.tiw.backend.dao.DocumentDAO;
import it.polimi.tiw.backend.dao.exceptions.DocumentCreationException;
import it.polimi.tiw.backend.dao.exceptions.DuplicateDocumentException;
import it.polimi.tiw.backend.utilities.Validators;
import it.polimi.tiw.backend.utilities.exceptions.FailedInputParsingException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import static it.polimi.tiw.backend.utilities.DatabaseConnectionBuilder.closeConnection;
import static it.polimi.tiw.backend.utilities.DatabaseConnectionBuilder.getConnectionFromServlet;

/**
 * This servlet manages the creation of a new document inside the DMS.
 */
@WebServlet(name = "CreateDocumentServlet", value = "/create/create-document")
public class CreateDocumentServlet extends HttpServlet {
    private Connection servletConnection;

    /**
     * Default constructor, called by the servlet container.
     */
    @SuppressWarnings("unused")
    public CreateDocumentServlet() {
        super();
    }

    /**
     * This method is called by the servlet container when the servlet is initialized.
     * It initializes the servletConnection and the templateEngine objects.
     */
    public void init() {
        servletConnection = getConnectionFromServlet(this);
    }

    /**
     * This method is called by the servlet container when the servlet is destroyed.
     * It closes the connection to the database.
     */
    public void destroy() {
        closeConnection(servletConnection);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // Retrieve and parse the parameters from the request
            String documentName = Validators.parseString(request.getParameter("documentName"));
            Date creationDate = new Date();
            String type = Validators.parseString(request.getParameter("type"));
            String summary = Validators.parseString(request.getParameter("summary"));
            int ownerID = ((User) request.getSession().getAttribute("user")).getUserID();
            int folderID = Validators.parseInt(request.getParameter("folderID"));

            // Create the document object
            Document document = new Document(documentName, creationDate, type, summary, ownerID, folderID);

            // Create the document in the database
            DocumentDAO documentDAO = new DocumentDAO(servletConnection);
            documentDAO.createDocument(document);

            // Redirect to the folder page
            response.sendRedirect(getServletContext().getContextPath() +
                    "/folder?folderID=" + folderID);
        } catch (FailedInputParsingException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Malformed request. " +
                    "Are you trying to hijack the request?");
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to create the document due to a critical error in the database.");
        } catch (DocumentCreationException | DuplicateDocumentException | InvalidArgumentException e) {
            // Redirect to the create page with an error message
            response.sendRedirect(getServletContext().getContextPath() +
                    "/create?errorCode=" + e.getErrorCode());
        }
    }
}
