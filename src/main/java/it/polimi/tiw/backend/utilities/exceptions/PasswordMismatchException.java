package it.polimi.tiw.backend.utilities.exceptions;

import it.polimi.tiw.backend.errorcodes.ErrorCodes;
import it.polimi.tiw.backend.utilities.templates.ExtendedException;

/**
 * This exception is thrown when the password and the password confirmation do not match.
 */
public class PasswordMismatchException extends ExtendedException {
    /**
     * This exception is thrown when an error occurs during the creation of a folder.
     */
    public PasswordMismatchException() {
        super(ErrorCodes.PasswordMismatchException.getErrorMessage());
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
