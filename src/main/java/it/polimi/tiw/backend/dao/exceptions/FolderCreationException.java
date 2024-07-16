package it.polimi.tiw.backend.dao.exceptions;

import it.polimi.tiw.backend.errorcodes.ErrorCodes;
import it.polimi.tiw.backend.utilities.templates.ExtendedException;

/**
 * This exception is thrown when an error occurs during the creation of a folder.
 */
public class FolderCreationException extends ExtendedException {
    /**
     * This constructor is used to create a new exception with a message.
     *
     * @param message the message of the exception
     */
    public FolderCreationException(String message) {
        super(message);
    }

    /**
     * This method returns the errorCode of the exception.
     *
     * @return the errorCode of the exception
     */
    public int getErrorCode() {
        return ErrorCodes.FolderCreationException.getErrorCode();
    }
}
