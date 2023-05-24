package com.couponsystemstage3.services;

import com.couponsystemstage3.entity_beans.Category;
import com.couponsystemstage3.entity_beans.Company;
import com.couponsystemstage3.entity_beans.Coupon;
import com.couponsystemstage3.entity_beans.CustomerPurchase;
import com.couponsystemstage3.exceptions.CouponSystemException;
import com.couponsystemstage3.exceptions.ErrMsg;
import com.couponsystemstage3.types.ClientStatus;
import com.couponsystemstage3.types.CouponStatus;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@Validated
@Getter
@Setter
@ToString
@AllArgsConstructor
public class CompanyServiceImpl extends ClientService implements CompanyService{

    private Long companyId;

    public CompanyServiceImpl(){
        super();
    }

    public boolean login(String email, String password) throws CouponSystemException {
        if (!companyRepository.existsByEmailAndPassword(email, password)) {
            throw new CouponSystemException(ErrMsg.INVALID_EMAIL_OR_PASSWORD);
        }
        return true;
    }

    public Long loginCompanyReturnId(String email, String password) throws CouponSystemException {
        if (companyRepository.existsByEmailAndPasswordAndClientStatus(email, password, ClientStatus.ACTIVE)){
            return companyRepository.findByEmailAndPasswordAndClientStatus(email, password, ClientStatus.ACTIVE).getId();
        } else{
            throw new CouponSystemException(ErrMsg.BAD_REQUEST);
        }
    }

    /**
     * The method receives the coupon,
     * checks:
     * 1) if the coupon belongs to received company,
     * 2) if the title of the coupon exists in received company,
     *  if company doesn't have the coupon with the same title -
     *  - the method adds received coupon,
     *  otherwise CouponSystemException is thrown.
     */
    public Coupon addCoupon(@NotNull(message = "The Coupon must not be null") Coupon coupon) throws CouponSystemException{
        if (coupon.getCompany().getId().equals(companyId)) {
            if (couponRepository.existsByTitleAndCompanyId(coupon.getTitle(), companyId)) {
                throw new CouponSystemException(ErrMsg.TITLE_EXISTS);
            } else {
                return couponRepository.save(coupon);
            }
        } else {
            throw new CouponSystemException(ErrMsg.ILLEGAL_ACTION);
        }
    }

    public Integer updateCoupon(@NotNull(message = "The couponId must not be null") Long couponId,
                             @NotNull(message = "The Coupon must not be null") Coupon coupon) throws CouponSystemException {
        Coupon couponFromDb = couponRepository.getCouponById(couponId);
        if (couponFromDb.getCompany().getId().equals(companyId)) {
            if (!couponId.equals(coupon.getId())) {
                throw new CouponSystemException(ErrMsg.UPDATE_COUPON_ID);
            }
            if (!couponFromDb.getCompany().getId().equals(coupon.getCompany().getId())) {
                throw new CouponSystemException((ErrMsg.UPDATE_COMPANY_ID_IN_COUPON));
            }
            //additional check as a result from Project requirements to method add()
            if ((!couponFromDb.getTitle().equals(coupon.getTitle())) && (couponRepository.existsByTitleAndCompanyId(coupon.getTitle(), companyId))) {
                throw new CouponSystemException(ErrMsg.TITLE_EXISTS);
            }
            return couponRepository.updateCoupon(coupon.getCategory(), coupon.getTitle(), coupon.getDescription(), coupon.getStartDate(), coupon.getEndDate(), coupon.getAmount(), coupon.getPrice(), coupon.getImage(), coupon.getId());
        } else {
            throw new CouponSystemException(ErrMsg.ILLEGAL_ACTION);
        }
    }


    /**
     * By receiving the coupon Id the method checks if that coupon belongs to logged in company,
     * changes status of company coupons to DISABLE,
     * deletes customer purchase from customers_vs_coupons table. If some of those operations did not succeed - the CouponSystemException is thrown.
     * In success returned 1.
     */
    public Integer removeCoupon(@NotNull Long id) throws CouponSystemException {
        if (couponRepository.findById(id).isPresent()) {
            Coupon coupon = couponRepository.getCouponById(id);
            if (coupon.getCompany().getId().equals(companyId)) {
                //deletes customer purchase from customers_purchases table
                List<CustomerPurchase> customerPurchases = customerPurchaseRepository.findByCouponId(id);
                if (!customerPurchases.isEmpty()) {
                    customerPurchases.forEach(cp -> customerPurchaseRepository.deleteByCouponId(id));
                }
                //changes status to DISABLE of the coupon
                coupon.setCouponStatus(CouponStatus.DISABLE);
            } else {
                throw new CouponSystemException(ErrMsg.ILLEGAL_ACTION);
            }
        } else {
            throw new CouponSystemException(ErrMsg.ID_NOT_FOUND);
        }
        return 1;
    }

    public Coupon getCouponByIdOfConnectedCompany(Long id) throws CouponSystemException {
        if (!couponRepository.existsByIdAndCompanyId(id, companyId)) {
            throw new CouponSystemException(ErrMsg.ILLEGAL_ACTION);
        } else {
            return couponRepository.getCouponByIdAndCompanyId(id, companyId);
        }
    }

    public Company findById(Long id) {
        return companyRepository.getById(id);
    }

    public Company getCompanyDetails() {
        return companyRepository.getById(companyId);
    }

    public List<Coupon> getAllCompanyCoupons(){
        return couponRepository.findAllByCompanyId(companyId);
    }

    public Coupon getCouponByIdAndTitle(Long id, String title) throws CouponSystemException{
        if (!couponRepository.existsByIdAndTitle(id, title)) {
            throw new CouponSystemException(ErrMsg.NO_ID_AND_TITLE_MATCH);
        } else {
            return couponRepository.getCouponByIdAndTitle(id, title);
        }
    }

    public List<Coupon> getCompanyCouponsByCategory(Category category) {
        return couponRepository.findByCategoryAndCompanyId(category, companyId);
    }

    public List<Coupon> getCompanyCouponsByCategoryAndStatus(Category category, CouponStatus couponStatus) {
        return couponRepository.findByCategoryAndCompanyIdAndCouponStatus(category, companyId, couponStatus);
    }

    public double getMaxPriceOfCouponsOfCompany() {
        Company company = companyRepository.getById(companyId);
        return couponRepository.findMaxPrice(company);
    }

    public Coupon getMostExpensiveCoupon() throws CouponSystemException{
        List<Coupon> coupons = couponRepository.findAll();
        Coupon expensiveCoupon = coupons.stream().sorted((c1, c2)-> (int)(c2.getPrice() - c1.getPrice())).findFirst().orElseThrow(CouponSystemException::new);
        return expensiveCoupon;
    }

    public Coupon getLeastExpensiveCoupon() throws CouponSystemException{
        List<Coupon> coupons = couponRepository.findAll();
        Coupon cheapCoupon = coupons.stream().sorted((c1, c2)-> (int)(c1.getPrice() - c2.getPrice())).findFirst().orElseThrow(()-> new CouponSystemException(ErrMsg.COUPON_DOES_NOT_EXIST));
        return  cheapCoupon;
    }

    public List<Coupon> findFromCompanyCouponsUpToMaxPrice(){
        double maxPrice = getMaxPriceOfCouponsOfCompany();
        return couponRepository.findFromCouponsOfTheCompany(maxPrice, companyId);
    }

    // My query)) == getExistingCouponsCategories() == that CouponStatus=CouponStatus.ABLE
    // Like in health system)) getMembersWhoGotTwoCoronaVaccines()
    public Set<Category> getAbleCouponsCategories() {
        return
        couponRepository
                .findByCouponStatus(CouponStatus.ABLE)
                .stream()
                .map(cr -> cr.getCategory())// ==  .map(Coupon::getCategory) - method reference
                .collect(Collectors.toSet());
    }
    // My query)) == getExistingCouponsCategories() == that CouponStatus=CouponStatus.ABLE
    // Like in health system)) getMembersWhoGotTwoCoronaVaccines()
    public List<Category> getDrinksCouponsCategories() { // Returns duplicates, so instead of "Distinct" I've done Set<>
        return
                couponRepository
                        .findByDescriptionEndingWith("drinks")
                        .stream()
                        .map(cr -> cr.getCategory())
                        .collect(Collectors.toList());
    }

    public Set<Category> getDisableCouponsCategories() {
        return
                couponRepository
                .findByCouponStatus(CouponStatus.DISABLE)
                .stream()
                .map(cr -> cr.getCategory())
                .collect(Collectors.toSet());
    }

    public Coupon getCouponByIdAndCouponStatus(Long id, CouponStatus couponStatus) throws CouponSystemException {
        return couponRepository.findByIdAndCouponStatus(id, couponStatus).orElseThrow(()-> new CouponSystemException(ErrMsg.ID_AND_COUPON_STATUS_NOR_FOUND));
    }


    /**
     * The method receives the coupon and company Id and checks:
     * 1) if each coupon belongs to received company,
     * 2) if the title of each coupon doesn't exists in received company,
     * 3) if company doesn't have this coupon -
     *  - the method adds received coupon,
     *  otherwise CouponSystemException is thrown.
     */
    // This method match for CompanyServiceImpl as a prototype, because prototype do not hold CompanyServiceImpl attribute for next/each! query!!, =>
    // so I need to pass the companyId/company as a parameter to this function.
    // @Scope(value = "prototype"),(as for Singleton also) Do not use in this project!
    /*public Coupon createCoupon2(@NotNull(message = "The Coupon must not be null") Coupon coupon,
                                @NotNull(message = "Company Id must not be null") Long companyId) throws CouponSystemException {
        if (couponRepository.existsByTitleAndCompanyId(coupon.getTitle(), companyId)) {
            throw new CouponSystemException(ErrMsg.TITLE_EXISTS);
        } else {
            return couponRepository.save(coupon);
        }
    }*/
}
