package com.couponsystemstage3.repositories;

import com.couponsystemstage3.entity_beans.Customer;
import com.couponsystemstage3.types.ClientStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.net.ssl.SSLSession;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer getCustomerById(Long id);

    Customer findCustomerByEmailAndPassword(@Email String email, @NotBlank String password);

    boolean existsByEmail(String email);

    @Modifying
    @Query(value = "UPDATE Customer c SET c.firstName = ?2, c.lastName = ?3,  c.email = ?4, c.password = ?5 WHERE c.firstName = ?1")
    Integer updateCustomerByName(String byName, String firstName, String lastName, String email, String password);

    @Modifying
    @Query(value = "UPDATE Customer c SET c.email = ?1, c.firstName = ?2,  c.lastName = ?3, c.password = ?4 WHERE c.id = ?5")
    Integer updateCustomer(String email, String firstName, String lastName, String password, Long id);

    Customer getCustomerByFirstName(String firstName);

    List<Customer> getAllByClientStatus(ClientStatus clientStatus);

    boolean existsByEmailAndPasswordAndClientStatus(String email, String password, ClientStatus active);

    Customer findByEmailAndPasswordAndClientStatus(String email, String password, ClientStatus active);

    boolean existsByEmailAndPassword(String email, String password);
}
