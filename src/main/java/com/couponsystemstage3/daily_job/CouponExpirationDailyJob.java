package com.couponsystemstage3.daily_job;

import com.couponsystemstage3.entity_beans.Coupon;
import com.couponsystemstage3.repositories.CouponRepository;
import com.couponsystemstage3.repositories.CustomerPurchaseRepository;
import com.couponsystemstage3.types.CouponStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Transactional
/**
 * Singleton by default as Spring bean */
public class CouponExpirationDailyJob {
    @Autowired
    CouponRepository couponRepository;

    @Autowired
    CustomerPurchaseRepository customerPurchaseRepository;

    @Scheduled(initialDelayString = "PT3S", fixedRateString = "${job.delay}") // Job that run every 4000 milliseconds
    //@Scheduled(cron = "00 00 * * */1") // “At 00:00 on every day-of-week.” == @Scheduled(cron = "@daily")
    //@Scheduled(fixedRate = 1000*60*60*24)
    public void updateCouponStatusAsDeleteCoupon() {

        // Bringing from db an expired coupons only
        List<Coupon> couponExpiredList = couponRepository.findAllByEndDateBefore(LocalDateTime.now());

        // Deletes customer purchase from customers_purchases table
        for (Coupon coupon : couponExpiredList) {
            customerPurchaseRepository.deleteByCouponId(coupon.getId());
        }

        // Changes status to DISABLE of the coupon
        couponExpiredList.forEach(status -> status.setCouponStatus(CouponStatus.DISABLE));

       /* *//* In order to show in test DISABLED coupons *//*
        System.out.println("After updating Status of expired coupons: ");
        couponExpiredList.forEach((System.out::println));*/
    }
}

//In case I want to delete(for real) expired coupons, I will need:
//1. customerPurchase table will demand to be treated first:  cascade on-delete can be helpful(in coupons table).
//2. treated the coupons table.
