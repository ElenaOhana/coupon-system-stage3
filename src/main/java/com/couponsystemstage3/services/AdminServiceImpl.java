package com.couponsystemstage3.services;

import com.couponsystemstage3.entity_beans.Company;

import com.couponsystemstage3.entity_beans.Coupon;
import com.couponsystemstage3.entity_beans.Customer;
import com.couponsystemstage3.exceptions.CouponSystemException;
import com.couponsystemstage3.exceptions.ErrMsg;
import com.couponsystemstage3.types.ClientStatus;
import com.couponsystemstage3.types.CouponStatus;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@Transactional
@Slf4j
@Validated
@Data
public class AdminServiceImpl extends ClientService implements AdminService{

    public AdminServiceImpl(){
        super();
    }

    /**
     * The method checks by email and password param if the admin inserted the right credentials.
     * Authentication
     */
    @Override
    public boolean login(String email, String password) throws CouponSystemException {
        if (!(email.equals("admin@admin.com") && password.equals("admin"))) { // !(_ && _) == (!_ || !_ )  // /* (!email.equals("admin@admin.com")) || (!password.equals("admin"))*/
            throw new CouponSystemException(ErrMsg.INVALID_EMAIL_OR_PASSWORD);
        }
        return true;
    }

    @Override
    public boolean isCompanyExistsByEmailAndName(String email, String name){
        Example<Company> example = Example.of(Company.from(email, name));
        return companyRepository.exists(example);
    }

    /**
     * The method receives the company and checks by company email and name if the company exists in Database, if company doesn't exist - the method adds the company,
     * if the company is null - NotFoundException is thrown,
     * otherwise throws CouponSystemException.
     */
    @Override
    public Company addCompany2(@NotNull Company company) throws CouponSystemException {
        if (companyRepository.existsByName(company.getName())) {
            throw new CouponSystemException(ErrMsg.COMPANY_NAME_EXISTS);
        }
        if (companyRepository.existsByEmail(company.getEmail())) {
            throw new CouponSystemException(ErrMsg.COMPANY_EMAIL_EXISTS);
        }
        return companyRepository.save(company);
    }

    /*
    * The method receives the companyId(what line in company's table in DB I want to update) and company(new company data).
    * */
    // I've changed method signature to (Long companyId, Company company) - easier way to update.
    @Override
    public Integer updateCompanyById(Long companyId, Company company) throws CouponSystemException{
        Company companyFromDb = companyRepository.getCompanyById(companyId);
        if (!companyFromDb.getId().equals(company.getId())) {
            throw new CouponSystemException(ErrMsg.UPDATE_COMPANY_ID);
        }
        if (!companyFromDb.getName().equals(company.getName())) {
            throw new CouponSystemException(ErrMsg.UPDATE_COMPANY_NAME);
        }
        //additional (my) check as a result from Project requirements to method add()
        if ((!companyFromDb.getEmail().equals(company.getEmail())) && (companyRepository.existsByEmail(company.getEmail()))) {
            throw new CouponSystemException(ErrMsg.COMPANY_EMAIL_EXISTS);
        }
        return companyRepository.updateCompany(company.getEmail(), company.getPassword(), company.getId());
    }

    /**
     * The method receives the company and gets by its email and password the company from DB in order to check
     * if the client trying to update the company id or company name. In addition, the method checks if there is a matching between existing company to update and received company.
     */ // This is the more complex way to update the company(without additional parameter - Long companyId).
    @Override
    public Integer updateCompany(@NotNull Company company) throws CouponSystemException{
        Company companyByIdFromDB = companyRepository.getCompanyById(company.getId());
        Company companyByEmailAndPassword = companyRepository.getCompanyByEmailAndPassword(company.getEmail(), company.getPassword());
        if (companyRepository.existsByEmailAndPassword(company.getEmail(), company.getPassword())) {
            if (!companyByEmailAndPassword.getId().equals(company.getId())) {
                throw new CouponSystemException(ErrMsg.UPDATE_ID);
            }
            if (!companyByEmailAndPassword.getName().equals(company.getName())) {
                throw new CouponSystemException(ErrMsg.UPDATE_NAME);
            }
        }
        else { // When there is no Email and Password like that in DB
            if (companyRepository.existsById(company.getId())) {
                if ((companyByIdFromDB.getId().equals(company.getId()) && companyByIdFromDB.getName().equals(company.getName()))) {
                    return companyRepository.updateCompany(company.getEmail(), company.getPassword(), company.getId());
                }else{
                    throw new CouponSystemException(ErrMsg.UPDATE_NO_MATCH);
                }
            } else {
                throw new CouponSystemException(ErrMsg.ID_NOT_FOUND);
            }
        }
        return 0;
    }

    /**
     * By receiving the company Id the method changes status of all coupons that the Company created to DISABLE,
     * deletes customer purchase from customers_purchases table,
     * and changes status to INACTIVE of the company. If id of the company is not found - the CouponSystemException is thrown.
     */
    @Override
    public Integer deleteCompanyAsChangeStatus(Long id) throws CouponSystemException {
        if (companyRepository.findById(id).isPresent()) {
            Company company = companyRepository.findById(id).get();
            //changes status to DISABLE of all coupons that the Company had created
            company.getCoupons().forEach(status -> status.setCouponStatus(CouponStatus.DISABLE));
            //deletes customer purchase of the company from customers_purchases table
            List<Coupon> couponList = couponRepository.findCompanyCouponsByCompanyId(id);
            couponList.forEach(c -> customerPurchaseRepository.deleteById(c.getId()));
            //changes status to INACTIVE of the company (clientStatus=INACTIVE)
            company.setClientStatus(ClientStatus.INACTIVE);
            return 1;
        } else {
            throw new CouponSystemException(ErrMsg.ID_NOT_FOUND);
        }
    }

    /**
     * The method gets all companies(includes their coupons) with ACTIVE ClientStatus from DB and returns them.
     */
    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.getAllByClientStatus(ClientStatus.ACTIVE);
    }

    /**
     * By receiving the company Id the method gets one company(includes its coupons) from DB and returns it.
     * The CouponSystemException is thrown when the Throwable is thrown by orElseThrow() method.
     */
    public Company getOneCompany(Long id) throws CouponSystemException {
        return companyRepository.findById(id).orElseThrow(
                () -> new CouponSystemException(ErrMsg.ID_NOT_FOUND));
    }

    @Override
    public Customer addCustomer(Customer customer) throws CouponSystemException {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new CouponSystemException(ErrMsg.CUSTOMER_MAIL_EXISTS);
        }
        return customerRepository.save(customer);
    }

    @Override
    public Integer updateCustomer(@NotNull Long customerId, @NotNull Customer customer) throws CouponSystemException {
        Customer customerFromDb = customerRepository.getCustomerById(customerId);
        if (!customerFromDb.getId().equals(customer.getId())) {
            throw new CouponSystemException(ErrMsg.UPDATE_CUSTOMER_ID);
        }
        //For real life I would check if there are the changes for: FirstName and LastName and email TOGETHER - than I would conclude that this is a NEW CUSTOMER so, I would delete the old CustomerPurchases for overrided customer.
        return customerRepository.updateCustomer(customer.getEmail(), customer.getFirstName(), customer.getLastName(), customer.getPassword(), customer.getId());
    }

    /*Does not need in this stage*/
    @Override
    public Integer updateCustomerByName(@NotNull Customer customer, @NotBlank(message = "byName parameter must not be blank") String byName) throws CouponSystemException {
        Customer customerFromDbByNAme = customerRepository.getCustomerByFirstName(byName);
        if (customerFromDbByNAme != null) {
            if (!customer.getId().equals(customerFromDbByNAme.getId())) {
                throw new CouponSystemException(ErrMsg.UPDATE_CUSTOMER_ID);
            }
        } else {
            throw new CouponSystemException(ErrMsg.CUSTOMER_NAME_DOES_NOT_EXIST_IN_DB);
        }
        return customerRepository.updateCustomerByName(byName, customer.getFirstName(), customer.getLastName(), customer.getEmail(), customer.getPassword());
    }

    @Override
    public Integer deleteCustomerAsChangeStatus(Long customerID) throws CouponSystemException {
        if (customerRepository.findById(customerID).isPresent()) {
            Customer customerFromDb = customerRepository.findById(customerID).get();
            // changes Customer status to INACTIVE
            customerFromDb.setClientStatus(ClientStatus.INACTIVE);
            // deletes customer purchase from customers_purchases table
            customerPurchaseRepository.deleteByCustomerId(customerID);
            // another way to delete customer purchase from customers_purchases table
            /*List<CustomerPurchase> customerPurchases = customerPurchaseRepository.findByCustomerId(customerID);
            customerPurchases.forEach(cp -> customerPurchaseRepository.deleteById(cp.getId()));*/
            return 1;
        }else {
            throw new CouponSystemException(ErrMsg.ID_NOT_FOUND);
        }
    }

    @Override
    public List<Customer> getAllCustomers(){
        return customerRepository.getAllByClientStatus(ClientStatus.ACTIVE);
    }

    @Override
    public Customer getOneCustomer(Long customerID) throws CouponSystemException {
        return customerRepository.findById(customerID).orElseThrow(() -> new CouponSystemException(ErrMsg.ID_NOT_FOUND));
    }

    //Methods for Category management.(Not necessary but I think it is useful for Admin and must be here)
   /* public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }*/
}
