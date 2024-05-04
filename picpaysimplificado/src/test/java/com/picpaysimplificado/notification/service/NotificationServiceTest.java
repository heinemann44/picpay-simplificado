package com.picpaysimplificado.notification.service;

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

import com.picpaysimplificado.notification.json.NotificationTransferResponse;
import com.picpaysimplificado.transaction.json.TransactionRequest;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Autowired
    @InjectMocks
    private NotificationServiceImpl service;
    
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
        Mockito.when(this.builder.baseUrl("https://run.mocky.io/v3/54dc2cf1-3add-45b5-b5a9-6bf7e7f1f4a6")).thenReturn(this.builder);
        Mockito.when(this.builder.build()).thenReturn(this.restClient);
        Mockito.when(this.builder.build().get()).thenReturn(this.requestHeadersUriSpec);
        Mockito.when(this.builder.build().get().retrieve()).thenReturn(this.responseSpec);
    }
    
    void setupResponse(final NotificationTransferResponse response){
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);  
        ResponseEntity<NotificationTransferResponse> responseEntity = new ResponseEntity<>(
            response,
            header, 
            HttpStatus.OK
            );
            
            Mockito.when(this.builder.build().get().retrieve().toEntity(NotificationTransferResponse.class)).thenReturn(responseEntity);
        }
        
        @Test
        void notifySuccess() throws Exception {
        NotificationTransferResponse response = new NotificationTransferResponse(true);
        this.setupResponse(response);
        
        try {
            TransactionRequest transaction = new TransactionRequest(new BigDecimal(10), 4L, 2L);
            this.service.notifyTransaction(transaction);
        } catch (Exception exception) {
            Assertions.assertNull(exception);
        }
    }
    
    @Test
    void notifyInsuccess() throws Exception {
        NotificationTransferResponse response = new NotificationTransferResponse(false);
        this.setupResponse(response);

        try {
            TransactionRequest transaction = new TransactionRequest(new BigDecimal(10), 4L, 2L);
            this.service.notifyTransaction(transaction);
        } catch (Exception exception) {
            Assertions.assertNull(exception);
        }
    }
}
