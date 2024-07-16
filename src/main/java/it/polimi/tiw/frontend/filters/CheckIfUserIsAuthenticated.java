package it.polimi.tiw.frontend.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet Filter implementation class CheckIfUserIsAuthenticated.
 * This filter intercepts requests to the login and registration pages and redirects authenticated users to the homepage
 * It ensures that authenticated users do not access authentication forms again.
 */
@WebFilter({"/register", "/login"})
public class CheckIfUserIsAuthenticated implements Filter {
    /**
     * This method checks if the user is already authenticated by looking for a user attribute in the session.
     * If the user is authenticated, they are redirected to the homepage. Otherwise, the request is allowed to proceed.
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
        HttpSession httpSession = ((jakarta.servlet.http.HttpServletRequest) servletRequest).getSession();
        // If the user is already authenticated, redirect them to the homepage
        if (!httpSession.isNew() && httpSession.getAttribute("user") != null) {
            ((jakarta.servlet.http.HttpServletResponse) servletResponse).sendRedirect("home");
        } else {
            // If the user is not authenticated, let them continue to the requested page
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}