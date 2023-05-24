package com.couponsystemstage3.advice;

import com.couponsystemstage3.exceptions.CouponSystemException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class CouponControllerAdvice {
/* @AfterThrowing advice/aspect (by default acts after Controller) */

    @ExceptionHandler(value={CouponSystemException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)/* 400-business logic */
    public ErrorDetails handleException(Exception e) {
        return new ErrorDetails("From CouponSystem", e.getMessage());
    }

    @ExceptionHandler(value={SecurityException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)/* 401 - Unauthorized (authentication) */
    public ErrorDetails handleException2(Exception e) {
        return new ErrorDetails("From Security", e.getMessage());
    }

    @ExceptionHandler(value={Exception.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails handleException3(Exception e) {
        return new ErrorDetails("From Exception", e.getMessage());
    }

    @ExceptionHandler(value={Error.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  /*500*/
    public ErrorDetails handleError(Error e) {
        return new ErrorDetails("From Error", "Error has occurred");
    }
}
