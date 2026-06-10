package org.example.authspringbootsecurity.security;

import org.example.authspringbootsecurity.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user for username: {}", username);

        return userRepository.findByUsername(username)
        .orElseThrow(() -> {
            log.warn("User not found with username: {}", username);

            return new UsernameNotFoundException("User not found with username" + username);
        });
    }
}
