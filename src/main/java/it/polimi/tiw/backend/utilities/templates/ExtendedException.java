package it.polimi.tiw.backend.utilities.templates;

/**
 * This class is the superclass of all the exceptions that can be thrown in the application.
 */
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
