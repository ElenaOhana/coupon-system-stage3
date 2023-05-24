package com.couponsystemstage3.filters;

import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/* If I want to maintain my server and do not let users get to my service */
//@Component
//@Order(2)
public class VacationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ((HttpServletResponse)response).sendError(HttpStatus.TOO_EARLY.value(), "Elena having fun");
    }
}
