package com.couponsystemstage3.services;

import com.couponsystemstage3.entity_beans.Coupon;
import com.couponsystemstage3.entity_beans.Customer;
import com.couponsystemstage3.entity_beans.CustomerPurchase;
import com.couponsystemstage3.exceptions.CouponSystemException;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface CustomerService {

    Long loginCustomerReturnId(String email, String password) throws CouponSystemException;
    CustomerPurchase createCustomerPurchase(@NotNull CustomerPurchase customerPurchase) throws CouponSystemException;
    List<CustomerPurchase> getCustomerPurchasesByCustomerId(Long id) throws CouponSystemException;
    List<Coupon> getCustomerPurchasesOfConnectedCustomerByCategoryId(Long categoryId);
    double findMaxPriceOfCustomer();
    List<Coupon> getCouponListLessThanMaxPrice();
    Customer getCustomerDetails();

    CustomerPurchase getCustomerPurchaseByCustomerIdAndCouponId(Long customerId, Long couponId) throws CouponSystemException;

}
