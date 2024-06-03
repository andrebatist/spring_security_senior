package com.dhabits.ss.demo.config;

import com.dhabits.ss.demo.domain.model.UserEntity;
import com.dhabits.ss.demo.domain.model.enums.Role;
import com.dhabits.ss.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .antMatchers("/resource/**").hasAnyAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .exceptionHandling(ex -> ex.accessDeniedPage("/UnAuthorized"))
                .csrf().disable();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner defaultUsers(UserRepository userRepository,
                                         PasswordEncoder encoder) {
        return args -> {
            if (userRepository.count() == 0) {
                UserEntity admin = UserEntity.builder()
                        .dateAdded(LocalDateTime.now())
                        .login("admin")
                        .password(encoder.encode("Hx=W!q3SMm"))
                        .role(Role.ADMIN)
                        .active(true)
                        .build();
                UserEntity user = UserEntity.builder()
                        .dateAdded(LocalDateTime.now())
                        .login("user")
                        .password(encoder.encode("2xI_b2y2"))
                        .role(Role.USER)
                        .active(true)
                        .build();
                userRepository.save(admin);
                userRepository.save(user);
            }
        };
    }

}
