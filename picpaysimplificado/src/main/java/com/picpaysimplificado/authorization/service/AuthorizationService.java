package com.picpaysimplificado.authorization.service;

import com.picpaysimplificado.transaction.json.TransactionRequest;
import com.picpaysimplificado.utils.exception.PicPayException;

public interface AuthorizationService {
    
    void authorizeTransaction(TransactionRequest transaction) throws PicPayException;
}
