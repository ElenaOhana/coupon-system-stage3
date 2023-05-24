package com.couponsystemstage3.security;

import com.couponsystemstage3.advice.SecurityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/* Manages the token map */
/* Instead of JWT */
@Service
public class TokenManager {

    /* Injected from config.SecConfig */
    @Autowired
    private HashMap<String, Information> tokensMap;

    public String createToken(ClientType clientType, long clientId) {
        String token = UUID.randomUUID().toString();
        Information information = new Information(clientType, clientId, LocalDateTime.now());
        tokensMap.put(token, information);
        return token;
    }

    /* If true so token exist and also from ADMINISTRATOR type. */
    public boolean isTokenExistAndAdmin(String token) throws SecurityException {
        try {
            return tokensMap.get(token).getClientType().equals(ClientType.ADMINISTRATOR);
        } catch (Exception e) {
            throw new SecurityException("unauthorizeddd!");
        }
    }

    /* If true so token exist and also from COMPANY type. */
    public boolean isTokenExistAndCompany(String token) throws SecurityException {
        try {
            return tokensMap.get(token).getClientType().equals(ClientType.COMPANY);
        } catch (Exception e) {
            throw new SecurityException("unauthorizeddd!");
        }
    }

    /* If true so token exist and also from CUSTOMER type. */
    public boolean isTokenExistAndCustomer(String token) throws SecurityException {
        try {
            return tokensMap.get(token).getClientType().equals(ClientType.CUSTOMER);
        } catch (Exception e) {
            throw new SecurityException("unauthorizeddd!");
        }
    }

    /* Deletes expired tokens - will run every minute. Will remove tokens that have existed longer than 30 minutes. */
    @Scheduled(fixedRate = 1000 * 60)
    public void deleteExpired(){
        tokensMap.values().removeIf(information -> information.getTime().isAfter(LocalDateTime.now().minusMinutes(30)));
        //tokensMap.entrySet().removeIf(info -> info.getValue().getTime().isAfter(LocalDateTime.now().minusMinutes(30)));
    }
}
