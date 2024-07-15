package it.polimi.tiw.backend.errorcodes;

/**
 * This enum contains all the error codes that can be used in the application by the backend.
 */
public enum ErrorCodes {
    /**
     * This error code is used when the arguments provided are not valid.
     */
    InvalidArgumentException(1001, "The arguments provided are not valid."),
    /**
     * This error code is used when the arguments provided exceed the maximum length.
     */
    TooLongArgumentException(1002, "The arguments provided exceed the maximum length."),
    /**
     * This error code is used when the email provided is not syntactically valid.
     */
    InvalidEmailException(1003, "The email provided is not syntactically valid."),
    /**
     * This error code is used when the registration of a user fails.
     */
    RegistrationException(1004, "Unable to register user." +
            " Check if the interested username is already taken.");

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
