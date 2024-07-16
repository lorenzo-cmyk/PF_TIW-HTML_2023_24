package it.polimi.tiw.frontend.controllers;

import it.polimi.tiw.backend.beans.Folder;
import it.polimi.tiw.backend.beans.User;
import it.polimi.tiw.backend.dao.FolderDAO;
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
@WebServlet(name = "HomePageServlet", value = "/home")
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
            // Retrieve the user object from the session
            User user = (User) request.getSession().getAttribute("user");
            // Build a FolderDAO object and use it to retrieve the client's folders tree
            FolderDAO folderDAO = new FolderDAO(servletConnection);
            TreeNode<Folder> foldersTree = folderDAO.buildFolderTree(-1, user.getUserID());
            // Display the homepage
            WebContext context = getWebContextFromServlet(this, request, response);
            context.setVariable("root", foldersTree);
            templateEngine.process("HomepageTemplate", context, response.getWriter());
        } catch (SQLException e) {
            // If a SQLException is thrown, we send an error directly to the client
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to retrieve the folders tree due to a critical error in the database.");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}
