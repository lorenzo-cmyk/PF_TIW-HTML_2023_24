package it.polimi.tiw.frontend.utilities.exceptions;

/**
 * This exception is thrown when the input parsing fails.
 */
public class FailedInputParsingException extends Exception {
    /**
     * This constructor is used to create a new exception with a message.
     *
     * @param message the message of the exception
     */
    public FailedInputParsingException(String message) {
        super(message);
    }
}
