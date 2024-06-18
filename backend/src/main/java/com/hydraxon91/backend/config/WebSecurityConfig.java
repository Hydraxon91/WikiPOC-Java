package com.hydraxon91.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers("/public/**").permitAll()   // Public endpoints, no authentication required
                            .requestMatchers("/admin/**").hasRole("ADMIN")   // Admin endpoints, must have ADMIN role
                            .requestMatchers("/api/**").authenticated();   // API endpoints, authentication required
                })
                .httpBasic(Customizer.withDefaults())   
                 // Disable CSRF for /public/**
                .csrf().disable()
                .cors(Customizer.withDefaults());

        return http.build();
    }
}
