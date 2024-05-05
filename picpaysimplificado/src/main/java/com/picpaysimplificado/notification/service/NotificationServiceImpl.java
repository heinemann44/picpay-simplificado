package com.picpaysimplificado.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.picpaysimplificado.notification.json.NotificationTransferResponse;
import com.picpaysimplificado.transaction.json.TransactionRequest;
import com.picpaysimplificado.utils.exception.PicPayException;

import lombok.Getter;

@Getter
@Service
public class NotificationServiceImpl implements NotificationService{

    @Autowired
    private RestClient.Builder builder;

    @Override
    public void notifyTransaction(TransactionRequest transaction) throws PicPayException {
        RestClient restClient = this.builder.baseUrl("https://run.mocky.io/v3/54dc2cf1-3add-45b5-b5a9-6bf7e7f1f4a6").build();

        ResponseEntity<NotificationTransferResponse> response = restClient.get().retrieve().toEntity(NotificationTransferResponse.class);

        if( response.getStatusCode().isError() || !response.getBody().isNotified() ){
            // TODO - Criar alguma fila para notificações que deram erro
        }
    }

}
