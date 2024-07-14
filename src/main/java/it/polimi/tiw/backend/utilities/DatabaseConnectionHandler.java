package it.polimi.tiw.backend.utilities;

import jakarta.servlet.ServletContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class contains utility methods to handle database connections.
 */
public class DatabaseConnectionHandler {
    /**
     * This method retrieves a connection to the database from the servlet context.
     *
     * @param servletContext the servlet context from which to retrieve the connection parameters
     * @return a connection to the database
     */
    public static Connection
    getConnectionFromServletContext(ServletContext servletContext) {
        Connection myConnection;
        try {
            // Retrieve the connection parameters from the servlet context (set in the web.xml file)
            String DBServerUrl = servletContext.getInitParameter("DBServerUrl");
            String DBServerUser = servletContext.getInitParameter("DBServerUser");
            String DBServerPassword = servletContext.getInitParameter("DBServerPassword");
            String DBConnectionDriver = servletContext.getInitParameter("DBConnectionDriver");
            // Try to load the connection driver
            Class.forName(DBConnectionDriver);
            // Try to establish the connection
            myConnection = DriverManager.getConnection(DBServerUrl, DBServerUser, DBServerPassword);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Failed to load the connection driver.");
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to establish the connection to the database.");
        }
        return myConnection;
    }

    /**
     * This method closes a connection to the database.
     *
     * @param connection the connection to close
     */
    public static void closeConnection(Connection connection) {
        // Check if the connection is null before trying to close it
        if (connection == null) {
            throw new IllegalArgumentException("The connection provided is null.");
        }

        // Try to close the connection
        try {
            connection.close();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to close the connection to the database.");
        }
    }
}
