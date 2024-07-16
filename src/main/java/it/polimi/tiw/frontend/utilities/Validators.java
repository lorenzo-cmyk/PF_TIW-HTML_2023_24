package it.polimi.tiw.frontend.utilities;

import it.polimi.tiw.frontend.utilities.exceptions.FailedInputParsingException;
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
            throw new PasswordMismatchException("The fields password and password confirmation do not match." +
                    " . Please check your input and try again.");
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
        throw new UnknownErrorCodeException("The error code provided does not exist." +
                " Please check your input and try again.");
    }

    /**
     * This method parse a string to a boolean.
     * If the string is "true" it returns true, if the string is "false" it returns false.
     * If the string is neither "true" nor "false" it throws a FailedInputParsingException.
     *
     * @param string the string to parse
     * @return the boolean parsed from the string
     * @throws FailedInputParsingException if the string is not "true" or "false"
     */
    public static boolean parseBoolean(String string) throws FailedInputParsingException {
        return switch (string) {
            case "true" -> true;
            case "false" -> false;
            default -> throw new FailedInputParsingException("The input provided is not valid." +
                    " Please check that all fields are valid and populated and try again .");
        };
    }

    /**
     * This method parse a string to an integer.
     * If the string is a valid integer it returns the integer,
     * if the string is not a valid integer it throws a FailedInputParsingException.
     *
     * @param string the string to parse
     * @return the integer parsed from the string
     * @throws FailedInputParsingException if the string is not a valid integer
     */
    public static int parseInt(String string) throws FailedInputParsingException {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            throw new FailedInputParsingException("The input provided is not valid." +
                    " Please check that all fields are valid and populated and try again .");
        }
    }

    /**
     * This method parse a string to check if it is valid.
     * If the string is not valid, it throws a FailedInputParsingException.
     *
     * @param string the string to parse
     * @return the string validated
     * @throws FailedInputParsingException if the string is not valid
     */
    public static String parseString(String string) throws FailedInputParsingException {
        if (string == null || string.isBlank() || string.isEmpty()) {
            throw new FailedInputParsingException("The input provided is not valid." +
                    " Please check that all fields are valid and populated and try again .");
        }
        return string;
    }
}
