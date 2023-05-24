package com.couponsystemstage3.repositories;

import com.couponsystemstage3.entity_beans.Category;
import com.couponsystemstage3.entity_beans.Company;
import com.couponsystemstage3.entity_beans.Coupon;
import com.couponsystemstage3.types.CouponStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Coupon getCouponById(Long id);

    List<Coupon> findCompanyCouponsByCompanyId(Long companyId);

    Coupon getCouponByIdAndTitle(Long id, String title);

    boolean existsByIdAndTitle(Long id, String title);

    List<Coupon> findByCouponStatus(CouponStatus couponStatus);

    Set<Coupon> findByDescriptionEndingWith(String description);

    boolean existsByCompanyId(Long companyId);

    boolean existsByTitleAndCompanyId(String title, Long companyId);

    Optional<Coupon> findByIdAndCouponStatus(Long id, CouponStatus couponStatus);

    @Modifying
    @Query(value = "UPDATE Coupon c SET c.category = ?1, c.title = ?2, c.description = ?3, c.startDate = ?4, c.endDate =?5, c.amount= ?6, c.price= ?7, c.image= ?8 WHERE c.id = ?9"/*, nativeQuery = true*/)
    Integer updateCoupon(Category category, String title, String description, LocalDateTime startDate, LocalDateTime endDate, int amount, double price, String image, Long id);

    List<Coupon> findByCategoryAndCompanyId(Category category, Long companyId);

    List<Coupon> findAllByCompanyId(Long companyId);

    Coupon getCouponByIdAndCompanyId(Long id, Long companyId);

    boolean existsByIdAndCompanyId(Long id, Long companyId);

    List<Coupon> findByCategoryAndCompanyIdAndCouponStatus(Category category, Long companyId, CouponStatus couponStatus);

    @Query(value = "SELECT max(price) FROM Coupon c WHERE c.company = ?1")
    double findMaxPrice(Company company);

    @Query(value = "SELECT * FROM coupons c WHERE c.price < ?1 AND c.company_id = ?2", nativeQuery = true)
    List<Coupon> findFromCouponsOfTheCompany(double maxPrice, Long companyId);

    Coupon getCouponByTitle(String title);

    Coupon getByCategory(Category category4);

    @Query(value = "SELECT * FROM coupons as c join customer_purchases as cvc on cvc.customer_id = ?1 WHERE cvc.coupon_id = c.id AND c.category_id = ?2", nativeQuery = true)
    List<Coupon> findByCustomerIdAndCategoryId(Long customerId, Long categoryId);

    @Query(value = "SELECT max(price) FROM coupons as c join customer_purchases as cvc on cvc.customer_id = ?1 WHERE cvc.coupon_id = c.id", nativeQuery = true)
    double findMaxPrice(Long customerId);

    List<Coupon> findAllByEndDateBefore(LocalDateTime localDateTime);
    boolean existsByTitle(String title);
    Coupon findCouponByIdAndTitle(Long id, String title);
    List<Coupon> findAllById(Long companyId);

    List<Coupon> findByCategory(@NotNull Category category);
}
