package com.couponsystemstage3.controllers;

import com.couponsystemstage3.advice.SecurityException;
import com.couponsystemstage3.dto.CompanyDto;
import com.couponsystemstage3.dto.CustomerDto;
import com.couponsystemstage3.entity_beans.Company;
import com.couponsystemstage3.entity_beans.Customer;
import com.couponsystemstage3.exceptions.CouponSystemException;
import com.couponsystemstage3.security.ClientType;
import com.couponsystemstage3.security.request.LoginRequest;
import com.couponsystemstage3.security.response.LoginResponse;
import com.couponsystemstage3.services.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class AdminController extends ClientController {

    public AdminController(){
        super();
    }

    @PostMapping("login")
    @Override
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws CouponSystemException {
        AdminService adminService = (AdminService) loginManager.login(loginRequest.getEmail(), loginRequest.getPassword(), ClientType.ADMINISTRATOR);
        String token = tokenManager.createToken(ClientType.ADMINISTRATOR, -1);
        return new ResponseEntity<>(new LoginResponse(token, ClientType.ADMINISTRATOR, loginRequest.getEmail()), HttpStatus.CREATED);
    }

    @GetMapping("company")
    public ResponseEntity<?> getAllCompanies(@RequestHeader("Authorization") String token) throws SecurityException {
        if (!tokenManager.isTokenExistAndAdmin(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(adminServiceImpl.getAllCompanies(), HttpStatus.OK);
    }


    @GetMapping("company/{id}")
    public ResponseEntity<?> getCompany(@RequestHeader("Authorization") String token, @PathVariable long id) throws SecurityException, CouponSystemException {
        if (!tokenManager.isTokenExistAndAdmin(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(adminServiceImpl.getOneCompany(id), HttpStatus.OK);
    }

    @PutMapping("company/{id}")
    public ResponseEntity<?> updateCompanyById(@RequestHeader("Authorization") String token, @PathVariable long id, @RequestBody Company company) throws SecurityException, CouponSystemException {
        if (!tokenManager.isTokenExistAndAdmin(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(adminServiceImpl.updateCompanyById(id, company), HttpStatus.OK);
    }

    @PostMapping("company")
    @ResponseBody//The Object returned in this method should be serialized into JSON file and to be returned in HTTPResponse body.
    public ResponseEntity<?> addCompany(@RequestHeader("Authorization") String token, @RequestBody Company company) throws SecurityException, CouponSystemException {
        if (!tokenManager.isTokenExistAndAdmin(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        adminServiceImpl.addCompany2(company);
        return new ResponseEntity<>(new CompanyDto(company.getId(), company.getName()), HttpStatus.CREATED);
    }

    @DeleteMapping("company/{id}")
    public ResponseEntity<?> deleteCompany(@RequestHeader("Authorization") String token,@PathVariable long id) throws SecurityException, CouponSystemException {
        if (!tokenManager.isTokenExistAndAdmin(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(adminServiceImpl.deleteCompanyAsChangeStatus(id), HttpStatus.OK);
    }

    // CUSTOMER
    @GetMapping("customer")
    public ResponseEntity<?> getAllCustomers(@RequestHeader("Authorization") String token) throws SecurityException {
        if (!tokenManager.isTokenExistAndAdmin(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(adminServiceImpl.getAllCustomers(), HttpStatus.OK);
    }


    @GetMapping("customer/{id}")
    public ResponseEntity<?> getCustomer(@RequestHeader("Authorization") String token, @PathVariable long id) throws SecurityException, CouponSystemException {
        if (!tokenManager.isTokenExistAndAdmin(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(adminServiceImpl.getOneCustomer(id), HttpStatus.OK);
    }

    @PutMapping("customer/{id}")
    public ResponseEntity<?> updateCustomerById(@RequestHeader("Authorization") String token, @PathVariable long id, @RequestBody Customer customer) throws SecurityException, CouponSystemException {
        if (!tokenManager.isTokenExistAndAdmin(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(adminServiceImpl.updateCustomer(id, customer), HttpStatus.OK);
    }

    @PostMapping("customer")
    @ResponseBody
    public ResponseEntity<?> addCustomer(@RequestHeader("Authorization") String token, @RequestBody Customer customer) throws SecurityException, CouponSystemException {
        if (!tokenManager.isTokenExistAndAdmin(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        adminServiceImpl.addCustomer(customer);
        return new ResponseEntity<>(new CustomerDto(customer.getId(), customer.getFirstName(), customer.getLastName(), customer.getEmail()), HttpStatus.CREATED);
    }

    @DeleteMapping("customer/{id}")
    public ResponseEntity<?> deleteCustomer(@RequestHeader("Authorization") String token,@PathVariable long id) throws SecurityException, CouponSystemException {
        if (!tokenManager.isTokenExistAndAdmin(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(adminServiceImpl.deleteCustomerAsChangeStatus(id), HttpStatus.OK);
    }
}
