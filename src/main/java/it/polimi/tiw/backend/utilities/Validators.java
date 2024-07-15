package it.polimi.tiw.backend.utilities;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * This class contains utility methods to validate data.
 */
public class Validators {
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
    public static boolean isUsernameValid(String username) {
        return username != null && username.matches("^[a-zA-Z0-9]+$");
    }
}
