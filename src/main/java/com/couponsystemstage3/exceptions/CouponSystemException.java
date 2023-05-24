package com.couponsystemstage3.exceptions;

public class CouponSystemException extends Exception {

    public CouponSystemException() {
        super();
    }

    public CouponSystemException(ErrMsg errMsg) {
        super(errMsg.getErrDescription());
    }

    public CouponSystemException(String message) {
        super(message);
    }

    public CouponSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public CouponSystemException(Throwable cause) {
        super(cause);
    }

    public CouponSystemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
