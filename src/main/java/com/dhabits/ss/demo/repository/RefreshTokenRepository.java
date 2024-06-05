package com.dhabits.ss.demo.repository;

import com.dhabits.ss.demo.domain.model.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findFirstByToken(String token);
}
