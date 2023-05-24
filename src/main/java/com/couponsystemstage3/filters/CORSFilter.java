package com.couponsystemstage3.filters;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class CORSFilter implements Filter {
/* @Before-AOP (by default acts before Controller) */

    public CORSFilter() {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        // Authorize (allow) all domains to consume the content
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Origin","http://localhost:3000");/* I get requests from PORT 3000(React). From Angular - 4200 */ /*Must have @CrossOrigin(origins = "*", allowedHeaders = "*") in Controllers */
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Methods","GET, OPTIONS, HEAD, PUT, POST, DELETE");
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Headers","authorization, Origin, Accept, x-auth-token, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");

        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        // For HTTP OPTIONS verb/method reply with ACCEPTED status code -- per CORS handshake
        if (request.getMethod().equals("OPTIONS")) {
            return;
        }

        // pass the request along the filter chain (In our case - pass to @Order(2)-LoginFilter)
        chain.doFilter(request, servletResponse);///*If there is a second Filter - go there..-LoginFilter
    }

    public void init(FilterConfig fConfig) throws ServletException {
    }

}