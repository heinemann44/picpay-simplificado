package com.picpaysimplificado.transaction.service;

import com.picpaysimplificado.transaction.json.TransactionRequest;
import com.picpaysimplificado.utils.exception.PicPayException;

public interface TransactionService {

    void transfer(TransactionRequest request) throws PicPayException ;
    
}
