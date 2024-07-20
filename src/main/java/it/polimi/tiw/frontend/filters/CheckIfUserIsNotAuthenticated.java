package it.polimi.tiw.frontend.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet Filter implementation class CheckIfUserIsNotAuthenticated.
 * This filter intercepts requests to the homepage and redirects non-authenticated users to the login page.
 * It ensures that only authenticated users can access the user-dependant pages.
 */
@WebFilter({"/logout", "/home", "/create", "/create/create-folder", "/create/create-document"})
public class CheckIfUserIsNotAuthenticated implements Filter {
    /**
     * This method checks if the user is not authenticated by looking for a user attribute in the session.
     * If the user is not authenticated, they are redirected to the login page.
     * Otherwise, the request is allowed to proceed to the requested page.
     *
     * @param servletRequest  The request to be processed.
     * @param servletResponse The response to be sent.
     * @param filterChain     The chain of filters the request is subject to.
     * @throws IOException      if an I/O error occurs during this filter's processing of the request.
     * @throws ServletException if the request could not be handled.
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        // Extract the session from the request
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        HttpSession httpSession = httpRequest.getSession();
        // If the user is not authenticated, redirect them to the login page
        if (httpSession.isNew() || httpSession.getAttribute("user") == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
        } else {
            // If the user is authenticated, let them continue to the requested page
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}