package it.polimi.tiw.frontend.utilities;

import it.polimi.tiw.frontend.utilities.exceptions.PasswordMismatchException;
import it.polimi.tiw.frontend.utilities.exceptions.UnknownErrorCodeException;

/**
 * This class provides some utility methods to validate data.
 */
public class Validators {
    /**
     * This method validates the password and the password confirmation.
     *
     * @param password             the password
     * @param passwordConfirmation the password confirmation
     * @throws PasswordMismatchException if the password and the password confirmation do not match
     */
    public static void validatePassword(String password, String passwordConfirmation) throws PasswordMismatchException {
        if (!password.equals(passwordConfirmation)) {
            throw new PasswordMismatchException("The fields password and password confirmation do not match.");
        }
    }

    /**
     * This method retrieves the errorMessage from the errorCode.
     *
     * @param errorCode the errorCode to retrieve the errorMessage from
     * @return the errorMessage of the errorCode
     * @throws UnknownErrorCodeException if the errorCode is unknown
     */
    public static String retrieveErrorMessageFromErrorCode(int errorCode) throws UnknownErrorCodeException {
        // Fist we scan the backend ErrorCodes enum to see if the errorCode is there
        for (it.polimi.tiw.backend.errorcodes.ErrorCodes backendErrorCode :
                it.polimi.tiw.backend.errorcodes.ErrorCodes.values()) {
            if (backendErrorCode.getErrorCode() == errorCode) {
                return backendErrorCode.getErrorMessage();
            }
        }
        // If the errorCode is not in the backend ErrorCodes enum, we scan the frontend ErrorCodes enum
        for (it.polimi.tiw.frontend.errorcodes.ErrorCodes frontendErrorCode :
                it.polimi.tiw.frontend.errorcodes.ErrorCodes.values()) {
            if (frontendErrorCode.getErrorCode() == errorCode) {
                return frontendErrorCode.getErrorMessage();
            }
        }

        // If the errorCode is not in the frontend ErrorCodes enum, we throw an exception
        throw new UnknownErrorCodeException("The error code " + errorCode + " is unknown.");
    }
}
