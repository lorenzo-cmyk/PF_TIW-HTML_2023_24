package it.polimi.tiw.frontend.controllers.homepage;

/**
 * Defines the enumeration for homepage actions with associated action codes.
 * This enumeration is used to represent different actions that can be performed
 * from the homepage, such as creating a document, creating a folder, or moving a document.
 * Each action is associated with a unique action code.
 */
public enum HomepageActionEnumeration {
    /**
     * Action for choosing a folder and creating a document within it.
     */
    CHOOSE_FOLDER_CREATE_DOCUMENT(1),
    /**
     * Action for choosing a folder and creating a new folder within it.
     */
    CHOOSE_FOLDER_CREATE_FOLDER(2),
    /**
     * Action for choosing a folder and moving a document to it.
     */
    CHOOSE_FOLDER_MOVE_DOCUMENT(3);

    // The unique action code associated with the homepage action.
    private final int actionCode;

    /**
     * Constructor for the enumeration, associating an action with its code.
     *
     * @param actionCode The unique code representing the action.
     */
    HomepageActionEnumeration(int actionCode) {
        this.actionCode = actionCode;
    }

    /**
     * Retrieves the action code associated with the homepage action.
     *
     * @return The action code.
     */
    public int getActionCode() {
        return actionCode;
    }

}
