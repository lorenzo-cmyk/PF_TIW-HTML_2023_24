package it.polimi.tiw.backend.dao.exceptions;

import it.polimi.tiw.backend.errorcodes.ErrorCodes;
import it.polimi.tiw.backend.utilities.templates.ExtendedException;

/**
 * This exception is thrown when an error occurs during the moving of a document.
 */
public class DocumentMovingException extends ExtendedException {
    /**
     * This constructor is used to create a new exception with the default message.
     */
    public DocumentMovingException() {
        super(ErrorCodes.DocumentMovingException.getErrorMessage());
    }

    /**
     * This method returns the errorCode of the exception.
     *
     * @return the errorCode of the exception
     */
    public int getErrorCode() {
        return ErrorCodes.DocumentMovingException.getErrorCode();
    }
}
