package com.dhabits.ss.demo.service.impls;

import com.dhabits.ss.demo.domain.model.AuthorizedUser;
import com.dhabits.ss.demo.repository.UserRepository;
import com.dhabits.ss.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findFirstByLogin(username)
                .map(AuthorizedUser::new)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format("User details not found with this username: %s",
                                username)));
    }
}
