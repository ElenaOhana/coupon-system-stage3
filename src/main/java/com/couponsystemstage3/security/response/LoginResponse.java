package com.couponsystemstage3.security.response;

import com.couponsystemstage3.security.ClientType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private ClientType clientType; /* To say to React which client to upload - admin/company/customer */

    private String email;
}
