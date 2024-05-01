package com.picpaysimplificado.user.repository;

import org.springframework.data.repository.CrudRepository;

import com.picpaysimplificado.user.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long>{
    
}
