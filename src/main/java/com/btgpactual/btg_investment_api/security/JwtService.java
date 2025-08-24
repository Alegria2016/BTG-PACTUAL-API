package com.btgpactual.btg_investment_api.security;
import com.btgpactual.btg_investment_api.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long jwtExpiration;

    public JwtService(@Value("${jwt.secret}") String secretKey,
                      @Value("${jwt.expiration}") long jwtExpiration) {
        // Versión 0.12.3: Usar Keys.hmacShaKeyFor para generar la clave
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.jwtExpiration = jwtExpiration;
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey) // Nuevo método: verifyWith() en lugar de setSigningKey()
                .build()
                .parseSignedClaims(token) // Nuevo método: parseSignedClaims() en lugar de parseClaimsJws()
                .getPayload();
    }

    // Métodos adicionales útiles para la versión 0.12.3
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        claims.put("roles", user.getRoles());

        return generateToken(claims, user);
    }

    public List<String> extractRoles(String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class);
    }

    public String extractFirstName(String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get("firstName", String.class);
    }

    public String extractLastName(String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get("lastName", String.class);
    }

    // Validación de token sin UserDetails
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Obtener tiempo restante del token
    public long getRemainingTime(String token) {
        Date expiration = extractExpiration(token);
        return expiration.getTime() - System.currentTimeMillis();
    }
}