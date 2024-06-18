package com.hydraxon91.backend.services.Authentication;

import com.hydraxon91.backend.models.UserModels.ApplicationUser;
import com.hydraxon91.backend.models.UserModels.Role;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class TokenService implements  ITokenServices{
    @Value("${jwt.valid-issuer}")
    private String jwtIssuer;
    
    @Value("${jwt.valid-audience}")
    private String jwtAudience;
    
    @Value("${jwt.token-time}")
    private int jwtExpiration;
    
    @Override
    public String createToken(ApplicationUser user, Role role){
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtExpiration * 1000);
        
        Key key = Keys.hmacShaKeyFor(jwtIssuer.getBytes());
        
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("username", user.getUsername())
                .claim("email", user.getEmail())
                .claim("role", role.getName())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setIssuer(jwtIssuer)
                .setAudience(jwtAudience)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
