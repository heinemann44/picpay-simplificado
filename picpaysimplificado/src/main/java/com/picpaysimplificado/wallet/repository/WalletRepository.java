package com.picpaysimplificado.wallet.repository;

import org.springframework.data.repository.CrudRepository;

import com.picpaysimplificado.wallet.entity.WalletEntity;

public interface WalletRepository extends CrudRepository<WalletEntity, Long>{
    
}
