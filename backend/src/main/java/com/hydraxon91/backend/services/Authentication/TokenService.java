package com.hydraxon91.backend.services.Authentication;

import com.hydraxon91.backend.models.UserModels.ApplicationUser;
import com.hydraxon91.backend.models.UserModels.Role;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
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

    @Value("${jwt.issuer-signing-key}")
    private String jwtSecretKey = "my-32-character-ultra-secure-and-ultra-long-secret"; // Property for the secret key, ideally fetched from a secure location

    private SecretKey secretKey;

    public TokenService() {
        // Initialize the secret key from the property
        this.secretKey = Keys.hmacShaKeyFor(jwtSecretKey.getBytes());
    }
    
    @Override
    public String createToken(ApplicationUser user, Role role){
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtExpiration * 1000);
        
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("username", user.getUsername())
                .claim("email", user.getEmail())
                .claim("role", role.getName())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setIssuer(jwtIssuer)
                .setAudience(jwtAudience)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    @Override
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
