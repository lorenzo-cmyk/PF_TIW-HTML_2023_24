package it.polimi.tiw.frontend.utilities.exceptions;

import it.polimi.tiw.backend.utilities.templates.ExtendedException;
import it.polimi.tiw.frontend.errorcodes.ErrorCodes;

/**
 * This exception is thrown when the input parsing fails.
 */
public class FailedInputParsingException extends ExtendedException {
    /**
     * This constructor is used to create a new exception with a message.
     *
     * @param message the message of the exception
     */
    public FailedInputParsingException(String message) {
        super(message);
    }

    /**
     * This method returns the errorCode of the exception.
     *
     * @return the errorCode of the exception
     */
    public int getErrorCode() {
        return ErrorCodes.FailedInputParsingException.getErrorCode();
    }
}
