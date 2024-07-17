package it.polimi.tiw.backend.utilities.exceptions;

import it.polimi.tiw.backend.errorcodes.ErrorCodes;
import it.polimi.tiw.backend.utilities.templates.ExtendedException;

/**
 * This exception is thrown when an unknown error code is provided.
 */
public class UnknownErrorCodeException extends ExtendedException {
    /**
     * This exception is thrown when an error occurs during the creation of a folder.
     */
    public UnknownErrorCodeException() {
        super(ErrorCodes.UnknownErrorCode.getErrorMessage());
    }

    /**
     * This method returns the errorCode of the exception.
     *
     * @return the errorCode of the exception
     */
    public int getErrorCode() {
        return ErrorCodes.UnknownErrorCode.getErrorCode();
    }
}
