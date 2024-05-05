package com.picpaysimplificado.transaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.picpaysimplificado.transaction.json.TransactionRequest;
import com.picpaysimplificado.transaction.service.TransactionService;
import com.picpaysimplificado.utils.exception.PicPayException;


@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transfer")
    public void transfer(@RequestBody TransactionRequest request) throws PicPayException {
        this.transactionService.transfer(request);
    }
    
}
