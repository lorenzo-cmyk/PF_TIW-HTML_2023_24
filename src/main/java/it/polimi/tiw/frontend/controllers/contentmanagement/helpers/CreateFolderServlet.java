package it.polimi.tiw.frontend.controllers.contentmanagement.helpers;


import it.polimi.tiw.backend.beans.Folder;
import it.polimi.tiw.backend.beans.User;
import it.polimi.tiw.backend.beans.exceptions.InvalidArgumentException;
import it.polimi.tiw.backend.dao.FolderDAO;
import it.polimi.tiw.backend.dao.exceptions.DuplicateFolderException;
import it.polimi.tiw.backend.dao.exceptions.FolderCreationException;
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
 * This servlet manages the creation of a new folder inside the DMS.
 */
@WebServlet(name = "CreateFolderServlet", value = "/create/create-folder")
public class CreateFolderServlet extends HttpServlet {
    private Connection servletConnection;

    /**
     * Default constructor, called by the servlet container.
     */
    @SuppressWarnings("unused")
    public CreateFolderServlet() {
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
            String folderName = Validators.parseString(request.getParameter("folderName"));
            int parentFolderID = Validators.parseInt(request.getParameter("parentFolderID"));
            int ownerID = ((User) request.getSession().getAttribute("user")).getUserID();

            // Create the folder object
            Folder folder = new Folder(folderName, ownerID, parentFolderID);

            // Create the folder in the database
            FolderDAO folderDAO = new FolderDAO(servletConnection);
            folderDAO.createFolder(folder);

            // Redirect to the home page
            response.sendRedirect(getServletContext().getContextPath() +
                    "/home");
        } catch (FailedInputParsingException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Malformed request. " +
                    "Are you trying to hijack the request?");
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to create the folder due to a critical error in the database.");
        } catch (FolderCreationException | DuplicateFolderException | InvalidArgumentException e) {
            // Redirect to the create page with an error message
            response.sendRedirect(getServletContext().getContextPath() +
                    "/create?errorCode=" + e.getErrorCode());
        }
    }

}
