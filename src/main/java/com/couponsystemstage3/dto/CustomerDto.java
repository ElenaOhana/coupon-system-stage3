package com.couponsystemstage3.dto;

import com.couponsystemstage3.types.ClientStatus;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class CustomerDto {

    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final ClientStatus clientStatus = ClientStatus.ACTIVE;

    public CustomerDto(Long id, String firstName, String lastName, String email) {
        this.id=id;
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
    }
}
