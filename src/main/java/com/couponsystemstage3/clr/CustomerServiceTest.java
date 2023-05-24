package com.couponsystemstage3.clr;

import com.couponsystemstage3.entity_beans.Category;
import com.couponsystemstage3.entity_beans.Coupon;
import com.couponsystemstage3.entity_beans.CustomerPurchase;
import com.couponsystemstage3.exceptions.CouponSystemException;
import com.couponsystemstage3.repositories.*;
import com.couponsystemstage3.security.ClientType;
import com.couponsystemstage3.security.LoginManager;
import com.couponsystemstage3.services.CustomerServiceImpl;
import com.couponsystemstage3.types.CouponStatus;
import com.couponsystemstage3.utils.ArtUtils;
import com.couponsystemstage3.utils.ConsoleColors;
import com.couponsystemstage3.utils.TestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Order(4)
@Component
public class CustomerServiceTest implements CommandLineRunner {

    @Autowired
    private LoginManager loginManager;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerServiceImpl customerServiceImpl;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CustomerPurchaseRepository customerPurchaseRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + ArtUtils.CUSTOMER_SERVICE_TEST);
        TestUtils.printTest("bad logging");
        try {
            System.out.println(loginManager.login("some_bad_mail@gmail.com", "1234", ClientType.CUSTOMER));
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }
        TestUtils.printTest("good logging");
        System.out.println(loginManager.login("anna_an44Gmail.com", "5555", ClientType.CUSTOMER));

        CustomerPurchase customerPurchaseAsDummyData2 = CustomerPurchase.builder()
                .customer(customerRepository.getCustomerById(2L))
                .coupon(couponRepository.getCouponById(5L))
                .couponTitle(couponRepository.getById(5L).getTitle())
                .customerName(customerRepository.getById(2L).getFirstName())
                .purchaseDateTime(LocalDateTime.now())
                .build();
        customerPurchaseRepository.save(customerPurchaseAsDummyData2);

        TestUtils.printTest(("Customer Purchase will fail due to the non-connected customer trying to buy the coupon"));
        try {
            customerServiceImpl.createCustomerPurchase(customerPurchaseAsDummyData2);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest(("Customer Purchase will fail due to trying to buy the same coupon"));
        CustomerPurchase existingCustomerPurchase = customerPurchaseRepository.findByCustomerIdAndCouponId(3L, 3L);
        try {
            customerServiceImpl.createCustomerPurchase(existingCustomerPurchase);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }


        TestUtils.printTest(("Customer Purchase will fail due to trying to buy the coupon with amount 0"));
        Coupon couponAsDummyDataCoupon = Coupon.builder()
                .company(companyRepository.getById(4L))
                .category(categoryRepository.getById(4L))
                .title("Skiing deal")
                .description("Finest Andora ski")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(7))
                .amount(0)
                .price(3800)
                .image("https://picsum.photos/200")
                .couponStatus(CouponStatus.ABLE)
                .build();
        couponRepository.save(couponAsDummyDataCoupon);
        CustomerPurchase customerPurchaseAsDummyData = CustomerPurchase.builder()
                .id(7L)
                .customer(customerRepository.getCustomerById(3L))
                .coupon(couponRepository.getCouponByTitle(couponAsDummyDataCoupon.getTitle()))
                .couponTitle(couponAsDummyDataCoupon.getTitle())
                .customerName(customerRepository.getById(3L).getFirstName())
                .purchaseDateTime(LocalDateTime.now())
                .build();
        try {
            customerServiceImpl.createCustomerPurchase(customerPurchaseAsDummyData);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest(("Customer Purchase will fail due to trying to buy the expired coupon"));
        Category category4 = categoryRepository.getById(4L);
        Coupon couponByCategoryFromDB = couponRepository.getByCategory(category4);
        couponByCategoryFromDB.setEndDate(LocalDateTime.now().minusDays(1));
        couponByCategoryFromDB.setAmount(5); // changes in Heap only
        couponRepository.save(couponByCategoryFromDB);
        customerPurchaseAsDummyData.setCoupon(couponByCategoryFromDB);
        try {
            customerServiceImpl.createCustomerPurchase(customerPurchaseAsDummyData);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest(("Customer Purchase will succeed"));
        couponByCategoryFromDB.setEndDate(LocalDateTime.now().plusDays(14));
        couponRepository.save(couponByCategoryFromDB);
        customerPurchaseAsDummyData.setCoupon(couponByCategoryFromDB);
        try {
            customerServiceImpl.createCustomerPurchase(customerPurchaseAsDummyData);
            System.out.println("The coupon "+ customerPurchaseAsDummyData.getCoupon().getId() +" that customer bought is: "+couponRepository.getByCategory(category4));
            System.out.println(customerPurchaseAsDummyData);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest(("Get all Customer Purchases of connected customer will fail due to not connected customer"));
        try {
            customerServiceImpl.getCustomerPurchasesByCustomerId(2L);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest(("Get all Customer Purchases of connected customer will fail due to INACTIVE customer"));
        try {
            customerServiceImpl.getCustomerPurchasesByCustomerId(1L);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest(("Get all Customer Purchases of connected customer will succeed"));
        List<CustomerPurchase> customerPurchases = customerServiceImpl.getCustomerPurchasesByCustomerId(customerServiceImpl.getCustomerId());
        customerPurchases.forEach(System.out::println);


        TestUtils.printTest(("Get all Customer Purchases (with NO passing ID)of connected customer will succeed"));
        List<CustomerPurchase> customerPurchases22 = customerServiceImpl.getCustomerPurchases();
        customerPurchases.forEach(System.out::println);


        TestUtils.printTest(("Get all Customer Purchases of connected customer by Category will succeed"));
        Category category1 = categoryRepository.getById(4L);
        List<Coupon> customerPurchasesByCategory = customerServiceImpl.getCustomerPurchasesOfConnectedCustomerByCategoryId(category1.getId());
        customerPurchasesByCategory.forEach(System.out::println);

        TestUtils.printTest("Get Max Price of connected customer will succeed");
        System.out.println(customerServiceImpl.findMaxPriceOfCustomer());

        TestUtils.printTest("Get coupons of connected customer up to max coupon purchases price will succeed");
        List<Coupon> couponListUpToMaxPriceOfCustomer = customerServiceImpl.getCouponListLessThanMaxPrice();
        couponListUpToMaxPriceOfCustomer.forEach(System.out::println);

        TestUtils.printTest(("Get connected customer details will succeed"));
        System.out.println(customerServiceImpl.getCustomerDetails());
    }
}
