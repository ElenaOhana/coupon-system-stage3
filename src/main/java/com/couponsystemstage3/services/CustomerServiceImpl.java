package com.couponsystemstage3.services;

import com.couponsystemstage3.entity_beans.Coupon;
import com.couponsystemstage3.entity_beans.Customer;
import com.couponsystemstage3.entity_beans.CustomerPurchase;
import com.couponsystemstage3.exceptions.CouponSystemException;
import com.couponsystemstage3.exceptions.ErrMsg;
import com.couponsystemstage3.types.ClientStatus;
import com.couponsystemstage3.types.CouponStatus;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
@Validated
@Data
public class CustomerServiceImpl extends ClientService implements CustomerService{

    private Long customerId;

    public CustomerServiceImpl(){
        super();
    }

    public boolean login(String email, String password) throws CouponSystemException{
        if (!customerRepository.existsByEmailAndPassword(email, password)) {
            throw new CouponSystemException(ErrMsg.INVALID_EMAIL_OR_PASSWORD);
        }
        return true;
    }

    public Long loginCustomerReturnId(String email, String password) throws CouponSystemException {
        if (customerRepository.existsByEmailAndPasswordAndClientStatus(email, password, ClientStatus.ACTIVE)){
            return customerRepository.findByEmailAndPasswordAndClientStatus(email, password, ClientStatus.ACTIVE).getId();
        } else{
            throw new CouponSystemException(ErrMsg.BAD_REQUEST);
        }
    }

    /**
     * Work in front of CustomerPurchaseRepository */
    public CustomerPurchase createCustomerPurchase(@NotNull CustomerPurchase customerPurchase) throws CouponSystemException {
        Long couponId = customerPurchase.getCoupon().getId();
        Coupon couponFromDb = couponRepository.getCouponById(couponId);

        CustomerPurchase newCustomerPurchase = CustomerPurchase.builder()
                .customer(customerPurchase.getCustomer())
                .coupon(couponFromDb)
                .couponTitle(couponFromDb.getTitle())
                .customerName(customerPurchase.getCustomerName())
                .purchaseDateTime(LocalDateTime.now())
                .build();

        if (customerPurchase.getCustomer().getId() != null) {
            if (customerPurchase.getCustomer().getId().equals(customerId)) {
                if (couponFromDb.getAmount() <= 0) {
                    throw new CouponSystemException(ErrMsg.NEGATIVE_COUPON_AMOUNT);
                }
                if (couponFromDb.getEndDate().isBefore(LocalDateTime.now())) {
                    throw new CouponSystemException(ErrMsg.COUPON_OVERDUE);
                }
                if (customerPurchaseRepository.existsByCustomerIdAndCouponId(customerPurchase.getCustomer().getId(), couponFromDb.getId())) {
                    throw new CouponSystemException(ErrMsg.COUPON_HAD_BOUGHT);
                }
                if (couponFromDb.getCouponStatus().equals(CouponStatus.DISABLE)) {
                    throw new CouponSystemException(ErrMsg.COUPON_STATUS_DISABLE);
                }
                int newAmount = couponFromDb.getAmount();
                newAmount--;
                couponFromDb.setAmount(newAmount);
                couponRepository.updateCoupon(couponFromDb.getCategory(), couponFromDb.getTitle(),
                        couponFromDb.getDescription(), couponFromDb.getStartDate(), couponFromDb.getEndDate(),
                        couponFromDb.getAmount(), couponFromDb.getPrice(), couponFromDb.getImage(), couponFromDb.getId());
                newCustomerPurchase.setCoupon(couponFromDb);
                customerPurchaseRepository.save(newCustomerPurchase);
                return newCustomerPurchase;
            }
        }
        throw new CouponSystemException(ErrMsg.ILLEGAL_ACTION);
    }

    public List<CustomerPurchase> getCustomerPurchasesByCustomerId(Long id) throws CouponSystemException {
        if (!id.equals(customerId)) {
            throw new CouponSystemException(ErrMsg.ILLEGAL_ACTION);
        }
        return customerPurchaseRepository.findByCustomerId(id);
    }
    public List<CustomerPurchase> getCustomerPurchases() {
        return customerPurchaseRepository.findByCustomerId(customerId);
    }

    public List<Coupon> getCustomerPurchasesOfConnectedCustomerByCategoryId(Long categoryId){
        return couponRepository.findByCustomerIdAndCategoryId(customerId, categoryId);
    }

    public double findMaxPriceOfCustomer() {
        return couponRepository.findMaxPrice(customerId);
    }

    public List<Coupon> getCouponListLessThanMaxPrice(){
        List<CustomerPurchase> customerPurchases = customerPurchaseRepository.findByCustomerId(customerId);
        List<Coupon> couponResultList = new ArrayList<>();
        for (CustomerPurchase customerPurchase : customerPurchases) {
            double priceOfDBCouponsOfCustomer = customerPurchase.getCoupon().getPrice();
            if (priceOfDBCouponsOfCustomer < findMaxPriceOfCustomer()) {
                couponResultList.add(customerPurchase.getCoupon());
            }
        }
        return couponResultList;
    }

    public Customer getCustomerDetails() {
        return customerRepository.getCustomerById(customerId);
    }

    public CustomerPurchase getCustomerPurchaseByCustomerIdAndCouponId(Long customerId, Long couponId) throws CouponSystemException {
        return customerPurchaseRepository.findByCustomerIdAndCouponId(customerId, couponId);
    }

}
