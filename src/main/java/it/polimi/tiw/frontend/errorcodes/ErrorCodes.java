package it.polimi.tiw.frontend.errorcodes;

/**
 * This enum contains all the error codes that can be used in the application by the frontend.
 */
@SuppressWarnings("SameParameterValue")
public enum ErrorCodes {
    /**
     * This error code is used when the arguments provided are not valid.
     */
    PasswordMismatchException(2001, "The fields password and password confirmation do not match.");

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
