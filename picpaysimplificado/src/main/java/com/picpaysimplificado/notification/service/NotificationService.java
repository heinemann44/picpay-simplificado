package com.picpaysimplificado.notification.service;

import com.picpaysimplificado.transaction.json.TransactionRequest;
import com.picpaysimplificado.utils.exception.PicPayException;

public interface NotificationService {
    
    void notifyTransaction(TransactionRequest transaction) throws PicPayException;
}
