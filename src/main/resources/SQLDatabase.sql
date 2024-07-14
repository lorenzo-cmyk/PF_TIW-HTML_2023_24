-- DMS Database Schema
-- This SQL script is designed to create the necessary database and tables for the Document Management System (DMS).
-- It includes the creation of three main tables: Users, Folders, and Documents, along with their relationships.

-- Create the database DMS
CREATE DATABASE IF NOT EXISTS DMS;

-- Use the database DMS
USE DMS;

-- Create the table "Users"
-- This table stores user information including a unique UserID, Username, PasswordHash, and Email.
CREATE TABLE IF NOT EXISTS Users
(
    UserID       INT AUTO_INCREMENT PRIMARY KEY, -- Unique identifier for each user
    Username     VARCHAR(64) UNIQUE NOT NULL,    -- User's chosen username, must be unique
    PasswordHash VARCHAR(128)       NOT NULL,    -- Hash of the user's password for security
    Email        VARCHAR(64) UNIQUE NOT NULL     -- User's email address, must be unique
);

-- Create the table "Folders"
-- This table represents folders in the system.
-- Each folder has an ID, name, creation date, and may have a parent folder.
CREATE TABLE IF NOT EXISTS Folders
(
    FolderID       INT AUTO_INCREMENT PRIMARY KEY, -- Unique identifier for each folder
    FolderName     VARCHAR(64) NOT NULL,           -- Name of the folder
    CreationDate   DATETIME    NOT NULL,           -- Date and time the folder was created
    OwnerID        INT         NOT NULL,           -- UserID of the folder's owner
    ParentFolderID INT,                            -- FolderID of the parent folder, if any
    FOREIGN KEY (OwnerID) REFERENCES Users (UserID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (ParentFolderID) REFERENCES Folders (FolderID) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create the table "Documents"
-- This table stores documents.
-- Each document has an ID, name, creation date, type, optional summary, owner, and containing folder.
CREATE TABLE IF NOT EXISTS Documents
(
    DocumentID   INT AUTO_INCREMENT PRIMARY KEY, -- Unique identifier for each document
    DocumentName VARCHAR(64) NOT NULL,           -- Name of the document
    CreationDate DATETIME    NOT NULL,           -- Date and time the document was created
    Type         VARCHAR(64) NOT NULL,           -- Type of the document (e.g., PDF, TXT)
    Summary      TEXT,                           -- Optional summary of the document's content
    OwnerID      INT         NOT NULL,           -- UserID of the document's owner
    FolderID     INT,                            -- FolderID of the folder containing the document
    FOREIGN KEY (OwnerID) REFERENCES Users (UserID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (FolderID) REFERENCES Folders (FolderID) ON DELETE CASCADE ON UPDATE CASCADE
);
