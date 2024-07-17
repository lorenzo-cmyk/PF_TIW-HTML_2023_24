package it.polimi.tiw.backend.dao.exceptions;

import it.polimi.tiw.backend.errorcodes.ErrorCodes;
import it.polimi.tiw.backend.utilities.templates.ExtendedException;

/**
 * This exception is thrown when a folder with the same name already exists in the parent directory.
 */
public class DuplicateFolderException extends ExtendedException {
    /**
     * This constructor is used to create a new exception with the default message.
     */
    public DuplicateFolderException() {
        super(ErrorCodes.DuplicateFolderException.getErrorMessage());
    }

    /**
     * This method returns the errorCode of the exception.
     *
     * @return the errorCode of the exception
     */
    public int getErrorCode() {
        return ErrorCodes.DuplicateFolderException.getErrorCode();
    }
}
