package com.dhabits.ss.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("jwt")
public class JwtProperties {

    private String secret;

    private long accessTokenValid;

    private long refreshTokenValid;
}
