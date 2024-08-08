package it.polimi.tiw.frontend.filters;

/**
 * Enum representing valid URLs for the application, detailing their authentication requirement
 */
public enum ValidURLsEnumeration {

    /**
     * Servlet for user authentication, no authentication required.
     */
    UserAuthenticationServlet("/login", false),
    /**
     * Servlet for user registration, no authentication required.
     */
    UserRegistrationServlet("/register", false),
    /**
     * Servlet for user disconnection, authentication required.
     */
    UserDisconnectionServlet("/logout", true),
    /**
     * Servlet for the homepage, authentication required.
     */
    HomepageServlet("/home", true),
    /**
     * Servlet for content management, authentication required.
     */
    ContentManagementServlet("/create", true),
    /**
     * Servlet for creating a document, authentication required.
     */
    CreateDocumentServlet("/create/create-document", true),
    /**
     * Servlet for creating a folder, authentication required.
     */
    CreateFolderServlet("/create/create-folder", true),
    /**
     * Servlet for moving a document, authentication required.
     */
    MoveDocumentServlet("/move/move-document", true),
    /**
     * Servlet for viewing folder content, authentication required.
     */
    ViewFolderContentServlet("/folder", true),
    /**
     * Servlet for viewing document details, authentication required.
     */
    ViewDocumentDetailsServlet("/document", true);

    private final String URL;
    private final boolean requiresAuthentication;

    /**
     * Constructor for enum constants.
     *
     * @param URL                    The URL path associated with the servlet.
     * @param requiresAuthentication Indicates if the servlet requires user authentication.
     */
    ValidURLsEnumeration(String URL, boolean requiresAuthentication) {
        this.URL = URL;
        this.requiresAuthentication = requiresAuthentication;
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

}