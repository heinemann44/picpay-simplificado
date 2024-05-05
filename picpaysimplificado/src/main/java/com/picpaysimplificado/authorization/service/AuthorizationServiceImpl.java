package com.picpaysimplificado.authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.picpaysimplificado.authorization.json.AuthorizationTransferResponse;
import com.picpaysimplificado.transaction.json.TransactionRequest;
import com.picpaysimplificado.utils.exception.ForbiddenException;
import com.picpaysimplificado.utils.exception.PicPayException;

public class AuthorizationServiceImpl implements AuthorizationService{

    @Autowired
    private RestClient.Builder builder;

    @Override
    public void authorizeTransaction(TransactionRequest transaction) throws PicPayException {
        RestClient restClient = this.builder.baseUrl("https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc").build();

        ResponseEntity<AuthorizationTransferResponse> response = restClient.get().retrieve().toEntity(AuthorizationTransferResponse.class);

        if( response.getStatusCode().isError() || !response.getBody().isAuthorized() ){
            throw new ForbiddenException("Não autorizado");
        }
    }
    
}
