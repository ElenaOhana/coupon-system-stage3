package com.couponsystemstage3.repositories;

import com.couponsystemstage3.entity_beans.Coupon;
import com.couponsystemstage3.entity_beans.CustomerPurchase;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerPurchaseRepository extends JpaRepository<CustomerPurchase, Long> {

    List<CustomerPurchase> findByCustomerId(Long customerId);

    CustomerPurchase findByCustomerIdAndCouponId(Long customerId, Long couponId);

    boolean existsByCustomerIdAndCouponId(Long customerId, Long couponId);

    List<CustomerPurchase> findByCouponId(Long couponId);

    void deleteByCouponId(Long couponId);

    void deleteByCustomerId(Long customerID);

}
