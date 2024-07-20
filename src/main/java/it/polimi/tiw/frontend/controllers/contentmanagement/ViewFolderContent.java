package it.polimi.tiw.frontend.controllers.contentmanagement;

import it.polimi.tiw.backend.beans.Document;
import it.polimi.tiw.backend.beans.Folder;
import it.polimi.tiw.backend.beans.User;
import it.polimi.tiw.backend.dao.DocumentDAO;
import it.polimi.tiw.backend.dao.FolderDAO;
import it.polimi.tiw.backend.utilities.Validators;
import it.polimi.tiw.backend.utilities.exceptions.FailedInputParsingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static it.polimi.tiw.backend.utilities.DatabaseConnectionBuilder.closeConnection;
import static it.polimi.tiw.backend.utilities.DatabaseConnectionBuilder.getConnectionFromServlet;
import static it.polimi.tiw.backend.utilities.ThymeleafObjectsBuilder.getTemplateEngineFromServlet;
import static it.polimi.tiw.backend.utilities.ThymeleafObjectsBuilder.getWebContextFromServlet;

/**
 * This servlet manages the visualization of the content of a folder inside the DMS.
 */
@WebServlet(name = "ViewFolderContent", value = "/folder")
public class ViewFolderContent extends HttpServlet {
    private Connection servletConnection;
    private TemplateEngine templateEngine;

    /**
     * Default constructor, called by the servlet container.
     */
    @SuppressWarnings("unused")
    public ViewFolderContent() {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // Retrieve and parse the folderID parameter from the request
            int folderID = Validators.parseInt(request.getParameter("folderID"));
            // Get the ownerID from the session and store it in a variable
            int ownerID = ((User) request.getSession().getAttribute("user")).getUserID();

            // Ensure that the folder I want to view actually exists and is owned by the user
            FolderDAO folderDAO = new FolderDAO(servletConnection);
            Folder folder = folderDAO.getFolderByID(folderID, ownerID);
            if (folder == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "The folder you are asking for does not exist or is not owned by the logged user." +
                                " Are you trying to hijack the request?");
                return;
            }
            // The folder actually exists and is owned by the user, so I can proceed
            String folderName = folder.getFolderName();
            List<Folder> subfolders = folderDAO.getSubfolders(folderID, ownerID);

            // Retrieve the documents contained in the folder
            DocumentDAO documentDAO = new DocumentDAO(servletConnection);
            List<Document> documents = documentDAO.getDocumentsByFolder(folderID, ownerID);

            // Create the WebContext object that will be passed to the Thymeleaf template
            WebContext webContext = getWebContextFromServlet(this, request, response);

            // Set the variables in the context
            webContext.setVariable("folderName", folderName);
            webContext.setVariable("subfolders", subfolders);
            webContext.setVariable("documents", documents);

            // Process the template
            templateEngine.process("ViewFolderContentTemplate", webContext, response.getWriter());
        } catch (FailedInputParsingException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Malformed request. " +
                    "Are you trying to hijack the request?");
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to retrieve the folder content due to a critical error in the database.");
        }
    }
}
