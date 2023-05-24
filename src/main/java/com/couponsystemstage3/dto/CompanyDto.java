package com.couponsystemstage3.dto;

import com.couponsystemstage3.types.ClientStatus;
import lombok.Getter;

@Getter
public class CompanyDto {
    private final Long id;
    private final String name;
    private ClientStatus clientStatus = ClientStatus.ACTIVE;

    public CompanyDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CompanyDto(Long id, String name, ClientStatus clientStatus) {
        this.id = id;
        this.name = name;
        this.clientStatus = clientStatus;
    }
}
