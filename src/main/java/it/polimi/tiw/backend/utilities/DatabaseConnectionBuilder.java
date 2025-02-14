package it.polimi.tiw.backend.utilities;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class contains utility methods to handle database connections.
 */
public class DatabaseConnectionBuilder {
    /**
     * This method retrieves a connection to the database from a given servlet.
     *
     * @param httpServlet the servlet from which context retrieve the connection parameters
     * @return a connection to the database
     */
    public static Connection
    getConnectionFromServlet(HttpServlet httpServlet) {
        // Retrieve the servlet context from the servlet
        ServletContext servletContext = httpServlet.getServletContext();
        // Try to build the connection object
        Connection myConnection;
        try {
            // Try to retrieve the connection parameters from environment variables first
            String DBServerUrl = System.getenv("DB_SERVER_URL");
            String DBServerUser = System.getenv("DB_SERVER_USER");
            String DBServerPassword = System.getenv("DB_SERVER_PASSWORD");
            String DBConnectionDriver = System.getenv("DB_CONNECTION_DRIVER");
            // If any of the environment variables are not set, fall back to the servlet context parameters (web.xml)
            if (DBServerUrl == null || DBServerUser == null || DBServerPassword == null || DBConnectionDriver == null) {
                DBServerUrl = servletContext.getInitParameter("DBServerUrl");
                DBServerUser = servletContext.getInitParameter("DBServerUser");
                DBServerPassword = servletContext.getInitParameter("DBServerPassword");
                DBConnectionDriver = servletContext.getInitParameter("DBConnectionDriver");
            }

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
