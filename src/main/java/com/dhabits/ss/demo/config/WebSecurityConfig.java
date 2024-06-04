package com.dhabits.ss.demo.config;

import com.dhabits.ss.demo.domain.model.UserEntity;
import com.dhabits.ss.demo.domain.model.enums.Role;
import com.dhabits.ss.demo.repository.UserRepository;
import com.dhabits.ss.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests((authorize) -> authorize
                        .antMatchers("/login/**").permitAll()
                        .antMatchers("/resource/**").authenticated()
                )
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
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
