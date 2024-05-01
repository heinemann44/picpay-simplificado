package com.picpaysimplificado.transaction.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.picpaysimplificado.user.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Table(name = "TRANSACTIONS")
public class TransactionEntity {

    @Id
    @Column( name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn( name = "PAYER")
    private UserEntity payer;

    @ManyToOne
    @JoinColumn( name = "PAYEE")
    private UserEntity payee;

    @Column( name = "VALUE")
    private BigDecimal value;

    @Column( name = "CREATED_AT")
    private LocalDateTime createdAt;

    @PrePersist
    private void beforeInsert(){
        this.createdAt = LocalDateTime.now();
    }
}
