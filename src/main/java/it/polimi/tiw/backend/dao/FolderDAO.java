package it.polimi.tiw.backend.dao;

import it.polimi.tiw.backend.beans.Folder;
import it.polimi.tiw.backend.beans.exceptions.InvalidArgumentException;
import it.polimi.tiw.backend.dao.exceptions.FolderCreationException;
import it.polimi.tiw.backend.dao.exceptions.FolderDeletionException;
import it.polimi.tiw.backend.utilities.templates.TreeNode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for managing folders.
 * This class provides methods to interact with the database and perform operations on folders.
 */
public class FolderDAO {
    private final Connection connection;

    /**
     * Constructor for the FolderDAO class.
     *
     * @param connection a Connection object, which represents the connection to the database.
     */
    public FolderDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * This method retrieves all the subfolders of a given folder.
     *
     * @param folderID the ID of the folder whose subfolders are to be retrieved.
     * @param ownerID  the ID of the user who owns the folder.
     * @return a List of Folder objects, which represent the subfolders of the given folder.
     * @throws SQLException if an error occurs while retrieving the subfolders from the database (SQL-Related).
     */
    public List<Folder> getSubfolders(int folderID, int ownerID) throws SQLException {
        // The raw SQL query for retrieving the subfolders of a folder.
        String findSubfoldersQuery;
        if (folderID != -1) {
            findSubfoldersQuery = "SELECT * FROM Folders WHERE OwnerID = ? AND ParentFolderID = ?";
        } else {
            findSubfoldersQuery = "SELECT * FROM Folders WHERE OwnerID = ? AND ParentFolderID IS NULL";
        }
        List<Folder> subfolders = new ArrayList<>();

        // Try-with-resources statement used to automatically
        // close the PreparedStatement when it is no longer needed.
        try (PreparedStatement preparedStatement = connection.prepareStatement(findSubfoldersQuery)) {
            // Set the parameters of the query.
            // If the folderID is -1, we set the parameter to null.
            preparedStatement.setInt(1, ownerID);
            if (folderID != -1) {
                preparedStatement.setInt(2, folderID);
            }

            // Execute the now parameterized query.
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Create a new Folder object for each row in the ResultSet.
                while (resultSet.next()) {
                    Folder subfolder = new Folder(
                            resultSet.getInt("FolderID"),
                            resultSet.getString("FolderName"),
                            resultSet.getDate("CreationDate"),
                            resultSet.getInt("OwnerID"),
                            // If the parentFolderID is null, we set it to -1.
                            resultSet.getObject("ParentFolderID") == null ? -1
                                    : resultSet.getInt("ParentFolderID")
                    );
                    subfolders.add(subfolder);
                }
            } catch (InvalidArgumentException ignored) {
                throw new IllegalStateException("Invalid data fetched from the database." +
                        " The database is in an inconsistent state.");
            }
        }
        return subfolders;
    }

    /**
     * This method creates a new folder in the database.
     *
     * @param newFolder the Folder object representing the folder to be created.
     * @throws SQLException            if an error occurs while creating the folder in the database (SQL-Related).
     * @throws FolderCreationException if an error occurs while creating the folder (non SQL-Related).
     */
    public void createFolder(Folder newFolder) throws SQLException, FolderCreationException {
        // The raw SQL query for creating a new folder.
        String createFolderQuery = "INSERT INTO Folders (FolderName, CreationDate, OwnerID, ParentFolderID)" +
                " VALUES (?, NOW(), ?, ?)";

        try {
            // Since we are writing to the database, we need to use a transaction
            // in order to ensure that the operation is atomic.
            connection.setAutoCommit(false);

            // Try-with-resources statement used to automatically
            // close the PreparedStatement when it is no longer needed.
            try (PreparedStatement preparedStatement = connection.prepareStatement(createFolderQuery)) {
                // Set the parameters of the query.
                preparedStatement.setString(1, newFolder.getFolderName());
                preparedStatement.setInt(2, newFolder.getOwnerID());
                preparedStatement.setInt(3, newFolder.getParentFolderID());

                // Execute the now parameterized query.
                preparedStatement.executeUpdate();

                // The folder was successfully created, so we can safely commit the transaction.
                connection.commit();
            }

        } catch (SQLException e) {
            // If an error occurs during the registration process, we need to roll back the transaction.
            connection.rollback();

            // If the error is due to a violation of the foreign key constraint, we throw a FolderAccessException.
            // This means that the parentFolderID and/or the ownerID are not valid.
            if (e.getErrorCode() == 1452) {
                throw new FolderCreationException();
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
     * This method is used to delete a folder (and all its content) from the database.
     *
     * @param folderID the ID of the folder to be deleted.
     * @throws SQLException            if an error occurs while deleting the folder from the database (SQL-Related).
     * @throws FolderDeletionException if an error occurs while deleting the folder (non SQL-Related).
     */
    public void deleteFolder(int folderID) throws SQLException, FolderDeletionException {
        // The raw SQL query for deleting a folder.
        String deleteFolderQuery = "DELETE FROM Folders WHERE FolderID = ?";

        try {
            // Since we are writing to the database, we need to use a transaction
            // in order to ensure that the operation is atomic.
            connection.setAutoCommit(false);

            // Try-with-resources statement used to automatically
            // close the PreparedStatement when it is no longer needed.
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteFolderQuery)) {
                // Set the parameters of the query.
                preparedStatement.setInt(1, folderID);

                // Execute the now parameterized query.
                preparedStatement.executeUpdate();

                // The folder was successfully deleted, so we can safely commit the transaction.
                connection.commit();
            }

        } catch (SQLException e) {
            // If an error occurs during the registration process, we need to roll back the transaction.
            connection.rollback();

            // If the error is due to a violation of the foreign key constraint, we throw a FolderAccessException.
            // This means that the folderID is not valid.
            if (e.getErrorCode() == 1451) {
                throw new FolderDeletionException();
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
     * This method retrieves a folder by looking for its ID.
     *
     * @param folderID the ID of the folder to be retrieved.
     * @param ownerID  the ID of the user who owns the folder.
     * @return a Folder object representing the folder with the given ID.
     * @throws SQLException if an error occurs while retrieving the folder from the database (SQL-Related).
     */
    public Folder getFolderByID(int folderID, int ownerID) throws SQLException {
        // The raw SQL query for retrieving a folder by its ID.
        String findFolderByIDQuery = "SELECT * FROM Folders WHERE FolderID = ? AND OwnerID = ?";

        // Try-with-resources statement used to automatically
        // close the PreparedStatement when it is no longer needed.
        try (PreparedStatement preparedStatement = connection.prepareStatement(findFolderByIDQuery)) {
            // Set the parameters of the query.
            preparedStatement.setInt(1, folderID);
            preparedStatement.setInt(2, ownerID);

            // Execute the now parameterized query.
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // If the query returns no results, we can just return null
                if (!resultSet.next()) {
                    return null;
                }

                // If the query returns a result, the folder is found.
                // We create a new Folder object with the data from the database.
                Folder foundFolder;
                try {
                    foundFolder = new Folder(
                            resultSet.getInt("FolderID"),
                            resultSet.getString("FolderName"),
                            resultSet.getDate("CreationDate"),
                            resultSet.getInt("OwnerID"),
                            // If the parentFolderID is null, we set it to -1.
                            resultSet.getObject("ParentFolderID") == null ? -1
                                    : resultSet.getInt("ParentFolderID")
                    );
                } catch (InvalidArgumentException e) {
                    throw new IllegalStateException("The database is corrupted." +
                            " The user data is invalid. Please check the database integrity.");
                }

                // If the query returns more than one result, the database is corrupted.
                if (resultSet.next()) {
                    throw new IllegalStateException("The database is corrupted." +
                            " More than one user with the same credentials.");
                }

                return foundFolder;
            }
        }
    }

    /**
     * This method builds the tree structure of the folders owned by a user.
     *
     * @param rootFolderID the ID of the root folder of the tree.
     * @param ownerID      the ID of the user who owns the folders.
     * @return a TreeNode object representing the root of the tree.
     * @throws SQLException if an error occurs while building the tree (SQL-Related).
     */
    public TreeNode<Folder> buildFolderTree(int rootFolderID, int ownerID) throws SQLException {
        TreeNode<Folder> rootNode = new TreeNode<>(null);
        List<Folder> rootSubfolders = getSubfolders(rootFolderID, ownerID);

        for (Folder subfolder : rootSubfolders) {
            rootNode.addChild(buildTreeNode(subfolder.getFolderID(), ownerID));
        }

        return rootNode;
    }

    private TreeNode<Folder> buildTreeNode(int folderID, int ownerID) throws SQLException {
        Folder folder = getFolderByID(folderID, ownerID);
        TreeNode<Folder> node = new TreeNode<>(folder);
        List<Folder> subfolders = getSubfolders(folderID, ownerID);

        for (Folder subfolder : subfolders) {
            node.addChild(buildTreeNode(subfolder.getFolderID(), ownerID));
        }

        return node;
    }

}
