package it.polimi.tiw.frontend.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;

/**
 * A filter that intercepts all requests to the application to ensure that
 * only authenticated users can access certain URLs. It redirects unauthenticated
 * users to the login page and prevents authenticated users from accessing
 * URLs not permitted for their authentication level.
 */
@WebFilter(urlPatterns = {"/*"})
public class FilterBadRequests implements Filter {

    /**
     * Filters requests based on user authentication and URL validity.
     *
     * @param servletRequest  The request to be processed.
     * @param servletResponse The response to be created.
     * @param filterChain     The filter chain for invoking the next filter or the resource.
     * @throws ServletException if an exception occurs that interferes with the filter's normal operation.
     * @throws IOException      if an I/O related error has occurred during the processing.
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        // Let the browser access to the static resources files without any check.
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        if (path.startsWith("/resources")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (isUserAuthenticated(httpRequest)) {
            // User authenticated: I need to check if the URL is valid for the user's role (e.g. /home, /create, ...)
            // If it is, I let the request pass, otherwise I redirect to the Homepage.
            if (!isURLAuthenticated(httpRequest)) {
                // isURLAuthenticated returns false if the URL is not valid for the user's status (e.g. /login &
                // /register) but also if the URL is not valid at all (e.g. /invalidURL). An authenticated user that
                // makes an invalid request is redirected to the homepage in both cases.

                // The conditions could be rewritten as: !isURLAuthenticated(httpRequest) || !isURLValid(httpRequest)
                // but the second check is redundant because the first one already includes it.

                httpResponse.sendRedirect(httpRequest.getContextPath() + "/home");
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } else {
            // User not authenticated: I need to check if the URL is valid and accessible without authentication
            // (e.g. /login & /register). If it is, I let the request pass, otherwise I redirect to Login page.
            if (isURLAuthenticated(httpRequest) || !isURLValid(httpRequest)) {
                // isURLAuthenticated returns true if the URL requires authentication. -> Go to the Login page.
                // isURLValid returns false if the URL is not valid at all. -> Go to the Login page.

                // The conditions cannot be simplified because the first one is a logical OR and the second one is a
                // logical AND: if I removed the second one, I would let pass invalid URLs.

                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }
    }

    /**
     * Checks if the user is authenticated by looking for a specific attribute in the session.
     *
     * @param httpServletRequest The request to check for authentication.
     * @return true if the user is authenticated, false otherwise.
     */
    private boolean isUserAuthenticated(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        return !httpSession.isNew() && httpSession.getAttribute("user") != null;
    }

    /**
     * Determines if the requested URL is valid based on a predefined list of valid URLs.
     *
     * @param httpServletRequest The request to check for URL validity.
     * @return true if the URL is valid, false otherwise.
     */
    private boolean isURLValid(HttpServletRequest httpServletRequest) {
        String requestURI = httpServletRequest.getRequestURI();
        String requestPath = requestURI.substring(httpServletRequest.getContextPath().length());
        return Arrays.stream(ValidURLsEnumeration.values()).anyMatch(validURL -> validURL.getURL().equals(requestPath));
    }

    /**
     * Checks if the requested URL requires authentication.
     *
     * @param httpServletRequest The request to check.
     * @return true if the URL requires authentication, false otherwise.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isURLAuthenticated(HttpServletRequest httpServletRequest) {
        String requestURI = httpServletRequest.getRequestURI();
        String requestPath = requestURI.substring(httpServletRequest.getContextPath().length());
        return Arrays.stream(ValidURLsEnumeration.values())
                .anyMatch(validURL -> validURL.getURL().equals(requestPath) && validURL.isRequiresAuthentication());
    }
}