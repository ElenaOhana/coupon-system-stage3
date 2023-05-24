package com.couponsystemstage3.controllers;

import com.couponsystemstage3.exceptions.CouponSystemException;
import com.couponsystemstage3.security.LoginManager;
import com.couponsystemstage3.security.TokenManager;
import com.couponsystemstage3.security.request.LoginRequest;
import com.couponsystemstage3.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public abstract class ClientController {

    @Autowired
    protected AdminService adminService;
    @Autowired
    protected CompanyService companyService;
    @Autowired
    protected CustomerService customerService;

    @Autowired
    protected AdminServiceImpl adminServiceImpl;

    @Autowired
    protected CompanyServiceImpl companyServiceImpl;

    @Autowired
    protected CustomerServiceImpl customerServiceImpl;

    @Autowired
    protected TokenManager tokenManager;

    @Autowired
    protected LoginManager loginManager;

    protected abstract ResponseEntity<?> login(LoginRequest loginRequest) throws CouponSystemException;;
}
