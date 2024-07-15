package it.polimi.tiw.backend.utilities;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

/**
 * This class contains utility methods to handle template engines.
 */
public class TemplateEngineBuilder {
    private static final String templateResolverPrefix = "/WEB-INF/templates/";
    private static final String templateResolverSuffix = ".html";

    /**
     * This method retrieves a template engine from a given servlet.
     *
     * @param httpServlet the servlet from which context build a new template engine
     * @return a template engine
     */
    public static TemplateEngine getTemplateEngineFromServlet(HttpServlet httpServlet) {
        // Since Thymeleaf 3.1 all Web-API based expressions utility objects are now deprecated 😊
        // Reference: https://www.thymeleaf.org/doc/articles/thymeleaf31whatsnew.html
        // I'm now no longer following the examples provided in the course, but I'm following the official documentation
        // Reference: https://www.thymeleaf.org/doc/tutorials/3.1/usingthymeleaf.html

        // First of all, I need to build a JakartaServletWebApplication object from the servlet context
        JakartaServletWebApplication jakartaServletWebApplication =
                JakartaServletWebApplication.buildApplication(httpServlet.getServletContext());
        // Then, I can use the JakartaServletWebApplication to build a WebApplicationTemplateResolver object
        // For clarity, I've extracted this step into a separate method (getWebApplicationTemplateResolver)
        WebApplicationTemplateResolver webApplicationTemplateResolver =
                getWebApplicationTemplateResolver(jakartaServletWebApplication);

        // Finally, I can build the TemplateEngine object using the template resolver
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(webApplicationTemplateResolver);

        return templateEngine;
    }

    /**
     * This method retrieves and configure a WebApplicationTemplateResolver object
     * from a giver JakartaServletWebApplication object.
     *
     * @param jakartaServletWebApplication the JakartaServletWebApplication object
     * @return a WebApplicationTemplateResolver object
     */
    private static WebApplicationTemplateResolver
    getWebApplicationTemplateResolver(JakartaServletWebApplication jakartaServletWebApplication) {
        WebApplicationTemplateResolver webApplicationTemplateResolver =
                new WebApplicationTemplateResolver(jakartaServletWebApplication);

        // Now, I'm free to set up the template resolver as I want (again, I'm following the official documentation)
        // Set the template mode to HTML
        webApplicationTemplateResolver.setTemplateMode(TemplateMode.HTML);
        // Set the prefix and suffix for the templates (Thymeleaf will look for templates in /WEB-INF/templates/*.html)
        webApplicationTemplateResolver.setPrefix(templateResolverPrefix);
        webApplicationTemplateResolver.setSuffix(templateResolverSuffix);
        // Enable template caching
        webApplicationTemplateResolver.setCacheable(true);
        // Set the cache TTL to 1 hour
        webApplicationTemplateResolver.setCacheTTLMs(3600000L);

        return webApplicationTemplateResolver;
    }

    /**
     * This method retrieves a WebContext object from a given servlet, request, and response.
     *
     * @param httpServlet the servlet from which context build a new JakartaServletWebApplication
     * @param request     the request from which build a new IWebExchange
     * @param response    the response from which build a new IWebExchange
     * @return a WebContext object
     */
    public static WebContext
    getWebContextFromServlet(HttpServlet httpServlet, HttpServletRequest request, HttpServletResponse response) {
        JakartaServletWebApplication jakartaServletWebApplication =
                JakartaServletWebApplication.buildApplication(httpServlet.getServletContext());
        IWebExchange exchange = jakartaServletWebApplication.buildExchange(request, response);
        return new WebContext(exchange, exchange.getLocale());
    }
}
