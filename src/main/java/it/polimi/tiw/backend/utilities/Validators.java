package it.polimi.tiw.backend.utilities;

import it.polimi.tiw.backend.errorcodes.ErrorCodes;
import it.polimi.tiw.backend.utilities.exceptions.FailedInputParsingException;
import it.polimi.tiw.backend.utilities.exceptions.PasswordMismatchException;
import it.polimi.tiw.backend.utilities.exceptions.UnknownErrorCodeException;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.Date;

/**
 * This class contains utility methods to validate data.
 */
public class Validators {
    /**
     * This method checks if a integer ID is valid. An integer ID is valid if it is greater than -2.
     *
     * @param id the integer ID to check
     * @return true if the integer ID is valid, false otherwise
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isIDValid(int id) {
        return id >= -1;
    }

    /**
     * This method checks if a Date is valid. A Date is valid if it is not null.
     *
     * @param date the Date to check
     * @return true if the Date is valid, false otherwise
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isDateValid(Date date) {
        return date != null;
    }

    /**
     * This method checks if a string is valid. A string is valid if it is not null, not empty and not blank.
     *
     * @param string the string to check
     * @return true if the string is valid, false otherwise
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isStringValid(String string) {
        return string != null && !string.isBlank() && !string.isEmpty();
    }

    /**
     * This method checks if an email is valid. We use the Apache Commons EmailValidator to check if the email is valid.
     *
     * @param email the email to check
     * @return true if the email is valid, false otherwise
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isEmailValid(String email) {
        // https://www.baeldung.com/java-email-validation-regex
        return EmailValidator.getInstance().isValid(email);
    }

    /**
     * This method checks if a username is valid. A username is valid if it only made of alphanumeric characters.
     *
     * @param username the username to check
     * @return true if the username is valid, false otherwise
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isUsernameValid(String username) {
        return username != null && username.matches("^[a-zA-Z0-9]+$");
    }

    /**
     * This method validates the password and the password confirmation.
     *
     * @param password             the password
     * @param passwordConfirmation the password confirmation
     * @throws PasswordMismatchException if the password and the password confirmation do not match
     */
    public static void validatePassword(String password, String passwordConfirmation) throws PasswordMismatchException {
        if (!password.equals(passwordConfirmation)) {
            throw new PasswordMismatchException();
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
        for (ErrorCodes entry : ErrorCodes.values()) {
            if (entry.getErrorCode() == errorCode) {
                return entry.getErrorMessage();
            }
        }

        throw new UnknownErrorCodeException();
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
            default -> throw new FailedInputParsingException();
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
            throw new FailedInputParsingException();
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
            throw new FailedInputParsingException();
        }
        return string;
    }
}
