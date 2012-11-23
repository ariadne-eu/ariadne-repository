package org.ariadne_eu.webui.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ariadne.config.PropertiesManager;
import org.ariadne_eu.utils.config.RepositoryConstants;

/**
 * Created by ben
 * Date: 24-mrt-2007
 * Time: 10:50:28
 * To change this template use File | Settings | File Templates.
 */
public class AdminOnlyFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // We need HTTP request objects
        HttpServletRequest hrequest = (HttpServletRequest) request;
        HttpServletResponse hresponse = (HttpServletResponse) response;

        if (hrequest.getSession().getAttribute("login") != null && hrequest.getSession().getAttribute("login").equals("true") &&
                hrequest.getSession().getAttribute("username") != null && hrequest.getSession().getAttribute("username").equals(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REPO_USERNAME)))
            // Allow request to proceed
            chain.doFilter(hrequest, hresponse);
        else
            request.getRequestDispatcher("/login/login.jsp").forward(request, response);
    }

    public void destroy() {
    }
}
