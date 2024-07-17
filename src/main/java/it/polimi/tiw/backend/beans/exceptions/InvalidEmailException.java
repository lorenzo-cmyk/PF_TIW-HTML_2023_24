package it.polimi.tiw.backend.beans.exceptions;

import it.polimi.tiw.backend.errorcodes.ErrorCodes;

/**
 * This exception is thrown when an invalid email is provided to a method.
 */
public class InvalidEmailException extends InvalidArgumentException {
    /**
     * This constructor is used to create a new exception with the default message.
     */
    public InvalidEmailException() {
        super(ErrorCodes.InvalidEmailException.getErrorMessage());
    }

    /**
     * This method returns the errorCode of the exception.
     *
     * @return the errorCode of the exception
     */
    public int getErrorCode() {
        return ErrorCodes.InvalidEmailException.getErrorCode();
    }
}
