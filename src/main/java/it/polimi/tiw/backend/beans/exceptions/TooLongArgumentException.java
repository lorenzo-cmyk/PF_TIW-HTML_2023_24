package it.polimi.tiw.backend.beans.exceptions;

import it.polimi.tiw.backend.errorcodes.ErrorCodes;

/**
 * This exception is thrown when an argument, provided to a method, is too long.
 */
public class TooLongArgumentException extends InvalidArgumentException {
    /**
     * This constructor is used to create a new exception with the default message.
     */
    public TooLongArgumentException() {
        super(ErrorCodes.TooLongArgumentException.getErrorMessage());
    }

    /**
     * This method returns the errorCode of the exception.
     *
     * @return the errorCode of the exception
     */
    public int getErrorCode() {
        return ErrorCodes.TooLongArgumentException.getErrorCode();
    }
}
