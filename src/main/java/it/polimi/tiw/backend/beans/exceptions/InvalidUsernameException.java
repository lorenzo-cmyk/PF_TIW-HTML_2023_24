package it.polimi.tiw.backend.beans.exceptions;

import it.polimi.tiw.backend.errorcodes.ErrorCodes;

/**
 * This exception is thrown when an invalid username is provided to a method.
 */
public class InvalidUsernameException extends InvalidArgumentException {
    /**
     * This constructor is used to create a new exception with a message.
     *
     * @param message the message of the exception
     */
    public InvalidUsernameException(String message) {
        super(message);
    }

    /**
     * This method returns the errorCode of the exception.
     *
     * @return the errorCode of the exception
     */
    public int getErrorCode() {
        return ErrorCodes.InvalidUsernameException.getErrorCode();
    }
}
