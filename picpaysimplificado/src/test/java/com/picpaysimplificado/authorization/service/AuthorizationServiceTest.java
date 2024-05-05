package com.picpaysimplificado.authorization.service;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersUriSpec;
import org.springframework.web.client.RestClient.ResponseSpec;

import com.picpaysimplificado.authorization.json.AuthorizationTransferResponse;
import com.picpaysimplificado.transaction.json.TransactionRequest;
import com.picpaysimplificado.utils.exception.ForbiddenException;

@ExtendWith(MockitoExtension.class)
public class AuthorizationServiceTest {

    @Autowired
    @InjectMocks
    private AuthorizationServiceImpl service;
    
    @Mock
    private RestClient restClient;
    
    @Mock
    private RestClient.Builder builder;

    @Mock
    private RequestHeadersUriSpec requestHeadersUriSpec;
    
    @Mock
    private ResponseSpec responseSpec;

    @BeforeEach
    void setupRestClient() {
        Mockito.when(this.builder.baseUrl("https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc")).thenReturn(this.builder);
        Mockito.when(this.builder.build()).thenReturn(this.restClient);
        Mockito.when(this.builder.build().get()).thenReturn(this.requestHeadersUriSpec);
        Mockito.when(this.builder.build().get().retrieve()).thenReturn(this.responseSpec);
    }
    
    void setupResponse(final AuthorizationTransferResponse response){
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);  
        ResponseEntity<AuthorizationTransferResponse> responseEntity = new ResponseEntity<>(
            response,
            header, 
            HttpStatus.OK
            );
            
        Mockito.when(this.builder.build().get().retrieve().toEntity(AuthorizationTransferResponse.class)).thenReturn(responseEntity);
    }
        
    @Test
    void authorizationSuccess() throws Exception {
        AuthorizationTransferResponse response = new AuthorizationTransferResponse("Autorizado");
        this.setupResponse(response);
        
        try {
            TransactionRequest transaction = new TransactionRequest(new BigDecimal(10), 4L, 2L);
            this.service.authorizeTransaction(transaction);
        } catch (Exception exception) {
            Assertions.assertNull(exception);
        }
    }

    @Test
    void authorizationForbidden() throws Exception {
        AuthorizationTransferResponse response = new AuthorizationTransferResponse("Não Autorizado");
        this.setupResponse(response);
        
        Assertions.assertThrows(ForbiddenException.class, () -> {
            TransactionRequest transaction = new TransactionRequest(new BigDecimal(10), 4L, 2L);
            this.service.authorizeTransaction(transaction);
        });
    }
    
}
