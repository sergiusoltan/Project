package main.java.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: Sergiu Soltan
 */
public class RestCacheFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        response.setHeader("Cache-Control","no-cache, no-store, max-age=0");
        response.setHeader("Pragma","no-cache");
        response.setDateHeader("Expires",0);

        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
