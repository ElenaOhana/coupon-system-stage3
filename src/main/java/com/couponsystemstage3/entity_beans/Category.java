package com.couponsystemstage3.entity_beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)/** IDENTITY - New counter for each table! */
    private Long id;

    @NotBlank
    @Size( min = 2, max = 45, message = "Category name must be between 2 and 45 characters")
    private String name;

    public Category(String name) {
        this.name = name;
    }

    /**
     * Bidirectional relation */
    @OneToMany(mappedBy = "category") // fetch = FetchType.LAZY doesn't work((
    @Singular
    @JsonIgnore
    private Collection<Coupon> coupons = new ArrayList<>();

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}


