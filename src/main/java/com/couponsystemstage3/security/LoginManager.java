package com.couponsystemstage3.security;

import com.couponsystemstage3.exceptions.CouponSystemException;
import com.couponsystemstage3.exceptions.ErrMsg;
import com.couponsystemstage3.services.ClientService;
import com.couponsystemstage3.services.CompanyServiceImpl;
import com.couponsystemstage3.services.CustomerServiceImpl;
import com.couponsystemstage3.services.AdminServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // Injects @(Autowired) on C'tor of all final attributes!
public class LoginManager {

    private final ApplicationContext context;

    private final AdminServiceImpl adminServiceImpl; // Singleton

    @Autowired
    protected CompanyServiceImpl companyServiceImpl; // Singleton
    @Autowired
    protected CustomerServiceImpl customerServiceImpl; // Singleton

    /*
     * The method log in the client:
     * the method checks by email and password param if client exists, and by clientType param which ClientFacade should return.
     * returns an abstract client facade if the client exists(has the correct credentials) and has correct client type, otherwise throws CouponSystemException.
     * null is returned when the ClientType doesn't exists.*/
    public ClientService login(String email, String password, ClientType clientType) throws CouponSystemException {
        if (clientType == ClientType.ADMINISTRATOR) {
            if (adminServiceImpl.login(email, password)) {
                return context.getBean(AdminServiceImpl.class);
            }
        } else if (clientType == ClientType.COMPANY) {
            if (companyServiceImpl.login(email, password)) {
                Long companyId = this.companyServiceImpl.loginCompanyReturnId(email, password); // if we need to save the ID in service =>Service will be Scope(Singleton).
                CompanyServiceImpl companyServiceImpl = context.getBean(CompanyServiceImpl.class);
                companyServiceImpl.setCompanyId(companyId);
                return companyServiceImpl;
            }
        } else if (clientType == ClientType.CUSTOMER) {
            if (customerServiceImpl.login(email, password)) {
                Long customerId = this.customerServiceImpl.loginCustomerReturnId(email, password);
                CustomerServiceImpl customerServiceImpl = context.getBean(CustomerServiceImpl.class);
                customerServiceImpl.setCustomerId(customerId);
                return customerServiceImpl;
            }
        }
        throw new CouponSystemException(ErrMsg.BAD_REQUEST);
    }
}
