package com.couponsystemstage3.entity_beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customerPurchases")
@Builder
public class CustomerPurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne /*( cascade = CascadeType.PERSIST )*/ //TODO doesn't work CLR with CascadeType.PERSIST
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Customer customer;

    @ManyToOne /*( cascade = CascadeType.PERSIST )*/  //TODO  doesn't work CLR with CascadeType.PERSIST
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Coupon coupon;

    //@JsonIgnore
    private String couponTitle; // My changes for view the name of Coupon in the table
    //@JsonIgnore
    private String customerName;
    //@JsonIgnore
    private LocalDateTime purchaseDateTime;

    public CustomerPurchase(Customer customer, Coupon coupon) {
        super();
        this.customer = customer;
        this.coupon = coupon;
        this.couponTitle = coupon.getTitle();// My changes for view the title of coupon in the table
        this.customerName = customer.getFirstName(); // My changes for view the name of customer in the table
        this.purchaseDateTime = LocalDateTime.now();
    }

    public CustomerPurchase(Long id, Customer customer, Coupon coupon) {
        this.id = id;
        this.customer = customer;
        this.coupon = coupon;
        this.couponTitle = coupon.getTitle();
        this.customerName = customer.getFirstName();
        this.purchaseDateTime = LocalDateTime.now();
    }
}
