package com.picpaysimplificado.transaction.repository;

import org.springframework.data.repository.CrudRepository;

import com.picpaysimplificado.transaction.entity.TransactionEntity;

public interface TransactionRepository extends CrudRepository<TransactionEntity, Long>{
    
}
