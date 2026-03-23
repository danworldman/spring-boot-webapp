package com.example.webapp.service;

import com.example.webapp.entity.User;
import com.example.webapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("=== ОШИБКА: ЮЗЕР " + username + " НЕ НАЙДЕН В БД ===");
                    return new UsernameNotFoundException("Not found");
                });

        return org.springframework.security.core.userdetails.User.builder() // собирается анкету для спринга
                .username(user.getUsername())
                .password(user.getPassword()) // хеш идёт спрингу
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(user.getRole())))
                .build();
    }
}