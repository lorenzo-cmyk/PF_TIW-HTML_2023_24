package it.polimi.tiw.backend.beans;

import it.polimi.tiw.backend.beans.exceptions.InvalidArgumentException;

import java.util.Date;

import static it.polimi.tiw.backend.utilities.Validators.*;

/**
 * This class represent a document of our DMS. It is used for communication with the DAOs classes.
 */
public class Document {
    private final int documentID;
    private final String documentName;
    private final Date creationDate;
    private final String type;
    private final String summary;
    private final int ownerID;
    private final int folderID;

    /**
     * This constructor is used to create a new document. It checks that all the fields provided are valid.
     *
     * @param documentID   the ID of the document
     * @param documentName the name of the document
     * @param creationDate the creation date of the document
     * @param type         the type of the document
     * @param summary      the summary of the document
     * @param ownerID      the ID of the owner of the document
     * @param folderID     the ID of the folder of the document
     * @throws InvalidArgumentException if any of the fields is invalid
     */
    public Document(int documentID, String documentName, Date creationDate, String type, String summary,
                    int ownerID, int folderID) throws InvalidArgumentException {
        if (!isIDValid(documentID) || !isStringValid(documentName) || !isDateValid(creationDate) ||
                !isStringValid(type) || !isStringValid(summary) || !isIDValid(ownerID) || !isIDValid(folderID)) {
            throw new InvalidArgumentException("Some of the arguments provided are invalid." +
                    " Please check your input and try again.");
        }

        this.documentID = documentID;
        this.documentName = documentName;
        this.creationDate = creationDate;
        this.type = type;
        this.summary = summary;
        this.ownerID = ownerID;
        this.folderID = folderID;
    }

    /**
     * This constructor is used to create a new document. It checks that all the fields provided are valid.
     *
     * @param documentName the name of the document
     * @param creationDate the creation date of the document
     * @param type         the type of the document
     * @param summary      the summary of the document
     * @param ownerID      the ID of the owner of the document
     * @param folderID     the ID of the folder of the document
     * @throws InvalidArgumentException if any of the fields is invalid
     */
    public Document(String documentName, Date creationDate, String type, String summary, int ownerID, int folderID)
            throws InvalidArgumentException {
        if (!isStringValid(documentName) || !isDateValid(creationDate) || !isStringValid(type) ||
                !isStringValid(summary) || !isIDValid(ownerID) || !isIDValid(folderID)) {
            throw new InvalidArgumentException("Some of the arguments provided are invalid." +
                    " Please check your input and try again.");
        }

        this.documentID = -1;
        this.documentName = documentName;
        this.creationDate = creationDate;
        this.type = type;
        this.summary = summary;
        this.ownerID = ownerID;
        this.folderID = folderID;
    }

    /**
     * This method returns the ID of the document.
     *
     * @return the ID of the document
     */
    public int getDocumentID() {
        return documentID;
    }

    /**
     * This method returns the name of the document.
     *
     * @return the name of the document
     */
    public String getDocumentName() {
        return documentName;
    }

    /**
     * This method returns the creation date of the document.
     *
     * @return the creation date of the document
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * This method returns the type of the document.
     *
     * @return the type of the document
     */
    public String getType() {
        return type;
    }

    /**
     * This method returns the summary of the document.
     *
     * @return the summary of the document
     */
    public String getSummary() {
        return summary;
    }

    /**
     * This method returns the ID of the owner of the document.
     *
     * @return the ID of the owner of the document
     */
    public int getOwnerID() {
        return ownerID;
    }

    /**
     * This method returns the ID of the folder of the document.
     *
     * @return the ID of the folder of the document
     */
    public int getFolderID() {
        return folderID;
    }
}
