package it.polimi.tiw.backend.dao.exceptions;

import it.polimi.tiw.backend.errorcodes.ErrorCodes;
import it.polimi.tiw.backend.utilities.templates.ExtendedException;

/**
 * This exception is thrown when a document with the same name already exists in the desired folder.
 */
public class DuplicateDocumentException extends ExtendedException {
    /**
     * This constructor is used to create a new exception with the default message.
     */
    public DuplicateDocumentException() {
        super(ErrorCodes.DuplicateDocumentException.getErrorMessage());
    }

    /**
     * This method returns the errorCode of the exception.
     *
     * @return the errorCode of the exception
     */
    public int getErrorCode() {
        return ErrorCodes.DuplicateDocumentException.getErrorCode();
    }
}
