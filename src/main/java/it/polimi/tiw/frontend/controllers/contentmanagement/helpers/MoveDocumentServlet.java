package it.polimi.tiw.frontend.controllers.contentmanagement.helpers;

import it.polimi.tiw.backend.beans.Document;
import it.polimi.tiw.backend.beans.Folder;
import it.polimi.tiw.backend.beans.User;
import it.polimi.tiw.backend.dao.DocumentDAO;
import it.polimi.tiw.backend.dao.FolderDAO;
import it.polimi.tiw.backend.dao.exceptions.DocumentMovingException;
import it.polimi.tiw.backend.utilities.Validators;
import it.polimi.tiw.backend.utilities.exceptions.FailedInputParsingException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static it.polimi.tiw.backend.utilities.DatabaseConnectionBuilder.closeConnection;
import static it.polimi.tiw.backend.utilities.DatabaseConnectionBuilder.getConnectionFromServlet;

/**
 * This servlet manages the movement of a document inside the DMS.
 */
@WebServlet(name = "MoveDocumentServlet", value = "/move/move-document")
public class MoveDocumentServlet extends HttpServlet {
    private Connection servletConnection;

    /**
     * Default constructor, called by the servlet container.
     */
    @SuppressWarnings("unused")
    public MoveDocumentServlet() {
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
            // Retrieve and parse the parameters from the request and the session
            int folderID = Validators.parseInt(request.getParameter("folderID"));
            int documentID = Validators.parseInt(request.getParameter("documentID"));
            int ownerID = ((User) request.getSession().getAttribute("user")).getUserID();

            // Verify that documentID and folderID are valid
            FolderDAO folderDAO = new FolderDAO(servletConnection);
            DocumentDAO documentDAO = new DocumentDAO(servletConnection);

            Folder folder = folderDAO.getFolderByID(folderID, ownerID);
            Document document = documentDAO.getDocumentByID(documentID, ownerID);

            if (folder == null || document == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Folder or document not found and/or" +
                        "not owned by the user. Are you trying to hijack the request?");
                return;
            }

            // Move the document to the new folder
            try {
                documentDAO.moveDocument(documentID, ownerID, folderID);
            } catch (DocumentMovingException e) {
                // Redirect to the original folder page with an error message
                response.sendRedirect(getServletContext().getContextPath() +
                        "/folder?folderID=" + document.getFolderID() + "&errorCode=" + e.getErrorCode());
            }

            // Redirect to the folder page
            response.sendRedirect(getServletContext().getContextPath() +
                    "/folder?folderID=" + folderID);
        } catch (FailedInputParsingException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Malformed request. " +
                    "Are you trying to hijack the request?");
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to move the document due to a critical error in the database.");
        }
    }
}
