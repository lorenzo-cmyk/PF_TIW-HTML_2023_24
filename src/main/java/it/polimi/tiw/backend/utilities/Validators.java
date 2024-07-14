package it.polimi.tiw.backend.utilities;

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
     * This method checks if an email is valid. An email is valid if it is not null and matches the regular expression
     * "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".
     *
     * @param email the email to check
     * @return true if the email is valid, false otherwise
     */
    public static boolean isEmailValid(String email) {
        return email != null && email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }
}
