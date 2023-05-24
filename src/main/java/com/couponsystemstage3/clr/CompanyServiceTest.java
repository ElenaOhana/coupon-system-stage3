package com.couponsystemstage3.clr;

import com.couponsystemstage3.entity_beans.Category;
import com.couponsystemstage3.entity_beans.Coupon;
import com.couponsystemstage3.exceptions.CouponSystemException;
import com.couponsystemstage3.repositories.CategoryRepository;
import com.couponsystemstage3.repositories.CouponRepository;
import com.couponsystemstage3.security.ClientType;
import com.couponsystemstage3.security.LoginManager;
import com.couponsystemstage3.services.AdminServiceImpl;
import com.couponsystemstage3.services.CompanyServiceImpl;
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

@Order(3)
@Component

public class CompanyServiceTest implements CommandLineRunner {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LoginManager loginManager;

    @Autowired
    private CompanyServiceImpl companyServiceImpl;

    @Autowired
    private AdminServiceImpl adminService;


    @Override
    public void run(String... args) throws Exception {
        System.out.println(ConsoleColors.BLACK_BOLD_BRIGHT + ArtUtils.COMPANY_SERVICE_TEST);
        TestUtils.printTest("bad logging");
        try {
            System.out.println(loginManager.login("cola_cola@gmail.com", "222222", ClientType.COMPANY));
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }
        TestUtils.printTest("bad logging due to the Company is INACTIVE already");
        try {
            System.out.println(loginManager.login("pepsi@gmail.com", "3232", ClientType.COMPANY));
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }
        TestUtils.printTest("good logging");
        try {
            System.out.println(loginManager.login("cola_cola_upd@gmail.com", "222222", ClientType.COMPANY));
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest("add coupon will fail due to existing title in the storage of this(the same) Company");
        Coupon couponWithExistingTitle = couponRepository.getCouponByIdAndTitle(1L, "Cola bottles-glass");
        Coupon newCouponToAdd = Coupon.builder()
                .id(1L)
                .company(companyServiceImpl.findById(1L))
                .category(categoryRepository.getById(1L))
                .title(couponWithExistingTitle.getTitle())
                .description("some description")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(12))
                .amount(90)
                .price(11.9)
                .image("https://picsum.photos/200")
                .couponStatus(CouponStatus.ABLE)
                .build();
        try {
            companyServiceImpl.addCoupon(newCouponToAdd);
            //companyServiceImpl.createCoupon2(newCouponToAdd, 2L);//FOR SCOPE PROTOTYPE!!
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest("add coupon will fail due to trying to add coupon of non-connected Company");
        Coupon couponFromNonConnectedCompany = Coupon.builder()
                .id(2L)
                .company(companyServiceImpl.findById(3L))
                .category(categoryRepository.getById(1L))
                .title(couponWithExistingTitle.getTitle())
                .description("add description")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(12))
                .amount(90)
                .price(11.9)
                .image("https://picsum.photos/200")
                .couponStatus(CouponStatus.ABLE)
                .build();
        try {
            companyServiceImpl.addCoupon(couponFromNonConnectedCompany);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest("add coupon with existing title will succeed because this is the title of different Company");
        Coupon newCouponToAdd2 = Coupon.builder()
                .company(companyServiceImpl.findById(1L))
                .category(categoryRepository.getById(1L))
                .title(couponRepository.getCouponById(3L).getTitle())
                .description("added new title!")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(12))
                .amount(90)
                .price(12.9)
                .image("https://picsum.photos/200")
                .couponStatus(CouponStatus.ABLE)
                .build();
        try {
            companyServiceImpl.addCoupon(newCouponToAdd2);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }
        couponRepository.findAll().forEach(System.out :: println);

        TestUtils.printTest("update will fail due to not allowed to change the coupon id");
        Coupon couponForUpdate = companyServiceImpl.getCouponByIdOfConnectedCompany(1L);
        Coupon newDataToCoupon = Coupon.builder()
                .id(2L)
                .company(companyServiceImpl.findById(2L))
                .category(categoryRepository.getById(1L))
                .title("barbells")
                .description("barbells 2 kg")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(5))
                .amount(10)
                .price(24.9)
                .image("https://picsum.photos/200")
                .couponStatus(CouponStatus.ABLE)
                .build();
        try {
            companyServiceImpl.updateCoupon(couponForUpdate.getId(), newDataToCoupon);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest("update will fail due to not allowed to change the coupon companyId");
        newDataToCoupon.setId(1L);
        newDataToCoupon.setCompany(adminService.getOneCompany(3L));
        try {
            companyServiceImpl.updateCoupon(couponForUpdate.getId(), newDataToCoupon);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest("update will fail due to not allowed to change the coupon title to existing title that that company has");
        newDataToCoupon.setId(1L);
        newDataToCoupon.setCompany(adminService.getOneCompany(1L));
        newDataToCoupon.setTitle("2+1");
        newDataToCoupon.setPrice(14.9);
        try {
            companyServiceImpl.updateCoupon(couponForUpdate.getId(), newDataToCoupon);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest("update coupon will fail due to trying to update coupon of non-connected Company");
        newDataToCoupon.setId(3L);
        newDataToCoupon.setCompany(adminService.getOneCompany(3L));
        newDataToCoupon.setTitle("some title");
        newDataToCoupon.setPrice(20.9);
        try {
            companyServiceImpl.updateCoupon(3L, newDataToCoupon);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest("update coupon price will succeed because updating with equals titles");
        newDataToCoupon.setId(1L);
        newDataToCoupon.setCompany(adminService.getOneCompany(1L));
        newDataToCoupon.setTitle("Cola bottles-glass");
        newDataToCoupon.setPrice(14.9);
        newDataToCoupon.setDescription("all drinks");
        try {
            companyServiceImpl.updateCoupon(couponForUpdate.getId(), newDataToCoupon);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Updated coupon is: " + companyServiceImpl.getCouponByIdOfConnectedCompany(1L));

        TestUtils.printTest("update coupon amount and title(to non-existing title) will succeed because updating to non-existing title");
        newDataToCoupon.setId(1L);
        newDataToCoupon.setCompany(adminService.getOneCompany(1L));
        newDataToCoupon.setTitle("Cola updated title bottles-glass");
        newDataToCoupon.setAmount(50);
        newDataToCoupon.setDescription("all drinks");
        try {
            companyServiceImpl.updateCoupon(couponForUpdate.getId(), newDataToCoupon);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Updated coupon is: " + companyServiceImpl.getCouponByIdOfConnectedCompany(1L));

        TestUtils.printTest("delete coupon will fail due to trying to delete coupon of non-connected Company");
        try {
            companyServiceImpl.removeCoupon(2L);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest("delete coupon will fail due to trying to delete non existent coupon");
        try {
            companyServiceImpl.removeCoupon(20L);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest("delete coupon will succeed");
        try {
            companyServiceImpl.removeCoupon(1L);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Coupons of connected company: " + couponRepository.findCompanyCouponsByCompanyId(1L));

        TestUtils.printTest("get all coupons of the company with companyId " + companyServiceImpl.getCompanyId() + " will succeed");
        List<Coupon> couponListOfTheCompany = companyServiceImpl.getAllCompanyCoupons();
        couponListOfTheCompany.forEach(System.out :: println);

        Category shopping = categoryRepository.getById(1L);
        TestUtils.printTest("get all coupons by category of the company with companyId "+ companyServiceImpl.getCompanyId() + " and categoryId " + shopping + " will succeed");
        List<Coupon> couponListOfTheCompanyByCategory = companyServiceImpl.getCompanyCouponsByCategory(shopping);
        couponListOfTheCompanyByCategory.forEach(System.out :: println);

        TestUtils.printTest("get all ABLE coupons by category of the company with companyId "+ companyServiceImpl.getCompanyId() + " and categoryId " + shopping + " will succeed");
        List<Coupon> couponListOfTheCompanyByCategoryAndStatus = companyServiceImpl.getCompanyCouponsByCategoryAndStatus(shopping, CouponStatus.ABLE);
        couponListOfTheCompanyByCategoryAndStatus.forEach(System.out :: println);

        TestUtils.printTest("Get max company coupon price will succeed");
        Double byMaxPriceOfCompany = companyServiceImpl.getMaxPriceOfCouponsOfCompany();
        System.out.println(byMaxPriceOfCompany);


        TestUtils.printTest("Get coupons up to max company coupon price will succeed");
        List<Coupon> couponListUpToMaxPriceOfCompany = companyServiceImpl.findFromCompanyCouponsUpToMaxPrice();
        couponListUpToMaxPriceOfCompany.forEach(System.out::println);

        TestUtils.printTest("Get connected company details will succeed, company id is: "+ companyServiceImpl.getCompanyId());
        System.out.println(companyServiceImpl.getCompanyDetails());


     /*   System.out.println("//////////////MY ADDITIONAL PRACTICE//////////////////////");
        TestUtils.printTest("My query : getCompanyCouponsByCategory");
        System.out.println(companyServiceImpl.getCompanyCouponsByCategory(shopping));

        TestUtils.printTest("My query : getCategoriesOfAbleCoupons");
        Company companyNike = Company.builder()
                .name("Nike")
                .email("nike@nike_u.com")
                .password("7777")
                .clientStatus(ClientStatus.ACTIVE)
                .build();
        adminService.addCompany2(companyNike);
        Coupon couponCategory2Able = Coupon.builder()
                .company(companyNike)
                .category(categoryRepository.getById(2L))
                .title("barbells")
                .description("barbells 2 kg")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(5))
                .amount(10)
                .price(24.9)
                .image("https://picsum.photos/200")
                .couponStatus(CouponStatus.ABLE)
                .build();
        companyServiceImpl.addCoupon(couponCategory2Able);
        Set<Category> categoryList = companyServiceImpl.getAbleCouponsCategories();
        System.out.println(categoryList);

        TestUtils.printTest("My query : getCategoriesOfDisableCoupons");
        Set<Category> categoryDisableList = companyServiceImpl.getDisableCouponsCategories();
        System.out.println(categoryDisableList);

        TestUtils.printTest("My query : getDrinksCouponsCategories");
        System.out.println(companyServiceImpl.getDrinksCouponsCategories());*/
    }
}
