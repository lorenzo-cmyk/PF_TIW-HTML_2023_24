package it.polimi.tiw.backend.beans.exceptions;

import it.polimi.tiw.backend.errorcodes.ErrorCodes;
import it.polimi.tiw.backend.utilities.templates.ExtendedException;

/**
 * This exception is thrown when an invalid argument is provided to a method.
 */
public class InvalidArgumentException extends ExtendedException {
    /**
     * This constructor is used to create a new exception with a message.
     *
     * @param message the message of the exception
     * @see InvalidEmailException
     * @see InvalidUsernameException
     * @see TooLongArgumentException
     */
    public InvalidArgumentException(String message) {
        super(message);
    }

    /**
     * This constructor is used to create a new exception with the default message.
     */
    public InvalidArgumentException() {
        super(ErrorCodes.InvalidArgumentException.getErrorMessage());
    }

    /**
     * This method returns the errorCode of the exception.
     *
     * @return the errorCode of the exception
     */
    public int getErrorCode() {
        return ErrorCodes.InvalidArgumentException.getErrorCode();
    }
}
