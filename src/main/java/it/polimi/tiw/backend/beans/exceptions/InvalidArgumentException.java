package it.polimi.tiw.backend.beans.exceptions;

/**
 * This exception is thrown when an invalid argument is provided to a method.
 */
public class InvalidArgumentException extends Exception {
    /**
     * This constructor is used to create a new exception with a message.
     *
     * @param message the message of the exception
     */
    public InvalidArgumentException(String message) {
        super(message);
    }
}
