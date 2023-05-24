package com.couponsystemstage3.security.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest { /* The class in order to hide the parameters */

    private String email;
    private String password;
}
