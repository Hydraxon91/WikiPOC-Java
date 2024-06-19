package com.hydraxon91.backend.security;

import com.hydraxon91.backend.services.Authentication.ITokenServices;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final ITokenServices tokenServices;

    public JwtTokenFilter(ITokenServices tokenServices) {
        this.tokenServices = tokenServices;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            final String token = authorizationHeader.substring(7);

            if (StringUtils.hasText(token)) {
                try {
                    String username = tokenServices.extractUsername(token);
                    String role = tokenServices.extractRole(token);

                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(username, null, List.of(() -> "ROLE_" + role.toUpperCase()));
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                } catch (Exception e) {
                    // Token validation failed
                    System.out.println("Token validation failed: " + e.getMessage());
                    // Clear the security context in case of an exception
                    SecurityContextHolder.clearContext();
                }
            }
        }
        chain.doFilter(request, response);
    }
}
