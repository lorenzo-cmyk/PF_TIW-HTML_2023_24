package it.polimi.tiw.backend.utilities.exceptions;

import it.polimi.tiw.backend.utilities.templates.ExtendedException;
import it.polimi.tiw.frontend.errorcodes.ErrorCodes;

/**
 * This exception is thrown when the password and the password confirmation do not match.
 */
public class PasswordMismatchException extends ExtendedException {
    /**
     * This constructor is used to create a new exception with a message.
     *
     * @param message the message of the exception
     */
    public PasswordMismatchException(String message) {
        super(message);
    }

    /**
     * This method returns the errorCode of the exception.
     *
     * @return the errorCode of the exception
     */
    public int getErrorCode() {
        return ErrorCodes.PasswordMismatchException.getErrorCode();
    }
}
