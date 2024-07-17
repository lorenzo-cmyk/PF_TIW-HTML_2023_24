package it.polimi.tiw.backend.dao;

import it.polimi.tiw.backend.beans.User;
import it.polimi.tiw.backend.beans.exceptions.InvalidArgumentException;
import it.polimi.tiw.backend.dao.exceptions.LoginException;
import it.polimi.tiw.backend.dao.exceptions.RegistrationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

            if (e.getErrorCode() == 1062) {
                // If the error is due to a unique constraint violation, we throw a RegistrationException.
                // This errorCode is specific to MySQL, and it means that a unique constraint was violated.
                throw new RegistrationException();
            } else {
                // If the error is due to another reason, we re-throw the exception.
                throw e;
            }

        } finally {
            // Restore the default behavior of the connection.
            connection.setAutoCommit(true);
        }
    }

    /**
     * This method authenticates a user by looking for it into the database.
     * It is called when a user tries to log in to the application.
     *
     * @param user a User object, which represents the user to be authenticated.
     *             The object must contain the username and the password hash.
     * @return a User object, which represents the authenticated user.
     * @throws SQLException   if an error occurs during the authentication process (SQL related).
     * @throws LoginException if the user is not found or the password is incorrect.
     */
    public User authenticateUser(User user) throws SQLException, LoginException {
        // The raw SQL query for authenticating a user.
        String authenticationQuery = "SELECT * FROM Users WHERE Username = ? AND PasswordHash = ?";

        // Try-with-resources statement used to automatically
        // close the PreparedStatement when it is no longer needed.
        try (PreparedStatement preparedStatement = connection.prepareStatement(authenticationQuery)) {
            // Set the parameters of the query.
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPasswordHash());

            // Execute the now parameterized query.
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // If the query returns no results, the user is not found or the password is incorrect.
                if (!resultSet.next()) {
                    throw new LoginException();
                }

                // If the query returns a result, the user is authenticated.
                // We create a new User object with the data from the database.
                User authenticatedUser;
                try {
                    authenticatedUser = new User(
                            resultSet.getInt("UserID"),
                            resultSet.getString("Username"),
                            resultSet.getString("PasswordHash"),
                            resultSet.getString("Email")
                    );
                } catch (InvalidArgumentException e) {
                    throw new IllegalStateException("The database is corrupted." +
                            " The user data is invalid. Please check the database integrity.");
                }

                // If the query returns more than one result, the database is corrupted.
                if (resultSet.next()) {
                    throw new IllegalStateException("The database is corrupted." +
                            " More than one user with the same credentials.");
                }

                return authenticatedUser;
            }
        }
    }
}
