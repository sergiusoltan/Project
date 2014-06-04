package main.java.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: Sergiu Soltan
 */
public class CacheControlFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        httpServletResponse.addHeader("Pragma", "no-cache");
        httpServletResponse.addHeader("Cache-Control", "no-store, max-age=0, must-revalidate, max-stale=0, post-check=0, pre-check=0"); //HTTP 1.1
        httpServletResponse.addHeader("Expires", "-1");
        filterChain.doFilter(servletRequest, httpServletResponse);
    }

    @Override
    public void destroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
