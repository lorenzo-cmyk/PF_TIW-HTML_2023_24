package it.polimi.tiw.backend.beans.exceptions;

import it.polimi.tiw.backend.errorcodes.ErrorCodes;

/**
 * This exception is thrown when an invalid email is provided to a method.
 */
public class InvalidEmailException extends InvalidArgumentException {
    /**
     * This constructor is used to create a new exception with a message.
     *
     * @param message the message of the exception
     */
    public InvalidEmailException(String message) {
        super(message);
    }

    /**
     * This method returns the error code of the exception.
     *
     * @return the error code of the exception
     */
    public ErrorCodes getErrorCode() {
        return ErrorCodes.InvalidEmailException;
    }
}
