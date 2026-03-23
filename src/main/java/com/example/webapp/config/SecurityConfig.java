package com.example.webapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //  поддержка безопасности
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // отключение CSRF для тестов через Postman
                .authorizeHttpRequests(auth -> auth
                        // разрешить всем смотреть документацию
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // разрешить ТОЛЬКО POST для всех
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/users").permitAll()
                        // остальные запросы доступны после логина
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults()) // включает стандартную форму в браузере
                .httpBasic(Customizer.withDefaults()); // позволяет слать логин и пароль через заголовки

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // бин для хеширования паролей
        return new BCryptPasswordEncoder();
    }
}