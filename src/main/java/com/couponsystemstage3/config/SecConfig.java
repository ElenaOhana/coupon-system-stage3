package com.couponsystemstage3.config;

import com.couponsystemstage3.security.Information;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SecConfig {

    @Bean
    public HashMap<String, Information> tokensMap(){
        return new HashMap<String, Information>();
    }
}
