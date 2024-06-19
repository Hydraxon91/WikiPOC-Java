package com.hydraxon91.backend.config;

import com.hydraxon91.backend.security.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    @Value("${allowed.origins}")
    private List<String> allowedOrigins;
    
    public WebSecurityConfig(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }
        
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource()).and()
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/public/**").permitAll()
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                .requestMatchers("/api/user-profiles/username/**").permitAll()
                                .requestMatchers("/api/**").authenticated()
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
//        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);        
        
        return source;
    }
}
