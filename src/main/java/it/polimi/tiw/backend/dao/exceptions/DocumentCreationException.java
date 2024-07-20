package it.polimi.tiw.backend.dao.exceptions;

import it.polimi.tiw.backend.errorcodes.ErrorCodes;
import it.polimi.tiw.backend.utilities.templates.ExtendedException;

/**
 * This exception is thrown when an error occurs during the creation of a document.
 */
public class DocumentCreationException extends ExtendedException {
    /**
     * This constructor is used to create a new exception with the default message.
     */
    public DocumentCreationException() {
        super(ErrorCodes.DocumentCreationException.getErrorMessage());
    }

    /**
     * This method returns the errorCode of the exception.
     *
     * @return the errorCode of the exception
     */
    public int getErrorCode() {
        return ErrorCodes.DocumentCreationException.getErrorCode();
    }
}
