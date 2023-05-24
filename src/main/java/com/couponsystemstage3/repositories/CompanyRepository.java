package com.couponsystemstage3.repositories;

import com.couponsystemstage3.entity_beans.Company;
import com.couponsystemstage3.entity_beans.Coupon;
import com.couponsystemstage3.entity_beans.Customer;
import com.couponsystemstage3.types.ClientStatus;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Repository
@Scope(value = "prototype")
public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Override
    <S extends Company> boolean exists(Example<S> example);// my example/try

    //isExist/s - works with Entity, but existsBy___  works with field.

    //The second opportunity to add the company
    boolean existsByName(String name); //Smart DIALECT Of Hibernate!!!
    boolean existsByEmail(String email);
    boolean existsById(Long id);
    boolean existsByEmailAndPassword(String email, String password);

    Company getCompanyById(Long id);
    //Company getById(Long id);
    Company getCompanyByName(String name);

    Company getCompanyByEmailAndPassword(@Email String email, @NotBlank String password);

    List<Company> getAllByClientStatus(ClientStatus clientStatus);

    //@Transactional - או ברמת הקלאס, או מעל המתודה
    @Modifying
    @Query(value = "UPDATE Company c SET c.email = ?1, c.password = ?2  WHERE c.id = ?3"/*, nativeQuery = true*/)
    Integer updateCompany(String email, String password, Long id);

    Company findByEmailAndPassword(String email, String password);

    Company findByEmailAndPasswordAndClientStatus(String email, String password, ClientStatus clientStatus);

    boolean existsByEmailAndPasswordAndClientStatus(String email, String password, ClientStatus clientStatus);

}
