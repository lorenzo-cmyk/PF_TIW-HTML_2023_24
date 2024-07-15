package it.polimi.tiw.backend.dao;

import it.polimi.tiw.backend.beans.User;
import it.polimi.tiw.backend.dao.exceptions.RegistrationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * DAO class for managing users.
 * This class provides methods to interact with the database and perform authentication and registration operations.
 */
public class UserDAO {
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
        // The raw SQL query for registering a new user.
        String registrationQuery = "INSERT INTO Users (Username, PasswordHash, Email) VALUES (?, ?, ?)";

        try {
            // Since we are writing to the database, we need to use a transaction
            // in order to ensure that the operation is atomic.
            connection.setAutoCommit(false);

            // Try-with-resources statement used to automatically
            // close the PreparedStatement when it is no longer needed.
            try (PreparedStatement preparedStatement = connection.prepareStatement(registrationQuery)) {
                // Set the parameters of the query.
                preparedStatement.setString(1, newUser.getUsername());
                preparedStatement.setString(2, newUser.getPasswordHash());
                preparedStatement.setString(3, newUser.getEmail());

                // Execute the now parameterized query.
                preparedStatement.executeUpdate();

                // The user was successfully registered, so we can safely commit the transaction.
                connection.commit();
            }
        } catch (SQLException e) {
            // If an error occurs during the registration process, we need to roll back the transaction.
            connection.rollback();

            if(e.getErrorCode() == 1062) {
                // If the error is due to a unique constraint violation, we throw a RegistrationException.
                // This errorCode is specific to MySQL, and it means that a unique constraint was violated.
                throw new RegistrationException("Unable to register user. " +
                        "Check if the interested username is already taken.");
            } else {
                // If the error is due to another reason, we re-throw the exception.
                throw e;
            }

        } finally {
            // Restore the default behavior of the connection.
            connection.setAutoCommit(true);
        }
    }
}
