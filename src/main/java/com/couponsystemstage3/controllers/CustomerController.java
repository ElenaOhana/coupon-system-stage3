package com.couponsystemstage3.controllers;

import com.couponsystemstage3.advice.SecurityException;
import com.couponsystemstage3.entity_beans.CustomerPurchase;
import com.couponsystemstage3.exceptions.CouponSystemException;
import com.couponsystemstage3.repositories.CouponRepository;
import com.couponsystemstage3.security.ClientType;
import com.couponsystemstage3.security.request.LoginRequest;
import com.couponsystemstage3.security.response.LoginResponse;
import com.couponsystemstage3.services.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"customer"})
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class CustomerController extends ClientController {

    @Autowired
    CouponRepository couponRepository;

    @PostMapping("login")
    @Override
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws CouponSystemException {
        CustomerServiceImpl customerServiceImpl = (CustomerServiceImpl) loginManager.login(loginRequest.getEmail(), loginRequest.getPassword(), ClientType.CUSTOMER);
        String token = tokenManager.createToken(ClientType.CUSTOMER, customerServiceImpl.getCustomerId());
        return new ResponseEntity<>(new LoginResponse(token, ClientType.CUSTOMER, loginRequest.getEmail()), HttpStatus.CREATED);
    }

    @PostMapping("customer_purchase")
    public ResponseEntity<?> purchaseCoupon(@RequestHeader("Authorization") String token, @RequestBody CustomerPurchase customerPurchase) throws SecurityException, CouponSystemException {
        if (!tokenManager.isTokenExistAndCustomer(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(customerServiceImpl.createCustomerPurchase(customerPurchase), HttpStatus.CREATED);
    }

    // Does not need by customerId
    @GetMapping("customer_purchase/{customerId}")
    public ResponseEntity<?> getCustomerPurchasesByCustomerId(@RequestHeader("Authorization") String token, @PathVariable long customerId) throws SecurityException, CouponSystemException {
        if (!tokenManager.isTokenExistAndCustomer(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(customerServiceImpl.getCustomerPurchasesByCustomerId(customerId), HttpStatus.OK);
    }
    @GetMapping("customer_purchase")
    public ResponseEntity<?> getCustomerPurchases(@RequestHeader("Authorization") String token) throws SecurityException, CouponSystemException {
        if (!tokenManager.isTokenExistAndCustomer(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(customerServiceImpl.getCustomerPurchases(), HttpStatus.OK);
    }

    @GetMapping("purchase_by_category/{categoryId}")
    public ResponseEntity<?> getCustomerPurchasesOfConnectedCustomerByCategoryId(@RequestHeader("Authorization") String token, @PathVariable long categoryId) throws SecurityException {
        if (!tokenManager.isTokenExistAndCustomer(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(customerServiceImpl.getCustomerPurchasesOfConnectedCustomerByCategoryId(categoryId), HttpStatus.OK);
    }

    @GetMapping("max_price_customer")
    public ResponseEntity<?> findMaxPriceOfCustomer(@RequestHeader("Authorization") String token) throws SecurityException {
        if (!tokenManager.isTokenExistAndCustomer(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(customerServiceImpl.findMaxPriceOfCustomer(), HttpStatus.OK);
    }

    @GetMapping("purchases_less_than_max_price")
    public ResponseEntity<?> getCouponListLessThanMaxPrice(@RequestHeader("Authorization") String token) throws SecurityException {
        if (!tokenManager.isTokenExistAndCustomer(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(customerServiceImpl.getCouponListLessThanMaxPrice(), HttpStatus.OK);
    }

    @GetMapping("details")
    public ResponseEntity<?> getCustomerDetails(@RequestHeader("Authorization") String token) throws SecurityException {
        if (!tokenManager.isTokenExistAndCustomer(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(customerServiceImpl.getCustomerDetails(), HttpStatus.OK);
    }

    //TODO
    @GetMapping("coupon")
    public ResponseEntity<?> getAllCoupons(@RequestHeader("Authorization") String token) throws SecurityException {
        if (!tokenManager.isTokenExistAndCustomer(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(couponRepository.findAll(), HttpStatus.OK);
    }
}
