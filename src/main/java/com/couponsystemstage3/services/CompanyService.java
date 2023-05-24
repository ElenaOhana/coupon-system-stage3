package com.couponsystemstage3.services;

import com.couponsystemstage3.entity_beans.Category;
import com.couponsystemstage3.entity_beans.Company;
import com.couponsystemstage3.entity_beans.Coupon;
import com.couponsystemstage3.exceptions.CouponSystemException;
import com.couponsystemstage3.types.CouponStatus;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

public interface CompanyService {

    Long loginCompanyReturnId(String email, String password) throws CouponSystemException;
    Coupon addCoupon(@NotNull(message = "The Coupon must not be null") Coupon coupon) throws CouponSystemException;

    Integer updateCoupon(@NotNull(message = "The couponId must not be null") Long couponId,
                      @NotNull(message = "The Coupon must not be null") Coupon coupon) throws CouponSystemException;

    Integer removeCoupon(@NotNull Long id) throws CouponSystemException;
    List<Coupon> getAllCompanyCoupons();
    Coupon getCouponByIdOfConnectedCompany(Long id) throws CouponSystemException;
    Company findById(Long id);
    Company getCompanyDetails();
    Coupon getCouponByIdAndTitle(Long id, String title) throws CouponSystemException;
    List<Coupon> getCompanyCouponsByCategory(Category category);
    List<Coupon> getCompanyCouponsByCategoryAndStatus(Category category, CouponStatus couponStatus);
    double getMaxPriceOfCouponsOfCompany();
    List<Coupon> findFromCompanyCouponsUpToMaxPrice();


    Set<Category> getAbleCouponsCategories();
    List<Category> getDrinksCouponsCategories();
    Set<Category> getDisableCouponsCategories();
    Coupon getCouponByIdAndCouponStatus(Long id, CouponStatus couponStatus) throws CouponSystemException;
}
