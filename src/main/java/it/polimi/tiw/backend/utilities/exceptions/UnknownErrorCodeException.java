package it.polimi.tiw.backend.utilities.exceptions;

import it.polimi.tiw.backend.utilities.templates.ExtendedException;
import it.polimi.tiw.frontend.errorcodes.ErrorCodes;

/**
 * This exception is thrown when an unknown error code is provided.
 */
public class UnknownErrorCodeException extends ExtendedException {
    /**
     * This constructor is used to create a new exception with a message.
     *
     * @param message the message of the exception
     */
    public UnknownErrorCodeException(String message) {
        super(message);
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
