package com.picpaysimplificado.wallet.entity;

import java.math.BigDecimal;

import com.picpaysimplificado.user.entity.UserEntity;
import com.picpaysimplificado.wallet.enums.WalletType;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "WALLETS")
@Data
public class WalletEntity {

    @Id
    @Column( name = "ID")
    private Long id;

    @OneToOne
    @JoinColumn( name = "USER")
    private UserEntity user;

    @Column( name = "TYPE")
    @Enumerated(EnumType.STRING)
    private WalletType type;

    @Column( name = "BALANCE")
    private BigDecimal balence;

}
