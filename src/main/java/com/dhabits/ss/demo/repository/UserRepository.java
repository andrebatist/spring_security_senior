package com.dhabits.ss.demo.repository;

import com.dhabits.ss.demo.domain.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {

    Optional<UserEntity> findFirstByLogin(String login);

}
