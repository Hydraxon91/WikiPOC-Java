package com.hydraxon91.backend.services.Authentication;

import com.hydraxon91.backend.models.UserModels.ApplicationUser;
import com.hydraxon91.backend.models.UserModels.Role;
import io.jsonwebtoken.Claims;
import jakarta.annotation.PostConstruct;
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
    private String jwtSecretKey; // Property for the secret key, ideally fetched from a secure location

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        // Initialize the secret key from the property
        this.secretKey = Keys.hmacShaKeyFor(jwtSecretKey.getBytes());
    }
    
    @Override
    public String createToken(ApplicationUser user, Role role){
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtExpiration * 1000 * 60);
        
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name", user.getUsername())
                .claim("email", user.getEmail())
                .claim("http://schemas.microsoft.com/ws/2008/06/identity/claims/role", role.getName())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setIssuer(jwtIssuer)
                .setAudience(jwtAudience)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String extractRole(String token) {
        return getClaims(token).get("http://schemas.microsoft.com/ws/2008/06/identity/claims/role", String.class);
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
