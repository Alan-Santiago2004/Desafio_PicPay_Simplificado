package com.projetoPicPaySimplificado.services;

import com.projetoPicPaySimplificado.authorizations.AuthorizationResponse;
import com.projetoPicPaySimplificado.domain.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class AuthorizationService {
    @Autowired
    private RestTemplate restTemplate;

    public boolean authorizeTransaction(User sender, BigDecimal value) throws Exception {
        boolean isAuthorized = false;
        ResponseEntity<AuthorizationResponse> response = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", AuthorizationResponse.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            AuthorizationResponse authorizationResponse = response.getBody();
            if (authorizationResponse != null && authorizationResponse.getStatus().equals("success") && authorizationResponse.getData() != null && authorizationResponse.getData().isAuthorization()) {
                isAuthorized = true;
            }
        }
        if(!isAuthorized){
            throw new Exception("Transação não autorizada");
        }
        return isAuthorized;
    }
}
