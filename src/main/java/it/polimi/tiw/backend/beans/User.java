package it.polimi.tiw.backend.beans;

import it.polimi.tiw.backend.beans.exceptions.InvalidArgumentException;
import it.polimi.tiw.backend.beans.exceptions.InvalidEmailException;
import it.polimi.tiw.backend.beans.exceptions.TooLongArgumentException;

import static it.polimi.tiw.backend.utilities.Validators.isEmailValid;
import static it.polimi.tiw.backend.utilities.Validators.isStringValid;

/**
 * This class represent a user of our DMS. It is used for communication with the DAOs classes.
 * This class cannot be a record because it needs to throw an exception in the constructor.
 */
public class User {
    private final String username;
    private final String passwordHash;
    private final String eMail;

    /**
     * This constructor is used to create a new user. It checks that all the fields are not null.
     *
     * @param username     the username of the user
     * @param passwordHash the password hash of the user
     * @param eMail        the email of the user
     * @throws InvalidArgumentException if any of the fields is invalid
     * @see it.polimi.tiw.backend.beans.exceptions
     */
    public User(String username, String passwordHash, String eMail) throws InvalidArgumentException {
        if (!isStringValid(username) || !isStringValid(passwordHash) || !isStringValid(eMail)) {
            throw new InvalidArgumentException("Some of the arguments provided are null or empty." +
                    " Please check your input and try again.");
        } else if (username.length() > 64 || passwordHash.length() > 128 || eMail.length() > 64) {
            throw new TooLongArgumentException("Some of the arguments provided are too long." +
                    " Please check your input and try again.");
        } else if (!isEmailValid(eMail)) {
            throw new InvalidEmailException("The email provided is not syntactically valid." +
                    " Please check your input and try again.");
        }

        this.username = username;
        this.passwordHash = passwordHash;
        this.eMail = eMail;
    }

    /**
     * This method returns the username of the user.
     *
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * This method returns the password hash of the user.
     *
     * @return the password hash of the user
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * This method returns the email of the user.
     *
     * @return the email of the user
     */
    public String getEmail() {
        return eMail;
    }
}
