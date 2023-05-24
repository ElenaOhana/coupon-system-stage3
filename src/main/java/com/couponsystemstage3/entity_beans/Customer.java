package com.couponsystemstage3.entity_beans;

import com.couponsystemstage3.types.ClientStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 45)
    @Size( min = 2, max = 45, message = "Customer first name must be between 2 and 45 characters")
    private String firstName;

    @NotBlank
    @Column(nullable = false, length = 45)
    @Size( min = 2, max = 45, message = "Customer last name must be between 2 and 45 characters")
    private String lastName;

    @Column(unique = true, nullable = false, length = 45)
    @Email
    private String email;

    @NotBlank
    @Column(nullable = false, length = 45)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /**
     * Default value of enum ClientStatus is ACTIVE */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 45)
    private ClientStatus clientStatus = ClientStatus.ACTIVE;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @Singular
    private Collection<CustomerPurchase> customerPurchases = new ArrayList<>();

    public Customer(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", clientStatus=" + clientStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(getId(), customer.getId()) && Objects.equals(getEmail(), customer.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail());
    }
}
