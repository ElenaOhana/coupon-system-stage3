package com.couponsystemstage3.clr;

import com.couponsystemstage3.entity_beans.*;
import com.couponsystemstage3.repositories.*;
import com.couponsystemstage3.types.ClientStatus;
import com.couponsystemstage3.types.CouponStatus;
import com.couponsystemstage3.utils.ArtUtils;
import com.couponsystemstage3.utils.ConsoleColors;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@Order(1)
@RequiredArgsConstructor
public class DummyData implements CommandLineRunner {
    /**
     * CommandLineRunner is a simple Spring Boot interface with a run method.
     * Spring Boot will automatically call the run method of all beans implementing
     * this interface after the application context has been loaded.
     * CommandLineRunner is an interface used to indicate that a bean should run
     * when it is contained within a SpringApplication.
     * A Spring Boot application can have multiple beans implementing CommandLineRunner.
     * These can be ordered with @Order .*/

    private final CustomerRepository customerRepository; // Instead of just @Autowired I used @RequiredArgsConstructor + final
    private final CompanyRepository companyRepository;
    private final CategoryRepository categoryRepository;
    private final CouponRepository couponRepository;
    private final CustomerPurchaseRepository customerPurchaseRepository;


    @Override
    public void run(String... args) throws Exception {
        System.out.println(ConsoleColors.BLUE + ArtUtils.CUSTOMERS + ConsoleColors.RESET);
        Customer customer1 = Customer.builder()
                .firstName("Avi")
                .lastName("Meshulam")
                .email("avi@gmail.com")
                .password("1234")
                .clientStatus(ClientStatus.ACTIVE)
                .build();

        Customer customer2 = Customer.builder()
                .firstName("Reshef")
                .lastName("Kama")
                .email("eeshef@gmail.com")
                .password("5678")
                .clientStatus(ClientStatus.ACTIVE)
                .build();

        Customer customer3 = Customer.builder()
                .firstName("Elena")
                .lastName("Petina")
                .email("elena_pe@gmail.com")
                .password("777")
                .clientStatus(ClientStatus.ACTIVE)
                .build();

        customerRepository.saveAll(Arrays.asList(customer1,customer2,customer3));
        customerRepository.findAll().forEach(System.out.format(ConsoleColors.BLUE)::println);

        System.out.println(ConsoleColors.PURPLE_BRIGHT + ArtUtils.COMPANIES + ConsoleColors.RESET);
        Company company1 = Company.builder()
                .name("Cola")
                .email("cola@gmail.com")
                .password("1234")
                .clientStatus(ClientStatus.ACTIVE)
                .build();

        Company company2 = Company.builder()
                .name("Pepsi")
                .email("pepsi@gmail.com")
                .password("3232")
                .clientStatus(ClientStatus.ACTIVE)
                .build();

        Company company3 = Company.builder()
                .name("Ivory")
                .email("ivory@gmail.com")
                .password("5555")
                .clientStatus(ClientStatus.ACTIVE)
                .build();

        companyRepository.saveAll(Arrays.asList(company1,company2,company3));
        companyRepository.findAll().forEach(System.out.format(ConsoleColors.PURPLE_BRIGHT)::println);

        System.out.println(ConsoleColors.RED+ArtUtils.CATEGORIES+ConsoleColors.RESET);
        Category category1 = Category.builder()
                .name("Shopping")
                .build();

        Category category2 = Category.builder()
                .name("Sport")
                .build();

        Category category3 = Category.builder()
                .name("PC")
                .build();

        Category category4 = Category.builder()
                .name("Traveling")
                .build();

        categoryRepository.saveAll(Arrays.asList(category1, category2, category3, category4));
        categoryRepository.findAll().forEach(System.out.format(ConsoleColors.RED)::println);

        Coupon coupon1 = Coupon.builder()
                .company(companyRepository.getById(1L))
                .category(categoryRepository.getById(1L))
                .title("Cola bottles-glass")
                .description("all drinks")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(12))
                .amount(100)
                .price(9.9)
                .image("https://picsum.photos/200")
                .couponStatus(CouponStatus.ABLE)
                .build();

        Coupon coupon2 = Coupon.builder()
                .company(companyRepository.getById(2L))
                .category(categoryRepository.getById(1L))
                .title("1+1")
                .description("all Pepsi drinks")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(15))
                .amount(100)
                .price(8.9)
                .image("https://picsum.photos/200")
                .couponStatus(CouponStatus.ABLE)
                .build();

        Coupon coupon3 = Coupon.builder()
                .company(companyRepository.getById(3L))
                .category(categoryRepository.getById(3L))
                .title("2+1")
                .description("all printers")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(12))
                .amount(100)
                .price(9.9)
                .image("https://picsum.photos/200")
                .couponStatus(CouponStatus.ABLE)
                .build();

        Coupon coupon4 = Coupon.builder()
                .company(companyRepository.getById(2L))
                .category(categoryRepository.getById(1L))
                .title("3+1")
                .description("Pepsi cans")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(15))
                .amount(100)
                .price(9.9)
                .image("https://picsum.photos/200")
                .couponStatus(CouponStatus.ABLE)
                .build();

        System.out.println(ConsoleColors.CYAN + ArtUtils.COUPONS + ConsoleColors.RESET);

        couponRepository.saveAll(Arrays.asList(coupon1,coupon2,coupon3, coupon4));
        couponRepository.findAll().forEach(System.out.format(ConsoleColors.CYAN)::println);
        CustomerPurchase customerPurchase1 = CustomerPurchase.builder()
                .customer(customerRepository.getCustomerById(1L))
                .coupon(couponRepository.getCouponById(1L))
                .couponTitle(couponRepository.getById(1L).getTitle())
                .customerName(customerRepository.getById(1L).getFirstName())
                .purchaseDateTime(LocalDateTime.now())
                .build();

        CustomerPurchase customerPurchase2 = CustomerPurchase.builder()
                .customer(customerRepository.getCustomerById(2L))
                .coupon(couponRepository.getCouponById(2L))
                .couponTitle(couponRepository.getById(2L).getTitle())
                .customerName(customerRepository.getById(2L).getFirstName())
                .purchaseDateTime(LocalDateTime.now())
                .build();

        CustomerPurchase customerPurchase3 = CustomerPurchase.builder()
                .customer(customerRepository.getCustomerById(3L))
                .coupon(couponRepository.getCouponById(3L))
                .couponTitle(couponRepository.getById(3L).getTitle())
                .customerName(customerRepository.getById(3L).getFirstName())
                .purchaseDateTime(LocalDateTime.now())
                .build();

        CustomerPurchase customerPurchase4 = CustomerPurchase.builder()
                .customer(customerRepository.getCustomerById(3L))
                .coupon(couponRepository.getCouponById(4L))
                .couponTitle(couponRepository.getById(4L).getTitle())
                .customerName(customerRepository.getById(3L).getFirstName())
                .purchaseDateTime(LocalDateTime.now())
                .build();

        CustomerPurchase customerPurchase5 = CustomerPurchase.builder()
                .customer(customerRepository.getCustomerById(2L))
                .coupon(couponRepository.getCouponById(1L))
                .couponTitle(couponRepository.getById(1L).getTitle())
                .customerName(customerRepository.getById(2L).getFirstName())
                .purchaseDateTime(LocalDateTime.now())
                .build();

        System.out.println(ConsoleColors.GREEN + ArtUtils.CUSTOMERS_PURCHASES + ConsoleColors.RESET);
        customerPurchaseRepository.saveAll(Arrays.asList(customerPurchase1, customerPurchase2, customerPurchase3, customerPurchase4, customerPurchase5));
        customerPurchaseRepository.findAll().forEach(System.out.format(ConsoleColors.GREEN) :: println);


        /*Coupon.builder() // to build an empty object*/
        /*Coupon.builder().build(); // return built object*/
        /*Coupon.builder().__"setter injection"__.build();// change the instance */
    }
}
