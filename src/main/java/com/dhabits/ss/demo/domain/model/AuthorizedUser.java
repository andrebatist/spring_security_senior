package com.dhabits.ss.demo.domain.model;

import com.dhabits.ss.demo.domain.model.enums.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AuthorizedUser extends User {

    private final UserEntity userEntity;

    public AuthorizedUser(UserEntity userEntity) {
        super(userEntity.getLogin(),
                userEntity.getPassword(),
                userEntity.isActive(),
                true,
                true,
                true,
                getUserAuthorities(userEntity.getRole()));
        this.userEntity = userEntity;
    }

    public int getId() {
        return userEntity.getId();
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getLogin();
    }

    public Role getRole() {
        return userEntity.getRole();
    }

    public String getLogin() {
        return userEntity.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return userEntity.isActive();
    }

    public static Optional<AuthorizedUser> get() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof AuthorizedUser)
                .map(principal -> (AuthorizedUser) principal);
    }

    private static Collection<? extends GrantedAuthority> getUserAuthorities(Role role) {
        return Stream.of(role)
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
