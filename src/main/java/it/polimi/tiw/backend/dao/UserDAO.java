package it.polimi.tiw.backend.dao;

import it.polimi.tiw.backend.beans.User;
import it.polimi.tiw.backend.exceptions.RegistrationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * DAO class for managing users.
 * This class provides methods to interact with the database and perform authentication and registration operations.
 */
public class UserDAO {
    /**
     * The connection to the database.
     */
    private final Connection connection;

    /**
     * Constructor for the UserDAO class.
     *
     * @param connection a Connection object, which represents the connection to the database.
     */
    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * This method registers a new user in the database. It is called when a user tries to register to the application.
     * It is the DB job to check if the user already exists.
     *
     * @param newUser a User object, which represents the user to be registered.
     * @throws SQLException          if an error occurs during the registration process (SQL related).
     * @throws RegistrationException if the registration fails.
     */
    public void registerUser(User newUser) throws SQLException, RegistrationException {
        // Since we are writing to the database, we need to use a transaction
        // in order to ensure that the operation is atomic.
        connection.setAutoCommit(false);

        // Try to register the new user.
        try {
            // The raw SQL query for registering a new user.
            String registrationQuery = "INSERT INTO Users (Username, PasswordHash, Email) VALUES (?, ?, ?)";

            // Try-with-resources statement used to automatically
            // close the PreparedStatement when it is no longer needed.
            try (PreparedStatement preparedStatement = connection.prepareStatement(registrationQuery)) {
                // Set the parameters of the query.
                preparedStatement.setString(1, newUser.getUsername());
                preparedStatement.setString(2, newUser.getPasswordHash());
                preparedStatement.setString(3, newUser.getEmail());

                // Execute the query and check if it had any effect.
                if (preparedStatement.executeUpdate() == 0) {
                    throw new RegistrationException("Unable to register user. " +
                            "Check if the interested username is already taken.");
                    // If the execution of the query returns 0, it means that no rows were affected.
                    // This means that the user was not registered. The method throws a RegistrationException.
                    // Since the operation had no effect we don't need to commit the transaction.
                }

                // The user was successfully registered, so we can safely commit the transaction.
                connection.commit();
            }
        } catch (SQLException e) {
            // If an error occurs during the registration process, we need to roll back the transaction.
            connection.rollback();
            throw e; // Re-throw the exception to the caller.
        } finally {
            // Restore the default behavior of the connection.
            connection.setAutoCommit(true);
        }
    }
}
