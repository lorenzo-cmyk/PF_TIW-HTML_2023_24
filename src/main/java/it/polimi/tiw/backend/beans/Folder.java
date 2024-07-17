package it.polimi.tiw.backend.beans;

import it.polimi.tiw.backend.beans.exceptions.InvalidArgumentException;

import java.util.Date;

import static it.polimi.tiw.backend.utilities.Validators.*;

/**
 * This class represent a folder of our DMS. It is used for communication with the DAOs classes.
 */
public class Folder {
    private final int folderID;
    private final String folderName;
    private final Date creationDate;
    private final int ownerID;
    private final int parentFolderID;

    /**
     * This constructor is used to create a new folder. It checks that all the fields provided are valid.
     *
     * @param folderID       the ID of the folder
     * @param folderName     the name of the folder
     * @param creationDate   the creation date of the folder
     * @param ownerID        the ID of the owner of the folder
     * @param parentFolderID the ID of the parent folder
     * @throws InvalidArgumentException if any of the fields is invalid
     */
    public Folder(int folderID, String folderName, Date creationDate, int ownerID, int parentFolderID)
            throws InvalidArgumentException {
        if (!isIDValid(folderID) || !isStringValid(folderName) || !isDateValid(creationDate) ||
                !isIDValid(ownerID) || !isIDValid(parentFolderID)) {
            throw new InvalidArgumentException();
        }

        this.folderID = folderID;
        this.folderName = folderName;
        this.creationDate = creationDate;
        this.ownerID = ownerID;
        this.parentFolderID = parentFolderID;
    }

    /**
     * This constructor is used to create a new folder. It checks that all the fields provided are valid.
     *
     * @param folderName     the name of the folder
     * @param ownerID        the ID of the owner of the folder
     * @param parentFolderID the ID of the parent folder
     * @throws InvalidArgumentException if any of the fields is invalid
     */
    public Folder(String folderName, int ownerID, int parentFolderID)
            throws InvalidArgumentException {
        if (!isStringValid(folderName) || !isIDValid(ownerID) || !isIDValid(parentFolderID)) {
            throw new InvalidArgumentException();
        }

        this.folderID = -1;
        this.folderName = folderName;
        this.creationDate = null;
        this.ownerID = ownerID;
        this.parentFolderID = parentFolderID;
    }

    /**
     * This method returns the ID of the folder.
     *
     * @return the ID of the folder
     */
    public int getFolderID() {
        return folderID;
    }

    /**
     * This method returns the name of the folder.
     *
     * @return the name of the folder
     */
    public String getFolderName() {
        return folderName;
    }

    /**
     * This method returns the creation date of the folder.
     *
     * @return the creation date of the folder
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * This method returns the ID of the owner of the folder.
     *
     * @return the ID of the owner of the folder
     */
    public int getOwnerID() {
        return ownerID;
    }

    /**
     * This method returns the ID of the parent folder.
     *
     * @return the ID of the parent folder
     */
    public int getParentFolderID() {
        return parentFolderID;
    }
}
