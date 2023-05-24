package com.couponsystemstage3.entity_beans;

import com.couponsystemstage3.types.CouponStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;


@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupons")
@Builder
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // strategy = GenerationType.IDENTITY => AutoIncrement
    private Long id;

    @ManyToOne
    @NotNull
    @ToString.Exclude // Prevention of Circular Dependency(and as a result-a stackoverflow error)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    //@JsonIgnore has precedence over JsonProperty
    private Company company; /** Relation Owner, FK */

    @ManyToOne
    @NotNull
    //@ToString.Exclude // By default, all non-static fields will be printed. If you want to skip some fields, you can annotate these fields with @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // enable as JsonProperty only to POST and PUT requests
    private Category category;

    /**
     * The title can changes from Company to Company => title not unique */
    @Column(nullable = false, length = 45)
    @Size( min = 2, max = 45, message = "Coupon title must be between 2 and 45 characters")
    private String title;

    @Column(nullable = false)
    @Lob
    private String description;

    @NotNull
    private LocalDateTime startDate;
    @NotNull
    private LocalDateTime endDate;

    @ColumnDefault("0")
    private int amount;

    @NotNull
    private double price;

    private String image;

    /**
     * Default value of enum CouponStatus is ABLE */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 45)
    CouponStatus couponStatus = CouponStatus.ABLE;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coupon)) return false;
        Coupon coupon = (Coupon) o;
        return Objects.equals(getId(), coupon.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
