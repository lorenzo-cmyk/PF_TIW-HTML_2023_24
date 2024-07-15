package it.polimi.tiw.backend.beans.exceptions;

/**
 * This exception is thrown when an argument, provided to a method, is too long.
 */
public class TooLongArgumentException extends InvalidArgumentException{
    /**
     * This constructor is used to create a new exception with a message.
     * @param message the message of the exception
     */
    public TooLongArgumentException(String message) {
        super(message);
    }
}
