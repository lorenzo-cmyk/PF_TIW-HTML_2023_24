package it.polimi.tiw.backend.dao;

import it.polimi.tiw.backend.beans.Document;
import it.polimi.tiw.backend.beans.Folder;
import it.polimi.tiw.backend.beans.exceptions.InvalidArgumentException;
import it.polimi.tiw.backend.dao.exceptions.DocumentCreationException;
import it.polimi.tiw.backend.dao.exceptions.DocumentDeletionException;
import it.polimi.tiw.backend.dao.exceptions.DocumentMovingException;
import it.polimi.tiw.backend.dao.exceptions.DuplicateDocumentException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for managing documents.
 * This class provides methods to interact with the database and perform operations on documents.
 */
public class DocumentDAO {
    private final Connection connection;

    /**
     * Constructor for the FolderDAO class.
     *
     * @param connection a Connection object, which represents the connection to the database.
     */
    public DocumentDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * This method retrieves a document by looking for its ID.
     *
     * @param documentID the ID of the document to be retrieved.
     * @param ownerID    the ID of the user who owns the document.
     * @return a Document object representing the document with the given ID (null if not found).
     * @throws SQLException if an error occurs while retrieving the document from the database (SQL-Related).
     */
    public Document getDocumentByID(int documentID, int ownerID) throws SQLException {
        // The raw SQL query for retrieving a document by its ID.
        String findDocumentByIDQuery = "SELECT * FROM Documents WHERE DocumentID = ? AND OwnerID = ?";

        // Try-with-resources statement used to automatically
        // close the PreparedStatement when it is no longer needed.
        try (PreparedStatement preparedStatement = connection.prepareStatement(findDocumentByIDQuery)) {
            // Set the parameters of the query.
            preparedStatement.setInt(1, documentID);
            preparedStatement.setInt(2, ownerID);

            // Execute the now parameterized query.
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // If the query returns no results, we can just return null
                if (!resultSet.next()) {
                    return null;
                }

                // If the query returns a result, the document is found.
                // We create a new Document object with the data from the database.
                Document foundDocument;
                try {
                    foundDocument = new Document(
                            resultSet.getInt("DocumentID"),
                            resultSet.getString("DocumentName"),
                            resultSet.getDate("CreationDate"),
                            resultSet.getString("Type"),
                            resultSet.getString("Summary"),
                            resultSet.getInt("OwnerID"),
                            resultSet.getInt("FolderID")
                    );
                } catch (InvalidArgumentException e) {
                    throw new IllegalStateException("The database is corrupted." +
                            " The retrieved data is invalid. Please check the database integrity.");
                }

                // If the query returns more than one result, the database is corrupted.
                if (resultSet.next()) {
                    throw new IllegalStateException("The database is corrupted." +
                            " More than one user with the same credentials.");
                }

                return foundDocument;
            }
        }
    }

    /**
     * This method retrieves all the documents in a given folder.
     *
     * @param folderID the ID of the folder whose documents are to be retrieved.
     * @param ownerID  the ID of the user who owns the documents.
     * @return a List of Document objects, which represent the documents of the given folder.
     * @throws SQLException if an error occurs while retrieving the documents from the database (SQL-Related).
     */
    public List<Document> getDocumentsByFolder(int folderID, int ownerID) throws SQLException {
        // The raw SQL query for retrieving the documents of a folder.
        String findDocumentsInFolderQuery;
        if (folderID != -1) {
            findDocumentsInFolderQuery = "SELECT * FROM Documents WHERE OwnerID = ? AND FolderID = ?";
        } else {
            findDocumentsInFolderQuery = "SELECT * FROM Documents WHERE OwnerID = ? AND FolderID IS NULL";
        }
        List<Document> documents = new ArrayList<>();

        // Try-with-resources statement used to automatically
        // close the PreparedStatement when it is no longer needed.
        try (PreparedStatement preparedStatement = connection.prepareStatement(findDocumentsInFolderQuery)) {
            // Set the parameters of the query.
            // If the folderID is -1, we set the parameter to null.
            preparedStatement.setInt(1, ownerID);
            if (folderID != -1) {
                preparedStatement.setInt(2, folderID);
            }

            // Execute the now parameterized query.
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Create a new Document object for each row in the ResultSet.
                while (resultSet.next()) {
                    try {
                        Document document = new Document(
                                resultSet.getInt("DocumentID"),
                                resultSet.getString("DocumentName"),
                                resultSet.getDate("CreationDate"),
                                resultSet.getString("Type"),
                                resultSet.getString("Summary"),
                                resultSet.getInt("OwnerID"),
                                resultSet.getInt("FolderID")
                        );
                        documents.add(document);
                    } catch (InvalidArgumentException e) {
                        throw new IllegalStateException("The database is corrupted." +
                                " The retrieved data is invalid. Please check the database integrity.");
                    }
                }

                return documents;
            }
        }
    }

    /**
     * This method creates a new document in the database.
     *
     * @param newDocument the Document object representing the document to be created.
     * @throws SQLException               if an error occurs while creating the document in the database (SQL-Related).
     * @throws DocumentCreationException  if an error occurs while creating the document (non SQL-Related).
     * @throws DuplicateDocumentException if a document with the same name already exists in the parent directory.
     */
    @SuppressWarnings("ExtractMethodRecommender")
    public void createDocument(Document newDocument)
            throws SQLException, DocumentCreationException, DuplicateDocumentException {
        // The raw SQL query for creating a new document.
        String createDocumentQuery;
        if (newDocument.getFolderID() != -1) {
            createDocumentQuery = "INSERT INTO Documents (DocumentName, CreationDate, Type, Summary, OwnerID, FolderID)"
                    + " VALUES (?, NOW(), ?, ?, ?, ?)";
        } else {
            createDocumentQuery = "INSERT INTO Documents (DocumentName, CreationDate, Type, Summary, OwnerID, FolderID)"
                    + " VALUES (?, NOW(), ?, ?, ?, NULL)";
        }

        try {
            // First, retrieve the documents into the parent folder.
            // If a document with the same name already exists, we throw a DuplicateDocumentException as
            // two documents with the same name cannot exist in the same directory.
            List<Document> documents = getDocumentsByFolder(newDocument.getFolderID(), newDocument.getOwnerID());
            for (Document document : documents) {
                if (document.getDocumentName().equals(newDocument.getDocumentName())) {
                    throw new DuplicateDocumentException();
                }
            }

            // Prevent the user from creating a document inside a folder that it does not own.
            FolderDAO folderDAO = new FolderDAO(connection);
            Folder parentFolder = folderDAO.getFolderByID(newDocument.getFolderID(), newDocument.getOwnerID());
            if (parentFolder == null && newDocument.getFolderID() != -1) {
                throw new SecurityException("The user is attempting to create a document " +
                        "inside a directory that it does not own. Security violation detected.");
            }

            // Since we are writing to the database, we need to use a transaction
            // in order to ensure that the operation is atomic.
            connection.setAutoCommit(false);

            // Try-with-resources statement used to automatically
            // close the PreparedStatement when it is no longer needed.
            try (PreparedStatement preparedStatement = connection.prepareStatement(createDocumentQuery)) {
                // Set the parameters of the query.
                preparedStatement.setString(1, newDocument.getDocumentName());
                preparedStatement.setString(2, newDocument.getType());
                preparedStatement.setString(3, newDocument.getSummary());
                preparedStatement.setInt(4, newDocument.getOwnerID());
                if (newDocument.getFolderID() != -1) {
                    preparedStatement.setInt(5, newDocument.getFolderID());
                }

                // Execute the now parameterized query.
                preparedStatement.executeUpdate();

                // The document was successfully created, so we can safely commit the transaction.
                connection.commit();
            }

        } catch (SQLException e) {
            // If an error occurs during the creation process, we need to roll back the transaction.
            connection.rollback();

            // If the error is due to a violation of the foreign key constraint, we throw a DocumentCreationException.
            // This means that the folderID and/or the ownerID are not valid.
            if (e.getErrorCode() == 1452) {
                throw new DocumentCreationException();
            } else {
                // If the error is due to another reason, we re-throw the exception.
                throw e;
            }
        } finally {
            // Restore the default behavior of the connection.
            connection.setAutoCommit(true);
        }
    }

    /**
     * This method is used to delete a document from the database.
     *
     * @param documentID the ID of the document to be deleted.
     * @param ownerID    the ID of the user who owns the document.
     * @throws SQLException              if an error occurs while deleting the document from the database (SQL-Related).
     * @throws DocumentDeletionException if an error occurs while deleting the document (non SQL-Related).
     */
    public void deleteDocument(int documentID, int ownerID) throws SQLException, DocumentDeletionException {
        // The raw SQL query for deleting a document.
        String deleteDocumentQuery = "DELETE FROM Documents WHERE DocumentID = ? AND OwnerID = ?";

        try {
            // Since we are writing to the database, we need to use a transaction
            // in order to ensure that the operation is atomic.
            connection.setAutoCommit(false);

            // Try-with-resources statement used to automatically
            // close the PreparedStatement when it is no longer needed.
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteDocumentQuery)) {
                // Set the parameters of the query.
                preparedStatement.setInt(1, documentID);
                preparedStatement.setInt(2, ownerID);

                // Execute the now parameterized query.
                preparedStatement.executeUpdate();

                // The document was successfully deleted, so we can safely commit the transaction.
                connection.commit();
            }

        } catch (SQLException e) {
            // If an error occurs during the registration process, we need to roll back the transaction.
            connection.rollback();

            // If the error is due to a violation of the foreign key constraint, we throw a DocumentDeletionException.
            // This means that the documentID and/or ownerID are not valid.
            if (e.getErrorCode() == 1451) {
                throw new DocumentDeletionException();
            } else {
                // If the error is due to another reason, we re-throw the exception.
                throw e;
            }
        } finally {
            // Restore the default behavior of the connection.
            connection.setAutoCommit(true);
        }
    }

    /**
     * This method is used to move a document to a different folder.
     *
     * @param documentID the ID of the document to be moved.
     * @param ownerID    the ID of the user who owns the document.
     * @param folderID   the ID of the folder to which the document is to be moved.
     * @throws SQLException            if an error occurs while moving the document in the database (SQL-Related).
     * @throws DocumentMovingException if an error occurs while moving the document (non SQL-Related).
     */
    public void moveDocument(int documentID, int ownerID, int folderID)
            throws SQLException, DocumentMovingException, DuplicateDocumentException {
        // The raw SQL query for moving a document.
        String moveDocumentQuery;
        if (folderID != -1) {
            moveDocumentQuery = "UPDATE Documents SET FolderID = ? WHERE DocumentID = ? AND OwnerID = ?";
        } else {
            moveDocumentQuery = "UPDATE Documents SET FolderID = NULL WHERE DocumentID = ? AND OwnerID = ?";
        }

        try {
            Document document = getDocumentByID(documentID, ownerID);
            // Check that the document we want to move exists and is owned by the user.
            if (document == null) {
                throw new DocumentMovingException();
            }
            // Check that the user is not attempting to move the document where it already is.
            if (document.getFolderID() == folderID) {
                return;
            }
            // Check that the user is not attempting to move the document to a folder that it does not own.
            FolderDAO folderDAO = new FolderDAO(connection);
            Folder newFolder = folderDAO.getFolderByID(folderID, ownerID);
            if (newFolder == null && folderID != -1) {
                throw new SecurityException("The user is attempting to move a document " +
                        "to a directory that it does not own. Security violation detected.");
            }

            // Retrieve the documents into the new folder.
            // If a document with the same name already exists, we throw a DocumentMovingException as
            // two documents with the same name cannot exist in the same directory.
            List<Document> documents = getDocumentsByFolder(folderID, ownerID);
            for (Document innerDocument : documents) {
                if (innerDocument.getDocumentName().equals(document.getDocumentName())) {
                    throw new DuplicateDocumentException();
                }
            }

            // Since we are writing to the database, we need to use a transaction
            // in order to ensure that the operation is atomic.
            connection.setAutoCommit(false);

            // Try-with-resources statement used to automatically
            // close the PreparedStatement when it is no longer needed.
            try (PreparedStatement preparedStatement = connection.prepareStatement(moveDocumentQuery)) {
                // Set the parameters of the query.
                if (folderID != -1) {
                    preparedStatement.setInt(1, folderID);
                    preparedStatement.setInt(2, documentID);
                    preparedStatement.setInt(3, ownerID);
                } else {
                    preparedStatement.setInt(1, documentID);
                    preparedStatement.setInt(2, ownerID);
                }

                // Execute the now parameterized query.
                preparedStatement.executeUpdate();

                // The document was successfully moved, so we can safely commit the transaction.
                connection.commit();
            }

        } catch (SQLException e) {
            // If an error occurs during the registration process, we need to roll back the transaction.
            connection.rollback();

            // If the error is due to a violation of the foreign key constraint, we throw a DocumentMovingException.
            // This means that the documentID and/or ownerID are not valid.
            if (e.getErrorCode() == 1452) {
                throw new DocumentMovingException();
            } else {
                // If the error is due to another reason, we re-throw the exception.
                throw e;
            }
        } finally {
            // Restore the default behavior of the connection.
            connection.setAutoCommit(true);
        }
    }
}
