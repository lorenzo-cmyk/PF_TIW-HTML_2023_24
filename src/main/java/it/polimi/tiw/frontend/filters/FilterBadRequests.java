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

        if (isUserAuthenticated(httpRequest)) {
            if (!isURLAuthenticated(httpRequest)) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/home");
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } else {
            if (isURLValid(httpRequest) && !isURLAuthenticated(httpRequest)) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
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