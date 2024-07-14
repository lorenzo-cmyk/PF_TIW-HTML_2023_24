package it.polimi.tiw.backend.beans;

import it.polimi.tiw.backend.exceptions.InvalidArgumentException;

import static it.polimi.tiw.backend.utilities.Validators.isEmailValid;
import static it.polimi.tiw.backend.utilities.Validators.isStringValid;

/**
 * This class represent a user of our DMS. It is used for communication with the DAOs classes.
 * This class cannot be a record because it needs to throw an exception in the constructor.
 */
public class User {
    private final int userID;
    private final String username;
    private final String passwordHash;
    private final String eMail;

    /**
     * This constructor is used to create a new user. It checks that all the fields are not null.
     *
     * @param userID       the ID of the user
     * @param username     the username of the user
     * @param passwordHash the password hash of the user
     * @param eMail        the email of the user
     * @throws InvalidArgumentException if any of the fields is invalid
     */
    public User(int userID, String username, String passwordHash, String eMail) throws InvalidArgumentException {
        if (userID < 0 || !isStringValid(username) || !isStringValid(passwordHash) || !isStringValid(eMail)) {
            throw new InvalidArgumentException("The arguments provided are not valid.");
        } else if (username.length() > 64 || passwordHash.length() > 128 || eMail.length() > 64) {
            throw new InvalidArgumentException("The arguments provided exceed the maximum length.");
        } else if (!isEmailValid(eMail)) {
            throw new InvalidArgumentException("The email provided is not syntactically valid.");
        }

        this.userID = userID;
        this.username = username;
        this.passwordHash = passwordHash;
        this.eMail = eMail;
    }

    /**
     * This method returns the ID of the user.
     *
     * @return the ID of the user
     */
    public int getUserID() {
        return userID;
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
