package com.dhabits.ss.demo.service.impls;

import com.dhabits.ss.demo.config.JwtProperties;
import com.dhabits.ss.demo.domain.model.RefreshToken;
import com.dhabits.ss.demo.repository.RefreshTokenRepository;
import com.dhabits.ss.demo.repository.UserRepository;
import com.dhabits.ss.demo.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtProperties jwtProperties;


    @Override
    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = RefreshToken.builder()
                .userEntity(userRepository.findFirstByLogin(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Null value for username = " + username)))
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(1000* jwtProperties.getRefreshTokenValid()))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findFirstByToken(token);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if(token.getExpiryDate().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please login");
        }
        return token;
    }
}
