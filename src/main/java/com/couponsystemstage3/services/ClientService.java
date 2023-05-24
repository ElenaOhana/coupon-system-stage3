package com.couponsystemstage3.services;

import com.couponsystemstage3.exceptions.CouponSystemException;
import com.couponsystemstage3.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * Created by Elena on 30 June, 2021
 */

public abstract class ClientService {

    @Autowired
    protected CompanyRepository companyRepository;

    @Autowired
    protected CustomerRepository customerRepository;

    @Autowired
    protected CouponRepository couponRepository;

    @Autowired
    protected CustomerPurchaseRepository customerPurchaseRepository;

    @Autowired
    protected CategoryRepository categoryRepository;

    public abstract boolean login(String email, String password) throws CouponSystemException;

}
