package com.couponsystemstage3.controllers;

import com.couponsystemstage3.advice.SecurityException;
import com.couponsystemstage3.dto.CompanyDto;
import com.couponsystemstage3.entity_beans.Category;
import com.couponsystemstage3.entity_beans.Company;
import com.couponsystemstage3.entity_beans.Coupon;
import com.couponsystemstage3.exceptions.CouponSystemException;
import com.couponsystemstage3.security.ClientType;
import com.couponsystemstage3.security.request.LoginRequest;
import com.couponsystemstage3.security.response.LoginResponse;
import com.couponsystemstage3.services.CompanyServiceImpl;
import com.couponsystemstage3.types.CouponStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("company")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class CompanyController extends ClientController {

    public CompanyController() {
        super();
    }

    @PostMapping("login")
    @Override
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws CouponSystemException {
        CompanyServiceImpl companyServiceImpl = (CompanyServiceImpl) loginManager.login(loginRequest.getEmail(), loginRequest.getPassword(), ClientType.COMPANY);
        String token = tokenManager.createToken(ClientType.COMPANY, companyServiceImpl.getCompanyId());
        return new ResponseEntity<>(new LoginResponse(token, ClientType.COMPANY, loginRequest.getEmail()), HttpStatus.CREATED);
    }

    @GetMapping("coupon")
    //@ResponseBody//To force the ResponseEntity return Body
    public ResponseEntity<?> getAllCoupons(@RequestHeader("Authorization") String token) throws CouponSystemException, SecurityException {
        if (!tokenManager.isTokenExistAndCompany(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(companyServiceImpl.getAllCompanyCoupons(), HttpStatus.OK);
    }

    @GetMapping("coupon/{id}")
    public ResponseEntity<?> getOneCoupon(@RequestHeader("Authorization") String token, @PathVariable long id) throws CouponSystemException, SecurityException {
        if (!tokenManager.isTokenExistAndCompany(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(companyServiceImpl.getCouponByIdOfConnectedCompany(id), HttpStatus.OK);
    }

    @PostMapping("coupon")
    public ResponseEntity<?> addCoupon(@RequestHeader("Authorization") String token, @RequestBody Coupon coupon) throws CouponSystemException, SecurityException {
        if (!tokenManager.isTokenExistAndCompany(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(companyServiceImpl.addCoupon(coupon), HttpStatus.CREATED);
    }

    @PutMapping("coupon/{id}")
    public ResponseEntity<?> updateCoupon(@RequestHeader("Authorization") String token, @PathVariable long id, @RequestBody Coupon coupon) throws SecurityException, CouponSystemException {
        if (!tokenManager.isTokenExistAndCompany(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(companyServiceImpl.updateCoupon(id, coupon), HttpStatus.OK);
    }

    @DeleteMapping("coupon/{id}")
    public ResponseEntity<?> deleteCouponById(@RequestHeader("Authorization") String token, @PathVariable long id)throws SecurityException,CouponSystemException{
        if (!tokenManager.isTokenExistAndCompany(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(companyServiceImpl.removeCoupon(id), HttpStatus.OK);
    }

    @GetMapping("details")
    public ResponseEntity<?> getCompanyDetails(@RequestHeader("Authorization") String token) throws SecurityException {
        if (!tokenManager.isTokenExistAndCompany(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Company companyFromDb = companyServiceImpl.getCompanyDetails();
        CompanyDto companyDto = new CompanyDto(companyFromDb.getId(), companyFromDb.getName(), companyFromDb.getClientStatus());
        return new ResponseEntity<>(companyDto, HttpStatus.OK);
    }

    @GetMapping("category")
    public ResponseEntity<?> getCompanyCouponsByCategory(@RequestHeader("Authorization") String token, @RequestBody Category category) throws SecurityException {
        if (!tokenManager.isTokenExistAndCompany(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(companyServiceImpl.getCompanyCouponsByCategory(category), HttpStatus.OK);
    }

    @GetMapping("category-and-couponStatus/{couponStatus}") // @PathVariable
    public ResponseEntity<?> getCompanyCouponsByCategoryAndStatus(@RequestHeader("Authorization") String token, @RequestBody Category category, @PathVariable CouponStatus couponStatus) throws SecurityException {
        if (!tokenManager.isTokenExistAndCompany(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(companyServiceImpl.getCompanyCouponsByCategoryAndStatus(category, couponStatus), HttpStatus.OK);
    }


    @GetMapping("max_price")
    public ResponseEntity<?> getMaxPriceOfCouponsOfCompany(@RequestHeader("Authorization") String token) throws SecurityException {
        if (!tokenManager.isTokenExistAndCompany(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(companyServiceImpl.getMaxPriceOfCouponsOfCompany(), HttpStatus.OK);
    }

    @GetMapping("coupons_less_than_max_price")
    public ResponseEntity<?> getCouponListLessThanMaxPrice(@RequestHeader("Authorization") String token) throws SecurityException {
        if (!tokenManager.isTokenExistAndCompany(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(companyServiceImpl.findFromCompanyCouponsUpToMaxPrice(), HttpStatus.OK);
    }
}
