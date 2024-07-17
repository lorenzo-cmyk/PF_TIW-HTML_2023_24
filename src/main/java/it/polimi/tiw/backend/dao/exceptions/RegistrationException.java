package it.polimi.tiw.backend.dao.exceptions;

import it.polimi.tiw.backend.errorcodes.ErrorCodes;
import it.polimi.tiw.backend.utilities.templates.ExtendedException;

/**
 * This exception is thrown when an error occurs during the registration process.
 */
public class RegistrationException extends ExtendedException {
    /**
     * This constructor is used to create a new exception with the default message.
     */
    public RegistrationException() {
        super(ErrorCodes.RegistrationException.getErrorMessage());
    }

    /**
     * This method returns the errorCode of the exception.
     *
     * @return the errorCode of the exception
     */
    public int getErrorCode() {
        return ErrorCodes.RegistrationException.getErrorCode();
    }
}
