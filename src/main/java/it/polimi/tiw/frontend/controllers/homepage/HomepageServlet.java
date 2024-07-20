package it.polimi.tiw.frontend.controllers.homepage;

import it.polimi.tiw.backend.beans.Folder;
import it.polimi.tiw.backend.beans.User;
import it.polimi.tiw.backend.dao.FolderDAO;
import it.polimi.tiw.backend.utilities.Validators;
import it.polimi.tiw.backend.utilities.exceptions.FailedInputParsingException;
import it.polimi.tiw.backend.utilities.templates.TreeNode;
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
 * This servlet is used to handle the homepage of the application.
 */
@WebServlet(name = "HomepageServlet", value = "/home")
public class HomepageServlet extends HttpServlet {
    private Connection servletConnection;
    private TemplateEngine templateEngine;

    /**
     * Default constructor, called by the servlet container.
     */
    @SuppressWarnings("unused")
    public HomepageServlet() {
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
            // Retrieving the actionCode parameter from the request
            int actionCode = Validators.parseInt(request.getParameter("actionCode") == null ?
                    "0" : request.getParameter("actionCode"));

            // Building the page message and the folderURL based on the actionCode
            String message;
            String folderURL;
            if (actionCode == HomepageActionEnumeration.CHOOSE_FOLDER_CREATE_FOLDER.getActionCode()) {
                message = "Choose the folder where you want to create the new directory";
                folderURL = getFoldersLink_CreateFolder(request);
            } else if (actionCode == HomepageActionEnumeration.CHOOSE_FOLDER_CREATE_DOCUMENT.getActionCode()) {
                message = "Choose the folder where you want to create the new document";
                folderURL = getFoldersLink_CreateDocument(request);
            } else if (actionCode == HomepageActionEnumeration.CHOOSE_FOLDER_MOVE_DOCUMENT.getActionCode()) {
                // TODO: Implementation not done yet
                message = "CHOOSE_FOLDER_MOVE_DOCUMENT";
                folderURL = "";
            } else {
                // TODO: Implementation not done yet
                message = "DEFAULT_ACTION_HOMEPAGE";
                folderURL = "";
            }

            // Retrieving the user object from the session
            User user = (User) request.getSession().getAttribute("user");
            // Building a FolderDAO object and using it to retrieve the client's folders tree
            FolderDAO folderDAO = new FolderDAO(servletConnection);
            TreeNode<Folder> foldersTree = folderDAO.buildFolderTree(-1, user.getUserID());

            // Creating the WebContext object that will be passed to the Thymeleaf template
            WebContext webContext = getWebContextFromServlet(this, request, response);

            webContext.setVariable("foldersTree", foldersTree);
            webContext.setVariable("message", message);
            webContext.setVariable("folderURL", folderURL);

            // Processing the template
            templateEngine.process("HomepageTemplate", webContext, response.getWriter());
        } catch (FailedInputParsingException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Malformed request. " +
                    "Are you trying to hijack the request?");
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to retrieve the folders tree due to a critical error in the database.");
        }
    }

    private String getFoldersLink_CreateFolder(HttpServletRequest request) throws FailedInputParsingException {
        // Retrieve the folderName from the request
        String folderName = Validators.parseString(request.getParameter("folderName"));

        // The URL will be relative and not absolute, as it will be used in the HTML
        return ("/create/create-folder?folderName=" + folderName);
    }

    private String getFoldersLink_CreateDocument(HttpServletRequest request) throws FailedInputParsingException {
        // Retrieve the documentName, type, and summary from the request
        String documentName = Validators.parseString(request.getParameter("documentName"));
        String type = Validators.parseString(request.getParameter("type"));
        String summary = Validators.parseString(request.getParameter("summary"));

        // The URL will be relative and not absolute, as it will be used in the HTML
        return ("/create/create-document?documentName=" + documentName +
                "&type=" + type +
                "&summary=" + summary);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}
