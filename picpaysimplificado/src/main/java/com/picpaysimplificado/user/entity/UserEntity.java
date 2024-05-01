package com.picpaysimplificado.user.entity;

import com.picpaysimplificado.wallet.entity.WalletEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "USERS")
@Data
public class UserEntity {

    @Id
    @Column( name = "ID")
    private Long id;

    @Column( name = "FULL_NAME")
    private String fullName;

    @Column( name = "DOCUMENT")
    private String document;

    @Column( name = "EMAIL")
    private String email;

    @Column( name = "PASSWORD")
    private String password;

    @OneToOne( mappedBy = "user")
    private WalletEntity wallet;

}
