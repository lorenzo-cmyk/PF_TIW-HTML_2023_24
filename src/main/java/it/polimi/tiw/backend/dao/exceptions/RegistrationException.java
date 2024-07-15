package it.polimi.tiw.backend.dao.exceptions;

/**
 * This exception is thrown when an error occurs during the registration process.
 */
public class RegistrationException extends Exception {
    /**
     * This constructor is used to create a new exception with a message.
     *
     * @param message the message of the exception
     */
    public RegistrationException(String message) {
        super(message);
    }
}
