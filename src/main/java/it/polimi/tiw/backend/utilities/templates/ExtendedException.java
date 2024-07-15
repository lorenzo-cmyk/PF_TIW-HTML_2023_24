package it.polimi.tiw.backend.utilities.templates;

public abstract class ExtendedException extends Exception {
    /**
     * This constructor is used to create a new exception with a message.
     *
     * @param message the message of the exception
     */
    public ExtendedException(String message) {
        super(message);
    }

    /**
     * This method returns the errorCode of the exception.
     *
     * @return the errorCode of the exception
     */
    public abstract int getErrorCode();
}
