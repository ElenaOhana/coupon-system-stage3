package com.couponsystemstage3.clr;

import com.couponsystemstage3.entity_beans.Company;
import com.couponsystemstage3.entity_beans.Customer;
import com.couponsystemstage3.exceptions.CouponSystemException;
import com.couponsystemstage3.repositories.CompanyRepository;
import com.couponsystemstage3.repositories.CustomerRepository;
import com.couponsystemstage3.security.ClientType;
import com.couponsystemstage3.security.LoginManager;
import com.couponsystemstage3.services.AdminService;
import com.couponsystemstage3.types.ClientStatus;
import com.couponsystemstage3.utils.ArtUtils;
import com.couponsystemstage3.utils.ConsoleColors;
import com.couponsystemstage3.utils.TestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Order(2)
@Component
public class AdminServiceTest implements CommandLineRunner {
    @Autowired
    private AdminService adminService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LoginManager loginManager;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(ConsoleColors.YELLOW_BOLD + ArtUtils.ADMIN_SERVICE_TEST);
        TestUtils.printTest("bad logging");
        try {
            System.out.println(loginManager.login("Jordan@gmail.com", "1111", ClientType.ADMINISTRATOR));
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest("good logging");
        System.out.println(loginManager.login("admin@admin.com", "admin", ClientType.ADMINISTRATOR));

        Company existingCompany= companyRepository.getById(1L);
        Company companyToAdd = Company.builder()
                .name(existingCompany.getName())
                .email("elena@gmail.com")
                .password("5757")
                .clientStatus(ClientStatus.ACTIVE)
                .build();

        TestUtils.printTest("add company will fail due to existing company name");
        try {
            adminService.addCompany2(companyToAdd);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest("add company will fail due to existing company mail");
        companyToAdd.setName("Elena LTD");
        companyToAdd.setEmail(existingCompany.getEmail());
        try {
            adminService.addCompany2(companyToAdd);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest("add company will succeed");
        companyToAdd.setEmail("elena@gmail.com");
        try {
            adminService.addCompany2(companyToAdd);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        companyRepository.findAll().forEach(System.out :: println);

        /////////////////////////////////////////////////////

        TestUtils.printTest("update company will fail due to not allowed to change the company id");
        Company newDataToCompany = Company.builder()
                /*.id(5L)*/ //It is not recommended because it will rewrite the existing entity in DB if it exists already.
                .name("Neviot")
                .email("neviot@gmail.com")
                .password("5757")
                .clientStatus(ClientStatus.ACTIVE)
                .build();
        Long companyId = 1L;
        try {
            adminService.updateCompanyById(companyId, newDataToCompany);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest("update company will fail due to not allowed to change the company name");
        newDataToCompany.setId(1L);
        newDataToCompany.setName("Choco-Pie");
        try {
            adminService.updateCompanyById(companyId, newDataToCompany);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        TestUtils.printTest("update company email will succeed");
        newDataToCompany.setName("Cola");
        newDataToCompany.setEmail("cola_cola_upd@gmail.com");
        try {
            adminService.updateCompanyById(companyId, newDataToCompany);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(companyRepository.getCompanyById(1L));

        TestUtils.printTest("update company password will succeed");
        newDataToCompany.setPassword("222222");
        try {
            adminService.updateCompanyById(companyId, newDataToCompany);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        companyRepository.findAll().forEach(System.out :: println);

        TestUtils.printTest("update company password will fail due to update the email to existing email");
        newDataToCompany.setId(3L);
        newDataToCompany.setName("Ivory");
        newDataToCompany.setEmail("cola_cola_upd@gmail.com");
        companyId = 3L;
        try {
            adminService.updateCompanyById(companyId, newDataToCompany);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        TestUtils.printTest("update company password will succeed because updating with equals emails");
        newDataToCompany.setId(3L);
        newDataToCompany.setName("Ivory");
        newDataToCompany.setEmail("ivory@gmail.com");
        newDataToCompany.setPassword("0000");
        companyId = 3L;
        try {
            adminService.updateCompanyById(companyId, newDataToCompany);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(companyRepository.getCompanyById(3L));

        // Test for complex method update with only one parameter
       /* TestUtils.printTest("update company will fail due to trying to change the company with other Id/or with other name.");
        existingCompany.setEmail("cola_cola_upd@gmail.com");
        try {
            adminService.updateCompany(existingCompany);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }*/

        //DELETE COMPANY//////////////////////////DELETE COMPANY////////////////////////DELETE COMPANY
        TestUtils.printTest("delete company will fail due to passing not existing id.");
        try {
            adminService.deleteCompanyAsChangeStatus(10L);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        companyId = 2L;
        TestUtils.printTest("delete company with id "+ companyId +" will succeed");
        try {
            adminService.deleteCompanyAsChangeStatus(companyId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(companyRepository.getById(companyId));

        ////////GET ALL COMPANIES
        TestUtils.printTest("get all companies succeed");
        try {
            List<Company> companyList = adminService.getAllCompanies();
            companyList.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        ///////// GET ONE COMPANY BY ID
        TestUtils.printTest("get one Company by id fail due to passing not existing id.");
        try {
            Company companyById = adminService.getOneCompany(23L);
            System.out.println(companyById);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest("get one Company by id succeed");
        try {
            Company companyById = adminService.getOneCompany(3L);
            System.out.println(companyById);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        ////////////CUSTOMER/////////////////////////////////////////////////////////////////
        TestUtils.printTest("add Customer will fail due to existing customer email");
        Customer existingCustomer = customerRepository.getById(1L);
        Customer customerToAdd = Customer.builder()
                .firstName("Gregory")
                .lastName("Katz")
                .clientStatus(ClientStatus.ACTIVE)
                .email(existingCustomer.getEmail())
                .password("345345")
                .build();
        try {
            adminService.addCustomer(customerToAdd);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest("add Customer will succeed");
        customerToAdd.setEmail("greg&comp@gmail.com");
        try {
            adminService.addCustomer(customerToAdd);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        customerRepository.findAll().forEach(System.out::println);

        ////////////UPDATE CUSTOMER BY NAME - DOES NOT NEED TO PROJECT DEMANDS, IT IS MY ADDITIONAL PRACTICE//////////
        /*TestUtils.printTest("update customer will fail due to not allowed to change the customer id");
        existingCustomer.setId(4L);
        try {
            adminService.updateCustomerByName(existingCustomer, "Avi");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest("update customer will fail due to not allowed to change the customer id");
        existingCustomer.setId(6L);
        try {
            adminService.updateCustomerByName(existingCustomer,"Reshef");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest("update customer will fail due to blank name receiving parameter");
        try {
            adminService.updateCustomerByName(customerToAdd, "");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest("update customer will fail due to null name receiving parameter(first name like this does not exists in db)");
        try {
            adminService.updateCustomerByName(existingCustomer,"Res");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest("update customer password will succeed");
        customerToAdd.setPassword("7777");
        try {
            adminService.updateCustomerByName(customerToAdd, "Gregory");
            System.out.println(customerRepository.getCustomerByFirstName("Gregory"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest("update customer first name will succeed");
        customerToAdd.setFirstName("Greg");
        try {
            adminService.updateCustomerByName(customerToAdd, "Gregory");
            System.out.println(customerRepository.getCustomerByFirstName("Greg"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest("update customer last name will succeed");
        customerToAdd.setLastName("Kazts");
        try {
            adminService.updateCustomerByName(customerToAdd, "Greg");
            System.out.println(customerRepository.getCustomerByFirstName("Greg")  + ConsoleColors.RESET);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }*/

        ////////////UPDATE CUSTOMER////////////////////////////
        TestUtils.printTest("update customer will fail due to not allowed to change the customer id");
        Customer newDataToCustomer = Customer.builder()
                .id(5L)
                .email("anna_an44Gmail.com")
                .firstName("Anna")
                .lastName("Gershkovich")
                .password("4444")
                .build();
        Long customerId = 3L;
        try {
            adminService.updateCustomer(customerId, newDataToCustomer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest("update customer data will succeed");
        newDataToCustomer.setId(3L);
        try {
            adminService.updateCustomer(customerId, newDataToCustomer);
            System.out.println(customerRepository.getCustomerById(3L));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest("update customer first name and password will succeed");
        newDataToCustomer.setFirstName("Anita");
        newDataToCustomer.setPassword("5555");
        try {
            adminService.updateCustomer(customerId, newDataToCustomer);
            System.out.println(customerRepository.getCustomerById(3L));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //DELETE CUSTOMER//////////////////////////DELETE CUSTOMER////////////////////////DELETE CUSTOMER
        TestUtils.printTest("delete customer will fail due to passing not existing id.");
        try {
            adminService.deleteCustomerAsChangeStatus(10L);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        customerId = 1L;
        TestUtils.printTest("delete customer with id "+ customerId +" will succeed");
        try {
            adminService.deleteCustomerAsChangeStatus(customerId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(customerRepository.getById(customerId));

        ////////GET ALL CUSTOMERS
        TestUtils.printTest("get all customers succeed");
        try {
            List<Customer> customerList = adminService.getAllCustomers();
            customerList.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        ///////// GET ONE CUSTOMER BY ID
        TestUtils.printTest("get one Customer by id fail due to passing not existing id.");
        try {
            Customer customerById = adminService.getOneCustomer(50L);
            System.out.println(customerById);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest("get one Customer by id succeed");
        try {
            Customer customerById = adminService.getOneCustomer(3L);
            System.out.println(customerById);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println( ConsoleColors.RESET); // ADMIN SERVICE TEST IS DONE!!!
    }
}
