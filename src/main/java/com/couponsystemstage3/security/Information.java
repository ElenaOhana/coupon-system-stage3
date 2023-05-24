package com.couponsystemstage3.security;

import com.couponsystemstage3.services.ClientService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Information {

    private ClientType clientType;
    private long id;
    private LocalDateTime time;
}
