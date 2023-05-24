package com.couponsystemstage3.clr;

import com.couponsystemstage3.daily_job.CouponExpirationDailyJob;
import com.couponsystemstage3.entity_beans.Coupon;
import com.couponsystemstage3.entity_beans.CustomerPurchase;
import com.couponsystemstage3.repositories.*;
import com.couponsystemstage3.types.CouponStatus;
import com.couponsystemstage3.utils.ArtUtils;
import com.couponsystemstage3.utils.ConsoleColors;
import com.couponsystemstage3.utils.TestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@Order(5)
public class DailyJobTest implements CommandLineRunner {

    @Autowired
    private CouponExpirationDailyJob couponExpirationDailyJob;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerPurchaseRepository customerPurchaseRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(ConsoleColors.MAGENTA_ITALIC + ArtUtils.DAILY_JOB_TEST);

        Coupon coupon = Coupon.builder()
                .company(companyRepository.getCompanyById(1L))
                .category(categoryRepository.getById(3L))
                .title("for daily-job testing")
                .description("daily-job description")
                .startDate(LocalDateTime.now().minusDays(10))
                .endDate(LocalDateTime.now().minusDays(1))
                .amount(90)
                .price(5.9)
                .image("https://picsum.photos/200")
                .couponStatus(CouponStatus.ABLE)
                .build();

        Coupon coupon2 = Coupon.builder()
                .company(companyRepository.getCompanyById(1L))
                .category(categoryRepository.getById(3L))
                .title("2 for daily-job testing")
                .description("2 daily-job description")
                .startDate(LocalDateTime.now().minusDays(10))
                .endDate(LocalDateTime.now().minusDays(5))
                .amount(90)
                .price(5.9)
                .image("https://picsum.photos/200")
                .couponStatus(CouponStatus.ABLE)
                .build();

        couponRepository.saveAll(Arrays.asList(coupon, coupon2));

        CustomerPurchase customerPurchase1 = CustomerPurchase.builder()
                .customer(customerRepository.getCustomerById(2L))
                .coupon(couponRepository.getCouponById(7L))
                .couponTitle(couponRepository.getById(7L).getTitle())
                .customerName(customerRepository.getById(2L).getFirstName())
                .purchaseDateTime(LocalDateTime.now().minusDays(3))
                .build();
        CustomerPurchase customerPurchase2 = CustomerPurchase.builder()
                .customer(customerRepository.getCustomerById(3L))
                .coupon(couponRepository.getCouponById(8L))
                .couponTitle(couponRepository.getById(8L).getTitle())
                .customerName(customerRepository.getById(3L).getFirstName())
                .purchaseDateTime(LocalDateTime.now().minusDays(6))
                .build();
        customerPurchaseRepository.saveAll(Arrays.asList(customerPurchase1, customerPurchase2));

        TestUtils.printTest("***** Updating Status of expired coupons to DISABLE and deleting customer purchasing with those expired coupons ****");
        System.out.println("Before updating Status of expired coupons: ");
        List<Coupon> couponExpiredList = couponRepository.findAllByEndDateBefore(LocalDateTime.now());
        couponExpiredList.forEach((System.out::println));
        System.out.println();

        couponExpirationDailyJob.updateCouponStatusAsDeleteCoupon();

        /* In order to show in test DISABLED coupons */
        System.out.println("After updating Status of expired coupons: ");
        List<Coupon> couponExpiredList2 = couponRepository.findAllByEndDateBefore(LocalDateTime.now());
        couponExpiredList2.forEach((System.out::println));

    }
}
