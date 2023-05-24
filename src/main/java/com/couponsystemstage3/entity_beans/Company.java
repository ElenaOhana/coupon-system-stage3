package com.couponsystemstage3.entity_beans;

import com.couponsystemstage3.types.ClientStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "companies")
public class Company {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 45, unique = true)
    @Size( min = 2, max = 45, message = "Company name must be between 2 and 45 characters")
    private String name;

    @Column(nullable = false, length = 45, unique = true)
    @Email
    @Basic(optional = false)
    private String email;

    @Column(nullable = false, length = 45)
    @Basic(optional = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /**
     * Default value of enum ClientStatus is ACTIVE */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 45)
    private ClientStatus clientStatus = ClientStatus.ACTIVE;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    @Singular
    private Collection<Coupon> coupons = new ArrayList<>(); // This reminds me that it is BIDIRECTIONAL relation ManyToOne

    public Company(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Company(Long id, String name, String email, String password, ClientStatus clientStatus) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.clientStatus = clientStatus;
    }

    public Company(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static Company from( String email, String password) {
        return new Company(email, password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Company)) return false;
        Company company = (Company) o;
        return Objects.equals(getId(), company.getId()) && Objects.equals(getName(), company.getName()) && Objects.equals(getEmail(), company.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getEmail());
    }
}
