package it.polimi.tiw.backend.errorcodes;

/**
 * This enum contains all the error codes that can be used in the application by the backend.
 */
public enum ErrorCodes {
    /**
     * This error code is used when the arguments provided are not valid.
     */
    InvalidArgumentException(1001, "Some of the arguments provided are null or empty." +
            " Please check your input and try again."),
    /**
     * This error code is used when the arguments provided exceed the maximum length.
     */
    TooLongArgumentException(1002, "Some of the arguments provided are too long." +
            " Please check your input and try again."),
    /**
     * This error code is used when the email provided is not syntactically valid.
     */
    InvalidEmailException(1003, "The email provided is not syntactically valid." +
            " Please check your input and try again."),
    /**
     * This error code is used when the registration of a user fails.
     */
    RegistrationException(1004, "Unable to register user." +
            " The username maybe already taken. Please try again with a different one."),
    /**
     * This error code is used when the username provided is not syntactically valid.
     */
    InvalidUsernameException(1005, "The username provided is not syntactically valid." +
            " Make sure it is made only of alphanumeric characters and try again.");

    private final int errorCode;
    private final String errorMessage;

    /**
     * This constructor is used to create a new ErrorCode with an errorCode and an errorMessage.
     *
     * @param errorCode    the errorCode of the ErrorCode
     * @param errorMessage the errorMessage of the ErrorCode
     */
    ErrorCodes(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * This method returns the error code of the error code.
     *
     * @return the error code of the error code
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * This method returns the error message of the error code.
     *
     * @return the error message of the error code
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}
