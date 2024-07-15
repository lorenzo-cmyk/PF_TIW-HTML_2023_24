package it.polimi.tiw.frontend.utilities.exceptions;

/**
 * This exception is thrown when an unknown error code is provided.
 */
public class UnknownErrorCodeException extends Exception {
    /**
     * This constructor is used to create a new exception with a message.
     *
     * @param message the message of the exception
     */
    public UnknownErrorCodeException(String message) {
        super(message);
    }
}
