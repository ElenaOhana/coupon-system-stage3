package com.couponsystemstage3.filters;

import com.couponsystemstage3.advice.SecurityException;
import com.couponsystemstage3.security.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(2)
public class LoginFilter implements Filter {
    /* @Before-AOP (by default acts before Controller) [AOP implemented by Proxy DP] */

    @Autowired
    private TokenManager tokenManager;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String url = ((HttpServletRequest) (request)).getRequestURI();
        String type = retrieveClientTypeFromUrl(url);

        if (url.contains("login")) {
            chain.doFilter(request, response);/*If there is a third Filter - go there, if is not - go to Controller(REST api)*/
            return;
        }
        String token = ((HttpServletRequest) (request)).getHeader("Authorization");

        switch (type) {
            case "admin":
                try {
                    if (tokenManager.isTokenExistAndAdmin(token)) {
                        chain.doFilter(request, response);  //If there is a third Filter - go there, if is not - go to Controller(REST api)
                    }
                } catch (SecurityException e) {
                    ((HttpServletResponse)response).sendError(401);
                }
                break;
            case "company":
                try {
                    if (tokenManager.isTokenExistAndCompany(token)) {
                            chain.doFilter(request, response);
                    }
                } catch (SecurityException e) {
                    ((HttpServletResponse)response).sendError(401);
                }
                break;
            case "customer":
                try {
                    if (tokenManager.isTokenExistAndCustomer(token)) {
                            chain.doFilter(request, response);
                    }
                } catch (SecurityException e) {
                    ((HttpServletResponse)response).sendError(401);
                }
                 break;
            default:
                ((HttpServletResponse)response).sendError(401); //Unauthorized
        }
    }

    private String retrieveClientTypeFromUrl(String url) {
           if (url.contains("admin")) {
            return "admin";
        }
        if (url.contains("company")) {
            return "company";
        }
        if (url.contains("customer")) {
            return "customer";
        }
        return "undefined";
    }
}



