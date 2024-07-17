package it.polimi.tiw.frontend.controllers.contentmanagement;

import it.polimi.tiw.backend.beans.Folder;
import it.polimi.tiw.backend.beans.User;
import it.polimi.tiw.backend.beans.exceptions.InvalidArgumentException;
import it.polimi.tiw.backend.dao.FolderDAO;
import it.polimi.tiw.backend.dao.exceptions.DuplicateFolderException;
import it.polimi.tiw.backend.dao.exceptions.FolderCreationException;
import it.polimi.tiw.backend.utilities.Validators;
import it.polimi.tiw.backend.utilities.exceptions.FailedInputParsingException;
import it.polimi.tiw.backend.utilities.exceptions.UnknownErrorCodeException;
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

import static it.polimi.tiw.backend.utilities.DatabaseConnectionBuilder.closeConnection;
import static it.polimi.tiw.backend.utilities.DatabaseConnectionBuilder.getConnectionFromServlet;
import static it.polimi.tiw.backend.utilities.ThymeleafObjectsBuilder.getTemplateEngineFromServlet;
import static it.polimi.tiw.backend.utilities.ThymeleafObjectsBuilder.getWebContextFromServlet;


@WebServlet(name = "ContentManagementServlet", value = "/content-management")
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

    @SuppressWarnings("ConstantValue")
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Extract parameters from the request (if they are present)
            boolean success = Validators.parseBoolean(request.getParameter("success") == null ?
                    "false" : request.getParameter("success"));
            int errorCode = Validators.parseInt(request.getParameter("errorCode") == null ?
                    "0" : request.getParameter("errorCode"));
            int parentFolderID = Validators.parseInt(request.getParameter("parentFolderID") == null ?
                    "-1" : request.getParameter("parentFolderID"));

            // Now, we create a new WebContext object and we process the template
            WebContext context = getWebContextFromServlet(this, request, response);

            if (!success && errorCode == 0) {
                context.setVariable("message", "Choose the action you want to perform.");
            } else if (!success && errorCode != 0) {
                context.setVariable("message", Validators.retrieveErrorMessageFromErrorCode(errorCode));
            } else if (success && errorCode == 0) {
                context.setVariable("message", "The action was successfully!" +
                        " The content of the DMS has been updated accordingly.");
            } else if (success && errorCode != 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid combination of parameters." +
                        " Are you trying to hijack the request?");
                return;
            }
            context.setVariable("parentFolderID", parentFolderID);

            templateEngine.process("ContentManagementTemplate", context, response.getWriter());

        } catch (FailedInputParsingException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Malformed request. " +
                    "Are you trying to hijack the request?");
        } catch (UnknownErrorCodeException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown error code provided. " +
                    "Are you trying to hijack the request?");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Extract the parameters from the request
            String folderName = Validators.parseString(request.getParameter("folderName"));
            int parentFolderID = Validators.parseInt(request.getParameter("parentFolderID") == null ?
                    "-1" : request.getParameter("parentFolderID"));

            // Retrieve the ownerID from the session
            int ownerID = ((User) request.getSession().getAttribute("user")).getUserID();

            // Create the folder in the database
            FolderDAO folderDAO = new FolderDAO(servletConnection);
            folderDAO.createFolder(
                    new Folder(folderName, ownerID, parentFolderID)
            );

            // Redirect to the content management page
            response.sendRedirect("content-management?success=true");
        } catch (FailedInputParsingException | InvalidArgumentException |
                 FolderCreationException | DuplicateFolderException e) {
            // Now we redirect the user to the registration page with the errorCode
            response.sendRedirect("content-management?errorCode=" + e.getErrorCode());
        } catch (SQLException e) {
            // If a SQLException is thrown, we send an error directly to the client
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to perform the requested action due to a critical error in the database.");
        }
    }
}
