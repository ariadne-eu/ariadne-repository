package org.ariadne_eu.utils.registry.auth;

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

import JFlex.Out;

/**
 * Set request's character encoding to UTF-8.
 * 
 * @author Michael Liao (askxuefeng@gmail.com)
 */
public class RegisteredOnlyFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
     // We need HTTP request objects
        HttpServletRequest hrequest = (HttpServletRequest) request;
        HttpServletResponse hresponse = (HttpServletResponse) response;
        if (!CheckDatabase.isInBlackList((String)hrequest.getSession().getAttribute("username")) && hrequest.getSession().getAttribute("openid")!=null && hrequest.getSession().getAttribute("login") != null && hrequest.getSession().getAttribute("login").equals("true")){
        		 chain.doFilter(request, response);
        }else if (CheckDatabase.isInBlackList((String)hrequest.getSession().getAttribute("username"))){
        	System.out.println("Black list!!! User:"+(String)hrequest.getSession().getAttribute("username"));
        }
        else
        {
        	System.out.println("Filter:"+hrequest.getRequestURI());
        	hrequest.getSession().setAttribute("contextURL", hrequest.getRequestURI());
        	request.getRequestDispatcher("/auth/openid?op=Google").forward(hrequest, hresponse);
        }
        /*else{
	        if (hrequest.getSession().getAttribute("login") != null && hrequest.getSession().getAttribute("login").equals("true") &&
	                hrequest.getSession().getAttribute("username") != null && hrequest.getSession().getAttribute("username").equals(PropertiesManager.getInstance().getProperty(RepositoryConstants.getInstance().REPO_USERNAME)))
	            // Allow request to proceed
	            chain.doFilter(hrequest, hresponse);
	        else
	            request.getRequestDispatcher("/login/login.jsp").forward(request, response);
        }*/
        
    }

    public void destroy() {
    }

}
