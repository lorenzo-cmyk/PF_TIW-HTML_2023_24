package it.polimi.tiw.frontend.filters;

/**
 * Enum representing valid URLs for the application, detailing their authentication
 * requirement and history override capability.
 */
public enum ValidURLsEnumeration {

    /**
     * Servlet for user authentication, no authentication required, does not override history.
     */
    UserAuthenticationServlet("/login", false, false),
    /**
     * Servlet for user registration, no authentication required, does not override history.
     */
    UserRegistrationServlet("/register", false, false),
    /**
     * Servlet for user disconnection, authentication required, does not override history.
     */
    UserDisconnectionServlet("/logout", true, false),
    /**
     * Servlet for the homepage, authentication required, overrides history.
     */
    HomepageServlet("/home", true, true),
    /**
     * Servlet for content management, authentication required, overrides history.
     */
    ContentManagementServlet("/create", true, true),
    /**
     * Servlet for creating a document, authentication required, does not override history.
     */
    CreateDocumentServlet("/create/create-document", true, false),
    /**
     * Servlet for creating a folder, authentication required, does not override history.
     */
    CreateFolderServlet("/create/create-folder", true, false),
    /**
     * Servlet for moving a document, authentication required, does not override history.
     */
    MoveDocumentServlet("/move/move-document", true, false),
    /**
     * Servlet for viewing folder content, authentication required, overrides history.
     */
    ViewFolderContentServlet("/folder", true, true),
    /**
     * Servlet for viewing document details, authentication required, overrides history.
     */
    ViewDocumentDetailsServlet("/document", true, true);

    private final String URL;
    private final boolean requiresAuthentication;
    private final boolean overrideHistory;

    /**
     * Constructor for enum constants.
     *
     * @param URL                    The URL path associated with the servlet.
     * @param requiresAuthentication Indicates if the servlet requires user authentication.
     * @param overrideHistory        Indicates if navigating to the servlet should override browser history.
     */
    ValidURLsEnumeration(String URL, boolean requiresAuthentication, boolean overrideHistory) {
        this.URL = URL;
        this.requiresAuthentication = requiresAuthentication;
        this.overrideHistory = overrideHistory;
    }

    /**
     * Gets the URL path associated with the servlet.
     *
     * @return The URL path.
     */
    public String getURL() {
        return URL;
    }

    /**
     * Checks if the servlet requires user authentication.
     *
     * @return True if authentication is required, false otherwise.
     */
    public boolean isRequiresAuthentication() {
        return requiresAuthentication;
    }

    /**
     * Checks if navigating to the servlet should override browser history.
     *
     * @return True if it should override, false otherwise.
     */
    public boolean isOverrideHistory() {
        return overrideHistory;
    }
}