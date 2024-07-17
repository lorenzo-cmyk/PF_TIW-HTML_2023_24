package it.polimi.tiw.backend.dao;


import it.polimi.tiw.backend.beans.Document;
import it.polimi.tiw.backend.beans.exceptions.InvalidArgumentException;

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
}
